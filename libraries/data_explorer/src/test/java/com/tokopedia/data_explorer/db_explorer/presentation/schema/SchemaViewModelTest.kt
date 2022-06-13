package com.tokopedia.data_explorer.db_explorer.presentation.schema

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.CopyDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.GetDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.RemoveDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.schema.usecases.GetTablesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Page
import com.tokopedia.data_explorer.db_explorer.presentation.databases.DatabaseViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class SchemaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getTablesUseCase = mockk<GetTablesUseCase>(relaxed = true)

    private lateinit var viewModel: SchemaViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = SchemaViewModel(getTablesUseCase)
    }

    @Test
    fun `getTables result exception`() {
        coEvery {
            getTablesUseCase.getTables(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getTables("/dbPath/")
        assert(viewModel.errorLiveData.value is Throwable)
    }

    @Test
    fun `getTables result success`() {
        val cell1 = Cell("gtm.db")
        val cell2 = Cell("android_metadata")
        val page = Page(cells = arrayListOf(cell1, cell2))
        coEvery {
            getTablesUseCase.getTables(any(), any(), any())
        } coAnswers {
            firstArg<(Page) -> Unit>().invoke(page)
        }
        viewModel.getTables("/dbPath/")
        assert(viewModel.tableListLiveData.value is List<Cell>)
        assert(viewModel.tableListLiveData.value?.size == 1)
    }

}