package com.tokopedia.topchat.chatlist.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREPLIED
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

interface ChatItemListContract {
    fun getChatListMessage(page: Int, filterIndex: Int, tab: String)
    fun chatMoveToTrash(messageId: Int, query: String)
}

class ChatItemListViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val chatListUseCase: GraphqlUseCase<ChatListPojo>,
        private val rawQueries: Map<String, String>,
        private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher), ChatItemListContract {

    private val _mutateChatList = MutableLiveData<Result<ChatListPojo>>()
    val mutateChatList: LiveData<Result<ChatListPojo>>
        get() = _mutateChatList

    private val _deleteChat = MutableLiveData<Result<ChatDelete>>()
    val deleteChat: LiveData<Result<ChatDelete>>
        get() = _deleteChat

    private val arrayFilterParam = arrayListOf(
            PARAM_FILTER_ALL,
            PARAM_FILTER_UNREAD,
            PARAM_FILTER_READ,
            PARAM_FILTER_UNREPLIED
    )

    override fun getChatListMessage(page: Int, filterIndex: Int, tab: String) {
        queryGetChatListMessage(page, arrayFilterParam[filterIndex], tab)
    }

    override fun chatMoveToTrash(messageId: Int, query: String) {
        //first, prepare a params
        val params = mapOf(PARAM_MESSAGE_ID to messageId)

        launchCatchError(block = {
            //get response data
            val data = withContext(dispatcher) {
                //make a request from query
                val request = GraphqlRequest(query, ChatDeleteStatus::class.java, params)
                graphqlRepository.getReseponse(listOf(request))
            }.getSuccessData<ChatDeleteStatus>()

            data.chatMoveToTrash?.let {
                _deleteChat.postValue(Success(it.list.first()))
            }
        }) {
            _deleteChat.postValue(Fail(it))
        }
    }

    private fun queryGetChatListMessage(page: Int, filter: String, tab: String) {
        rawQueries[ChatListQueriesConstant.QUERY_CHAT_LIST_MESSAGE]?.let { query ->
            val params = mapOf(
                    ChatListQueriesConstant.PARAM_PAGE to page,
                    ChatListQueriesConstant.PARAM_FILTER to filter,
                    ChatListQueriesConstant.PARAM_TAB to tab
            )

            chatListUseCase.setTypeClass(ChatListPojo::class.java)
            chatListUseCase.setRequestParams(params)
            chatListUseCase.setGraphqlQuery(query)
            chatListUseCase.execute(
                    onSuccessGetChatListMessage(page),
                    onErrorGetChatListMessage()
            )
        }
    }

    private fun onErrorGetChatListMessage(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _mutateChatList.value = Fail(it)
        }
    }

    private fun onSuccessGetChatListMessage(page: Int): (ChatListPojo) -> Unit {
        return {
            _mutateChatList.value = Success(it)
        }
    }

    companion object {
        const val PARAM_MESSAGE_ID = "messageId"
    }
}
