package com.tokopedia.topchat.chatlist.view.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.seller_migration_common.listener.SellerHomeFragmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.data.ChatListPreference
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ActivityComponentFactory
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.view.TopChatListAction
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity.Companion.BUYER_ANALYTICS_LABEL
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity.Companion.SELLER_ANALYTICS_LABEL
import com.tokopedia.topchat.chatlist.view.adapter.ChatListPagerAdapter
import com.tokopedia.topchat.chatlist.view.listener.ChatListContract
import com.tokopedia.topchat.chatlist.view.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.view.uimodel.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.view.uimodel.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.view.uimodel.base.BaseIncomingItemWebSocketModel.Companion.ROLE_BUYER
import com.tokopedia.topchat.chatlist.view.uimodel.base.BaseIncomingItemWebSocketModel.Companion.ROLE_SELLER
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatTabCounterViewModel
import com.tokopedia.topchat.chatlist.view.viewmodel.WebSocketViewModel
import com.tokopedia.topchat.common.TopChatErrorLogger
import com.tokopedia.topchat.common.custom.ToolTipSearchPopupWindow
import com.tokopedia.topchat.common.data.TopChatResult
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ChatTabListFragment constructor() :
    BaseDaggerFragment(),
    ChatListContract.TabFragment,
    SellerHomeFragmentListener {

    override fun getScreenName(): String = "/new-inbox/chat"

    private lateinit var fragmentAdapter: ChatListPagerAdapter
    private val tabList = ArrayList<ChatListPagerAdapter.ChatListTab>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var chatListAnalytics: ChatListAnalytic

    @Inject
    lateinit var chatListPref: ChatListPreference

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var webSocketViewModel: WebSocketViewModel
    private lateinit var viewModel: ChatTabCounterViewModel
    private var searchToolTip: ToolTipSearchPopupWindow? = null

    private var coachMarkOnBoarding = CoachMarkBuilder().build()
    private var fragmentViewCreated = false
    private var isFinishShowingCoachMarkOnBoarding = false

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var chatTabListListener: Listener? = null

    interface Listener {
        fun getActivityToolbar(): Toolbar
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_tab_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindView(view)
        initViewModel()
        initTabList()
        initObservers()
        initViewPagerAdapter()
        initViewPager()
        initViewModel()
        initOnBoarding()
        initToolTip()
        initBackground()
        initData()
    }

    private fun initBackground() {
        if (GlobalConfig.isSellerApp()) {
            context?.let {
                viewPager?.setBackgroundColor(
                    MethodChecker.getColor(
                        it,
                        unifyprinciplesR.color.Unify_Background
                    )
                )
            }
        }
    }

    override fun onAttachActivity(context: Context?) {
        if (context is Listener) {
            chatTabListListener = context
        }
    }

    override fun onStart() {
        super.onStart()
        initWebsocketChatObserver()
        webSocketViewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        stopWebsocketLiveDataObserver()
        clearLiveDataValue()
        webSocketViewModel.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        searchToolTip?.dismiss()
    }

    private fun initToolTip() {
        searchToolTip = ToolTipSearchPopupWindow(context, chatListPref)
    }

    private fun isOnBoardingAlreadyShown(): Boolean {
        return chatListPref.coachMarkShown
    }

    override fun initInjector() {
        if (activity is ChatListActivity) {
            getComponent(ChatListComponent::class.java).inject(this)
        } else {
            initInjectorSellerApp()
        }
    }

    private fun initInjectorSellerApp() {
        ActivityComponentFactory.instance.createChatListComponent(
            requireActivity().application,
            requireContext()
        ).inject(this)
    }

    override fun notifyViewCreated() {
        if (!fragmentViewCreated) {
            webSocketViewModel.connectWebSocket()
            fragmentViewCreated = true
        }
    }

    private fun initObservers() {
        // Setup flow observer
        viewModel.setupViewModelObserver()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeChatNotificationCounter()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                observeError()
            }
        }

        observeLastVisitedTab()
    }

    private suspend fun observeChatNotificationCounter() {
        viewModel.chatListNotificationUiState.collectLatest { uiState ->
            if (tabList.isNotEmpty()) {
                tabList[0].counter = uiState.unreadSeller.toString()
                if (tabList.size > 1) {
                    tabList[1].counter = uiState.unreadBuyer.toString()
                }
                setNotificationCounterOnTab()
            }
        }
    }

    private fun observeLastVisitedTab() {
        var job: Job? = null
        job = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chatLastSelectedTab.collectLatest {
                when (it) {
                    is TopChatResult.Success -> {
                        initTabLayout(it.data)
                        job?.cancel() // Cancel after init tab layout based on datastore cache
                    }
                    else -> Unit // no-op
                }
            }
        }
    }

    private suspend fun observeError() {
        viewModel.errorUiState.collectLatest {
            it.error?.let { (throwable, methodName) ->
                TopChatErrorLogger.logExceptionToServerLogger(
                    TopChatErrorLogger.PAGE.TOPCHAT_LIST_TAB,
                    throwable,
                    userSession.deviceId.orEmpty(),
                    methodName
                )
            }
        }
    }

    private fun bindView(view: View) {
        tabLayout = view.findViewById(R.id.tl_chat_list)
        viewPager = view.findViewById(R.id.vp_chat_list)
    }

    private fun initTabList() {
        if (userSession.hasShop()) {
            addSellerTabFragment()
        }

        if (!GlobalConfig.isSellerApp()) {
            addBuyerTabFragment()
        }
    }

    private fun initTabLayout(lastSeenPosition: Int) {
        if (tabList.size == 1) {
            tabLayout?.hide()
            return
        }

        tabLayout?.removeAllTabs()
        for (i in 0 until tabList.size) {
            tabLayout?.newTab()?.let { tabLayout?.addTab(it) }
            tabLayout?.setBackgroundColor(
                MethodChecker.getColor(
                    context,
                    unifyprinciplesR.color.Unify_Background
                )
            )
        }

        val tabCount = tabLayout?.tabCount ?: 0
        for (i in 0 until tabCount) {
            val title = tabList[i].title
            val icon = tabList[i].icon
            val counter = tabList[i].counter
            val tab = tabLayout?.getTabAt(i)
            tab?.customView = icon?.let { createCustomView(title, it, counter) }
            if (i == viewPager?.currentItem) {
                setTabViewColor(tab?.customView, i, SELECTED_TEXT_COLOR, SELECTED_ICON_COLOR)
            }
        }

        tabLayout?.elevation = 0f
        tabLayout?.background = context?.let {
            ContextCompat.getDrawable(it, R.drawable.bg_chat_list_tab_layout)
        }
        tabLayout?.tabMode = TabLayout.MODE_FIXED

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabViewColor(
                    tab.customView,
                    tab.position,
                    UNSELECTED_TEXT_COLOR,
                    UNSELECTED_ICON_COLOR
                )
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.setCurrentItem(tab.position, true)
                viewModel.processAction(TopChatListAction.SetLastVisitedTab(tab.position))
                setTabViewColor(
                    tab.customView,
                    tab.position,
                    SELECTED_TEXT_COLOR,
                    SELECTED_ICON_COLOR
                )
                with(chatListAnalytics) {
                    eventClickTabChat(if (tab.position == 0) SELLER_ANALYTICS_LABEL else BUYER_ANALYTICS_LABEL)
                }
            }
        })

        if (tabList.size == 1) {
            tabLayout?.hide()
        } else {
            val selectedTab = arguments?.getInt(SELECTED_TAB_KEY)
            if (selectedTab != null) {
                goToSelectedTab(selectedTab)
            } else {
                goToLastSeenTab(lastSeenPosition)
            }
        }
    }

    private fun createCustomView(title: String, icon: Drawable, counter: String): View? {
        val customView = LayoutInflater.from(context).inflate(R.layout.item_chat_tab, null)
        val titleView = customView.findViewById<TextView>(R.id.title)
        val iconView = customView.findViewById<ImageView>(R.id.icon)
        titleView.text = setTitleTab(title, counter)
        iconView.setImageDrawable(icon)
        return customView
    }

    private fun setTabViewColor(customView: View?, position: Int, textColor: Int, iconColor: Int) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(
            MethodChecker.getColor(
                context,
                textColor
            )
        )

        val icon = customView?.findViewById<ImageView>(R.id.icon)
        when (position) {
            0 -> {
                icon?.setImageDrawable(
                    context?.let {
                        getIconUnifyDrawable(
                            it,
                            IconUnify.SHOP,
                            context?.let { ContextCompat.getColor(it, iconColor) }
                        )
                    }
                )
            }
            1 -> {
                icon?.setImageDrawable(
                    context?.let {
                        getIconUnifyDrawable(
                            it,
                            IconUnify.SMILE,
                            context?.let { ContextCompat.getColor(it, iconColor) }
                        )
                    }
                )
            }
        }
    }

    private fun goToSelectedTab(selectedTab: Int) {
        viewPager?.setCurrentItem(selectedTab, false)
    }

    private fun goToLastSeenTab(lastSeenPosition: Int) {
        if (lastSeenPosition == -1) return
        viewPager?.setCurrentItem(lastSeenPosition, false)
    }

    private fun setTitleTab(title: String, counter: String): CharSequence? {
        if (counter.toLongOrZero() > 0) {
            val counterFormatted: String =
                if (counter.toLongOrZero() > 99) {
                    "99+"
                } else {
                    counter
                }

            return if (title.length > MAX_LENGTH_TITLE) {
                title.take(TITLE_LENGTH) + ".. ($counterFormatted)"
            } else {
                "$title ($counterFormatted)"
            }
        }
        return MethodChecker.fromHtml(title)
    }

    private fun addSellerTabFragment() {
        val sellerFragment = createSellerTabFragment()
        val sellerTabFragment = ChatListPagerAdapter.ChatListTab(
            userSession.shopName,
            sellerFragment,
            context?.let { getIconUnifyDrawable(it, IconUnify.SHOP) }
        )
        tabList.add(sellerTabFragment)
    }

    protected open fun createSellerTabFragment(): ChatListFragment {
        return ChatListFragment.createFragment(ChatListQueriesConstant.PARAM_TAB_SELLER)
    }

    private fun addBuyerTabFragment() {
        val buyerFragment = createBuyerTabFragment()
        val buyerTabFragment = ChatListPagerAdapter.ChatListTab(
            userSession.name,
            buyerFragment,
            context?.let { getIconUnifyDrawable(it, IconUnify.SMILE) }
        )

        tabList.add(buyerTabFragment)
    }

    protected open fun createBuyerTabFragment(): ChatListFragment {
        return ChatListFragment.createFragment(ChatListQueriesConstant.PARAM_TAB_USER)
    }

    private fun initViewPagerAdapter() {
        fragmentAdapter = ChatListPagerAdapter(childFragmentManager)
        fragmentAdapter.setItemList(tabList)
    }

    private fun initViewPager() {
        viewPager?.adapter = fragmentAdapter
        viewPager?.offscreenPageLimit = tabList.size
        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    private fun initViewModel() {
        viewModelProvider = ViewModelProvider(this, viewModelFactory)
        webSocketViewModel = viewModelProvider.get(WebSocketViewModel::class.java)
        viewModel = viewModelProvider.get(ChatTabCounterViewModel::class.java)
    }

    private fun initWebsocketChatObserver() {
        webSocketViewModel.itemChat.observe(this) { result ->
            when (result) {
                is Success -> {
                    when (result.data) {
                        is IncomingChatWebSocketModel -> forwardToFragment(result.data as IncomingChatWebSocketModel)
                        is IncomingTypingWebSocketModel -> forwardToFragment(result.data as IncomingTypingWebSocketModel)
                    }
                }
                else -> {}
            }
        }
    }

    private fun forwardToFragment(incomingChatWebSocketModel: IncomingChatWebSocketModel) {
        Timber.d(incomingChatWebSocketModel.toString())
        val contactId = incomingChatWebSocketModel.getContactId()
        val tag = incomingChatWebSocketModel.getTag()
        val fragment: ChatListFragment? = determineFragmentByTag(contactId, tag)
        fragment?.processIncomingMessage(incomingChatWebSocketModel)
    }

    private fun forwardToFragment(incomingTypingWebSocketModel: IncomingTypingWebSocketModel) {
        Timber.d(incomingTypingWebSocketModel.toString())
        val contactId = incomingTypingWebSocketModel.getContactId()
        val tag = incomingTypingWebSocketModel.getTag()
        val fragment: ChatListFragment? = determineFragmentByTag(contactId, tag)
        fragment?.processIncomingMessage(incomingTypingWebSocketModel)
    }

    private fun determineFragmentByTag(fromUid: String, tag: String): ChatListFragment? {
        if (isBuyerOnly() && isFromSeller(fromUid, tag)) return getBuyerFragment()
        if (isFromBuyer(fromUid, tag)) return getSellerFragment()
        if (isFromSeller(fromUid, tag)) return getBuyerFragment()
        if (isFromMyselfAsSeller(fromUid, tag)) return getSellerFragment()
        return null
    }

    private fun isFromBuyer(fromUid: String, tag: String): Boolean {
        return (tag == ROLE_BUYER && fromUid != userSession.userId)
    }

    private fun isFromSeller(fromUid: String, tag: String): Boolean {
        return (tag == ROLE_SELLER && fromUid != userSession.userId) && !GlobalConfig.isSellerApp()
    }

    private fun isFromMyselfAsSeller(fromUid: String, tag: String): Boolean {
        return (tag == ROLE_SELLER && fromUid == userSession.userId)
    }

    private fun getBuyerFragment(): ChatListFragment {
        val buyerPosition = if (isBuyerOnly()) 0 else 1
        return fragmentAdapter.getItem(buyerPosition) as ChatListFragment
    }

    private fun getSellerFragment(): ChatListFragment {
        return fragmentAdapter.getItem(0) as ChatListFragment
    }

    private fun isBuyerOnly(): Boolean {
        return tabList.size == 1
    }

    private fun setNotificationCounterOnTab() {
        val tabCount = tabLayout?.tabCount ?: return
        for (i in 0 until tabCount) {
            setupTabTitleAt(i)
        }
    }

    private fun setupTabTitleAt(tabPosition: Int) {
        val title = tabList[tabPosition].title
        val counter = tabList[tabPosition].counter
        val tabTitle = setTitleTab(title, counter)
        val tab = tabLayout?.getTabAt(tabPosition)

        tab?.customView?.findViewById<TextView>(R.id.title)?.apply {
            text = tabTitle
        }
    }

    private fun initData() {
        loadNotificationCounter()
    }

    override fun loadNotificationCounter() {
        viewModel.processAction(TopChatListAction.RefreshCounter(userSession.shopId))
    }

    override fun showSearchOnBoardingTooltip() {
        if (
            chatListPref.searchTooltipShown ||
            !isFinishShowingCoachMarkOnBoarding ||
            activity?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) == false
        ) {
            return
        }
        val toolbar = chatTabListListener?.getActivityToolbar()
        toolbar?.post {
            val searchView = toolbar.findViewById<View>(R.id.menu_chat_search)
            searchToolTip?.showAtBottom(searchView)
        }
    }

    override fun closeSearchTooltip() {
        searchToolTip?.dismissOnBoarding()
    }

    override fun onScrollToTop() {
        (getSellerFragment() as? ChatListItemListener)?.onScrollToTop()
    }

    private fun initOnBoarding() {
        if (!userSession.hasShop()) {
            isFinishShowingCoachMarkOnBoarding = true
            return
        }
        tabLayout?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    tabLayout?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    if (!isOnBoardingAlreadyShown()) {
                        showOnBoarding()
                    } else {
                        isFinishShowingCoachMarkOnBoarding = true
                    }
                }
            })
    }

    private fun showOnBoarding() {
        val tabCount = tabLayout?.childCount ?: return
        if (tabCount < 0) return
        val tabViewGroup = tabLayout?.getChildAt(0) as ViewGroup
        if (tabViewGroup.childCount < 2) return

        val sellerTab = tabViewGroup.getChildAt(0)
        val buyerTab = tabViewGroup.getChildAt(1)
        val tutorials = arrayListOf(
            CoachMarkItem(
                sellerTab,
                getString(R.string.coach_tab_title_seller),
                getString(R.string.coach_tab_description_seller)
            ),
            CoachMarkItem(
                buyerTab,
                getString(R.string.coach_tab_title_buyer),
                getString(R.string.coach_tab_description_buyer)
            )
        )
        coachMarkOnBoarding.onFinishListener = {
            isFinishShowingCoachMarkOnBoarding = true
            showSearchOnBoardingTooltip()
        }
        coachMarkOnBoarding.show(activity, TAG_ONBOARDING, tutorials)
        chatListPref.coachMarkShown = true
    }

    private fun stopWebsocketLiveDataObserver() {
        webSocketViewModel.itemChat.removeObservers(this)
    }

    private fun clearLiveDataValue() {
        webSocketViewModel.clearItemChatValue()
    }

    companion object {
        val TAG_ONBOARDING = ChatTabListFragment::class.java.name + ".OnBoarding"
        private const val MAX_LENGTH_TITLE = 10
        private const val TITLE_LENGTH = 9

        // Text Color vals
        private val SELECTED_TEXT_COLOR = unifyprinciplesR.color.Unify_GN500
        private val UNSELECTED_TEXT_COLOR = unifyprinciplesR.color.Unify_NN600

        // Icon Color vals
        private val SELECTED_ICON_COLOR = unifyprinciplesR.color.Unify_GN500
        private val UNSELECTED_ICON_COLOR = unifyprinciplesR.color.Unify_NN500

        const val SELECTED_TAB_KEY = "selected_tab"

        @JvmStatic
        fun create(bundle: Bundle? = null): ChatTabListFragment {
            return ChatTabListFragment().also { fragment ->
                bundle?.let {
                    fragment.arguments = it
                }
            }
        }
    }
}
