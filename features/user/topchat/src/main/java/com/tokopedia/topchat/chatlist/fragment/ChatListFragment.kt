package com.tokopedia.topchat.chatlist.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamString
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
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_SELLER
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_TAB_USER
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.listener.ChatListContract
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.model.EmptyChatModel
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ChatChangeStateResponse
import com.tokopedia.topchat.chatlist.pojo.ChatListDataPojo
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatItemListViewModel.Companion.arrayFilterParam
import com.tokopedia.topchat.chatlist.widget.FilterMenu
import com.tokopedia.topchat.chatroom.view.viewmodel.ReplyParcelableModel
import com.tokopedia.topchat.chatsetting.view.activity.ChatSettingActivity
import com.tokopedia.topchat.common.TopChatInternalRouter
import com.tokopedia.topchat.common.analytics.TopChatAnalytics
import com.tokopedia.topchat.common.data.TopchatItemMenu
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
open class ChatListFragment constructor() : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ChatListItemListener, LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var chatListAnalytics: ChatListAnalytic

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val chatItemListViewModel by lazy { viewModelFragmentProvider.get(ChatItemListViewModel::class.java) }
    private lateinit var performanceMonitoring: PerformanceMonitoring
    private var chatTabListContract: ChatListContract.TabFragment? = null
    private var mUserSeen = false
    private var mViewCreated = false

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
        super.onCreate(savedInstanceState)
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
        mViewCreated = true
        tryViewCreatedFirstSight()
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(view)
        initView(view)
        setObserver()
        setupSellerBroadcast()
        setupChatSellerBannedStatus()
        setupEmptyModel()
    }

    private fun setupChatSellerBannedStatus() {
        if (!isTabSeller()) return
        chatItemListViewModel.chatBannedSellerStatus.observe(viewLifecycleOwner, Observer {
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
        chatItemListViewModel.broadCastButtonVisibility.observe(viewLifecycleOwner, Observer { visibility ->
            when (visibility) {
                true -> {
                    broadCastButton.show()
                }
                false -> broadCastButton.hide()
            }
        })
        chatItemListViewModel.broadCastButtonUrl.observe(viewLifecycleOwner, Observer { applink ->
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

    private fun tryViewCreatedFirstSight() {
        if (mUserSeen && mViewCreated) {
            onViewCreatedFirstSight(view)
        }
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
        chatItemListViewModel.mutateChatList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessGetChatList(it.data.data)
                is Fail -> onFailGetChatList(it.throwable)
            }
        })

        chatItemListViewModel.deleteChat.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    adapter?.deleteItem(itemPositionLongClicked, emptyUiModel)
                    decreaseNotificationCounter()
                }
                is Fail -> view?.let {
                    Toaster.make(it, getString(R.string.delete_chat_default_error_message), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                }
            }
        })

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

            updateItemOnIndex(index, newChat)
        }
    }

    private fun updateItemOnIndex(
            index: Int,
            newChat: IncomingChatWebSocketModel,
            readStatus: Int = ChatItemListViewHolder.STATE_CHAT_UNREAD
    ) {
        adapter?.let { adapter ->
            when {
                index >= adapter.list.size -> {
                    return
                }
                //not found on list
                index == RecyclerView.NO_POSITION -> {
                    adapter.onNewItemChatMessage(newChat, chatItemListViewModel.pinnedMsgId)
                    increaseNotificationCounter()
                }
                //found on list, not the first
                index >= 0 -> {
                    adapter.onNewIncomingChatMessage(index, newChat, readStatus, chatItemListViewModel.pinnedMsgId)
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

    private fun showFilterDialog() {
        activity?.let {
            if (filterMenu.isAdded) return@let
            val itemMenus = ArrayList<TopchatItemMenu>()
            val arrayFilterString = chatItemListViewModel.getFilterTittles(it, isTabSeller())

            for ((index, title) in arrayFilterString.withIndex()) {
                if (index == filterChecked) itemMenus.add(TopchatItemMenu(title, true))
                else itemMenus.add(TopchatItemMenu(title, false))
            }

            val title = getString(R.string.menu_chat_filter)
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
        chatItemListViewModel.clearPinUnpinData()
        chatItemListViewModel.resetState()
        if (isTabSeller()) {
            chatItemListViewModel.loadTopBotWhiteList()
            chatItemListViewModel.loadChatBannedSellerStatus()
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
        chatItemListViewModel.getChatListMessage(page, filterChecked, sightTag)
    }

    override fun chatItemClicked(element: ItemChatListPojo, itemPosition: Int) {
        activity?.let {
            with(chatListAnalytics) {
                eventClickChatList(
                        if (isTabSeller()) ChatListActivity.SELLER_ANALYTICS_LABEL
                        else ChatListActivity.BUYER_ANALYTICS_LABEL)
            }
            val intent = RouteManager.getIntent(it, ApplinkConst.TOPCHAT, element.msgId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            intent.putExtra(TopChatInternalRouter.Companion.RESULT_INBOX_CHAT_PARAM_INDEX, itemPosition)
            this@ChatListFragment.startActivityForResult(intent, OPEN_DETAIL_MESSAGE)
            it.overridePendingTransition(0, 0)
        }
    }

    override fun deleteChat(element: ItemChatListPojo, itemPosition: Int) {
        chatItemListViewModel.chatMoveToTrash(element.msgId.toInt())
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
                Unit
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (!mUserSeen && isVisibleToUser) {
            mUserSeen = true
            onUserFirstSight()
            tryViewCreatedFirstSight()
        }
        onUserVisibleChanged(isVisibleToUser)
    }

    private fun onViewCreatedFirstSight(view: View?) {
        Timber.d("$sightTag onViewCreatedFirstSight")
        chatTabListContract?.notifyViewCreated()
        loadInitialData()
    }

    private fun onUserFirstSight() {
        Timber.d("$sightTag onUserFirstSight")
    }


    private fun onUserVisibleChanged(visible: Boolean) {
        Timber.d("$sightTag onUserVisibleChanged $visible")
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
        chatItemListViewModel.pinUnpinChat(msgId, isPinChat,
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
            Toaster.make(it, errorMsg, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
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

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        const val CHAT_TAB_TITLE = "chat_tab_title"
        const val CHAT_SELLER_EMPTY = "https://ecs7.tokopedia.net/img/android/others/chat-seller-empty.png"
        const val CHAT_BUYER_EMPTY = "https://ecs7.tokopedia.net/img/android/others/chat-buyer-empty.png"
        const val CHAT_SELLER_EMPTY_SMART_REPLY = "https://ecs7.tokopedia.net/android/others/toped_confused.webp"
        const val TAG = "ChatListFragment"

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