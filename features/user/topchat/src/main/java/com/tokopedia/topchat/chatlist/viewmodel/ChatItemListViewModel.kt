package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREPLIED
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_MESSAGE_ID
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_PAGE
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.QUERY_CHAT_LIST_MESSAGE
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.QUERY_DELETE_CHAT_MESSAGE
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

interface ChatItemListContract {
    fun getChatListMessage(page: Int, filterIndex: Int, tab: String)
    fun chatMoveToTrash(messageId: Int)
}

class ChatItemListViewModel @Inject constructor(
        private val repository: GraphqlRepository,
        private val chatListUseCase: GraphqlUseCase<ChatListPojo>,
        private val queries: Map<String, String>,
        private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher), ChatItemListContract {

    private val _mutateChatList = MutableLiveData<Result<ChatListPojo>>()
    val mutateChatList: LiveData<Result<ChatListPojo>>
        get() = _mutateChatList

    private val _deleteChat = MutableLiveData<Result<ChatDelete>>()
    val deleteChat: LiveData<Result<ChatDelete>>
        get() = _deleteChat

    companion object{
        val arrayFilterParam = arrayListOf(
                PARAM_FILTER_ALL,
                PARAM_FILTER_UNREAD,
                PARAM_FILTER_UNREPLIED)
    }

    override fun getChatListMessage(page: Int, filterIndex: Int, tab: String) {
        queryGetChatListMessage(page, arrayFilterParam[filterIndex], tab)
    }

    private fun queryGetChatListMessage(page: Int, filter: String, tab: String) {
        queries[QUERY_CHAT_LIST_MESSAGE]?.let { query ->
            val params = mapOf(
                    PARAM_PAGE to page,
                    PARAM_FILTER to filter,
                    PARAM_TAB to tab
            )

            chatListUseCase.apply {
                setTypeClass(ChatListPojo::class.java)
                setRequestParams(params)
                setGraphqlQuery(query)
                execute({ result ->
                    _mutateChatList.value = Success(result)
                }, { error ->
                    error.printStackTrace()
                    _mutateChatList.value = Fail(error)
                })
            }
        }
    }

    override fun chatMoveToTrash(messageId: Int) {
        queries[QUERY_DELETE_CHAT_MESSAGE]?.let { query ->
            val params = mapOf(PARAM_MESSAGE_ID to messageId)

            launchCatchError(block = {
                val data = withContext(dispatcher) {
                    val request = GraphqlRequest(query, ChatDeleteStatus::class.java, params)
                    repository.getReseponse(listOf(request))
                }.getSuccessData<ChatDeleteStatus>()

                if(data.chatMoveToTrash.list.isNotEmpty()) {
                    _deleteChat.value = Success(data.chatMoveToTrash.list.first())
                }
            }) {
                _deleteChat.value = Fail(it)
            }
        }
    }

}
