package com.tokopedia.navigation.presentation.activity

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
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.domain.pojo.NotificationUpdateUnread
import com.tokopedia.navigation.presentation.adapter.NotificationFragmentAdapter
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationUpdateComponent
import com.tokopedia.navigation.presentation.fragment.NotificationFragment
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment
import com.tokopedia.navigation.presentation.presenter.NotificationActivityPresenter
import com.tokopedia.navigation.presentation.view.listener.NotificationActivityContract
import javax.inject.Inject

/**
 * Created by meta on 20/06/18.
 */
@DeepLink(ApplinkConst.NOTIFICATION)
class NotificationActivity : BaseTabActivity(), HasComponent<BaseAppComponent>, NotificationActivityContract.View,
        NotificationUpdateFragment.NotificationUpdateListener {

    @Inject
    lateinit var presenter: NotificationActivityPresenter

    @Inject
    lateinit var analytics: NotificationUpdateAnalytics

    private var fragmentAdapter: NotificationFragmentAdapter? = null
    private val tabList = ArrayList<NotificationFragmentAdapter.NotificationFragmentItem>()
    private var updateCounter = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        tabList.add(NotificationFragmentAdapter.NotificationFragmentItem(getString(R.string.title_notification_activity), NotificationFragment()))
        tabList.add(NotificationFragmentAdapter.NotificationFragmentItem(getString(R.string.title_notification_update), NotificationUpdateFragment()))
        super.onCreate(savedInstanceState)
        initInjector()
        initView()
    }

    fun initInjector() {
        DaggerNotificationUpdateComponent.builder()
                .baseAppComponent(component).build().inject(this)
        presenter.attachView(this)
    }

    private fun initView() {
        initTabLayout()
        presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
    }

    override fun onSuccessLoadNotifUpdate() {
        clearNotifCounter(INDEX_NOTIFICATION_UPDATE)
    }

    private fun onSuccessGetUpdateUnreadCounter(): (NotificationUpdateUnread) -> Unit {
        return {
            if (it.pojo.notifUnreadInt > 0) {
                var notif = tabLayout.getTabAt(INDEX_NOTIFICATION_UPDATE)?.customView?.findViewById<View>(R.id.circle)
                notif?.visibility = View.VISIBLE
            }
            updateCounter = it.pojo.notifUnreadInt
            setCounterNotificationUpdate()
        }
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
                sendAnalytics(tab.position)
                clearNotifCounter(tab.position)
                setTabSelectedView(tab.customView)
                resetCircle(tab.customView)
            }
        })
    }

    private fun clearNotifCounter(position: Int) {
        if(position == INDEX_NOTIFICATION_UPDATE) {
            presenter.clearNotifCounter()
            resetCounterNotificationUpdate()
        }
    }


    override fun resetCounterNotificationUpdate() {
        updateCounter = 0
        setCounterNotificationUpdate()
    }

    private fun sendAnalytics(position: Int) {
        if(position == INDEX_NOTIFICATION_UPDATE) {
            analytics.trackClickNewestInfo()
        }
    }

    private fun resetCircle(customView: View?) {
        var notif = customView?.findViewById<View>(R.id.circle)
        notif?.visibility = View.GONE
    }

    private fun setTabSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.Green_G500))
    }

    private fun setTabUnSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, R.color.Neutral_N200))
    }

    private fun createCustomView(title: String): View? {
        var customView = LayoutInflater.from(this).inflate(R.layout.item_notification_tab_title, null);
        var titleView = customView.findViewById<TextView>(R.id.title)
        titleView.text = title
        return customView
    }

    override fun getViewPagerAdapter(): PagerAdapter? {
        fragmentAdapter = NotificationFragmentAdapter(supportFragmentManager)
        fragmentAdapter?.setItemList(tabList)

        return fragmentAdapter
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_notification_page
    }

    override fun getPageLimit(): Int {
        return tabList.size + 1
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    private fun setCounterNotificationUpdate() {
        val defaultTitle = getString(R.string.title_notification_update)
        var counter = ""
        if (updateCounter > 0) {
            counter = getString(R.string.title_counter_update_notification, updateCounter.toString())
        } else if (updateCounter > 99) {
            counter = getString(R.string.title_counter_update_notification, getString(R.string.exceed_ninety_nine))
        }
        val titleView: TextView? = tabLayout.getTabAt(INDEX_NOTIFICATION_UPDATE)?.customView?.findViewById(R.id.title)
        titleView?.let {
            it.text = String.format("%s%s", defaultTitle, counter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


    companion object {

        var INDEX_NOTIFICATION_ACTIVITY = 0
        var INDEX_NOTIFICATION_UPDATE = 1

        fun start(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }
    }
}
