package lt.kvk.library.management;

import lt.kvk.library.management.models.Author;
import lt.kvk.library.management.models.Book;
import lt.kvk.library.management.models.BookGenre;
import lt.kvk.library.management.models.Borrowable;
import lt.kvk.library.management.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
	private Library library;

	@BeforeEach
	public void setUp() {
		library = new Library();
		Author author1 = new Author("J.K. Rowling");
		Author author2 = new Author("J.R.R. Tolkien");
		library.addBook(new Book("Harry Potter", author1, BookGenre.FICTION));
		library.addBook(new Book("The Hobbit", author2, BookGenre.FANTASY));
	}

	@Test
	public void testSearchBooksByTitle() {
		List<Book> results = library.searchBooksByTitle("Harry Potter");
		assertEquals(1, results.size());
		assertEquals("Harry Potter", results.get(0).getTitle());
	}

	@Test
	public void testSearchBooksByAuthor() {
		List<Book> results = library.searchBooksByAuthor("J.R.R. Tolkien");
		assertEquals(1, results.size());
		assertEquals("The Hobbit", results.get(0).getTitle());
	}

	@Test
	public void testFilterBooksByGenre() {
		List<Book> results = library.filterBooksByGenre(BookGenre.FANTASY);
		assertEquals(1, results.size());
		assertEquals("The Hobbit", results.get(0).getTitle());
	}

	@Test
	public void testSortBooksByTitle() {
		List<Book> results = library.sortBooksByTitle();
		assertEquals(2, results.size());
		assertEquals("Harry Potter", results.get(0).getTitle());
		assertEquals("The Hobbit", results.get(1).getTitle());
	}

	@Test
	public void testSortBooksByAuthor() {
		List<Book> results = library.sortBooksByAuthor();
		assertEquals(2, results.size());
		assertEquals("J.K. Rowling", results.get(0).getAuthor().getName());
		assertEquals("J.R.R. Tolkien", results.get(1).getAuthor().getName());
	}

	@Test
	public void testBorrowable() {
		Book book = library.searchBooksByTitle("Harry Potter").get(0);
		assertFalse(book.isBorrowed());

		book.borrowItem();
		assertTrue(book.isBorrowed());

		book.returnItem();
		assertFalse(book.isBorrowed());
	}

	@Test
	public void testBorrowableInterface() {
		Borrowable borrowable = library.searchBooksByTitle("Harry Potter").get(0);
		assertFalse(borrowable.isBorrowed());

		borrowable.borrowItem();
		assertTrue(borrowable.isBorrowed());

		borrowable.returnItem();
		assertFalse(borrowable.isBorrowed());
	}

	@Test
	public void testWriteAndReadBooks() throws IOException {
		List<Book> books = List.of(
				new Book("Knyga1", new Author("Autorius1"), BookGenre.FICTION),
				new Book("Knyga2", new Author("Autorius2"), BookGenre.MYSTERY)
		);

		FileUtils.writeBooksToFile(books); // Perrašome failą
		List<Book> readBooks = FileUtils.readBooksFromFile();

		assertEquals(books.size(), readBooks.size());
		for (int i = 0; i < books.size(); i++) {
			assertEquals(books.get(i).getTitle(), readBooks.get(i).getTitle());
			assertEquals(books.get(i).getAuthor().getName(), readBooks.get(i).getAuthor().getName());
			assertEquals(books.get(i).getGenre(), readBooks.get(i).getGenre());
		}
	}

	@Test
	public void testWriteAndReadAuthors() throws IOException {
		List<Author> authors = List.of(
				new Author("Autorius1"),
				new Author("Autorius2")
		);

		FileUtils.writeAuthorsToFile(authors); // Perrašome failą
		List<Author> readAuthors = FileUtils.readAuthorsFromFile();

		assertEquals(authors.size(), readAuthors.size());
		for (int i = 0; i < authors.size(); i++) {
			assertEquals(authors.get(i).getName(), readAuthors.get(i).getName());
		}
	}
}