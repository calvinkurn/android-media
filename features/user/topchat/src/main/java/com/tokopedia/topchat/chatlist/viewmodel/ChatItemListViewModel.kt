package com.tokopedia.topchat.chatlist.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.MUTATION_MARK_CHAT_AS_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_TOPBOT
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREPLIED
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_MESSAGE_ID
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_MESSAGE_IDS
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.QUERY_BLAST_SELLER_METADATA
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.QUERY_DELETE_CHAT_MESSAGE
import com.tokopedia.topchat.chatlist.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.pojo.chatblastseller.BlastSellerMetaDataResponse
import com.tokopedia.topchat.chatlist.pojo.chatblastseller.ChatBlastSellerMetadata
import com.tokopedia.topchat.chatlist.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.usecase.*
import com.tokopedia.topchat.chatroom.view.viewmodel.ReplyParcelableModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.HashSet

/**
 * Created by stevenfredian on 10/19/17.
 */

interface ChatItemListContract {
    fun getChatListMessage(page: Int, filterIndex: Int, tab: String)
    fun chatMoveToTrash(messageId: Int)
    fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun loadChatBannedSellerStatus()
    fun pinUnpinChat(
            msgId: String,
            isPinChat: Boolean = true,
            onSuccess: (Boolean) -> Unit,
            onError: (Throwable) -> Unit
    )
}

