package com.tokopedia.topchat.chatlist.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
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
import com.tokopedia.design.component.Menus
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.adapter.ChatListAdapter
import com.tokopedia.topchat.chatlist.adapter.decoration.ChatListItemDecoration
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_TOPBOT
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREPLIED
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.pojo.ChatListDataPojo
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel.Companion.arrayFilterParam
import com.tokopedia.topchat.chatlist.viewmodel.ChatListWebSocketViewModel
import com.tokopedia.topchat.chatlist.widget.FilterMenu
import com.tokopedia.topchat.chatroom.view.custom.ChatFilterView
import com.tokopedia.topchat.chatsetting.view.activity.ChatSettingActivity
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
class ChatListInboxFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ChatListItemListener, LifecycleOwner, InboxFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var chatListAnalytics: ChatListAnalytic

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy {
        viewModelProvider.get(ChatItemListViewModel::class.java)
    }
    private val webSocket by lazy {
        viewModelProvider.get(ChatListWebSocketViewModel::class.java)
    }

    private lateinit var performanceMonitoring: PerformanceMonitoring

    @RoleType
    private var role: Int = RoleType.BUYER
    private var itemPositionLongClicked: Int = -1
    private var filterChecked = 0
    private var filterMenu = FilterMenu()
    private var chatBannedSellerTicker: Ticker? = null
    private var rv: RecyclerView? = null
    private var rvAdapter: ChatListAdapter? = null
    private var chatFilter: ChatFilterView? = null
    private var emptyUiModel: Visitable<*>? = null
    private var broadCastButton: FloatingActionButton? = null
    private var containerListener: InboxFragmentContainer? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId() = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "chatlist"

    override fun onAttachActivity(context: Context?) {
        if (context is InboxFragmentContainer) {
            containerListener = context
        }
    }

    override fun onRoleChanged(role: Int) {
        if (!::viewModelFactory.isInitialized) return
        if (assignRole(role)) {
            viewModel.reset()
            chatFilter?.reset()
            chatFilter?.onRoleChanged(isTabSeller())
            webSocket.onRoleChanged(role)
            if (isResumed) {
                loadInitialData()
                setupSellerBroadcast()
            }
        }
    }

    override fun onPageClickedAgain() {
        rv?.scrollToPosition(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performanceMonitoring = PerformanceMonitoring.start(getFpmKey())
        initRole()
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

    private fun initRole() {
        assignRole(containerListener?.role)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.chat_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_chat_filter -> {
                chatListAnalytics.eventClickFilterChat()
                showFilterDialog()
                true
            }
            R.id.menu_chat_setting -> {
                val intent = ChatSettingActivity.getIntent(context, isTabSeller())
                startActivity(intent)
                true
            }
            R.id.menu_chat_search -> {
                RouteManager.route(context, ApplinkConstInternalMarketplace.CHAT_SEARCH)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)?.also {
            initView(it)
            setUpRecyclerView(it)
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

    private fun setupLifecycleObserver() {
        viewLifecycleOwner.lifecycle.addObserver(webSocket)
    }

    private fun setupFilter() {
        chatFilter?.show()
        chatFilter?.init(isTabSeller())
        chatFilter?.setFilterListener(
                object : ChatFilterView.FilterListener {
                    override fun onFilterChanged(filterType: String) {
                        viewModel.filter = filterType
                        loadInitialData()
                    }
                }
        )
    }

    private fun setupChatSellerBannedStatus() {
        if (!isTabSeller()) return
        viewModel.chatBannedSellerStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> updateChatBannedSellerStatus(it.data)
            }
        })
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
        viewModel.broadCastButtonVisibility.observe(viewLifecycleOwner, Observer { visibility ->
            when (visibility) {
                true -> {
                    broadCastButton?.show()
                }
                false -> broadCastButton?.hide()
            }
        })
        viewModel.broadCastButtonUrl.observe(viewLifecycleOwner, Observer { applink ->
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
                                        ApplinkConstInternalSellerapp.SELLER_HOME_CHAT, applink
                                )
                        )
                    }
                    startActivity(intent)
                } else {
                    chatListAnalytics.eventClickBroadcastButton()
                    RouteManager.route(context, applink)
                }
            }
        })
    }

    private fun initView(view: View) {
        broadCastButton = view.findViewById(R.id.fab_broadcast)
        chatBannedSellerTicker = view.findViewById(R.id.ticker_ban_status)
        rv = view.findViewById(R.id.recycler_view)
        chatFilter = view.findViewById(R.id.cf_chat_list)
    }

    private fun setUpRecyclerView(view: View) {
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
        viewModel.mutateChatList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetChatList(it.data.data)
                is Fail -> onFailGetChatList(it.throwable)
            }
        })
        viewModel.deleteChat.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    adapter?.deleteItem(itemPositionLongClicked, emptyUiModel)
                    decreaseNotificationCounter()
                    showToaster(R.string.title_success_delete_chat)
                }
                is Fail -> view?.let {
                    Toaster.make(it, getString(R.string.delete_chat_default_error_message), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                }
            }
        })
        viewModel.isWhitelistTopBot.observe(viewLifecycleOwner,
                Observer { isWhiteListTopBot ->
                    chatFilter?.updateIsWhiteListTopBot(isWhiteListTopBot)
                }
        )
        viewModel.isChatAdminEligible.observe(viewLifecycleOwner, Observer { result ->
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
        })
    }

    private fun setupWebSocketObserver() {
        webSocket.itemChat.observe(viewLifecycleOwner,
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
                            }
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
                //not found on list
                index == RecyclerView.NO_POSITION -> {
                    if (newChat.isFromMySelf(role, userSession.userId)) return
                    adapter.onNewItemChatMessage(newChat, viewModel.pinnedMsgId)
                    increaseNotificationCounter()
                }
                //found on list, not the first
                index >= 0 -> {
                    val shouldUpdateReadStatus = !newChat.isFromMySelf(role, userSession.userId)
                            && !isReplyFromActiveRoom
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

    fun processIncomingMessage(newItem: IncomingTypingWebSocketModel) {
        adapter?.let { adapter ->
            if (
                    (adapter.list.isNotEmpty() && adapter.list[0] is LoadingModel) ||
                    adapter.list.isEmpty() ||
                    filterChecked == arrayFilterParam.indexOf(PARAM_FILTER_READ)
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

    private fun animateWhenOnTop() {
        if ((getRecyclerView(view)?.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() == 0) {
            getRecyclerView(view)?.smoothScrollToPosition(0)
        }
    }

    private fun onSuccessGetChatList(data: ChatListDataPojo) {
        renderList(data.list, data.hasNext)
        fpmStopTrace()
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
        return ChatListTypeFactoryImpl(this, chatListAnalytics)
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

    private fun showFilterDialog() {
        activity?.let {
            if (filterMenu.isAdded) return@let
            val itemMenus = ArrayList<Menus.ItemMenus>()
            val arrayFilterString = viewModel.getFilterTittles(it, isTabSeller())

            for ((index, title) in arrayFilterString.withIndex()) {
                if (index == filterChecked) itemMenus.add(Menus.ItemMenus(title, true))
                else itemMenus.add(Menus.ItemMenus(title, false))
            }

            val title = getString(com.tokopedia.design.R.string.label_filter)
            filterMenu.apply {
                setTitle(title)
                setItemMenuList(itemMenus)
                setOnItemMenuClickListener { menu, pos ->
                    chatListAnalytics.eventClickListFilterChat(menu.title.toLowerCase())
                    filterChecked = pos
                    loadInitialData()
                    dismiss()
                }
            }.show(childFragmentManager, FilterMenu.TAG)
        }
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
        DaggerChatListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatListContextModule(context?.let { ChatListContextModule(it) })
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {
        viewModel.getChatListMessage(page, role)
    }

    override fun chatItemClicked(element: ItemChatListPojo, itemPosition: Int) {
        activity?.let {
            with(chatListAnalytics) {
                eventClickChatList(
                        if (isTabSeller()) ChatListActivity.SELLER_ANALYTICS_LABEL
                        else ChatListActivity.BUYER_ANALYTICS_LABEL)
            }
            webSocket.activeRoom = element.msgId
            RouteManager.getIntent(it, ApplinkConst.TOPCHAT, element.msgId).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }.let { intent ->
                startActivityForResult(intent, OPEN_DETAIL_MESSAGE)
            }
            it.overridePendingTransition(0, 0)
        }
    }

    override fun deleteChat(element: ItemChatListPojo, itemPosition: Int) {
        viewModel.chatMoveToTrash(element.msgId.toInt())
        itemPositionLongClicked = itemPosition
    }

    override fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        viewModel.markChatAsRead(msgIds, result)
    }

    override fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        viewModel.markChatAsUnread(msgIds, result)
    }

    override fun increaseNotificationCounter() {
        containerListener?.increaseChatUnreadCounter()
    }

    override fun decreaseNotificationCounter() {
        containerListener?.decreaseChatUnreadCounter()
    }

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

            if (viewModel.filter == PARAM_FILTER_UNREAD ||
                    viewModel.filter == PARAM_FILTER_UNREPLIED) {
                image = CHAT_BUYER_EMPTY
                title = it.getString(R.string.empty_chat_read_all_title)
                subtitle = it.getString(R.string.empty_chat_read_all_subtitle)
                ctaText = ""
                ctaApplink = ""
            } else if (viewModel.filter == PARAM_FILTER_TOPBOT) {
                image = CHAT_SELLER_EMPTY_SMART_REPLY
                title = it.getString(R.string.empty_chat_smart_reply)
                subtitle = ""
                ctaText = ""
                ctaApplink = ""
            }
        }

        return EmptyChatModel(title, subtitle, image, ctaText, ctaApplink, isTopAds)
    }

    override fun onSwipeRefresh() {
        containerListener?.refreshNotificationCounter()
        super.onSwipeRefresh()
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

    override fun pinUnpinChat(element: ItemChatListPojo, position: Int, isPinChat: Boolean) {
        val msgId = element.msgId
        viewModel.pinUnpinChat(msgId, isPinChat,
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

    private fun showToaster(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                    .show()
        }
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
            Toaster.make(it, errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun onChatAdminNoAccess() {
        swipeToRefresh?.isEnabled = false
        adapter?.showNoAccessView()
    }

    override fun returnToSellerHome() {}

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        const val CHAT_SELLER_EMPTY = "https://ecs7.tokopedia.net/img/android/others/chat-seller-empty.png"
        const val CHAT_BUYER_EMPTY = "https://ecs7.tokopedia.net/img/android/others/chat-buyer-empty.png"
        const val CHAT_SELLER_EMPTY_SMART_REPLY = "https://ecs7.tokopedia.net/android/others/toped_confused.webp"
        const val TAG = "ChatListFragment"

        fun createFragment(): ChatListInboxFragment {
            val bundle = Bundle()
            val fragment = ChatListInboxFragment()
            fragment.arguments = bundle
            return fragment
        }

    }
}