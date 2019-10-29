package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.data.DataTest
import com.tokopedia.topchat.data.MockDeleteChatList
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Matchers.any
import org.mockito.Matchers.eq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class ChatItemListViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock lateinit var graphqlRepository: GraphqlRepository
    @Mock lateinit var userSessionInterface: UserSessionInterface
    @Mock lateinit var usecase: GraphqlUseCase<ChatListPojo>
    @Mock lateinit var rawQuery: Map<String, String>

    @Mock lateinit var result: Observer<Result<ChatDelete>>
    @Captor lateinit var resultCaptor: ArgumentCaptor<Result<ChatDelete>>

    private lateinit var viewModel: ChatItemListViewModel

    @ExperimentalCoroutinesApi
    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = ChatItemListViewModel(graphqlRepository, usecase, rawQuery, Dispatchers.Unconfined)
        `when`(userSessionInterface.userId).thenReturn("1")
    }

    @Test fun `should return success for delete chat item by message id`() = runBlocking {
        //given
        val (request, response) = givenData(MockDeleteChatList.chatMoveToTrash)

        //when
        `when`(graphqlRepository.getReseponse(listOf(request))).thenReturn(response)

        viewModel.deleteChat.observeForever(result)
        viewModel.chatMoveToTrash(1)

        //then
        verify(result, atLeastOnce()).onChanged(resultCaptor.capture())
        val allValueCaptors = resultCaptor.allValues

        assert(allValueCaptors.size > 0)
        println(allValueCaptors)
    }

    @Test fun `should fail delete chat item`() = runBlocking {
        //given
        val (request, response) = givenData(MockDeleteChatList.failChatMoveToTrash)

        //when
        `when`(graphqlRepository.getReseponse(listOf(request))).thenReturn(response)
        viewModel.deleteChat.observeForever(result)
        viewModel.chatMoveToTrash(1)

        //then
        verify(result, atLeastOnce()).onChanged(resultCaptor.capture())
        val allValueCaptors = resultCaptor.allValues

        assert(allValueCaptors.size > 0)
        println(allValueCaptors)
    }

    private fun givenMockResponse(json: String): ChatDeleteStatus {
        return Gson().fromJson(json, ChatDeleteStatus::class.java)
    }

    private fun givenData(json: String): DataTest {
        val request = GraphqlRequest(
                any(String::class.java),
                eq(ChatDeleteStatus::class.java),
                anyMapOf(String::class.java, Any::class.java))

        val response = GraphqlResponse(
                /* response success */
                mapOf(ChatDeleteStatus::class.java to givenMockResponse(json)),
                /* response error */
                mapOf(ChatDeleteStatus::class.java to listOf()),
                /* cache handler */
                false
        )

        return DataTest(request, response)
    }

}