class ChatItemListViewModel @Inject constructor(
        private val repository: GraphqlRepository,
        private val queries: Map<String, String>,
        private val chatWhitelistFeature: GetChatWhitelistFeature,
        private val chatBannedSellerUseCase: ChatBanedSellerUseCase,
        private val pinChatUseCase: MutationPinChatUseCase,
        private val unpinChatUseCase: MutationUnpinChatUseCase,
        private val getChatListUseCase: GetChatListMessageUseCase,
        private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher), ChatItemListContract {

    private val _mutateChatList = MutableLiveData<Result<ChatListPojo>>()
    val mutateChatList: LiveData<Result<ChatListPojo>>
        get() = _mutateChatList

    private val _deleteChat = MutableLiveData<Result<ChatDelete>>()
    val deleteChat: LiveData<Result<ChatDelete>>
        get() = _deleteChat

    private val _broadCastButtonVisibility = MutableLiveData<Boolean>()
    val broadCastButtonVisibility: LiveData<Boolean>
        get() = _broadCastButtonVisibility

    private val _broadCastButtonUrl = MutableLiveData<String>()
    val broadCastButtonUrl: LiveData<String>
        get() = _broadCastButtonUrl

    private val _chatBannedSellerStatus = MutableLiveData<Result<Boolean>>()
    val chatBannedSellerStatus: LiveData<Result<Boolean>>
        get() = _chatBannedSellerStatus

    val pinnedMsgId: HashSet<String> = HashSet()
    val unpinnedMsgId: HashSet<String> = HashSet()

    override fun getChatListMessage(page: Int, filterIndex: Int, tab: String) {
        queryGetChatListMessage(page, arrayFilterParam[filterIndex], tab)
    }

    private fun queryGetChatListMessage(page: Int, filter: String, tab: String) {
        getChatListUseCase.getChatList(page, filter, tab,
                { chats, pinChats, unpinChats ->
                    if (page == 1) {
                        pinnedMsgId.addAll(pinChats)
                    }
                    unpinnedMsgId.addAll(unpinChats)
                    _mutateChatList.value = Success(chats)
                },
                {
                    _mutateChatList.value = Fail(it)
                }
        )
    }

    override fun chatMoveToTrash(messageId: Int) {
        queries[QUERY_DELETE_CHAT_MESSAGE]?.let { query ->
            val params = mapOf(PARAM_MESSAGE_ID to messageId)

            launchCatchError(block = {
                val data = withContext(dispatcher) {
                    val request = GraphqlRequest(query, ChatDeleteStatus::class.java, params)
                    repository.getReseponse(listOf(request))
                }.getSuccessData<ChatDeleteStatus>()

                if (data.chatMoveToTrash.list.isNotEmpty()) {
                    _deleteChat.value = Success(data.chatMoveToTrash.list.first())
                }
            }) {
                _deleteChat.value = Fail(it)
            }
        }
    }

    override fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        val query = queries[MUTATION_MARK_CHAT_AS_READ] ?: return
        changeMessageState(query, msgIds, result)
    }

    override fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        val query = queries[MUTATION_MARK_CHAT_AS_UNREAD] ?: return
        changeMessageState(query, msgIds, result)
    }

    override fun loadChatBannedSellerStatus() {
        chatBannedSellerUseCase.getStatus({
            _chatBannedSellerStatus.value = Success(it)
        }, {
            _chatBannedSellerStatus.value = Fail(it)
        })
    }

    override fun pinUnpinChat(
            msgId: String,
            isPinChat: Boolean,
            onSuccess: (Boolean) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        if (isPinChat) {
            pinChatUseCase.pinChat(msgId, onSuccess, onError)
        } else {
            unpinChatUseCase.unpinChat(msgId, onSuccess, onError)
        }
    }

    private fun changeMessageState(
            query: String,
            msgIds: List<String>,
            result: (Result<ChatChangeStateResponse>) -> Unit
    ) {
        val params = mapOf(PARAM_MESSAGE_IDS to msgIds)

        launchCatchError(block = {
            val data = withContext(dispatcher) {
                val request = GraphqlRequest(query, ChatChangeStateResponse::class.java, params)
                repository.getReseponse(listOf(request))
            }.getSuccessData<ChatChangeStateResponse>()
            result(Success(data))
        }
        ) {
            result(Fail(it))
        }
    }

    fun loadChatBlastSellerMetaData() {
        val query = queries[QUERY_BLAST_SELLER_METADATA] ?: return
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                val request = GraphqlRequest(query, BlastSellerMetaDataResponse::class.java, emptyMap())
                repository.getReseponse(listOf(request))
            }.getSuccessData<BlastSellerMetaDataResponse>()
            onSuccessLoadChatBlastSellerMetaData(data.chatBlastSellerMetadata)
        }) {
            onErrorLoadChatBlastSellerMetaData(it)
        }
    }

    private fun onSuccessLoadChatBlastSellerMetaData(metaData: ChatBlastSellerMetadata) {
        val broadCastUrl = metaData.url
        broadCastButtonVisibility(true)
        setBroadcastButtonUrl(broadCastUrl)
    }

    private fun onErrorLoadChatBlastSellerMetaData(throwable: Throwable) {
        broadCastButtonVisibility(false)
    }

    private fun broadCastButtonVisibility(visibility: Boolean) {
        _broadCastButtonVisibility.value = visibility
    }

    private fun setBroadcastButtonUrl(broadCastUrl: String) {
        _broadCastButtonUrl.value = broadCastUrl
    }

    fun getReplyTimeStampFrom(lastItem: ReplyParcelableModel): String {
        return (lastItem.replyTime.toLongOrZero() / 1000000L).toString()
    }

    fun loadTopBotWhiteList() {
        chatWhitelistFeature.getWhiteList(
                GetChatWhitelistFeature.PARAM_VALUE_FEATURE_TOPBOT, ::onSuccessLoadWhiteList, ::onErrorGetWhiteList
        )
    }

    private fun onSuccessLoadWhiteList(chatWhitelistFeatureResponse: ChatWhitelistFeatureResponse) {
        if (chatWhitelistFeatureResponse.chatWhitelistFeature.isWhitelist) {
            arrayFilterParam.add(PARAM_FILTER_TOPBOT)
        }
    }

    private fun onErrorGetWhiteList(throwable: Throwable) {
        throwable.printStackTrace()
    }

    fun getFilterTittles(context: Context, isTabSeller: Boolean): List<String> {
        val filters = arrayListOf(
                context.getString(R.string.filter_chat_all),
                context.getString(R.string.filter_chat_unread),
                context.getString(R.string.filter_chat_unreplied)
        )
        if (arrayFilterParam.size > 3 && isTabSeller) {
            filters.add(context.getString(R.string.filter_chat_smart_reply))
        }
        return filters
    }

    companion object {
        val arrayFilterParam = arrayListOf(
                PARAM_FILTER_ALL,
                PARAM_FILTER_UNREAD,
                PARAM_FILTER_UNREPLIED
        )
    }
}
