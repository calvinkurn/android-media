package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsearch.data.GetMultiChatSearchResponse
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchListHeaderUiModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatSearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var loadInitialDataObserver: Observer<Boolean>

    @RelaxedMockK
    lateinit var triggerSearchObserver: Observer<String>

    @RelaxedMockK
    lateinit var emptyQueryObserver: Observer<Boolean>

    @RelaxedMockK
    lateinit var errorMessageObserver: Observer<Throwable>

    @RelaxedMockK
    lateinit var searchResultObserver: Observer<List<Visitable<*>>>

    @RelaxedMockK
    lateinit var getSearchQueryUseCase: GetSearchQueryUseCase
    lateinit var viewModel: ChatSearchViewModel

    object Dummy {
        val exQuery = "tokopedia"
        val exPage = 2
        val exGetChatSearchResponse = GetMultiChatSearchResponse()
        val exThrowable = Throwable()
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = ChatSearchViewModel(Dispatchers.Unconfined, getSearchQueryUseCase)
    }

    @Test
    fun `User input new query first page for the first time`() {
        // Given
        viewModel.loadInitialData.observeForever(loadInitialDataObserver)
        viewModel.triggerSearch.observeForever(triggerSearchObserver)

        // When
        viewModel.onSearchQueryChanged(Dummy.exQuery)

        // Then
        verifyOrder {
            loadInitialDataObserver.onChanged(true)
            triggerSearchObserver.onChanged(Dummy.exQuery)
            getSearchQueryUseCase.doSearch(any(), any(), Dummy.exQuery, 1, false)
        }
    }

    @Test
    fun `User input new query when previous search progress is still running`() {
        // Given
        every { getSearchQueryUseCase.isSearching } returns true
        viewModel.loadInitialData.observeForever(loadInitialDataObserver)
        viewModel.triggerSearch.observeForever(triggerSearchObserver)

        // When
        viewModel.onSearchQueryChanged(Dummy.exQuery)

        // Then
        verifyOrder {
            getSearchQueryUseCase.cancelRunningSearch()
            loadInitialDataObserver.onChanged(true)
            triggerSearchObserver.onChanged(Dummy.exQuery)
            getSearchQueryUseCase.doSearch(any(), any(), Dummy.exQuery, 1)
        }
    }

    @Test
    fun `User input empty query`() {
        // Given
        viewModel.emptyQuery.observeForever(emptyQueryObserver)

        // When
        viewModel.onSearchQueryChanged(Dummy.exQuery)
        viewModel.onSearchQueryChanged("")

        // Then
        verifyOrder {
            getSearchQueryUseCase.cancelRunningSearch()
            emptyQueryObserver.onChanged(true)
        }
        verify(exactly = 1) { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) }
    }

    @Test
    fun `User input the same query`() {
        // Given
        viewModel.emptyQuery.observeForever(emptyQueryObserver)

        // When
        viewModel.onSearchQueryChanged("")

        // Then
        verify { getSearchQueryUseCase wasNot Called }
    }

    @Test
    fun `Success load new query`() {
        // Given
        viewModel.searchResults.observeForever(searchResultObserver)
        every { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) } answers {
            val onSuccess = firstArg<(GetMultiChatSearchResponse, SearchListHeaderUiModel?, SearchListHeaderUiModel?) -> Unit>()
            onSuccess.invoke(Dummy.exGetChatSearchResponse, null, null)
        }

        // When
        viewModel.onSearchQueryChanged(Dummy.exQuery)

        // Then
        verify { searchResultObserver.onChanged(emptyList()) }
    }

    @Test
    fun `Fail load new query`() {
        // Given
        viewModel.errorMessage.observeForever(errorMessageObserver)
        every { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) } answers {
            val onError = secondArg<(Throwable) -> Unit>()
            onError.invoke(Dummy.exThrowable)
        }

        // When
        viewModel.onSearchQueryChanged(Dummy.exQuery)

        // Then
        verify { errorMessageObserver.onChanged(Dummy.exThrowable) }
    }

    @Test
    fun `User load next page query`() {
        // Given
        every { getSearchQueryUseCase.hasNext } returns true

        // When
        viewModel.loadPage(Dummy.exPage)

        // Then
        verify { getSearchQueryUseCase.doSearch(any(), any(), any(), Dummy.exPage, true) }
    }

    @Test
    fun `User retry load page`() {
        // Given
        viewModel.errorMessage.observeForever(errorMessageObserver)
        every { getSearchQueryUseCase.doSearch(any(), any(), any(), any()) } answers {
            val onError = secondArg<(Throwable) -> Unit>()
            onError.invoke(Dummy.exThrowable)
        }
        viewModel.onSearchQueryChanged(Dummy.exQuery)

        // When
        viewModel.loadPage(1)

        // Then
        verify(exactly = 2) { getSearchQueryUseCase.doSearch(any(), any(), Dummy.exQuery, 1) }
    }

    @Test
    fun `Is first page`() {
        viewModel.onSearchQueryChanged(Dummy.exQuery)

        assertEquals(true, viewModel.isFirstPage())

        every { getSearchQueryUseCase.hasNext } returns true

        viewModel.loadPage(Dummy.exPage)

        assertEquals(false, viewModel.isFirstPage())
    }

    @Test
    fun `on reset live data`() {
        viewModel.emptyQuery.observeForever(emptyQueryObserver)
        viewModel.searchResults.observeForever(searchResultObserver)
        viewModel.loadInitialData.observeForever(loadInitialDataObserver)
        viewModel.errorMessage.observeForever(errorMessageObserver)
        viewModel.triggerSearch.observeForever(triggerSearchObserver)

        viewModel.resetLiveData()

        emptyQueryObserver.onChanged(false)
        searchResultObserver.onChanged(null)
        loadInitialDataObserver.onChanged(false)
        errorMessageObserver.onChanged(null)
        triggerSearchObserver.onChanged(null)
    }
}