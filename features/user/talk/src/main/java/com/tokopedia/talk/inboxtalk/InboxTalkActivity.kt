package com.tokopedia.talk.inboxtalk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.talk.R
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.inboxtalk.adapter.InboxTalkPagerAdapter
import com.tokopedia.talk.inboxtalk.di.DaggerInboxTalkComponent
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.activity_inbox_talk.*
import javax.inject.Inject

/**
 * @author by nisie on 8/27/18.
 */

class InboxTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    private lateinit var inboxTalkPagerAdapter: InboxTalkPagerAdapter

    @Inject
    lateinit var userSession: UserSession

    lateinit var titles: Array<String>

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
}
