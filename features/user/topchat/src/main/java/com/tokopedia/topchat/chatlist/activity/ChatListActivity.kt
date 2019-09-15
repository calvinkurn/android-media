package com.tokopedia.topchat.chatlist.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.ChatListPagerAdapter
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment
import com.tokopedia.topchat.chatlist.listener.ChatListWebSocketContract
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.viewmodel.ChatTabCounterViewModel
import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class ChatListActivity : BaseTabActivity()
        , HasComponent<ChatListComponent>
        , ChatListWebSocketContract.Activity{

    private lateinit var fragmentAdapter: ChatListPagerAdapter
    private val tabList = ArrayList<ChatListPagerAdapter.ChatListTab>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

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
        tabList.add(ChatListPagerAdapter.ChatListTab(
                userSession.shopName,
                "0",
                ChatListFragment.createFragment(ChatListQueriesConstant.PARAM_TAB_SELLER),
                R.drawable.ic_chat_icon_shop
        ))
        tabList.add(ChatListPagerAdapter.ChatListTab(
                userSession.name,
                "0",
                ChatListFragment.createFragment(ChatListQueriesConstant.PARAM_TAB_USER),
                R.drawable.ic_chat_icon_account
        ))
        super.onCreate(savedInstanceState)

        initTabLayout()
        setObserver()
        initData()
    }

    private fun setObserver() {
        viewModelProvider = ViewModelProviders.of(this@ChatListActivity, viewModelFactory)

        webSocketViewModel = viewModelProvider.get(WebSocketViewModel::class.java)
        webSocketViewModel?.itemChat?.observe(this,
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

        chatNotifCounterViewModel = viewModelProvider.get(ChatTabCounterViewModel::class.java)
        chatNotifCounterViewModel.chatNotifCounter.observe(this,
                Observer { result ->
                    when (result) {
                        is Success -> {
                            tabList[0].counter = result.data.chatNotifications.chatTabCounter.unreadsSeller.toString()
                            tabList[1].counter = result.data.chatNotifications.chatTabCounter.unreadsUser.toString()
                            initTabLayout()
                        }
                    }
                }
        )

    }

    private fun initData() {
        chatNotifCounterViewModel.queryGetNotifCounter()
    }


    private fun forwardToFragment(incomingChatWebSocketModel: IncomingChatWebSocketModel) {
        debug("stevenObserver", incomingChatWebSocketModel.toString())
        val fragment: ChatListFragment = determineFragmentByTag(incomingChatWebSocketModel.contact?.tag)
        fragment.processIncomingMessage(incomingChatWebSocketModel)
    }


    private fun forwardToFragment(incomingTypingWebSocketModel: IncomingTypingWebSocketModel) {
        debug("stevenObserver", incomingTypingWebSocketModel.toString())
        val fragment: ChatListFragment = determineFragmentByTag(incomingTypingWebSocketModel.contact?.tag)
        fragment.processIncomingMessage(incomingTypingWebSocketModel)
    }

    private fun determineFragmentByTag(tag: String?): ChatListFragment {
        return when (tag) {
            "User" -> fragmentAdapter.getItem(0) as ChatListFragment
            else -> fragmentAdapter.getItem(1) as ChatListFragment
        }
    }


    override fun notifyViewCreated() {
        if(!fragmentViewCreated) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.elevation = 10f
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
                setTabSelectedView(tab.customView)
            }
        })
    }

    private fun createCustomView(title: String, icon: Int, counter: String): View? {
        val customView = LayoutInflater.from(this).inflate(R.layout.item_chat_tab, null)
        val titleView = customView.findViewById<TextView>(R.id.title)
        val iconView = customView.findViewById<ImageView>(R.id.icon)
        titleView.text = setTitleTab(title,counter)
        iconView.setImageDrawable(MethodChecker.getDrawable(this, icon))
        return customView
    }

    private fun setTitleTab(title: String, counter: String): CharSequence? {
        if(counter.toLongOrZero() > 0) {
            val counterFormatted: String =
                if (counter.toLongOrZero() > 99) {
                    "99+"
                } else {
                    counter
                }

            return if(title.length > 10) {
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
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.TOPCHAT_IDLESS)
        @JvmStatic
        fun createIntent(context: Context, extras: Bundle) = createIntent(context)
    }


    companion object {
        fun createIntent(context: Context) = Intent(context, ChatListActivity::class.java)
    }

    override fun getComponent(): ChatListComponent {
        return DaggerChatListComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}
