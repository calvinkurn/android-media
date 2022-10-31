package com.tokopedia.topchat.chatlist.view.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueries.MUTATION_CHAT_MARK_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueries.MUTATION_CHAT_MARK_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueries.QUERY_CHAT_BLAST_SELLER_METADATA
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_TOPBOT
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREPLIED
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_MESSAGE_IDS
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_SELLER
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_USER
import com.tokopedia.topchat.chatlist.domain.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.domain.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller.BlastSellerMetaDataResponse
import com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller.ChatBlastSellerMetadata
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.chatlist.domain.usecase.ChatBanedSellerUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatWhitelistFeature
import com.tokopedia.topchat.chatlist.domain.usecase.GetOperationalInsightUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.MutationPinChatUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.MutationUnpinChatUseCase
import com.tokopedia.topchat.chatroom.view.uimodel.ReplyParcelableModel
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.util.Utils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

interface ChatItemListContract {
    fun getChatListMessage(page: Int, filterIndex: Int, tab: String)
    fun chatMoveToTrash(messageId: String)
    fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit)
    fun loadChatBannedSellerStatus()
    fun pinUnpinChat(
            msgId: String,
            isPinChat: Boolean = true,
            onSuccess: (Boolean) -> Unit,
            onError: (Throwable) -> Unit
    )

    fun clearPinUnpinData()
    fun resetState()
}

