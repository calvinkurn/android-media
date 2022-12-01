package com.tokopedia.privacycenter.searchhistory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.DeleteSearchHistoryParam
import com.tokopedia.privacycenter.data.ItemSearch
import com.tokopedia.privacycenter.domain.DeleteSearchHistoryResult
import com.tokopedia.privacycenter.domain.DeleteSearchHistoryUseCase
import com.tokopedia.privacycenter.domain.SearchHistoryUseCase
import com.tokopedia.privacycenter.ui.searchhistory.SearchHistoryViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchHistoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: SearchHistoryViewModel

    private val searchHistoryUseCase = mockk<SearchHistoryUseCase>(relaxed = true)
    private val deleteHistoryUseCase = mockk<DeleteSearchHistoryUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = SearchHistoryViewModel(
            searchHistoryUseCase,
            deleteHistoryUseCase,
            dispatcher
        )
    }

    @Test
    fun `get search list then success`() {
        val list = listOf(ItemSearch(), ItemSearch())
        val expected = PrivacyCenterStateResult.Success(list)

        coEvery { searchHistoryUseCase(Unit) } returns expected
        viewModel.getSearchHistory()

        val result = viewModel.listSearchHistory.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(expected, result)
    }

    @Test
    fun `get search list then failed`() {
        val throwable = Throwable()
        val expected = PrivacyCenterStateResult.Fail<List<ItemSearch>>(throwable)

        coEvery { searchHistoryUseCase(Unit) } throws throwable
        viewModel.getSearchHistory()

        val result = viewModel.listSearchHistory.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(expected, result)
    }

    @Test
    fun `delete one search history then success delete`() {
        val position = 0
        val itemSearch = ItemSearch()
        val expected = DeleteSearchHistoryResult.Success(position, false)
        val parameter = DeleteSearchHistoryParam(
            clearAll = false,
            query = itemSearch.title,
            type = itemSearch.type,
            id = itemSearch.id,
            position = position
        )

        coEvery { deleteHistoryUseCase(parameter) } returns expected
        viewModel.deleteSearchHistory(position = position, itemSearch = itemSearch)

        val result = viewModel.deleteSearchHistory.getOrAwaitValue()
        assertTrue(result is DeleteSearchHistoryResult.Success)
        assertEquals(expected, result)
    }

    @Test
    fun `delete one search history then failed delete`() {
        val position = 0
        val itemSearch = ItemSearch()
        val expected = DeleteSearchHistoryResult.Failed(position, false)
        val parameter = DeleteSearchHistoryParam(
            clearAll = false,
            query = itemSearch.title,
            type = itemSearch.type,
            id = itemSearch.id,
            position = position
        )

        coEvery { deleteHistoryUseCase(parameter) } returns expected
        viewModel.deleteSearchHistory(position = position, itemSearch = itemSearch)

        val result = viewModel.deleteSearchHistory.getOrAwaitValue()
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(expected, result)
    }

    @Test
    fun `delete one search history then throwable`() {
        val position = 0
        val itemSearch = ItemSearch()
        val throwable = Throwable()
        val expected = DeleteSearchHistoryResult.Failed(position, false, throwable)
        val parameter = DeleteSearchHistoryParam(
            clearAll = false,
            query = itemSearch.title,
            type = itemSearch.type,
            id = itemSearch.id,
            position = position
        )

        coEvery { deleteHistoryUseCase(parameter) } throws throwable
        viewModel.deleteSearchHistory(position = position, itemSearch = itemSearch)

        val result = viewModel.deleteSearchHistory.getOrAwaitValue()
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(expected.throwable, result.throwable)
        assertEquals(expected.position, result.position)
        assertEquals(expected.isClearAll, result.isClearAll)
    }

    @Test
    fun `delete one search history with item null then throwable`() {
        val position = 0
        val itemSearch: ItemSearch? = null
        val throwable = Throwable()
        val expected = DeleteSearchHistoryResult.Failed(position, false, throwable)
        val parameter = DeleteSearchHistoryParam(
            clearAll = false,
            query = itemSearch?.title.toString(),
            type = itemSearch?.type.toString(),
            id = itemSearch?.id.toString(),
            position = position
        )

        coEvery { deleteHistoryUseCase(parameter) } throws throwable
        viewModel.deleteSearchHistory(position = position, itemSearch = itemSearch)

        val result = viewModel.deleteSearchHistory.getOrAwaitValue()
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(expected.throwable, result.throwable)
        assertEquals(expected.position, result.position)
        assertEquals(expected.isClearAll, result.isClearAll)
    }

    @Test
    fun `delete all search history then success delete`() {
        val position = -1
        val expected = DeleteSearchHistoryResult.Success(position = position, isClearAll = true)
        val parameter = DeleteSearchHistoryParam(clearAll = true)

        coEvery { deleteHistoryUseCase(parameter) } returns expected
        viewModel.deleteSearchHistory(clearAll = true)

        val result = viewModel.deleteSearchHistory.getOrAwaitValue()
        assertTrue(result is DeleteSearchHistoryResult.Success)
        assertEquals(expected, result)
    }

    @Test
    fun `delete all search history then failed delete`() {
        val position = -1
        val expected = DeleteSearchHistoryResult.Failed(position, true)
        val parameter = DeleteSearchHistoryParam(clearAll = true)

        coEvery { deleteHistoryUseCase(parameter) } returns expected
        viewModel.deleteSearchHistory(clearAll = true)

        val result = viewModel.deleteSearchHistory.getOrAwaitValue()
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(expected, result)
    }

    @Test
    fun `delete all search history then throwable`() {
        val position = -1
        val throwable = Throwable()
        val expected = DeleteSearchHistoryResult.Failed(position, true, throwable)
        val parameter = DeleteSearchHistoryParam(clearAll = true)

        coEvery { deleteHistoryUseCase(parameter) } throws throwable
        viewModel.deleteSearchHistory(clearAll = true)

        val result = viewModel.deleteSearchHistory.getOrAwaitValue()
        assertTrue(result is DeleteSearchHistoryResult.Failed)
        assertEquals(expected.throwable, result.throwable)
        assertEquals(expected.position, result.position)
        assertEquals(expected.isClearAll, result.isClearAll)
    }
}
