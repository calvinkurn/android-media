package com.tokopedia.talk.inboxtalk.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.talk.R
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkPagerAdapter
import com.tokopedia.talk.inboxtalk.view.listener.GetUnreadNotificationListener
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_talk_inbox.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent>,
        GetUnreadNotificationListener {

    private lateinit var inboxTalkPagerAdapter: InboxTalkPagerAdapter

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics : TalkAnalytics

    private lateinit var titles: Array<String>

    companion object {

        val NAVIGATION = "nav"

        val INBOX_ALL = "inbox-talk"
        val MY_PRODUCT = "inbox-talk-my-product"
        val FOLLOWING = "inbox-talk-following"

        @JvmStatic
        fun createIntent(context: Context) = Intent(context, InboxTalkActivity::class.java)

    }

    object DeepLinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.TALK)
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return (context.applicationContext as TalkRouter).getInboxTalkCallingIntent(context)
        }

    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_talk_inbox
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)

        initPagerAdapter()

    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun initInjector() {
        val inboxTalkComponent = DaggerInboxTalkComponent.builder()
                .talkComponent(component)
                .build()
        inboxTalkComponent.inject(this)
    }

    private fun initPagerAdapter() {
        if (GlobalConfig.isSellerApp()) {
            titles = arrayOf(getString(R.string.title_tab_talk_my_product))
            tabLayout.visibility = View.GONE
        } else if (userSession.hasShop()) {
            titles = arrayOf(getString(R.string.title_tab_talk_all),
                    getString(R.string.title_tab_talk_my_product),
                    getString(R.string.title_tab_talk_follow))
            tabLayout.visibility = View.VISIBLE
        } else {
            titles = arrayOf(getString(R.string.title_tab_talk_all))
            tabLayout.visibility = View.GONE
        }

        inboxTalkPagerAdapter = InboxTalkPagerAdapter(supportFragmentManager, titles)

        viewPager.offscreenPageLimit = title.length
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.adapter = inboxTalkPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        for (i: Int in 0 until tabLayout.tabCount) {
            tabLayout.getTabAt(i)?.customView = getTabCustomView(titles[i])
        }
        tabLayout.getTabAt(0)?.run {
            setTabSelected(this)
        }


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTabUnSelected(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                analytics.trackSelectTab(tab.text.toString().toLowerCase())
                setTabSelected(tab)
            }
        })
    }

    private fun setTabUnSelected(tab: TabLayout.Tab) {
        tab.customView?.run {
            val view: View = this
            val title: TextView = view.findViewById(R.id.title)
            title.setTextColor(MethodChecker.getColor(this.context, R.color.black_38))
            title.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        }
    }

    private fun setTabSelected(tab: TabLayout.Tab) {
        tab.customView?.run {
            val view: View = this
            val title: TextView = view.findViewById(R.id.title)
            title.setTextColor(MethodChecker.getColor(this.context, R.color.medium_green))
            title.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }
    }

    private fun getTabCustomView(titleText: String): View {
        val view: View = LayoutInflater.from(this).inflate(R.layout.custom_tab_inbox_talk, null)

        val title: TextView = view.findViewById(R.id.title)
        val notification: TextView = view.findViewById(R.id.notification)

        title.text = titleText
        notification.text = "0"
        return view
    }

    override fun onGetNotification(notifCount: Int, nav: String) {
        if (tabLayout.visibility == View.VISIBLE) {
            tabLayout.getTabAt(inboxTalkPagerAdapter.getFragmentPosition(nav))?.customView?.run {
                val notification: TextView = this.findViewById(R.id.notification)

                if (notifCount > 0) {
                    notification.visibility = View.VISIBLE
                    notification.text = notifCount.toString()
                } else {
                    notification.visibility = View.GONE
                }
            }
        }
    }
}
