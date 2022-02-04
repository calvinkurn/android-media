package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chatsearch.data.ChatSearch
import com.tokopedia.topchat.chatsearch.data.Contact
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.usecase.GetSearchContactQueryUseCase
import com.tokopedia.topchat.chatsearch.view.uimodel.SearchResultUiModel
import com.tokopedia.topchat.chatsearch.viewmodel.ChatContactLoadMoreViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.invoke
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatContactLoadMoreViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var searchContactQueryUseCase: GetSearchContactQueryUseCase

    private val dispatchers: CoroutineDispatcher = Dispatchers.Unconfined

    private lateinit var viewModel : ChatContactLoadMoreViewModel

    private val testMessageId: Long = 123
    private val testPage = 1
    private val testQuery = "testQuery"
    private val testFirstResponse: GetChatSearchResponse = GetChatSearchResponse()
    private val testThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = ChatContactLoadMoreViewModel(dispatchers, searchContactQueryUseCase)
        viewModel.page = 0
    }

    @Test
    fun should_get_chat_search_response_when_success_load_more() {
        //Given
        val expectedResponse = GetChatSearchResponse(ChatSearch(
            Contact(count = "1", hasNext = false, searchResults = listOf(
                SearchResultUiModel(msgId = testMessageId)
            ))
        ))
        coEvery {
            searchContactQueryUseCase.doSearch(captureLambda(), any(), any(), any(), any())
        } answers {
            val onSuccess = lambda<(GetChatSearchResponse) -> Unit>()
            onSuccess.invoke(expectedResponse)
        }

        //When
        viewModel.loadSearchResult(
            page = testPage,
            query = testQuery,
            firstResponse = testFirstResponse
        )

        //Then
        Assert.assertEquals(
            expectedResponse,
            viewModel.searchResult.value
        )
    }

    @Test
    fun should_get_error_search_response_when_success_load_more() {
        //Given
        coEvery {
            searchContactQueryUseCase.doSearch(any(), captureLambda(), any(), any(), any())
        } answers {
            val onError = lambda<(Throwable) -> Unit>()
            onError.invoke(testThrowable)
        }

        //When
        viewModel.loadSearchResult(
            page = testPage,
            query = testQuery,
            firstResponse = testFirstResponse
        )

        //Then
        Assert.assertEquals(
            testThrowable,
            viewModel.errorSearchResults.value
        )
    }

    @Test
    fun should_give_true_when_first_page() {
        //Given
        val testFirstPage = 1

        //When
        viewModel.page = testFirstPage

        //Then
        Assert.assertTrue(viewModel.isFirstPage())
    }

    @Test
    fun should_give_false_when_page_less_than_one() {
        //Given
        val testNotFirstPage = 0

        //When
        viewModel.page = testNotFirstPage

        //Then
        Assert.assertFalse(viewModel.isFirstPage())
    }

    @Test
    fun should_give_false_when_page_more_than_one() {
        //Given
        val testNotFirstPage = 2

        //When
        viewModel.page = testNotFirstPage

        //Then
        Assert.assertFalse(viewModel.isFirstPage())
    }

    @Test
    fun should_get_zero_page_at_first() {
        //Then
        Assert.assertEquals(0, viewModel.page)
    }
}