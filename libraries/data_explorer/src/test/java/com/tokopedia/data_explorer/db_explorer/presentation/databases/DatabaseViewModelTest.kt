package com.tokopedia.data_explorer.db_explorer.presentation.databases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.CopyDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.GetDatabasesUseCase
import com.tokopedia.data_explorer.db_explorer.domain.databases.usecases.RemoveDatabasesUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DatabaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getDatabasesUseCase = mockk<GetDatabasesUseCase>(relaxed = true)
    private val removeDatabasesUseCase = mockk<RemoveDatabasesUseCase>(relaxed = true)
    private val copyDatabasesUseCase = mockk<CopyDatabasesUseCase>(relaxed = true)

    private lateinit var viewModel: DatabaseViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = DatabaseViewModel(getDatabasesUseCase, removeDatabasesUseCase,copyDatabasesUseCase)
    }

    @Test
    fun `browseDatabases result exception`() {
        coEvery {
            getDatabasesUseCase.getDatabases(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.browseDatabases()
        assert(viewModel.errorLiveData.value is Throwable)
    }

    @Test
    fun `browseDatabases result success`() {
        val list = arrayListOf<DatabaseDescriptor>()
        list.add(DatabaseDescriptor(true, "parent", "test.db"))
        coEvery {
            getDatabasesUseCase.getDatabases(any(), any(), any())
        } coAnswers {
            firstArg<(List<DatabaseDescriptor>) -> Unit>().invoke(list)
        }
        viewModel.browseDatabases()
        assert(viewModel.databaseListLiveData.value is List<DatabaseDescriptor>)
        assert((viewModel.databaseListLiveData.value)?.getOrNull(0)?.name == "test.db")
    }

    @Test
    fun `removeDatabase result exception`() {
        val testDb = DatabaseDescriptor(true, "parent", "test.db")
        coEvery {
            removeDatabasesUseCase.removeDatabases(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.removeDatabase(testDb)
        assert(viewModel.actionPerformedLiveData.value is Fail)
    }

    @Test
    fun `removeDatabase result deletion successful`() {
        val testDb = DatabaseDescriptor(true, "parent", "test.db")
        val list = arrayListOf<DatabaseDescriptor>()
        list.add(testDb)

        coEvery {
            removeDatabasesUseCase.removeDatabases(any(), any(), any())
        } coAnswers {
            firstArg<(List<DatabaseDescriptor>) -> Unit>().invoke(list)
        }
        viewModel.removeDatabase(testDb)
        assert(viewModel.actionPerformedLiveData.value is Success)
    }

    @Test
    fun `removeDatabase result deletion not successful`() {
        val testDb = DatabaseDescriptor(true, "parent", "test.db")

        val list = listOf<DatabaseDescriptor>()

        coEvery {
            removeDatabasesUseCase.removeDatabases(any(), any(), any())
        } coAnswers {
            firstArg<(List<DatabaseDescriptor>) -> Unit>().invoke(list)
        }
        viewModel.removeDatabase(testDb)
        assert(viewModel.actionPerformedLiveData.value is Fail)
    }

    @Test
    fun `copyDatabase result exception`() {
        val testDb = DatabaseDescriptor(true, "parent", "test.db")
        val list = arrayListOf<DatabaseDescriptor>()
        list.add(testDb)
        coEvery {
            copyDatabasesUseCase.copyDatabases(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.copyDatabase(testDb)
        assert(viewModel.actionPerformedLiveData.value is Fail)
    }

    @Test
    fun `copyDatabase result copy successful`() {
        val testDb = DatabaseDescriptor(true, "parent", "test.db")
        val list = arrayListOf<DatabaseDescriptor>()
        list.add(testDb)
        coEvery {
            copyDatabasesUseCase.copyDatabases(any(), any(), any())
        } coAnswers {
            firstArg<(List<DatabaseDescriptor>) -> Unit>().invoke(list)
        }
        viewModel.copyDatabase(testDb)
        assert(viewModel.actionPerformedLiveData.value is Success)
    }

    @Test
    fun `copyDatabase result copyDatabase not successful`() {
        val testDb = DatabaseDescriptor(true, "parent", "test.db")
        val list = listOf<DatabaseDescriptor>()

        coEvery {
            copyDatabasesUseCase.copyDatabases(any(), any(), any())
        } coAnswers {
            firstArg<(List<DatabaseDescriptor>) -> Unit>().invoke(list)
        }
        viewModel.copyDatabase(testDb)
        assert(viewModel.actionPerformedLiveData.value is Fail)
    }
}