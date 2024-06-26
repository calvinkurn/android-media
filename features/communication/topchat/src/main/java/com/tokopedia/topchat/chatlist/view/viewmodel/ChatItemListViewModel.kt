package com.tokopedia.topchat.chatlist.view.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shopadmin.common.util.AccessId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueries.MUTATION_CHAT_MARK_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueries.MUTATION_CHAT_MARK_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_MESSAGE_IDS
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_SELLER
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_USER
import com.tokopedia.topchat.chatlist.data.util.TopChatListResourceProvider
import com.tokopedia.topchat.chatlist.domain.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.domain.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListParam
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.TopChatListFilterEnum
import com.tokopedia.topchat.chatlist.domain.pojo.chatblastseller.ChatBlastSellerMetadata
import com.tokopedia.topchat.chatlist.domain.pojo.chatlistticker.ChatListTickerResponse
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.topchat.chatlist.domain.usecase.ChatBanedSellerUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatBlastSellerMetaDataUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListMessageUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListTickerUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatWhitelistFeature
import com.tokopedia.topchat.chatlist.domain.usecase.GetOperationalInsightUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.MutationPinChatUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.MutationUnpinChatUseCase
import com.tokopedia.topchat.chatlist.view.TopChatListAction
import com.tokopedia.topchat.chatlist.view.uimodel.TopChatListFilterUiState
import com.tokopedia.topchat.chatroom.view.uimodel.ReplyParcelableModel
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.data.TopChatResult
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.common.util.Utils
import com.tokopedia.topchat.common.util.Utils.getBuildVersion
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

