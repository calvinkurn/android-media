package com.tokopedia.topchat.chatlist.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seller_migration_common.isSellerMigrationEnabled
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_READ
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_TOPBOT
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_SELLER
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_USER
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.domain.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListDataPojo
import com.tokopedia.topchat.chatlist.domain.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.domain.pojo.chatlistticker.ChatListTickerResponse
import com.tokopedia.topchat.chatlist.domain.pojo.operational_insight.ShopChatTicker
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.view.adapter.ChatListAdapter
import com.tokopedia.topchat.chatlist.view.adapter.decoration.ChatListItemDecoration
import com.tokopedia.topchat.chatlist.view.adapter.typefactory.ChatListTypeFactoryImpl
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder
import com.tokopedia.topchat.chatlist.view.listener.ChatListContract
import com.tokopedia.topchat.chatlist.view.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.view.listener.ChatListTickerListener
import com.tokopedia.topchat.chatlist.view.uimodel.ChatListTickerUiModel
import com.tokopedia.topchat.chatlist.view.uimodel.EmptyChatModel
import com.tokopedia.topchat.chatlist.view.uimodel.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.view.uimodel.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatItemListViewModel.Companion.arrayFilterParam
import com.tokopedia.topchat.chatlist.view.widget.FilterMenu
import com.tokopedia.topchat.chatlist.view.widget.OperationalInsightBottomSheet
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.uimodel.ReplyParcelableModel
import com.tokopedia.topchat.chatsetting.view.activity.ChatSettingActivity
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.analytics.TopChatAnalyticsKt
import com.tokopedia.topchat.common.data.TopchatItemMenu
import com.tokopedia.topchat.common.util.Utils.getOperationalInsightStateReport
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

