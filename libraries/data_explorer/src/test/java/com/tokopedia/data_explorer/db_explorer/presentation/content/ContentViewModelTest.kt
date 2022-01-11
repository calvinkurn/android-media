package com.tokopedia.data_explorer.db_explorer.presentation.content

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Order
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.CopyDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.GetDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.RemoveDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.pragma.usecases.GetTableInfoUseCase
import com.tokopedia.data_explorer.db_explorer.domain.schema.usecases.DropTableContentUseCase
import com.tokopedia.data_explorer.db_explorer.domain.schema.usecases.GetTableContentUseCase
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.extensions.InvalidPageRequestException
import com.tokopedia.data_explorer.db_explorer.presentation.Constants
import com.tokopedia.data_explorer.db_explorer.presentation.databases.DatabaseViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.internal.matchers.Null
import java.lang.NullPointerException

@ExperimentalCoroutinesApi
class ContentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getTableInfoUseCase = mockk<GetTableInfoUseCase>(relaxed = true)
    private val getTableContentUseCase = mockk<GetTableContentUseCase>(relaxed = true)
    private val dropTableContentUseCase = mockk<DropTableContentUseCase>(relaxed = true)

    private lateinit var viewModel: ContentViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = ContentViewModel(getTableInfoUseCase, getTableContentUseCase,dropTableContentUseCase)
    }

    @Test
    fun `getTableInfo result exception`() {
        coEvery {
            getTableInfoUseCase.getTableInfo(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getTableInfo("test.db")
        assert(viewModel.errorLiveData.value is Throwable)
    }

    @Test
    fun `getTableInfo result columns fetched`() {
        val cell = Cell("column_name")
        val page = Page(cells = arrayListOf(cell))
        coEvery {
            getTableInfoUseCase.getTableInfo(any(), any(), any())
        } coAnswers {
            firstArg<(Page) -> Unit>().invoke(page)
        }
        viewModel.getTableInfo("test.db")
        assert(viewModel.columnHeaderLiveData.value is List<Cell>)
        assert(viewModel.columnHeaderLiveData.value?.get(0)?.text == "column_name")
    }


    @Test
    fun `getTableRowsCount fetch failed`() {
        coEvery {
            getTableContentUseCase.getTable(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getTableRowsCount("test.db")
        assert(viewModel.resultRowLiveData.value == false)
    }

    @Test
    fun `getTableRowsCount rows fetch success`() {
        val cell = Cell("12")
        val page = Page(cells = arrayListOf(cell))
        coEvery {
            getTableContentUseCase.getTable(any(), any(), any())
        } coAnswers {
            firstArg<(Page) -> Unit>().invoke(page)
        }
        viewModel.getTableRowsCount("test.db")
        assert(viewModel.resultRowLiveData.value == true)
        assert(viewModel.totalResults == 12)
    }

    @Test
    fun `getTableContent case - empty table totalResults = 0`() {
        viewModel.getTableContent("test.db", orderBy = "", Order.ASCENDING)
        assert(viewModel.errorLiveData.value is NullPointerException)
        assert(viewModel.errorLiveData.value?.message == Constants.ErrorMessages.NO_CONTENT)
    }

    @Test
    fun `getTableContent case - empty table invalidPageRequest`() {
        every { viewModel.totalResults } answers { 6 }
        every {  viewModel.currentPage } answers { 3 }
        viewModel.getTableContent("test.db", orderBy = "", Order.ASCENDING)
        assert(viewModel.errorLiveData.value is InvalidPageRequestException)
        assert(viewModel.errorLiveData.value?.message == Constants.ErrorMessages.INVALID_PAGE_REQUEST)
    }

    @Test
    fun `getTableContent case - result fetch - exception`() {
        every { viewModel.totalResults } answers { 6 }
        coEvery { getTableContentUseCase.getTable(any(), any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getTableContent("test.db", orderBy = "", Order.ASCENDING)
        assert(viewModel.errorLiveData.value is Throwable)
    }

    @Test
    fun `getTableContent case - result fetch content success`() {
        val page = Page(cells = arrayListOf(Cell("cell1"), Cell("cell2")))
        every { viewModel.totalResults } answers { 6 }

        coEvery { getTableContentUseCase.getTable(any(), any(), any()) } coAnswers {
            firstArg<(Page) -> Unit>().invoke(page)
        }
        viewModel.getTableContent("test.db", orderBy = "", Order.ASCENDING)
        assert(viewModel.contentLiveData.value is List<Cell>)
        assert(viewModel.contentLiveData.value?.get(0)?.text == "cell1")
    }

    @Test
    fun `dropTable case - failure dropping table`() {
        coEvery { dropTableContentUseCase.dropTable(any(), any(), any()) } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.dropTable("test.db")
        assert(viewModel.errorLiveData.value is Throwable)
    }

    @Test
    fun `dropTable case - success table dropped`() {
        val page = Page(cells = listOf())
        coEvery { dropTableContentUseCase.dropTable(any(), any(), any()) } coAnswers {
            firstArg<(Page) -> Unit>().invoke(page)
        }
        viewModel.dropTable("test.db")
        assert(viewModel.errorLiveData.value is NullPointerException)
        assert(viewModel.errorLiveData.value?.message == Constants.ErrorMessages.DELETION_SUCCESS)
    }

    @Test
    fun updateHeader() {
        val cell = Cell(text = "test")
        every { viewModel.columnHeaderList } answers { arrayListOf(cell) }
        viewModel.updateHeader(cell)
        assert(viewModel.columnHeaderList[0].active)
    }
}