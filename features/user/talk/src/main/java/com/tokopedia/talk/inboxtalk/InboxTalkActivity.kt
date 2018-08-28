package com.tokopedia.talk.inboxtalk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.talk.R
import com.tokopedia.talk.inboxtalk.adapter.InboxTalkPagerAdapter
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_inbox_talk.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkActivity : BaseSimpleActivity() {

    private lateinit var inboxTalkPagerAdapter: InboxTalkPagerAdapter

    @Inject
    lateinit var userSession: UserSession

    private val titles by lazy {
        arrayOf(getString(R.string.title_tab_talk_all),
                getString(R.string.title_tab_talk_my_product),
                getString(R.string.title_tab_talk_follow))
    }

    companion object {

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
        return R.layout.activity_inbox_talk
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)

        initPagerAdapter()
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.adapter = inboxTalkPagerAdapter

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab) {

            }
        })

    }

    private fun initInjector() {

    }

    private fun initPagerAdapter() {
        inboxTalkPagerAdapter = InboxTalkPagerAdapter(supportFragmentManager, titles)
    }
}
