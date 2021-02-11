package com.tokopedia.talk.feature.inbox.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTracking
import com.tokopedia.talk.feature.inbox.data.TalkInboxTab
import com.tokopedia.talk.feature.inbox.di.DaggerTalkInboxContainerComponent
import com.tokopedia.talk.feature.inbox.di.TalkInboxContainerComponent
import com.tokopedia.talk.feature.inbox.presentation.adapter.TalkInboxContainerAdapter
import com.tokopedia.talk.feature.inbox.presentation.listener.TalkInboxListener
import com.tokopedia.talk.R
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.setCounter
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.setNotification
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_talk_inbox.*
import kotlinx.android.synthetic.main.fragment_talk_inbox_container.*
import javax.inject.Inject

class TalkInboxContainerFragment : BaseDaggerFragment(), HasComponent<TalkInboxContainerComponent>, TalkInboxListener {

    companion object {
        const val SELLER_TAB_INDEX = 0
        const val BUYER_TAB_INDEX = 1
        const val HIDE_TAB_COUNTER = -1
        fun createNewInstance(): TalkInboxContainerFragment {
            return TalkInboxContainerFragment()
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var talkInboxTracking: TalkInboxTracking

    private var sellerUnreadCount = 0L
    private var buyerUnreadCount = 0L
    private var isFirstTimeEnterPage = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_talk_inbox_container, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun getComponent(): TalkInboxContainerComponent? {
        return activity?.run {
            DaggerTalkInboxContainerComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setupViewPager()
        setupAdapter()
        setupTabLayout()
    }

    override fun updateUnreadCounter(sellerUnread: Long, buyerUnread: Long) {
        sellerUnreadCount = sellerUnread
        talkInboxTabs.tabLayout.getTabAt(SELLER_TAB_INDEX)?.setCounter(if(sellerUnread > 0) sellerUnread.toInt() else HIDE_TAB_COUNTER)
        buyerUnreadCount = buyerUnread
        talkInboxTabs.tabLayout.getTabAt(BUYER_TAB_INDEX)?.setCounter(if(buyerUnread > 0) buyerUnread.toInt() else HIDE_TAB_COUNTER)
        if(isFirstTimeEnterPage) {
            isFirstTimeEnterPage = false
            when {
                buyerUnreadCount > 0 && sellerUnreadCount == 0L -> {
                    selectBuyerTab()
                }
                else -> {
                    selectSellerTab()
                }
            }
        }
    }

    private fun selectSellerTab() {
        talkInboxViewPager.currentItem = SELLER_TAB_INDEX
    }

    private fun selectBuyerTab() {
        talkInboxViewPager.currentItem = BUYER_TAB_INDEX
    }

    private fun setupViewPager() {
        val tabTitles = getTabTitles()
        tabTitles.forEach {
            talkInboxTabs.addNewTab(it)
        }
        talkInboxViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                talkInboxTabs.getUnifyTabLayout().getTabAt(position)?.select()
            }
        })
    }

    private fun setupTabLayout() {
        talkInboxTabs.customTabMode = TabLayout.MODE_FIXED
        talkInboxTabs.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                //No Op
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //No Op
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                trackTabChange(tab.position)
                talkInboxViewPager.setCurrentItem(tab.position, true)
            }

        })
    }

    private fun setupAdapter() {
        talkInboxViewPager.adapter = TalkInboxContainerAdapter(getFragmentList(), this)
    }

    private fun getFragmentList(): List<Fragment> {
        return listOf(
                TalkInboxFragment.createNewInstance(TalkInboxTab.TalkShopInboxTab(), this),
                TalkInboxFragment.createNewInstance(TalkInboxTab.TalkBuyerInboxTab(), this)
        )
    }

    private fun getTabTitles(): List<String> {
        return with(userSession) {
            listOf(MethodChecker.fromHtml(shopName).toString(), name)
        }
    }

    private fun trackTabChange(position: Int) {
        if(position == SELLER_TAB_INDEX) {
            talkInboxTracking.eventClickTab(TalkInboxTab.SHOP_OLD, userSession.userId, userSession.shopId, sellerUnreadCount)
        } else {
            talkInboxTracking.eventClickTab(TalkInboxTab.BUYER_TAB, userSession.userId, userSession.shopId, buyerUnreadCount)
        }
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(headerTalkInboxContainer)
                headerTalkInboxContainer?.title = getString(R.string.title_talk_discuss)
            }
        }
    }
}