open class ChatListFragment constructor() :
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

    private val viewModelFragmentProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val chatItemListViewModel by lazy { viewModelFragmentProvider.get(ChatItemListViewModel::class.java) }
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private var chatTabListContract: ChatListContract.TabFragment? = null

    private var sightTag = ""
    private var itemPositionLongClicked: Int = -1
    private var filterChecked = 0
    private var filterMenu = FilterMenu()
    private var chatBannedSellerTicker: Ticker? = null
    private var rv: RecyclerView? = null
    private var emptyUiModel: Visitable<*>? = null
    private var menu: Menu? = null
    private lateinit var broadCastButton: FloatingActionButton

    override fun getRecyclerViewResourceId() = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId() = R.id.swipe_refresh_layout
    override fun getScreenName(): String = ""

    override fun onAttachActivity(context: Context?) {
        if (context is ChatListContract.TabFragment) {
            chatTabListContract = context
            return
        }
        parentFragment?.let { parent ->
            if (parent is ChatListContract.TabFragment) {
                chatTabListContract = parent
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        performanceMonitoring = PerformanceMonitoring.start(getFpmKey())
        sightTag = getParamString(CHAT_TAB_TITLE, arguments, null, "")
        setHasOptionsMenu(true)
    }

    private fun getFpmKey() = if (GlobalConfig.isSellerApp()) {
        TopChatAnalytics.FPM_CHAT_LIST_SELLERAPP
    } else {
        TopChatAnalytics.FPM_CHAT_LIST
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()

        if (GlobalConfig.isSellerApp()) {
            inflater.inflate(R.menu.chat_options_menu_sellerapp, menu)
        } else {
            inflater.inflate(R.menu.chat_options_menu, menu)
        }
        setChatMenuItem()
        this.menu = menu
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
                chatTabListContract?.closeSearchTooltip()
                RouteManager.route(context, ApplinkConstInternalMarketplace.CHAT_SEARCH)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("$sightTag onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        initView(view)
        setObserver()
        setupSellerBroadcast()
        setupChatSellerBannedStatus()
        setupEmptyModel()
        notifyAndGetChatListMessage(view)
    }

    private fun setupChatSellerBannedStatus() {
        if (!isTabSeller()) return
        chatItemListViewModel.chatBannedSellerStatus.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> updateChatBannedSellerStatus(it.data)
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

    override fun onPrepareOptionsMenu(menu: Menu) {
        chatTabListContract?.showSearchOnBoardingTooltip()
        super.onPrepareOptionsMenu(menu)
    }

    private fun setupSellerBroadcast() {
        if (!isTabSeller()) return
        setupSellerBroadcastButton()
        chatItemListViewModel.loadChatBlastSellerMetaData()
    }

    private fun setupSellerBroadcastButton() {
        chatItemListViewModel.broadCastButtonVisibility.observe(
            viewLifecycleOwner,
            Observer { visibility ->
                when (visibility) {
                    true -> {
                        broadCastButton.show()
                    }
                    false -> broadCastButton.hide()
                }
            }
        )
        chatItemListViewModel.broadCastButtonUrl.observe(
            viewLifecycleOwner,
            Observer { applink ->
                if (applink.isNullOrEmpty()) return@Observer
                broadCastButton.setOnClickListener {
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
                }
            }
        )
    }

    private fun initView(view: View) {
        showLoading()
        broadCastButton = view.findViewById(R.id.fab_broadcast)
        chatBannedSellerTicker = view.findViewById(R.id.ticker_ban_status)
        rv = view.findViewById(R.id.recycler_view)
    }

    private fun setUpRecyclerView(view: View) {
        val recyclerView = super.getRecyclerView(view)
        recyclerView?.let {
            it.setHasFixedSize(true)
            for (i in 0 until it.itemDecorationCount) {
                it.removeItemDecorationAt(i)
            }
            it.addItemDecoration(ChatListItemDecoration(context))
        }
    }

    private fun setObserver() {
        chatItemListViewModel.mutateChatList.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetChatList(it.data.data)
                    if (GlobalConfig.isSellerApp() && isFirstPage()) {
                        chatItemListViewModel.getOperationalInsight(userSession.shopId)
                    } else if (!GlobalConfig.isSellerApp() && isFirstPage()) {
                        chatItemListViewModel.getChatListTicker()
                    }
                }
                is Fail -> onFailGetChatList(it.throwable)
            }
        }

        chatItemListViewModel.deleteChat.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    adapter?.deleteItem(itemPositionLongClicked, emptyUiModel)
                    decreaseNotificationCounter()
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

        chatItemListViewModel.isChatAdminEligible.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    result.data.let { isEligible ->
                        if (isEligible) {
                            showOrHideChatMenuItem(true)
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

        chatItemListViewModel.chatOperationalInsight.observe(viewLifecycleOwner) {
            if (it is Success && it.data.showTicker == true) {
                adapter?.addElement(0, it.data)
            } else if (chatItemListViewModel.shouldShowBubbleTicker()) {
                addBubbleChatTicker()
            }
        }

        chatItemListViewModel.chatListTicker.observe(viewLifecycleOwner) { result ->
            if (result is Success) {
                val tickerChatListIndex = adapter?.list?.indexOfFirst { it -> it is ChatListTickerUiModel }

                if (tickerChatListIndex == RecyclerView.NO_POSITION) {
                    setChatListTickerBuyer(result.data)
                    setChatListTickerSeller(result.data)
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
            message = getString(com.tokopedia.topchat.R.string.topchat_bubble_ticker_message),
            applink = ApplinkConstInternalMarketplace.TOPCHAT_BUBBLE_ACTIVATION
        ).apply {
            this.showCloseButton = true
            this.sharedPreferenceKey = ChatItemListViewModel.BUBBLE_TICKER_PREF_NAME
        }
        adapter?.addElement(Int.ZERO, chatListTicker)
    }

    override fun onChatListTickerClicked(applink: String) {
        if (applink.isNotBlank()) {
            context?.let {
                if (applink == ApplinkConst.TokoFood.TOKOFOOD_ORDER) {
                    chatListAnalytics.clickChatDriverTicker(getRoleStr())
                }
                RouteManager.route(it, applink)
            }
        }
    }

    override fun onDismissTicker(element: ChatListTickerUiModel) {
        adapter?.removeElement(element)
        if (element.sharedPreferenceKey.isNotBlank()) {
            chatItemListViewModel.saveTickerPref(ChatItemListViewModel.BUBBLE_TICKER_PREF_NAME)
        }
    }

    fun processIncomingMessage(newChat: IncomingChatWebSocketModel) {
        adapter?.let { adapter ->
            if (
                (adapter.list.isNotEmpty() && adapter.list[0] is LoadingModel) ||
                filterChecked == arrayFilterParam.indexOf(PARAM_FILTER_READ)
            ) {
                return
            }

            val index = adapter.list.indexOfFirst { chat ->
                return@indexOfFirst chat is ItemChatListPojo && chat.msgId == newChat.messageId
            }

            val shouldUpdateReadStatus = !newChat.isFromMySelf(getRole(), userSession.userId)
            updateItemOnIndex(
                index,
                newChat,
                shouldUpdateExistingChatReadStatus = shouldUpdateReadStatus
            )
        }
    }

    private fun updateItemOnIndex(
        index: Int,
        newChat: IncomingChatWebSocketModel,
        readStatus: Int = ChatItemListViewHolder.STATE_CHAT_UNREAD,
        shouldUpdateExistingChatReadStatus: Boolean = false
    ) {
        adapter?.let { adapter ->
            when {
                index >= adapter.list.size -> {
                    return
                }
                // not found on list
                index == RecyclerView.NO_POSITION -> {
                    if (!newChat.isFromMySelf(getRole(), userSession.userId)) {
                        adapter.onNewItemChatMessage(newChat, chatItemListViewModel.pinnedMsgId)
                        increaseNotificationCounter()
                    }
                }
                // found on list, not the first
                index >= 0 -> {
                    adapter.onNewIncomingChatMessage(
                        index = index,
                        newChat = newChat,
                        readStatus = readStatus,
                        pinnedMsgId = chatItemListViewModel.pinnedMsgId,
                        shouldUpdateReadStatus = shouldUpdateExistingChatReadStatus
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
                rv?.post {
                    showLoading()
                }
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
        return ChatListAdapter(this, adapterTypeFactory)
    }

    override fun getAdapter(): ChatListAdapter? {
        super.getAdapter()?.let {
            return it as ChatListAdapter
        }
        return null
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun onChatListTickerImpressed() {
        chatListAnalytics.impressOnChatDriverTicker(getRoleStr())
    }

    private fun getRoleStr(): String {
        return if (isTabSeller()) ChatListAnalytic.Other.SELLER else ChatListAnalytic.Other.BUYER
    }

    private fun showFilterDialog() {
        activity?.let {
            if (filterMenu.isAdded) return@let
            val itemMenus = ArrayList<TopchatItemMenu>()
            val arrayFilterString = chatItemListViewModel.getFilterTitles(it, isTabSeller())

            for ((index, title) in arrayFilterString.withIndex()) {
                if (index == filterChecked) {
                    itemMenus.add(TopchatItemMenu(title, hasCheck = true))
                } else {
                    itemMenus.add(TopchatItemMenu(title))
                }
            }

            val title = getString(R.string.menu_chat_filter)
            filterMenu.apply {
                setTitle(title)
                setItemMenuList(itemMenus)
                setOnItemMenuClickListener { menu, pos ->
                    chatListAnalytics.eventClickListFilterChat(
                        menu.title.lowercase(Locale.getDefault())
                    )
                    filterChecked = pos
                    loadInitialData()
                    dismiss()
                }
            }.show(childFragmentManager, FilterMenu.TAG)
        }
    }

    override fun loadInitialData() {
        super.loadInitialData()
        chatItemListViewModel.clearPinUnpinData()
        chatItemListViewModel.resetState()
        if (isTabSeller()) {
            chatItemListViewModel.loadTopBotWhiteList()
            chatItemListViewModel.loadChatBannedSellerStatus()
        }
    }

    override fun initInjector() {
        if (activity is ChatListActivity) {
            getComponent(ChatListComponent::class.java).inject(this)
        } else {
            initInjectorSellerApp()
        }
    }

    private fun initInjectorSellerApp() {
        DaggerChatListComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication?)?.baseAppComponent)
            .chatListContextModule(context?.let { ChatListContextModule(it) })
            .build()
            .inject(this)
    }

    override fun loadData(page: Int) {
        chatItemListViewModel.getChatListMessage(page, filterChecked, sightTag)
    }

    override fun chatItemClicked(
        element: ItemChatListPojo,
        itemPosition: Int,
        lastActiveChat: Pair<ItemChatListPojo?, Int?>
    ) {
        activity?.let {
            with(chatListAnalytics) {
                eventClickChatList(
                    if (isTabSeller()) {
                        ChatListActivity.SELLER_ANALYTICS_LABEL
                    } else {
                        ChatListActivity.BUYER_ANALYTICS_LABEL
                    }
                )
            }
            val intent = RouteManager.getIntent(it, ApplinkConst.TOPCHAT, element.msgId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, itemPosition)
            intent.putExtra(Constant.CHAT_CURRENT_ACTIVE, element.msgId)
            intent.putExtra(Constant.CHAT_USER_ROLE_KEY, getRole())
            this@ChatListFragment.startActivityForResult(intent, OPEN_DETAIL_MESSAGE)
            it.overridePendingTransition(0, 0)
        }
    }

    override fun deleteChat(element: ItemChatListPojo, itemPosition: Int) {
        chatItemListViewModel.chatMoveToTrash(element.msgId)
        itemPositionLongClicked = itemPosition
    }

    override fun markChatAsRead(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        chatItemListViewModel.markChatAsRead(msgIds, result)
    }

    override fun markChatAsUnread(msgIds: List<String>, result: (Result<ChatChangeStateResponse>) -> Unit) {
        chatItemListViewModel.markChatAsUnread(msgIds, result)
    }

    override fun increaseNotificationCounter() {
        when (sightTag) {
            PARAM_TAB_USER -> chatTabListContract?.increaseUserNotificationCounter()
            PARAM_TAB_SELLER -> chatTabListContract?.increaseSellerNotificationCounter()
        }
    }

    override fun decreaseNotificationCounter() {
        when (sightTag) {
            PARAM_TAB_USER -> chatTabListContract?.decreaseUserNotificationCounter()
            PARAM_TAB_SELLER -> chatTabListContract?.decreaseSellerNotificationCounter()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DETAIL_MESSAGE) {
            data?.extras?.let { extras ->
                itemPositionLongClicked = extras.getInt(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, -1)
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val moveToTop = extras.getBoolean(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_MOVE_TO_TOP)
                        if (moveToTop) {
                            val lastItem = extras.getParcelable<ReplyParcelableModel>(TopChatInternalRouter.Companion.RESULT_LAST_ITEM)
                            lastItem?.let {
                                val replyTimeStamp = chatItemListViewModel.getReplyTimeStampFrom(lastItem)
                                val model = IncomingChatWebSocketModel(lastItem.messageId, lastItem.msg, replyTimeStamp)
                                updateItemOnIndex(itemPositionLongClicked, model, ChatItemListViewHolder.STATE_CHAT_READ)
                            }
                        }
                    }
                    TopChatInternalRouter.Companion.CHAT_DELETED_RESULT_CODE -> {
                        adapter?.deleteItem(itemPositionLongClicked, emptyUiModel)
                        showToaster(R.string.title_success_delete_chat)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeLiveDataObserver()
        chatItemListViewModel.flush()
    }

    private fun removeLiveDataObserver() {
        chatItemListViewModel.mutateChatList.removeObservers(this)
        chatItemListViewModel.deleteChat.removeObservers(this)
        chatItemListViewModel.broadCastButtonVisibility.removeObservers(this)
        chatItemListViewModel.broadCastButtonUrl.removeObservers(this)
        chatItemListViewModel.chatBannedSellerStatus.removeObservers(this)
        chatItemListViewModel.isChatAdminEligible.removeObservers(this)
    }

    override fun onScrollToTop() {
        rv?.post {
            rv?.smoothScrollToPosition(RV_TOP_POSITION)
        }
    }

    override fun onOperationalInsightTickerShown(element: ShopChatTicker) {
        TopChatAnalyticsKt.eventViewOperationalInsightTicker(
            shopId = userSession.shopId,
            stateReport = getOperationalInsightStateReport(element.isMaintain)
        )
    }

    override fun onOperationalInsightTickerClicked(element: ShopChatTicker) {
        val operationalInsightBottomSheet = OperationalInsightBottomSheet(
            element,
            userSession.shopId
        )
        operationalInsightBottomSheet.show(childFragmentManager, FilterMenu.TAG)
        TopChatAnalyticsKt.eventClickOperationalInsightTicker(
            shopId = userSession.shopId,
            stateReport = getOperationalInsightStateReport(element.isMaintain)
        )
    }

    override fun onOperationalInsightCloseButtonClicked(visitable: Visitable<*>) {
        adapter?.removeElement(visitable)
        chatItemListViewModel.saveNextMondayDate()
        if (visitable is ShopChatTicker) {
            TopChatAnalyticsKt.eventClickCloseOperationalInsightTicker(
                shopId = userSession.shopId,
                stateReport = getOperationalInsightStateReport(visitable.isMaintain)
            )
        }
    }

    private fun notifyAndGetChatListMessage(view: View?) {
        chatTabListContract?.notifyViewCreated()
        chatItemListViewModel.getChatListMessage(1, filterChecked, sightTag)
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
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
            when (sightTag) {
                PARAM_TAB_SELLER -> {
                    title = it.getString(R.string.title_topchat_empty_chat)
                    subtitle = it.getString(R.string.seller_empty_chat_subtitle)
                    image = CHAT_SELLER_EMPTY
                    ctaText = it.getString(R.string.title_topchat_manage_product)
                    ctaApplink = ApplinkConstInternalSellerapp.CENTRALIZED_PROMO + "?redirect_to_sellerapp=true"
                    isTopAds = true
                }
                PARAM_TAB_USER -> {
                    title = it.getString(R.string.title_topchat_empty_chat)
                    subtitle = it.getString(R.string.buyer_empty_chat_subtitle)
                    image = CHAT_BUYER_EMPTY
                }
            }

            if (filterChecked == arrayFilterParam.indexOf(PARAM_FILTER_UNREAD)) {
                image = CHAT_BUYER_EMPTY
                title = it.getString(R.string.empty_chat_read_all_title)
                subtitle = ""
                ctaText = ""
                ctaApplink = ""
            } else if (filterChecked == arrayFilterParam.indexOf(PARAM_FILTER_TOPBOT)) {
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
        super.onSwipeRefresh()
        chatTabListContract?.loadNotificationCounter()
    }

    override fun trackChangeReadStatus(element: ItemChatListPojo) {
        chatListAnalytics.trackChangeReadStatus(element)
    }

    override fun trackDeleteChat(element: ItemChatListPojo) {
        chatListAnalytics.trackDeleteChat(element)
    }

    override fun isTabSeller(): Boolean {
        return sightTag == PARAM_TAB_SELLER
    }

    override fun getSupportChildFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun pinUnpinChat(element: ItemChatListPojo, position: Int, isPinChat: Boolean) {
        val msgId = element.msgId
        chatItemListViewModel.pinUnpinChat(
            msgId,
            isPinChat,
            {
                element.updatePinStatus(isPinChat)
                if (isPinChat) {
                    // chat pinned.
                    onSuccessPinChat(element, position)
                } else if (!isPinChat && chatItemListViewModel.unpinnedMsgId.contains(element.msgId)) {
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

    override fun returnToSellerHome() {
        if (GlobalConfig.isSellerApp()) {
            RouteManager.route(context, ApplinkConstInternalSellerapp.SELLER_HOME)
        }
    }

    private fun onSuccessUnpinChat(element: ItemChatListPojo, position: Int) {
        adapter?.unpinChatItem(
            element,
            position,
            chatItemListViewModel.pinnedMsgId.size,
            chatItemListViewModel.chatListHasNext,
            chatItemListViewModel.unpinnedMsgId
        )
        chatItemListViewModel.pinnedMsgId.remove(element.msgId)
        showToaster(R.string.title_success_unpin_chat)
    }

    private fun onSuccessPinChat(element: ItemChatListPojo, position: Int) {
        adapter?.pinChatItem(element, position)
        rv?.scrollToPosition(0)
        chatItemListViewModel.pinnedMsgId.add(element.msgId)
        showToaster(R.string.title_success_pin_chat)
    }

    private fun onSuccessUnpinPreviouslyLoadedChat(element: ItemChatListPojo, position: Int) {
        adapter?.putToOriginalPosition(element, position, chatItemListViewModel.pinnedMsgId.size)
        chatItemListViewModel.pinnedMsgId.remove(element.msgId)
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
            Toaster.build(it, errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    private fun onChatAdminNoAccess() {
        swipeToRefresh?.isEnabled = false
        adapter?.showNoAccessView()
        if (GlobalConfig.isSellerApp()) {
            showOrHideChatMenuItem(false)
        }
    }

    /**
     * Hide chat menu item only if Sellerapp and no access
     */
    private fun setChatMenuItem() {
        if (chatItemListViewModel.isChatAdminEligible.value != null) {
            if (!chatItemListViewModel.isAdminHasAccess && GlobalConfig.isSellerApp()) {
                showOrHideChatMenuItem(false)
            } else {
                showOrHideChatMenuItem(true)
            }
        }
    }

    private fun showOrHideChatMenuItem(isShow: Boolean) {
        menu?.findItem(R.id.menu_chat_search)?.isVisible = isShow
        menu?.findItem(R.id.menu_chat_filter)?.isVisible = isShow
        menu?.findItem(R.id.menu_chat_setting)?.isVisible = isShow
    }

    private fun getRole(): Int {
        return if (isTabSeller()) {
            RoleType.SELLER
        } else {
            RoleType.BUYER
        }
    }

    private fun isFromTopChatRoom(): Boolean {
        return activity is TopChatRoomActivity
    }

    override fun onResume() {
        super.onResume()
        if (!isFromTopChatRoom()) {
            adapter?.resetActiveChatIndicator()
        }
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        const val CHAT_TAB_TITLE = "chat_tab_title"
        const val CHAT_SELLER_EMPTY = "https://images.tokopedia.net/img/android/others/chat-seller-empty.png"
        const val CHAT_BUYER_EMPTY = "https://images.tokopedia.net/img/android/others/chat-buyer-empty.png"
        const val CHAT_SELLER_EMPTY_SMART_REPLY = "https://images.tokopedia.net/android/others/toped_confused.webp"
        const val TAG = "ChatListFragment"

        private const val RV_TOP_POSITION = 0

        @JvmStatic
        fun createFragment(title: String): ChatListFragment {
            val bundle = Bundle()
            bundle.putString(CHAT_TAB_TITLE, title)
            val fragment = ChatListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