class ChatItemListViewModel @Inject constructor(
    private val repository: GraphqlRepository,
    private val chatWhitelistFeature: GetChatWhitelistFeature,
    private val chatBannedSellerUseCase: ChatBanedSellerUseCase,
    private val pinChatUseCase: MutationPinChatUseCase,
    private val unpinChatUseCase: MutationUnpinChatUseCase,
    private val getChatListUseCase: GetChatListMessageUseCase,
    private val authorizeAccessUseCase: AuthorizeAccessUseCase,
    private val moveChatToTrashUseCase: MutationMoveChatToTrashUseCase,
    private val operationalInsightUseCase: GetOperationalInsightUseCase,
    private val sharedPref: SharedPreferences,
    private val userSession: UserSessionInterface,
    private val abTestPlatform: AbTestPlatform,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main), ChatItemListContract {

    var filter: String = PARAM_FILTER_ALL
        set(value) {
            field = value
            cancelAllUseCase()
        }

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

    private val _isWhitelistTopBot = MutableLiveData<Boolean>()
    val isWhitelistTopBot: LiveData<Boolean>
        get() = _isWhitelistTopBot

    private val _isChatAdminEligible = MutableLiveData<Result<Boolean>>()
    val isChatAdminEligible: LiveData<Result<Boolean>>
        get() = _isChatAdminEligible

    private val _chatOperationalInsight = MutableLiveData<Result<ShopChatTicker>>()
    val chatOperationalInsight: LiveData<Result<ShopChatTicker>>
        get()= _chatOperationalInsight

    private var getChatAdminAccessJob: Job? = null

    val chatListHasNext: Boolean get() = getChatListUseCase.hasNext
    val pinnedMsgId: HashSet<String> = HashSet()
    val unpinnedMsgId: HashSet<String> = HashSet()
    val isAdminHasAccess: Boolean
        get() = (_isChatAdminEligible.value as? Success)?.data == true

    override fun getChatListMessage(page: Int, filterIndex: Int, tab: String) {
        whenChatAdminAuthorized(tab) {
            queryGetChatListMessage(page, arrayFilterParam[filterIndex], tab)
        }
    }

    fun getChatListMessage(page: Int, @RoleType role: Int) {
        val tabRole = when (role) {
            RoleType.BUYER -> PARAM_TAB_USER
            RoleType.SELLER -> PARAM_TAB_SELLER
            else -> PARAM_TAB_USER
        }

        whenChatAdminAuthorized(tabRole) {
            queryGetChatListMessage(page, filter, tabRole)
        }
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

    fun whenChatAdminAuthorized(tab: String, action: () -> Unit) {
        val isTabUser = tab == PARAM_TAB_USER
        _isChatAdminEligible.value.let { result ->
            when {
                isTabUser -> action()
                result != null && result is Success -> {
                    if (result.data) {
                        action()
                    } else {
                        _isChatAdminEligible.value = Success(false)
                    }
                }
                else -> setChatAdminAccessJob()
            }
        }
    }

    private fun setChatAdminAccessJob() {
        if (getChatAdminAccessJob?.isCompleted != false) {
            getChatAdminAccessJob =
                    launchCatchError(
                            block = {
                                _isChatAdminEligible.postValue(withContext(dispatcher.io) {
                                    Success(getIsChatAdminAccessAuthorized())
                                })
                            },
                            onError = {
                                _isChatAdminEligible.value = Fail(it)
                            }
                    )
        }
    }

    private suspend fun getIsChatAdminAccessAuthorized(): Boolean {
        return if (userSession.isShopOwner) {
            true
        } else {
            checkChatAdminEligiblity()
        }
    }

    private suspend fun checkChatAdminEligiblity(): Boolean {
        return AuthorizeAccessUseCase.createRequestParams(userSession.shopId.toLongOrZero(), AccessId.CHAT).let { requestParams ->
            authorizeAccessUseCase.execute(requestParams)
        }
    }

    override fun chatMoveToTrash(messageId: String) {
        launch {
            try {
                val result = moveChatToTrashUseCase(messageId)
                if(result.chatMoveToTrash.list.isNotEmpty()) {
                    val deletedChat = result.chatMoveToTrash.list.first()
                    if(deletedChat.isSuccess == Constant.INT_STATUS_TRUE) {
                        _deleteChat.value = Success(deletedChat)
                        clearFromPinUnpin(deletedChat.messageId)
                    } else {
                        _deleteChat.value = Fail(Throwable(deletedChat.detailResponse))
                    }
                }
            } catch (throwable: Throwable) {
                _deleteChat.value = Fail(throwable)
            }
        }
    }

    private fun clearFromPinUnpin(messageId: String) {
        pinnedMsgId.remove(messageId)
        unpinnedMsgId.remove(messageId)
    }

    override fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        val query = MUTATION_CHAT_MARK_READ
        changeMessageState(query, msgIds, result)
    }

    override fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        val query = MUTATION_CHAT_MARK_UNREAD
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

    override fun clearPinUnpinData() {
        pinnedMsgId.clear()
        unpinnedMsgId.clear()
    }

    override fun resetState() {
        getChatListUseCase.reset()
    }

    private fun changeMessageState(
            query: String,
            msgIds: List<String>,
            result: (Result<ChatChangeStateResponse>) -> Unit
    ) {
        val params = mapOf(PARAM_MESSAGE_IDS to msgIds)

        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val request = GraphqlRequest(query, ChatChangeStateResponse::class.java, params)
                repository.response(listOf(request))
            }.getSuccessData<ChatChangeStateResponse>()
            result(Success(data))
        }
        ) {
            result(Fail(it))
        }
    }

    fun loadChatBlastSellerMetaData() {
        val query = QUERY_CHAT_BLAST_SELLER_METADATA
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val request = GraphqlRequest(query, BlastSellerMetaDataResponse::class.java, emptyMap())
                repository.response(listOf(request))
            }.getSuccessData<BlastSellerMetaDataResponse>()
            getChatAdminAccessJob?.join()
            if (isAdminHasAccess) {
                onSuccessLoadChatBlastSellerMetaData(data.chatBlastSellerMetadata)
            } else {
                onErrorLoadChatBlastSellerMetaData()
            }
        }) {
            onErrorLoadChatBlastSellerMetaData()
        }
    }

    private fun onSuccessLoadChatBlastSellerMetaData(metaData: ChatBlastSellerMetadata) {
        val broadCastUrl = metaData.urlBroadcast
        broadCastButtonVisibility(true)
        setBroadcastButtonUrl(broadCastUrl)
    }

    private fun onErrorLoadChatBlastSellerMetaData() {
        broadCastButtonVisibility(false)
    }

    private fun broadCastButtonVisibility(visibility: Boolean) {
        _broadCastButtonVisibility.value = visibility
    }

    private fun setBroadcastButtonUrl(broadCastUrl: String) {
        _broadCastButtonUrl.value = broadCastUrl
    }

    fun getReplyTimeStampFrom(lastItem: ReplyParcelableModel): String {
        return (lastItem.replyTime.toLongOrZero() / ONE_MILLION).toString()
    }

    fun loadTopBotWhiteList() {
        chatWhitelistFeature.getWhiteList(
                GetChatWhitelistFeature.PARAM_VALUE_FEATURE_TOPBOT, ::onSuccessLoadWhiteList, ::onErrorGetWhiteList
        )
    }

    private fun onSuccessLoadWhiteList(chatWhitelistFeatureResponse: ChatWhitelistFeatureResponse) {
        _isWhitelistTopBot.value = chatWhitelistFeatureResponse.chatWhitelistFeature.isWhitelist
        if (chatWhitelistFeatureResponse.chatWhitelistFeature.isWhitelist) {
            arrayFilterParam.add(PARAM_FILTER_TOPBOT)
        }
    }

    private fun onErrorGetWhiteList(throwable: Throwable) {
        throwable.printStackTrace()
    }

    fun getFilterTitles(context: Context, isTabSeller: Boolean): List<String> {
        val filters = arrayListOf(
                context.getString(R.string.filter_chat_all),
                context.getString(R.string.filter_chat_unread)
        )
        if (isTabSeller) {
            filters.add(context.getString(R.string.filter_chat_unreplied))
            if (arrayFilterParam.size > SELLER_FILTER_THRESHOLD) {
                filters.add(context.getString(R.string.filter_chat_smart_reply))
            }
        }
        return filters
    }

    fun hasFilter(): Boolean {
        return filter != PARAM_FILTER_ALL
    }

    fun reset() {
        filter = PARAM_FILTER_ALL
    }

    private fun cancelAllUseCase() {
        getChatListUseCase.cancelRunningOperation()
        coroutineContext.cancelChildren()
    }

    fun getOperationalInsight(shopId: String) {
        launchCatchError(block = {
            val dataResponse = operationalInsightUseCase(shopId)
            dataResponse.shopChatTicker?.let {
                if (it.showTicker == true) {
                    val shouldShowTicker = shouldShowOperationalInsightTicker()
                    it.showTicker = shouldShowTicker
                }
                _chatOperationalInsight.value = Success(it)
            }
        }, onError = {
            _chatOperationalInsight.value = Fail(it)
        })
    }

    private fun shouldShowOperationalInsightTicker(): Boolean {
        val nextMonday = sharedPref.getLong(getTickerPrefName(), 0)
        val todayTimeMillis = System.currentTimeMillis()
        return todayTimeMillis > nextMonday
    }

    fun saveNextMondayDate() {
        val newNextMonday = Utils.getNextParticularDay(Calendar.MONDAY)
        sharedPref.edit()
            .putLong(getTickerPrefName(), newNextMonday)
            .apply()
    }

    private fun getTickerPrefName(): String {
        return "${OPERATIONAL_INSIGHT_NEXT_MONDAY}_${userSession.userId}"
    }

    fun shouldShowBubbleTicker(): Boolean {
        return sharedPref.getBoolean(getTickerPrefName(), true) &&
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) &&
            getRollenceIsBubbleChatEnabled()
    }

    fun saveTickerPref(prefName: String) {
        sharedPref.edit()
            .putBoolean(prefName, false)
            .apply()
    }

    private fun getRollenceIsBubbleChatEnabled(): Boolean {
        return try {
            abTestPlatform.getString(
                RollenceKey.KEY_ROLLENCE_BUBBLE_CHAT, RollenceKey.KEY_ROLLENCE_BUBBLE_CHAT
            ) == RollenceKey.KEY_ROLLENCE_BUBBLE_CHAT
        } catch (e: Exception) {
            true
        }
    }

    companion object {
        private const val SELLER_FILTER_THRESHOLD = 3
        private const val ONE_MILLION = 1_000_000L
        const val OPERATIONAL_INSIGHT_NEXT_MONDAY = "topchat_operational_insight_next_monday"
        const val BUBBLE_TICKER_PREF_NAME = "topchat_seller_bubble_chat_ticker"
        val arrayFilterParam = arrayListOf(
                PARAM_FILTER_ALL,
                PARAM_FILTER_UNREAD,
                PARAM_FILTER_UNREPLIED
        )
    }
}