interface ChatItemListContract {
    fun getChatListMessage(page: Int, filter: TopChatListFilterEnum, tab: String)
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
    private val getChatListTickerUseCase: GetChatListTickerUseCase,
    private val getChatBlastSellerMetaDataUseCase: GetChatBlastSellerMetaDataUseCase,
    private val cacheManager: TopchatCacheManager,
    private val userSession: UserSessionInterface,
    private val resourceProvider: TopChatListResourceProvider,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main), ChatItemListContract {

    private val _actionFlow =
        MutableSharedFlow<TopChatListAction>(extraBufferCapacity = 16)

    var filter: TopChatListFilterEnum = TopChatListFilterEnum.FILTER_ALL
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

    private val _isWhitelistTopBot = MutableStateFlow<Boolean?>(null)
    val isWhitelistTopBot: StateFlow<Boolean?>
        get() = _isWhitelistTopBot

    private val _isChatAdminEligible = MutableLiveData<Result<Boolean>>()
    val isChatAdminEligible: LiveData<Result<Boolean>>
        get() = _isChatAdminEligible

    private val _chatOperationalInsight = MutableLiveData<Result<ShopChatTicker>>()
    val chatOperationalInsight: LiveData<Result<ShopChatTicker>>
        get() = _chatOperationalInsight

    private val _chatListTicker = MutableLiveData<Result<ChatListTickerResponse.ChatListTicker>>()
    val chatListTicker: LiveData<Result<ChatListTickerResponse.ChatListTicker>>
        get() = _chatListTicker

    private var getChatAdminAccessJob: Job? = null

    val chatListHasNext: Boolean get() = getChatListUseCase.hasNext
    val pinnedMsgId: HashSet<String> = HashSet()
    val unpinnedMsgId: HashSet<String> = HashSet()
    val isAdminHasAccess: Boolean
        get() = (_isChatAdminEligible.value as? Success)?.data == true

    private val _chatListFilterUiState = MutableStateFlow(TopChatListFilterUiState())
    val chatListFilterUiState = _chatListFilterUiState.asStateFlow()

    init {
        setBuyerFilterData()
    }

    fun processAction(action: TopChatListAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    fun setupViewModelObserver() {
        _actionFlow.process()
    }

    private fun Flow<TopChatListAction>.process() {
        onEach {
            when (it) {
                is TopChatListAction.SetFilter -> {
                    _chatListFilterUiState.update { uiState ->
                        uiState.copy(selectedFilter = it.filter)
                    }
                }
                else -> Unit
            }
        }
            .flowOn(dispatcher.immediate)
            .launchIn(viewModelScope)
    }

    override fun getChatListMessage(page: Int, filter: TopChatListFilterEnum, tab: String) {
        whenChatAdminAuthorized(tab) {
            queryGetChatListMessage(page, filter, tab)
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

    private fun queryGetChatListMessage(page: Int, filter: TopChatListFilterEnum, tab: String) {
        val p = ChatListParam(page, filter.filterString, tab)
        launch {
            try {
                val result = getChatListUseCase(p)
                if (page == 1) {
                    pinnedMsgId.addAll(result.pinned)
                }
                unpinnedMsgId.addAll(result.unpinned)
                _mutateChatList.value = Success(result.chatListPojo)
            } catch (e: Exception) {
                _mutateChatList.value = Fail(e)
            }
        }
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
            getChatAdminAccessJob = viewModelScope.launch {
                try {
                    withContext(dispatcher.io) {
                        _isChatAdminEligible.postValue(
                            Success(getIsChatAdminAccessAuthorized())
                        )
                    }
                } catch (throwable: Throwable) {
                    _isChatAdminEligible.value = Fail(throwable)
                }
            }
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
        return AuthorizeAccessUseCase.createRequestParams(
            userSession.shopId.toLongOrZero(),
            AccessId.CHAT
        ).let { requestParams ->
            authorizeAccessUseCase.execute(requestParams)
        }
    }

    override fun chatMoveToTrash(messageId: String) {
        launch {
            try {
                val result = moveChatToTrashUseCase(messageId)
                if (result.chatMoveToTrash.list.isNotEmpty()) {
                    val deletedChat = result.chatMoveToTrash.list.first()
                    if (deletedChat.isSuccess == Constant.INT_STATUS_TRUE) {
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

    override fun markChatAsRead(
        msgIds: List<String>,
        result: (Result<ChatChangeStateResponse>) -> Unit
    ) {
        val query = MUTATION_CHAT_MARK_READ
        changeMessageState(query, msgIds, result)
    }

    override fun markChatAsUnread(
        msgIds: List<String>,
        result: (Result<ChatChangeStateResponse>) -> Unit
    ) {
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

        launchCatchError(
            block = {
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
        viewModelScope.launch {
            try {
                getChatBlastSellerMetaDataUseCase(Unit).collect {
                    getChatAdminAccessJob?.join()
                    if (isAdminHasAccess) {
                        onSuccessLoadChatBlastSellerMetaData(it.chatBlastSellerMetadata)
                    } else {
                        onErrorLoadChatBlastSellerMetaData()
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                onErrorLoadChatBlastSellerMetaData()
            }
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
        viewModelScope.launch {
            try {
                chatWhitelistFeature(GetChatWhitelistFeature.PARAM_VALUE_FEATURE_TOPBOT).collectLatest {
                    when (it) {
                        is TopChatResult.Success -> {
                            _isWhitelistTopBot.value = it.data.chatWhitelistFeature.isWhitelist
                            setSellerFilterData(it.data.chatWhitelistFeature.isWhitelist)
                        }
                        is TopChatResult.Error -> {
                            _isWhitelistTopBot.value = false
                            setSellerFilterData(false)
                        }
                        is TopChatResult.Loading -> Unit
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                _isWhitelistTopBot.value = false
            }
        }
    }

    private fun setSellerFilterData(isWhiteListed: Boolean) {
        val listFilterSeller: MutableList<Pair<TopChatListFilterEnum, String>> = mutableListOf(
            Pair(TopChatListFilterEnum.FILTER_ALL, resourceProvider.getStringResource(R.string.filter_chat_all)),
            Pair(TopChatListFilterEnum.FILTER_UNREAD, resourceProvider.getStringResource(R.string.filter_chat_unread)),
            Pair(TopChatListFilterEnum.FILTER_UNREPLIED, resourceProvider.getStringResource(R.string.filter_chat_unreplied))
        ).apply {
            if (isWhiteListed) {
                add(Pair(TopChatListFilterEnum.FILTER_TOPBOT, resourceProvider.getStringResource(R.string.filter_chat_smart_reply)))
            }
        }
        _chatListFilterUiState.update {
            it.copy(filterListSeller = listFilterSeller)
        }
    }

    private fun setBuyerFilterData() {
        val listFilterBuyer: MutableList<Pair<TopChatListFilterEnum, String>> = mutableListOf(
            Pair(TopChatListFilterEnum.FILTER_ALL, resourceProvider.getStringResource(R.string.filter_chat_all)),
            Pair(TopChatListFilterEnum.FILTER_UNREAD, resourceProvider.getStringResource(R.string.filter_chat_unread)),
            Pair(TopChatListFilterEnum.FILTER_BROADCAST, resourceProvider.getStringResource(R.string.filter_chat_broadcast))
        )
        _chatListFilterUiState.update {
            it.copy(filterListBuyer = listFilterBuyer)
        }
    }

    fun hasFilter(): Boolean {
        return filter != TopChatListFilterEnum.FILTER_ALL
    }

    private fun cancelAllUseCase() {
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

    fun getChatListTicker() {
        launchCatchError(block = {
            val response = getChatListTickerUseCase(Unit).chatlistTicker
            _chatListTicker.value = Success(response)
        }, onError = {
                _chatListTicker.value = Fail(it)
            })
    }

    private fun shouldShowOperationalInsightTicker(): Boolean {
        val nextMonday = cacheManager.getLongCache(getTickerPrefName(), 0)
        val todayTimeMillis = System.currentTimeMillis()
        return todayTimeMillis > nextMonday
    }

    fun saveNextMondayDate() {
        val newNextMonday = Utils.getNextParticularDay(Calendar.MONDAY)
        cacheManager.saveLongCache(getTickerPrefName(), newNextMonday)
    }

    private fun getTickerPrefName(): String {
        return "${OPERATIONAL_INSIGHT_NEXT_MONDAY}_${userSession.userId}"
    }

    fun shouldShowBubbleTicker(): Boolean {
        return getBooleanCache(BUBBLE_TICKER_PREF_NAME) &&
            (getBuildVersion() >= Build.VERSION_CODES.R)
    }

    fun saveBooleanCache(cacheName: String, value: Boolean) {
        cacheManager.saveState(cacheName, value)
    }

    fun getBooleanCache(cacheName: String): Boolean {
        return cacheManager.getPreviousState(cacheName, true)
    }

    companion object {
        private const val ONE_MILLION = 1_000_000L
        const val OPERATIONAL_INSIGHT_NEXT_MONDAY = "topchat_operational_insight_next_monday"
        const val BUBBLE_TICKER_PREF_NAME = "topchat_seller_bubble_chat_ticker"
    }
}
