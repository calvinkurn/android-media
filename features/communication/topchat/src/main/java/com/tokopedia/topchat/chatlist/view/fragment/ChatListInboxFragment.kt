package com.tokopedia.topchat.chatlist.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.chat_common.util.EndlessRecyclerViewScrollUpListener
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.stories.widget.StoriesWidgetManager
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.storiesManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.di.ActivityComponentFactory
import com.tokopedia.topchat.chatlist.domain.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.TopChatListFilterEnum
import com.tokopedia.topchat.chatlist.domain.pojo.chatlistticker.ChatListTickerResponse
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.view.adapter.ChatListAdapter
import com.tokopedia.topchat.chatlist.view.adapter.decoration.ChatListItemDecoration
import com.tokopedia.topchat.chatlist.view.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.view.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.view.listener.ChatListTickerListener
import com.tokopedia.topchat.chatlist.view.uimodel.ChatListTickerUiModel
import com.tokopedia.topchat.chatlist.view.uimodel.EmptyChatModel
import com.tokopedia.topchat.chatlist.view.uimodel.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.view.uimodel.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatListWebSocketViewModel
import com.tokopedia.topchat.chatlist.view.widget.BroadcastButtonLayout
import com.tokopedia.topchat.chatlist.view.widget.BroadcastButtonLayout.Companion.BROADCAST_FAB_LABEL_PREF_NAME
import com.tokopedia.topchat.chatlist.view.widget.BroadcastButtonLayout.Companion.BROADCAST_FAB_LABEL_ROLLENCE_KEY
import com.tokopedia.topchat.chatlist.view.widget.FilterMenu
import com.tokopedia.topchat.chatlist.view.widget.OperationalInsightBottomSheet
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.custom.ChatFilterView
import com.tokopedia.topchat.chatroom.view.listener.TopChatRoomFlexModeListener
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt
import com.tokopedia.topchat.common.util.Utils
import com.tokopedia.topchat.common.util.Utils.getOperationalInsightStateReport
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
open class ChatListInboxFragment :
    BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
    ChatListItemListener,
    LifecycleOwner,
    ChatListTickerListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var chatListAnalytics: ChatListAnalytic

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var abTestPlatform: AbTestPlatform

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider.get(ChatItemListViewModel::class.java)
    }
    private val webSocket by lazy {
        viewModelProvider.get(ChatListWebSocketViewModel::class.java)
    }

    private lateinit var performanceMonitoring: PerformanceMonitoring

    private val mStoriesWidgetManager by storiesManager(StoriesEntryPoint.TopChatList) {
        setScrollingParent(rv)
    }

    @RoleType
    private var role: Int = RoleType.BUYER
    private var itemPositionLongClicked: Int = -1
    private var chatBannedSellerTicker: Ticker? = null
    private var rv: RecyclerView? = null
    private var rvAdapter: ChatListAdapter? = null
    private var chatFilter: ChatFilterView? = null
    private var emptyUiModel: Visitable<*>? = null
    private var broadCastButton: BroadcastButtonLayout? = null
    var chatRoomFlexModeListener: TopChatRoomFlexModeListener? = null
    var stopTryingIndicator = false

    private var currentActiveMessageId: String? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId() = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "chatlist"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(getFpmKey())
        initRoleFromChatRoom()
        initWebSocket()
        setHasOptionsMenu(false)
    }

    override fun onStart() {
        super.onStart()
        processPendingMessage()
    }

    private fun processPendingMessage() {
        webSocket.pendingMessages.entries.removeAll { entry ->
            val pendingMessage = entry.value
            processIncomingMessage(
                newChat = pendingMessage.message,
                counterIncrement = pendingMessage.count,
                isReplyFromActiveRoom = pendingMessage.isReplyFromActiveRoom
            )
            true
        }
    }

    private fun initRoleFromChatRoom() {
        // From ChatRoom with Flex Foldables
        role = arguments?.getInt(Constant.CHAT_USER_ROLE_KEY) ?: RoleType.BUYER
        assignRole(role)
    }

    private fun initWebSocket() {
        webSocket.onRoleChanged(role)
        webSocket.connectWebSocket()
    }

    private fun assignRole(@RoleType chosenRole: Int?): Boolean {
        chosenRole?.let {
            this.role = chosenRole
            return true
        }
        return false
    }

    private fun getFpmKey() = if (GlobalConfig.isSellerApp()) {
        TopChatAnalytics.FPM_CHAT_LIST_SELLERAPP
    } else {
        TopChatAnalytics.FPM_CHAT_LIST
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)?.also {
            initView(it)
            setUpRecyclerView()
            setupObserver()
            setupSellerBroadcastButtonObserver()
            setupSellerBroadcast()
            setupChatSellerBannedStatus()
            setupEmptyModel()
            setupFilter()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycleObserver()
    }

    override fun onScrollToTop() {}

    override fun onOperationalInsightTickerShown(element: ShopChatTicker) {
        TopChatAnalyticsKt.eventViewOperationalInsightTicker(
            shopId = userSession.shopId,
            stateReport = getOperationalInsightStateReport(element.isMaintain)
        )
    }

    override fun onOperationalInsightTickerClicked(element: ShopChatTicker) {
        val operationalInsightBottomSheet = OperationalInsightBottomSheet()
        operationalInsightBottomSheet.setData(element, userSession.shopId)
        operationalInsightBottomSheet.show(childFragmentManager, FilterMenu.TAG)
        TopChatAnalyticsKt.eventClickOperationalInsightTicker(
            shopId = userSession.shopId,
            stateReport = getOperationalInsightStateReport(element.isMaintain)
        )
    }

    override fun onOperationalInsightCloseButtonClicked(visitable: Visitable<*>) {
        adapter?.removeElement(visitable)
        viewModel.saveNextMondayDate()
        if (visitable is ShopChatTicker) {
            TopChatAnalyticsKt.eventClickCloseOperationalInsightTicker(
                shopId = userSession.shopId,
                stateReport = getOperationalInsightStateReport(visitable.isMaintain)
            )
        }
    }

    private fun setupLifecycleObserver() {
        viewLifecycleOwner.lifecycle.addObserver(webSocket)
    }

    private fun setupFilter() {
        chatFilter?.show()
        chatFilter?.init(isTabSeller())
        chatFilter?.setFilterListener(
            object : ChatFilterView.FilterListener {
                override fun onFilterChanged(filterType: TopChatListFilterEnum) {
                    viewModel.filter = filterType
                    loadInitialData()
                }
            }
        )
    }

    private fun setupChatSellerBannedStatus() {
        if (!isTabSeller()) return
        viewModel.chatBannedSellerStatus.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> updateChatBannedSellerStatus(it.data)
                    else -> {
                        // no-op
                    }
                }
            }
        )
    }

    private fun setupEmptyModel() {
        emptyUiModel = emptyDataViewModel
    }

    private fun updateChatBannedSellerStatus(isBanned: Boolean) {
        if (isBanned) {
            showBannedTicker()
        } else {
            chatBannedSellerTicker?.hide()
        }
    }

    private fun showBannedTicker() {
        chatBannedSellerTicker?.apply {
            val description = this.context.getString(R.string.desc_topchat_chat_banned_seller)
            show()
            tickerTitle = this.context.getString(R.string.title_topchat_chat_banned_seller)
            setHtmlDescription(description)
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    RouteManager.route(context, linkUrl.toString())
                }

                override fun onDismiss() {}
            })
        }
    }

    private fun setupSellerBroadcast() {
        if (!isTabSeller()) {
            broadCastButton?.hide()
        } else {
            viewModel.loadChatBlastSellerMetaData()
            viewModel.loadChatBlastSellerMetaData()
        }
    }

    private fun setupSellerBroadcastButtonObserver() {
        viewModel.broadCastButtonVisibility.observe(
            viewLifecycleOwner
        ) { visibility ->
            broadCastButton?.toggleBroadcastButton(
                shouldShow = visibility,
                shouldShowLabel = shouldShowBroadcastFabNewLabel()
            )
        }
        viewModel.broadCastButtonUrl.observe(
            viewLifecycleOwner,
            Observer { applink ->
                if (applink.isNullOrEmpty()) return@Observer
                broadCastButton?.setOnClickListener {
                    if (isSellerMigrationEnabled(context)) {
                        val screenName = SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT
                        val intent = context?.let { context ->
                            SellerMigrationActivity.createIntent(
                                context = context,
                                featureName = SellerMigrationFeatureName.FEATURE_BROADCAST_CHAT,
                                screenName = screenName,
                                appLinks = arrayListOf(
                                    ApplinkConstInternalSellerapp.SELLER_HOME_CHAT,
                                    applink
                                )
                            )
                        }
                        startActivity(intent)
                    } else {
                        chatListAnalytics.eventClickBroadcastButton()
                        RouteManager.route(context, applink)
                    }
                    markBroadcastNewLabel()
                }
            }
        )
    }

    private fun markBroadcastNewLabel() {
        if (shouldShowBroadcastFabNewLabel()) {
            viewModel.saveBooleanCache(
                cacheName = "${BROADCAST_FAB_LABEL_PREF_NAME}_${userSession.userId}",
                value = false
            )
            broadCastButton?.toggleBroadcastLabel(
                shouldShowLabel = false
            )
        }
    }

    private fun shouldShowBroadcastFabNewLabel(): Boolean {
        val labelCache = viewModel.getBooleanCache(
            "${BROADCAST_FAB_LABEL_PREF_NAME}_${userSession.userId}"
        )
        val rollenceValue = getRollenceValue(BROADCAST_FAB_LABEL_ROLLENCE_KEY)
        return labelCache && rollenceValue
    }

    private fun initView(view: View) {
        broadCastButton = view.findViewById(R.id.layout_fab_broadcast)
        chatBannedSellerTicker = view.findViewById(R.id.ticker_ban_status)
        rv = view.findViewById(R.id.recycler_view)
        chatFilter = view.findViewById(R.id.cf_chat_list)
    }

    private fun setUpRecyclerView() {
        rv?.apply {
            setHasFixedSize(true)
            for (i in 0 until itemDecorationCount) {
                removeItemDecorationAt(i)
            }
            addItemDecoration(ChatListItemDecoration(context))
            itemAnimator = null
        }
    }

    private fun setupObserver() {
        setupWebSocketObserver()
        viewModel.mutateChatList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        onSuccessGetChatList(it.data.data)
                        if (GlobalConfig.isSellerApp() && isFirstPage()) {
                            viewModel.getOperationalInsight(userSession.shopId)
                        } else if (!GlobalConfig.isSellerApp() && isFirstPage()) {
                            viewModel.getChatListTicker()
                        }
                    }
                    is Fail -> onFailGetChatList(it.throwable)
                }
            }
        )
        viewModel.deleteChat.observe(
            viewLifecycleOwner,
            Observer { result ->
                when (result) {
                    is Success -> {
                        adapter?.deleteItem(itemPositionLongClicked, emptyUiModel)
                        decreaseNotificationCounter()
                        showToaster(R.string.title_success_delete_chat)
                    }
                    is Fail -> view?.let {
                        Toaster.build(
                            it,
                            getString(R.string.delete_chat_default_error_message),
                            Snackbar.LENGTH_LONG,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        )
        viewModel.isChatAdminEligible.observe(
            viewLifecycleOwner,
            Observer { result ->
                when (result) {
                    is Success -> {
                        result.data.let { isEligible ->
                            if (isEligible) {
                                loadInitialData()
                            } else {
                                onChatAdminNoAccess()
                            }
                        }
                    }
                    is Fail -> {
                        showGetListError(result.throwable)
                    }
                }
            }
        )
        viewModel.chatOperationalInsight.observe(viewLifecycleOwner) {
            if (it is Success && it.data.showTicker == true) {
                adapter?.addElement(0, it.data)
            } else if (viewModel.shouldShowBubbleTicker() && Utils.getShouldBubbleChatEnabled()) {
                addBubbleChatTicker()
            }
        }

        viewModel.chatListTicker.observe(viewLifecycleOwner) { result ->
            if (result is Success) {
                val tickerChatListIndex = adapter?.list?.indexOfFirst { it -> it is ChatListTickerUiModel }

                if (tickerChatListIndex == RecyclerView.NO_POSITION) {
                    setChatListTickerBuyer(result.data)
                    setChatListTickerSeller(result.data)
                }
            }
        }
        observeWhitelist()
    }

    private fun observeWhitelist() {
        lifecycleScope.launch {
            withStarted {
                launch {
                    viewModel.isWhitelistTopBot.collectLatest {
                        it?.let {
                            chatFilter?.updateIsWhiteListTopBot(it)
                        }
                    }
                }
            }
        }
    }

    private fun setChatListTickerBuyer(result: ChatListTickerResponse.ChatListTicker) {
        if (result.tickerBuyer.enable && !isTabSeller()) {
            val tickerChatListBuyer = ChatListTickerUiModel(
                result.tickerBuyer.message,
                result.tickerBuyer.tickerType,
                applink = ApplinkConst.TokoFood.TOKOFOOD_ORDER
            )
            adapter?.addElement(Int.ZERO, tickerChatListBuyer)
        }
    }

    private fun setChatListTickerSeller(result: ChatListTickerResponse.ChatListTicker) {
        if (result.tickerSeller.enable && isTabSeller()) {
            val tickerChatListSeller = ChatListTickerUiModel(
                result.tickerSeller.message,
                result.tickerSeller.tickerType,
                applink = String.EMPTY
            )
            adapter?.addElement(Int.ZERO, tickerChatListSeller)
        }
    }

    private fun addBubbleChatTicker() {
        val chatListTicker: ChatListTickerUiModel = ChatListTickerUiModel(
            message = getString(R.string.topchat_bubble_ticker_message),
            applink = ApplinkConstInternalMarketplace.TOPCHAT_BUBBLE_ACTIVATION
        ).apply {
            this.showCloseButton = true
            this.sharedPreferenceKey = ChatItemListViewModel.BUBBLE_TICKER_PREF_NAME
        }
        adapter?.addElement(Int.ZERO, chatListTicker)
    }

    override fun onDismissTicker(element: ChatListTickerUiModel) {
        adapter?.removeElement(element)
        if (element.sharedPreferenceKey.isNotBlank()) {
            viewModel.saveBooleanCache(
                cacheName = ChatItemListViewModel.BUBBLE_TICKER_PREF_NAME,
                value = false
            )
        }
    }

    private fun setupWebSocketObserver() {
        webSocket.itemChat.observe(
            viewLifecycleOwner,
            Observer { result ->
                when (result) {
                    is Success -> {
                        when (result.data) {
                            is IncomingChatWebSocketModel -> processIncomingMessage(
                                result.data as IncomingChatWebSocketModel
                            )
                            is IncomingTypingWebSocketModel -> processIncomingMessage(
                                result.data as IncomingTypingWebSocketModel
                            )
                            else -> {
                                // no-op
                            }
                        }
                    }
                    else -> {
                        // no-op
                    }
                }
            }
        )
    }

    private fun processIncomingMessage(
        newChat: IncomingChatWebSocketModel,
        counterIncrement: Int = 1,
        isReplyFromActiveRoom: Boolean = false
    ) {
        if (newChat.isForOtherRole(role, userSession.userId)) return
        val chatIndex = rvAdapter?.findChat(newChat) ?: return
        if (chatIndex == RecyclerView.NO_POSITION && viewModel.hasFilter()) return
        updateItemOnIndex(
            index = chatIndex,
            newChat = newChat,
            counterIncrement = counterIncrement,
            isReplyFromActiveRoom = isReplyFromActiveRoom
        )
    }

    private fun updateItemOnIndex(
        index: Int,
        newChat: IncomingChatWebSocketModel,
        readStatus: Int = ChatItemListViewHolder.STATE_CHAT_UNREAD,
        counterIncrement: Int = 1,
        isReplyFromActiveRoom: Boolean = false
    ) {
        adapter?.let { adapter ->
            when {
                index >= adapter.list.size -> {
                    return
                }
                // not found on list
                index == RecyclerView.NO_POSITION -> {
                    addNewChatToList(newChat)
                }
                // found on list, not the first
                index >= 0 -> {
                    val shouldUpdateReadStatus = !newChat.isFromMySelf(role, userSession.userId) &&
                        !isReplyFromActiveRoom
                    adapter.onNewIncomingChatMessage(
                        index = index,
                        newChat = newChat,
                        readStatus = readStatus,
                        pinnedMsgId = viewModel.pinnedMsgId,
                        counterIncrement = counterIncrement,
                        shouldUpdateReadStatus = shouldUpdateReadStatus
                    )
                }
            }
        }
    }

    private fun addNewChatToList(newChat: IncomingChatWebSocketModel) {
        if (chatRoomFlexModeListener?.isFlexMode() == true &&
            newChat.isFromMySelf(role, userSession.userId)
        ) {
            adapter?.activeChat?.first?.attributes?.contact?.let {
                adapter?.onNewItemChatFromSelfMessage(newChat, viewModel.pinnedMsgId, it)
                setIndicatorCurrentActiveChat(newChat.msgId)
            }
        } else if (!newChat.isFromMySelf(role, userSession.userId)) {
            adapter?.onNewItemChatMessage(newChat, viewModel.pinnedMsgId)
            increaseNotificationCounter()
        }
    }

    private fun processIncomingMessage(newItem: IncomingTypingWebSocketModel) {
        adapter?.let { adapter ->
            if (
                (adapter.list.isNotEmpty() && adapter.list[0] is LoadingModel) ||
                adapter.list.isEmpty()
            ) {
                return
            }

            val existingThread = adapter.list.find {
                it is ItemChatListPojo && it.msgId == newItem.messageId
            }

            val index = adapter.list.indexOf(existingThread)

            when {
                index >= 0 -> {
                    if (newItem.isTyping) {
                        adapter.notifyItemChanged(index, ChatItemListViewHolder.PAYLOAD_TYPING_STATE)
                    } else {
                        adapter.notifyItemChanged(index, ChatItemListViewHolder.PAYLOAD_STOP_TYPING_STATE)
                    }
                }
            }
        }
    }

    private fun onSuccessGetChatList(data: ChatListPojo.ChatListDataPojo) {
        renderList(data.list, data.hasNext)
        if (role == RoleType.BUYER) {
            mStoriesWidgetManager.updateStories(
                data.list.mapNotNull { if (it.isSeller()) it.id else null }
            )
        }
        fpmStopTrace()
        setIndicatorCurrentActiveChat(currentActiveMessageId)
    }

    private fun onFailGetChatList(throwable: Throwable) {
        showGetListError(throwable)
        fpmStopTrace()
    }

    private fun fpmStopTrace() {
        if (::performanceMonitoring.isInitialized && isFirstPage()) {
            performanceMonitoring.stopTrace()
        }
    }

    private fun isFirstPage(): Boolean = currentPage == 1

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollUpListener(getRecyclerView(view)?.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                showLoading()
                if (totalItemsCount > 1) {
                    loadData(page)
                }
            }
        }
    }

    override fun getAdapterTypeFactory(): ChatListTypeFactoryImpl {
        return ChatListTypeFactoryImpl(this, this, chatListAnalytics)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory> {
        return ChatListAdapter(this, adapterTypeFactory).also {
            rvAdapter = it
        }
    }

    override fun getAdapter(): ChatListAdapter? {
        super.getAdapter()?.let {
            return it as ChatListAdapter
        }
        return null
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadInitialData() {
        super.loadInitialData()
        viewModel.clearPinUnpinData()
        viewModel.resetState()
        if (isTabSeller()) {
            viewModel.loadTopBotWhiteList()
            viewModel.loadChatBannedSellerStatus()
        }
    }

    override fun initInjector() {
        generateChatListComponent()
            .inject(this)
    }

    override fun onChatListTickerClicked(appLink: String) {
        if (appLink.isNotBlank()) {
            context?.let {
                when (appLink) {
                    ApplinkConstInternalMarketplace.TOPCHAT_BUBBLE_ACTIVATION -> {
                        TopChatAnalyticsKt.eventClickBubbleChatRecommendationTicker(userSession.shopId)
                    }
                    ApplinkConst.TokoFood.TOKOFOOD_ORDER -> {
                        chatListAnalytics.clickChatDriverTicker(getRoleStr())
                    }
                }
                RouteManager.route(it, appLink)
            }
        }
    }

    override fun onChatListTickerImpressed(appLink: String) {
        if (appLink.isNotBlank()) {
            when (appLink) {
                ApplinkConstInternalMarketplace.TOPCHAT_BUBBLE_ACTIVATION -> {
                    TopChatAnalyticsKt.eventImpressionBubbleChatRecommendationTicker(userSession.shopId)
                }
                ApplinkConst.TokoFood.TOKOFOOD_ORDER -> {
                    chatListAnalytics.impressOnChatDriverTicker(getRoleStr())
                }
            }
        }
    }

    protected open fun generateChatListComponent() = ActivityComponentFactory.instance.createChatListComponent(
        requireActivity().application,
        requireContext()
    )

    override fun loadData(page: Int) {
        viewModel.getChatListMessage(page, role)
    }

    override fun chatItemClicked(
        element: ItemChatListPojo,
        itemPosition: Int,
        lastActiveChat: Pair<ItemChatListPojo?, Int?>
    ) {
        activity?.let {
            trackChatItemClicked(element, itemPosition)
            webSocket.activeRoom = element.msgId
            if (isFromTopChatRoom() && chatRoomFlexModeListener?.isFlexMode() == true) {
                handleChatRoomAndFlexMode(element, itemPosition, lastActiveChat)
            }
        }
    }

    private fun trackChatItemClicked(
        element: ItemChatListPojo,
        position: Int
    ) {
        val role = if (isTabSeller()) {
            ChatListActivity.SELLER_ANALYTICS_LABEL
        } else {
            ChatListActivity.BUYER_ANALYTICS_LABEL
        }

        with(chatListAnalytics) {
            eventClickChatList(
                role = role,
                messageId = element.msgId,
                sourceName = getTrackerSourceName(element),
                readStatus = element.getLiteralReadStatus(),
                label = element.label,
                iconLabel = element.labelIcon,
                counter = element.totalUnread,
                position = (position + 1).toString()
            )
        }
    }

    private fun getTrackerSourceName(element: ItemChatListPojo): String {
        return when {
            element.isReplyTopBot() -> ChatListAnalytic.Other.SMART_REPLY
            element.label.isNotBlank() -> ChatListAnalytic.Other.BROADCAST
            else -> ChatListAnalytic.Other.MANUAL
        }
    }

    private fun getRoleStr(): String {
        return if (isTabSeller()) ChatListAnalytic.Other.SELLER else ChatListAnalytic.Other.BUYER
    }

    private fun handleChatRoomAndFlexMode(
        element: ItemChatListPojo,
        itemPosition: Int,
        lastActiveChat: Pair<ItemChatListPojo?, Int?>
    ) {
        if (element.msgId != adapter?.activeChat?.first?.msgId) {
            adapter?.notifyItemChanged(itemPosition, element)
            adapter?.deselectActiveChatIndicator(element)
            adapter?.activeChat = lastActiveChat
            chatRoomFlexModeListener?.onClickAnotherChat(element)
            currentActiveMessageId = element.msgId
        }
    }

    override fun deleteChat(element: ItemChatListPojo, itemPosition: Int) {
        viewModel.chatMoveToTrash(element.msgId)
        itemPositionLongClicked = itemPosition
    }

    override fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        viewModel.markChatAsRead(msgIds, result)
    }

    override fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        viewModel.markChatAsUnread(msgIds, result)
    }

    override fun increaseNotificationCounter() {}

    override fun decreaseNotificationCounter() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DETAIL_MESSAGE) {
            val extras = data?.extras ?: return
            val msgId = extras.getString(ApplinkConst.Chat.MESSAGE_ID, "") ?: ""
            if (msgId.isEmpty()) return
            when (resultCode) {
                TopChatInternalRouter.Companion.CHAT_DELETED_RESULT_CODE -> {
                    onReturnFromChatRoomChatDeleted(msgId)
                }
            }
        }
    }

    private fun onReturnFromChatRoomChatDeleted(msgId: String) {
        webSocket.deletePendingMsgWithId(msgId)
        rvAdapter?.deleteItem(msgId)
        if (isListEmpty) {
            showEmpty()
        }
        showToaster(R.string.title_success_delete_chat)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeLiveDataObserver()
        webSocket.clearPendingMessages()
        viewModel.flush()
    }

    private fun removeLiveDataObserver() {
        viewModel.mutateChatList.removeObservers(this)
        viewModel.deleteChat.removeObservers(this)
        viewModel.broadCastButtonVisibility.removeObservers(this)
        viewModel.broadCastButtonVisibility.removeObservers(this)
        viewModel.broadCastButtonUrl.removeObservers(this)
        viewModel.chatBannedSellerStatus.removeObservers(this)
        viewModel.isChatAdminEligible.removeObservers(this)
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        var title = ""
        var subtitle = ""
        var image = ""
        var ctaText = ""
        var ctaApplink = ""
        var isTopAds = false
        activity?.let {
            when (role) {
                RoleType.SELLER -> {
                    title = it.getString(R.string.title_topchat_empty_chat)
                    subtitle = it.getString(R.string.seller_empty_chat_subtitle)
                    image = CHAT_SELLER_EMPTY
                    ctaText = it.getString(R.string.title_topchat_manage_product)
                    ctaApplink = ApplinkConstInternalSellerapp.CENTRALIZED_PROMO + "?redirect_to_sellerapp=true"
                    isTopAds = true
                }
                RoleType.BUYER -> {
                    title = it.getString(R.string.title_topchat_empty_chat)
                    subtitle = it.getString(R.string.buyer_empty_chat_subtitle)
                    image = CHAT_BUYER_EMPTY
                }
            }

            if (viewModel.filter == TopChatListFilterEnum.FILTER_UNREAD ||
                viewModel.filter == TopChatListFilterEnum.FILTER_UNREPLIED
            ) {
                image = CHAT_BUYER_EMPTY
                title = it.getString(R.string.empty_chat_read_all_title)
                subtitle = it.getString(R.string.empty_chat_read_all_subtitle)
                ctaText = ""
                ctaApplink = ""
            } else if (viewModel.filter == TopChatListFilterEnum.FILTER_TOPBOT) {
                image = CHAT_SELLER_EMPTY_SMART_REPLY
                title = it.getString(R.string.empty_chat_smart_reply)
                subtitle = ""
                ctaText = ""
                ctaApplink = ""
            }
        }

        return EmptyChatModel(title, subtitle, image, ctaText, ctaApplink, isTopAds)
    }

    override fun trackChangeReadStatus(element: ItemChatListPojo) {
        chatListAnalytics.trackChangeReadStatus(element)
    }

    override fun trackDeleteChat(element: ItemChatListPojo) {
        chatListAnalytics.trackDeleteChat(element)
    }

    override fun isTabSeller(): Boolean {
        return role == RoleType.SELLER
    }

    override fun getSupportChildFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun getStoriesWidgetManager(): StoriesWidgetManager? {
        return mStoriesWidgetManager
    }

    override fun pinUnpinChat(element: ItemChatListPojo, position: Int, isPinChat: Boolean) {
        val msgId = element.msgId
        viewModel.pinUnpinChat(
            msgId,
            isPinChat,
            {
                element.updatePinStatus(isPinChat)
                if (isPinChat) {
                    // chat pinned.
                    onSuccessPinChat(element, position)
                } else if (!isPinChat && viewModel.unpinnedMsgId.contains(element.msgId)) {
                    // chat unpinned and can be restored to current list.
                    onSuccessUnpinPreviouslyLoadedChat(element, position)
                } else {
                    // check if it can be repositioned in the middle or append at the end when
                    // hasNext is false. else chat unpinned and can not be restored
                    // to current list, just remove the item.
                    onSuccessUnpinChat(element, position)
                }
            },
            {
                showSnackbarError(it)
            }
        )
    }

    private fun onSuccessUnpinChat(element: ItemChatListPojo, position: Int) {
        adapter?.unpinChatItem(
            element,
            position,
            viewModel.pinnedMsgId.size,
            viewModel.chatListHasNext,
            viewModel.unpinnedMsgId
        )
        viewModel.pinnedMsgId.remove(element.msgId)
        showToaster(R.string.title_success_unpin_chat)
    }

    private fun onSuccessPinChat(element: ItemChatListPojo, position: Int) {
        adapter?.pinChatItem(element, position)
        rv?.scrollToPosition(0)
        viewModel.pinnedMsgId.add(element.msgId)
        showToaster(R.string.title_success_pin_chat)
    }

    private fun onSuccessUnpinPreviouslyLoadedChat(element: ItemChatListPojo, position: Int) {
        adapter?.putToOriginalPosition(element, position, viewModel.pinnedMsgId.size)
        viewModel.pinnedMsgId.remove(element.msgId)
        showToaster(R.string.title_success_unpin_chat)
    }

    private fun showToaster(@StringRes message: Int) {
        view?.let {
            Toaster.build(it, it.context.getString(message), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                .show()
        }
    }

    private fun showSnackbarError(throwable: Throwable) {
        view?.let {
            val errorMsg = ErrorHandler.getErrorMessage(it.context, throwable)
            Toaster.build(it, errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onChatAdminNoAccess() {
        swipeToRefresh?.isEnabled = false
        adapter?.showNoAccessView()
    }

    override fun returnToSellerHome() {}

    private fun isFromTopChatRoom(): Boolean {
        return activity is TopChatRoomActivity
    }

    fun setIndicatorCurrentActiveChat(msgId: String? = null) {
        if (isFromTopChatRoom() && chatRoomFlexModeListener?.isFlexMode() == true) {
            val currentActiveChat = if (msgId.isNullOrEmpty()) {
                arguments?.getString(Constant.CHAT_CURRENT_ACTIVE)
            } else {
                msgId
            }
            if (currentActiveChat != null) {
                val pair = adapter?.getItemPosition(currentActiveChat)
                val activateChat = pair?.first
                val activateChatPosition = pair?.second
                if (activateChat != null && activateChatPosition != null) {
                    activateChat.markAsActive()
                    adapter?.notifyItemChanged(activateChatPosition, activateChat)
                    adapter?.activeChat = pair
                    if (!stopTryingIndicator) stopTryingIndicator = true
                    currentActiveMessageId = currentActiveChat
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isFromTopChatRoom()) {
            adapter?.resetActiveChatIndicator()
        }
    }

    private fun getRollenceValue(key: String): Boolean {
        return try {
            abTestPlatform.getString(key, "").isNotEmpty()
        } catch (t: Throwable) {
            Timber.d(t)
            false
        }
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        const val CHAT_SELLER_EMPTY = TokopediaImageUrl.CHAT_SELLER_EMPTY
        const val CHAT_BUYER_EMPTY = TokopediaImageUrl.CHAT_BUYER_EMPTY
        const val CHAT_SELLER_EMPTY_SMART_REPLY = "https://images.tokopedia.net/android/others/toped_confused.webp"
        const val TAG = "ChatListFragment"
        private const val NO_INT_ARGUMENT = 0

        fun createFragment(
            @RoleType role: Int?,
            currentActiveChat: String?
        ): ChatListInboxFragment {
            val fragment = ChatListInboxFragment()
            fragment.arguments = createBundle(role, currentActiveChat)
            return fragment
        }

        private fun createBundle(
            @RoleType role: Int? = null,
            currentActiveChat: String? = null
        ): Bundle {
            val bundle = Bundle()
            if (role != null) {
                bundle.putInt(Constant.CHAT_USER_ROLE_KEY, role)
            }
            if (currentActiveChat != null) {
                bundle.putString(Constant.CHAT_CURRENT_ACTIVE, currentActiveChat)
            }
            return bundle
        }
    }
}
