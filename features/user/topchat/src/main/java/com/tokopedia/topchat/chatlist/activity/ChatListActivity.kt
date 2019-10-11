package com.tokopedia.topchat.chatlist.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.ChatListPagerAdapter
import com.tokopedia.topchat.chatlist.analytic.ChatListAnalytic
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.listener.ChatListContract
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatTabCounterViewModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class ChatListActivity : BaseTabActivity()
        , HasComponent<ChatListComponent>
        , ChatListContract.Activity {

    private lateinit var fragmentAdapter: ChatListPagerAdapter
    private val tabList = ArrayList<ChatListPagerAdapter.ChatListTab>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var chatListAnalytics: ChatListAnalytic

    lateinit var viewModelProvider: ViewModelProvider
    lateinit var webSocketViewModel: WebSocketViewModel
    lateinit var chatNotifCounterViewModel: ChatTabCounterViewModel

    private var fragmentViewCreated = false

    private fun initInjector() {
        component.inject(this)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_chat_list
    }

    override fun getViewPagerAdapter(): PagerAdapter? {
        fragmentAdapter = ChatListPagerAdapter(supportFragmentManager)
        fragmentAdapter.setItemList(tabList)

        return fragmentAdapter
    }

    override fun getPageLimit(): Int {
        return tabList.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        if (userSession.shopId.toLongOrZero() > 0) {
            tabList.add(ChatListPagerAdapter.ChatListTab(
                    userSession.shopName,
                    "0",
                    ChatListFragment.createFragment(ChatListQueriesConstant.PARAM_TAB_SELLER),
                    R.drawable.ic_chat_icon_shop
            ))
        }
        tabList.add(ChatListPagerAdapter.ChatListTab(
                userSession.name,
                "0",
                ChatListFragment.createFragment(ChatListQueriesConstant.PARAM_TAB_USER),
                R.drawable.ic_chat_icon_account
        ))
        super.onCreate(savedInstanceState)

        setupViewModel()
        initTabLayout()
        setObserver()
        initData()
        initOnBoarding()
    }

    private fun initOnBoarding() {
        if (!userSession.hasShop()) return
        tabLayout.viewTreeObserver.addOnGlobalLayoutListener {
            if (!isOnBoardingAlreadyShown())  {
                showOnBoarding()
            }
        }
    }

    private fun showOnBoarding() {
        if (tabLayout.childCount < 0) return
        val tabViewGroup = tabLayout.getChildAt(0) as ViewGroup
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
        CoachMark().show(this@ChatListActivity, TAG_ONBOARDING, tutorials)
        CoachMarkPreference.setShown(this, TAG_ONBOARDING, true)
    }

    private fun isOnBoardingAlreadyShown(): Boolean {
        return CoachMarkPreference.hasShown(this, TAG_ONBOARDING)
    }

    private fun setupViewModel() {
        viewModelProvider = ViewModelProviders.of(this@ChatListActivity, viewModelFactory)
        webSocketViewModel = viewModelProvider.get(WebSocketViewModel::class.java)
        chatNotifCounterViewModel = viewModelProvider.get(ChatTabCounterViewModel::class.java)
    }

    private fun setObserver() {
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

        chatNotifCounterViewModel.chatNotifCounter.observe(this,
                Observer { result ->
                    when (result) {
                        is Success -> {
                            tabList[0].counter = result.data.chatNotifications.chatTabCounter.unreadsSeller.toString()
                            if(tabList.size > 1) {
                                tabList[1].counter = result.data.chatNotifications.chatTabCounter.unreadsUser.toString()
                            }
                            setNotificationCounterOnTab()
                        }
                    }
                }
        )
    }

    private fun initData() {
        loadNotificationCounter()
    }

    override fun loadNotificationCounter() {
        chatNotifCounterViewModel.queryGetNotifCounter()
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

    private fun forwardToFragment(incomingChatWebSocketModel: IncomingChatWebSocketModel) {
        debug(TAG, incomingChatWebSocketModel.toString())
        val fragment: ChatListFragment? = determineFragmentByTag(incomingChatWebSocketModel.contact?.tag)
        fragment?.processIncomingMessage(incomingChatWebSocketModel)
    }


    private fun forwardToFragment(incomingTypingWebSocketModel: IncomingTypingWebSocketModel) {
        debug(TAG, incomingTypingWebSocketModel.toString())
        val fragment: ChatListFragment? = determineFragmentByTag(incomingTypingWebSocketModel.contact?.tag)
        fragment?.processIncomingMessage(incomingTypingWebSocketModel)
    }

    private fun determineFragmentByTag(tag: String?): ChatListFragment? {
        val fragment = when (tag) {
            "User" -> fragmentAdapter.getItem(0)
            else -> fragmentAdapter.getItem(1)
        }

        return if(fragment == null) {
            null
        } else  {
            fragment as ChatListFragment
        }
    }


    override fun notifyViewCreated() {
        if (!fragmentViewCreated) {
            webSocketViewModel.connectWebSocket()
            fragmentViewCreated = true
        }
    }

    private fun initTabLayout() {
        tabLayout.removeAllTabs()
        for (i in 0 until tabList.size) {
            tabLayout.addTab(tabLayout.newTab())
            tabLayout.setBackgroundColor(MethodChecker.getColor(this, R.color.white))
        }
        for (i in 0 until tabLayout.tabCount) {
            val title = tabList[i].title
            val icon = tabList[i].icon
            val counter = tabList[i].counter
            val tab = tabLayout.getTabAt(i)
            tab?.customView = createCustomView(title, icon, counter)
            if (i == viewPager.currentItem) {
                setTabSelectedView(tab?.customView)
            }
        }

        tabLayout.setBackgroundResource(R.color.white)
        tabLayout.tabMode = TabLayout.MODE_FIXED

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabUnSelectedView(tab.customView)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.setCurrentItem(tab.position, true)
                chatNotifCounterViewModel.setLastVisitedTab(this@ChatListActivity, tab.position)
                setTabSelectedView(tab.customView)
                with(chatListAnalytics) {
                    eventClickTabChat(if (tab.position == 0) SELLER_ANALYTICS_LABEL else BUYER_ANALYTICS_LABEL)
                }
            }
        })

        if (tabList.size == 1) {
            tabLayout.hide()
        } else {
            goToLastSeenTab()
        }
    }

    private fun goToLastSeenTab() {
        chatNotifCounterViewModel.getLastVisitedTab(this).apply {
            if (this == -1) return@apply
            viewPager.currentItem = this
        }
    }

    private fun setNotificationCounterOnTab() {
        for (i in 0 until tabLayout.tabCount) {
            setupTabTitleAt(i)
        }
    }

    private fun setupTabTitleAt(tabPosition: Int) {
        val title = tabList[tabPosition].title
        val counter = tabList[tabPosition].counter
        val tabTitle = setTitleTab(title, counter)
        val tab = tabLayout.getTabAt(tabPosition)

        tab?.customView?.findViewById<TextView>(R.id.title)?.apply {
            text = tabTitle
        }
    }

    private fun createCustomView(title: String, icon: Int, counter: String): View? {
        val customView = LayoutInflater.from(this).inflate(R.layout.item_chat_tab, null)
        val titleView = customView.findViewById<TextView>(R.id.title)
        val iconView = customView.findViewById<ImageView>(R.id.icon)
        titleView.text = setTitleTab(title, counter)
        iconView.setImageDrawable(MethodChecker.getDrawable(this, icon))
        return customView
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
        return title
    }

    private fun setTabSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.Green_G500))

        val icon = customView?.findViewById<ImageView>(R.id.icon)?.drawable
        icon?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, MethodChecker.getColor(this, R.color.Green_G500))
        }
    }

    private fun setTabUnSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.Neutral_N200))

        val icon = customView?.findViewById<ImageView>(R.id.icon)?.drawable
        icon?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, MethodChecker.getColor(this, R.color.Neutral_N200))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketViewModel.itemChat.removeObservers(this)
        webSocketViewModel.clear()
        chatNotifCounterViewModel.chatNotifCounter.removeObservers(this)
        chatNotifCounterViewModel.clear()
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.TOPCHAT_IDLESS)
        @JvmStatic
        fun createIntent(context: Context, extras: Bundle) = createIntent(context)
    }


    companion object {
        const val BUYER_ANALYTICS_LABEL = "buyer"
        const val SELLER_ANALYTICS_LABEL = "seller"
        const val TAG = "ChatListActivity"
        private val TAG_ONBOARDING = ChatListActivity::class.java.name + ".OnBoarding"
        fun createIntent(context: Context) = Intent(context, ChatListActivity::class.java)
    }

    override fun getComponent(): ChatListComponent {
        return DaggerChatListComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}
