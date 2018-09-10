package com.tokopedia.talk.inboxtalk.view.activity

import android.content.Context
import android.content.Intent
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.talk.R
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import com.tokopedia.talk.inboxtalk.view.adapter.InboxTalkPagerAdapter
import com.tokopedia.talk.inboxtalk.view.listener.GetUnreadNotificationListener
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_talk_inbox.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent>,
        GetUnreadNotificationListener {

    private lateinit var inboxTalkPagerAdapter: InboxTalkPagerAdapter

    @Inject
    lateinit var userSession: UserSession

    lateinit var titles: Array<String>


    companion object {

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
            return Intent(context, InboxTalkActivity::class.java)
                    .putExtras(extras)
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
        if (userSession.hasShop()) {
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
        for (i: Int in 0 until tabLayout.tabCount - 1) {
            tabLayout.getTabAt(i)?.customView = getTabCustomView(titles[i])
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun getTabCustomView(titleText: String): View {
        val view: View = LayoutInflater.from(this).inflate(R.layout.custom_tab_inbox_talk, null)

        val title: TextView = view.findViewById(R.id.title)
        val notif: TextView = view.findViewById(R.id.notification)

        title.text = titleText
        notif.text = "0"
        return view
    }

    override fun onGetNotification(notification: Int, nav: String) {
        if (tabLayout.visibility == View.VISIBLE) {
            //TODO SET NOTIFICATION
        }
    }
}
