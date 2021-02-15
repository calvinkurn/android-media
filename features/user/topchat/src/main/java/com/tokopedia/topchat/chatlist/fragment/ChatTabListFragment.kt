package com.tokopedia.topchat.chatlist.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.activity.ChatListActivity.Companion.BUYER_ANALYTICS_LABEL
import com.tokopedia.topchat.chatlist.activity.ChatListActivity.Companion.SELLER_ANALYTICS_LABEL
import com.tokopedia.topchat.chatlist.adapter.ChatListPagerAdapter
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.listener.ChatListContract
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel.Companion.ROLE_BUYER
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel.Companion.ROLE_SELLER
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatTabCounterViewModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import com.tokopedia.topchat.common.custom.ToolTipSearchPopupWindow
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

open class ChatTabListFragment constructor() : BaseDaggerFragment(), ChatListContract.TabFragment {

    override fun getScreenName(): String = "/new-inbox/chat"

    private lateinit var fragmentAdapter: ChatListPagerAdapter
    private val tabList = ArrayList<ChatListPagerAdapter.ChatListTab>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var chatListAnalytics: ChatListAnalytic

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var webSocketViewModel: WebSocketViewModel
    private lateinit var chatNotifCounterViewModel: ChatTabCounterViewModel
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_tab_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindView(view)
        initViewModel()
        initTabList()
        initViewPagerAdapter()
        initViewPager()
        initTabLayout()
        initViewModel()
        initData()
        initOnBoarding()
        initChatCounterObserver()
        initToolTip()
        initBackground()
        context?.let { UpdateShopActiveService.startService(it) }
    }

    private fun initBackground() {
        if (GlobalConfig.isSellerApp()) {
            context?.let {
                viewPager?.setBackgroundColor(MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
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
        stopLiveDataObserver()
        searchToolTip?.dismiss()
    }

    /**
     * set to `protected open` so that it can be disabled on UI test
     */
    protected open fun initToolTip() {
        searchToolTip = ToolTipSearchPopupWindow(context, chatNotifCounterViewModel)
    }

    protected open fun isOnBoardingAlreadyShown(): Boolean {
        return context?.let { CoachMarkPreference.hasShown(it, TAG_ONBOARDING) } ?: true
    }

    override fun initInjector() {
        DaggerChatListComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatListContextModule(context?.let { ChatListContextModule(it) })
                .build()
                .inject(this)
    }

    override fun notifyViewCreated() {
        if (!fragmentViewCreated) {
            webSocketViewModel.connectWebSocket()
            fragmentViewCreated = true
        }
    }

    override fun increaseUserNotificationCounter() {
        increaseNotificationCounter(R.drawable.ic_chat_icon_account)
    }

    override fun increaseSellerNotificationCounter() {
        increaseNotificationCounter(R.drawable.ic_chat_icon_shop)
    }

    override fun decreaseUserNotificationCounter() {
        decreaseNotificationCounter(R.drawable.ic_chat_icon_account)
    }

    override fun decreaseSellerNotificationCounter() {
        decreaseNotificationCounter(R.drawable.ic_chat_icon_shop)
    }

    private fun initChatCounterObserver() {
        chatNotifCounterViewModel.chatNotifCounter.observe(viewLifecycleOwner,
                Observer { result ->
                    when (result) {
                        is Success -> {
                            tabList[0].counter = result.data.chatNotifications.chatTabCounter.unreadsSeller.toString()
                            if (tabList.size > 1) {
                                tabList[1].counter = result.data.chatNotifications.chatTabCounter.unreadsUser.toString()
                            }
                            setNotificationCounterOnTab()
                        }
                    }
                }
        )
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

    private fun initTabLayout() {
        if (tabList.size == 1) {
            tabLayout?.hide()
            return
        }

        tabLayout?.removeAllTabs()
        for (i in 0 until tabList.size) {
            tabLayout?.newTab()?.let { tabLayout?.addTab(it) }
            tabLayout?.setBackgroundColor(MethodChecker.getColor(
                    context, com.tokopedia.unifyprinciples.R.color.Unify_N0
            ))
        }

        val tabCount = tabLayout?.tabCount ?: 0
        for (i in 0 until tabCount) {
            val title = tabList[i].title
            val icon = tabList[i].icon
            val counter = tabList[i].counter
            val tab = tabLayout?.getTabAt(i)
            tab?.customView = createCustomView(title, icon, counter)
            if (i == viewPager?.currentItem) {
                setTabSelectedView(tab?.customView)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout?.elevation = 0f
        }
        tabLayout?.background = context?.let {
            ContextCompat.getDrawable(it, R.drawable.bg_chat_list_tab_layout)
        }
        tabLayout?.tabMode = TabLayout.MODE_FIXED

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabUnSelectedView(tab.customView)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager?.setCurrentItem(tab.position, true)
                context?.let { chatNotifCounterViewModel.setLastVisitedTab(it, tab.position) }
                setTabSelectedView(tab.customView)
                with(chatListAnalytics) {
                    eventClickTabChat(if (tab.position == 0) SELLER_ANALYTICS_LABEL else BUYER_ANALYTICS_LABEL)
                }
            }
        })

        if (tabList.size == 1) {
            tabLayout?.hide()
        } else {
            goToLastSeenTab()
        }
    }

    private fun createCustomView(title: String, icon: Int, counter: String): View? {
        val customView = LayoutInflater.from(context).inflate(R.layout.item_chat_tab, null)
        val titleView = customView.findViewById<TextView>(R.id.title)
        val iconView = customView.findViewById<ImageView>(R.id.icon)
        titleView.text = setTitleTab(title, counter)
        iconView.setImageDrawable(MethodChecker.getDrawable(context, icon))
        return customView
    }

    private fun setTabSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(
                context, com.tokopedia.unifyprinciples.R.color.Unify_G500
        ))

        val icon = customView?.findViewById<ImageView>(R.id.icon)?.drawable
        icon?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, MethodChecker.getColor(
                    context, com.tokopedia.unifyprinciples.R.color.Unify_G500
            ))
        }
    }

    private fun setTabUnSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(
                context, com.tokopedia.unifyprinciples.R.color.Unify_N200
        ))

        val icon = customView?.findViewById<ImageView>(R.id.icon)?.drawable
        icon?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, MethodChecker.getColor(
                    context, com.tokopedia.unifyprinciples.R.color.Unify_N200
            ))
        }
    }

    private fun goToLastSeenTab() {
        context?.let {
            chatNotifCounterViewModel.getLastVisitedTab(it).apply {
                if (this == -1) return@apply
                viewPager?.setCurrentItem(this, false)
            }
        }
    }

    private fun setTitleTab(title: String, counter: String): CharSequence? {
        if (counter.toLongOrZero() > 0) {
            val counterFormatted: String =
                    if (counter.toLongOrZero() > 99) {
                        "99+"
                    } else {
                        counter
                    }

            return if (title.length > 10) {
                title.take(9) + ".. ($counterFormatted)"
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
                R.drawable.ic_chat_icon_shop
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
                R.drawable.ic_chat_icon_account
        )
        tabList.add(buyerTabFragment)
    }

    open protected fun createBuyerTabFragment(): ChatListFragment {
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
        viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        webSocketViewModel = viewModelProvider.get(WebSocketViewModel::class.java)
        chatNotifCounterViewModel = viewModelProvider.get(ChatTabCounterViewModel::class.java)
    }

    private fun initWebsocketChatObserver() {
        webSocketViewModel.itemChat.observe(this,
                Observer { result ->
                    when (result) {
                        is Success -> {
                            when (result.data) {
                                is IncomingChatWebSocketModel -> forwardToFragment(result.data as IncomingChatWebSocketModel)
                                is IncomingTypingWebSocketModel -> forwardToFragment(result.data as IncomingTypingWebSocketModel)
                            }
                        }
                    }
                }
        )
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
        return (tag == ROLE_SELLER && fromUid != userSession.userId)
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
        chatNotifCounterViewModel.queryGetNotifCounter()
    }

    override fun showSearchOnBoardingTooltip() {
        if (
                chatNotifCounterViewModel.isSearchOnBoardingTooltipHasShown() ||
                !isFinishShowingCoachMarkOnBoarding ||
                activity?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.STARTED) == false
        ) return
        val toolbar = chatTabListListener?.getActivityToolbar()
        toolbar?.post {
            val searchView = toolbar.findViewById<View>(R.id.menu_chat_search)
            searchToolTip?.showAtBottom(searchView)
        }
    }

    override fun closeSearchTooltip() {
        searchToolTip?.dismissOnBoarding()
    }

    private fun decreaseNotificationCounter(iconId: Int) {
        for ((tabIndex, tab) in tabList.withIndex()) {
            if (tab.icon == iconId) {
                decreaseTabCounter(tabIndex, tab)
            }
        }
    }

    private fun increaseNotificationCounter(iconId: Int) {
        for ((tabIndex, tab) in tabList.withIndex()) {
            if (tab.icon == iconId) {
                increaseTabCounter(tabIndex, tab)
            }
        }
    }

    private fun increaseTabCounter(tabIndex: Int, tab: ChatListPagerAdapter.ChatListTab) {
        tab.increaseTabCounter()
        setupTabTitleAt(tabIndex)
    }

    private fun decreaseTabCounter(tabIndex: Int, tab: ChatListPagerAdapter.ChatListTab) {
        tab.decreaseTabCounter()
        setupTabTitleAt(tabIndex)
    }

    private fun initOnBoarding() {
        if (!userSession.hasShop()) {
            isFinishShowingCoachMarkOnBoarding = true
            return
        }
        tabLayout?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
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
        context?.let { CoachMarkPreference.setShown(it, TAG_ONBOARDING, true) }
    }


    private fun stopLiveDataObserver() {
        if (::chatNotifCounterViewModel.isInitialized) {
            chatNotifCounterViewModel.chatNotifCounter.removeObservers(this)
        }
    }

    private fun stopWebsocketLiveDataObserver() {
        webSocketViewModel.itemChat.removeObservers(this)
    }

    private fun clearLiveDataValue() {
        webSocketViewModel.clearItemChatValue()
    }

    companion object {
        private val TAG_ONBOARDING = ChatTabListFragment::class.java.name + ".OnBoarding"

        @JvmStatic
        fun create(): ChatTabListFragment {
            return ChatTabListFragment()
        }
    }

}