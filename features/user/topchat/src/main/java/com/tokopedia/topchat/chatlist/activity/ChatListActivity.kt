package com.tokopedia.topchat.chatlist.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.adapter.ChatListPagerAdapter
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.fragment.ChatListFragment

class ChatListActivity : BaseTabActivity()
        , HasComponent<ChatListComponent> {

    private var fragmentAdapter: ChatListPagerAdapter? = null
    private val tabList = ArrayList<ChatListPagerAdapter.ChatListTab>()

    override fun getLayoutRes(): Int {
        return R.layout.activity_chat_list
    }

    override fun getViewPagerAdapter(): PagerAdapter? {
        fragmentAdapter = ChatListPagerAdapter(supportFragmentManager)
        fragmentAdapter?.setItemList(tabList)

        return fragmentAdapter
    }

    override fun getPageLimit(): Int {
        return tabList.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        tabList.add(ChatListPagerAdapter.ChatListTab("Toko saya", "0", ChatListFragment()))
        tabList.add(ChatListPagerAdapter.ChatListTab("Saya", "0", ChatListFragment()))
        super.onCreate(savedInstanceState)

        initTabLayout()
    }

    private fun initTabLayout() {
        for (i in 0 until tabList.size) {
            tabLayout.addTab(tabLayout.newTab())
        }
        for (i in 0 until tabLayout.tabCount) {
            val title = tabList[i].title
            val tab = tabLayout.getTabAt(i)
            tab?.customView = createCustomView(title)
            if (i == viewPager.currentItem) {
                setTabSelectedView(tab?.customView)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.elevation = 0f
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

    private fun createCustomView(title: String): View? {
        val customView = LayoutInflater.from(this).inflate(R.layout.item_chat_tab, null);
        val titleView = customView.findViewById<TextView>(R.id.title)
        titleView.text = title
        return customView
    }

    private fun setTabSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.Green_G500))
    }

    private fun setTabUnSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.Neutral_N200))
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
