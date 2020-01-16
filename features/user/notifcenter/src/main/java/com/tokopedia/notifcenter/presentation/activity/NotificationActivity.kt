package com.tokopedia.notifcenter.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.util.getParamInt
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.consts.Resources.Green_G500
import com.tokopedia.notifcenter.data.consts.Resources.Neutral_N200
import com.tokopedia.notifcenter.data.entity.NotifCenterSendNotifData
import com.tokopedia.notifcenter.data.entity.NotificationUpdateUnread
import com.tokopedia.notifcenter.di.DaggerNotificationUpdateComponent
import com.tokopedia.notifcenter.presentation.adapter.NotificationFragmentAdapter
import com.tokopedia.notifcenter.presentation.contract.NotificationActivityContract
import com.tokopedia.notifcenter.presentation.fragment.NotificationTransactionFragment
import com.tokopedia.notifcenter.presentation.fragment.NotificationUpdateFragment
import com.tokopedia.notifcenter.presentation.presenter.NotificationActivityPresenter
import com.tokopedia.notifcenter.util.CacheManager
import com.tokopedia.notifcenter.util.NotifPreference
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject

/**
 * Created by meta on 20/06/18.
 */

class NotificationActivity : BaseTabActivity(),
        HasComponent<BaseAppComponent>,
        NotificationActivityContract.View,
        NotificationUpdateFragment.NotificationUpdateListener {

    @Inject lateinit var presenter: NotificationActivityPresenter
    @Inject lateinit var analytics: NotificationUpdateAnalytics
    @Inject lateinit var notifPreference: NotifPreference
    @Inject lateinit var cacheManager: CacheManager

    private var fragmentAdapter: NotificationFragmentAdapter? = null
    private val tabList = ArrayList<NotificationFragmentAdapter.NotificationFragmentItem>()
    private var updateCounter = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        tabList.add(NotificationFragmentAdapter.NotificationFragmentItem(getString(R.string.title_notification_activity), NotificationTransactionFragment()))
        tabList.add(NotificationFragmentAdapter.NotificationFragmentItem(getString(R.string.title_notification_update), NotificationUpdateFragment()))

        super.onCreate(savedInstanceState)
        initInjector()
        initView()

        baseContext?.let {
            val remoteConfig = FirebaseRemoteConfigImpl(it)
            val redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(RED_DOT_GIMMICK_REMOTE_CONFIG_KEY, false)
            val redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif
            if (redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus) {
                notifPreference.isDisplayedGimmickNotif = true
                presenter.sendNotif(onSuccessSendNotif(), onErrorSendNotif())
            }
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun onSuccessSendNotif(): (NotifCenterSendNotifData) -> Unit {
        return {
            presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
        }
    }

    private fun onErrorSendNotif(): (Throwable) -> Unit {
        return {
            presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
        }
    }

    fun initInjector() {
        DaggerNotificationUpdateComponent.builder()
                .baseAppComponent(component).build().inject(this)
        presenter.attachView(this)
    }

    private fun initView() {
        val initialIndexPage = if (cacheManager.isExist(KEY_TAB_POSITION)) {
            cacheManager.read(KEY_TAB_POSITION)
        } else {
            getParamInt(Intent.EXTRA_TITLE,
                    intent.extras,
                    null,
                    INDEX_NOTIFICATION_ACTIVITY)
        }
        initTabLayout(initialIndexPage)
        presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
        presenter.getIsTabUpdate(this)
    }

    override fun goToUpdateTab() {
        if (cacheManager.isExist(KEY_TAB_POSITION)) {
            val position = cacheManager.read(KEY_TAB_POSITION)
            changeTabPager(position)
        } else {
            viewPager.currentItem = 1
        }
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

    private fun initTabLayout(initialIndexPage: Int) {
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
                changeTabPager(tab.position)
                clearNotifCounter(tab.position)
                setTabSelectedView(tab.customView)
                resetCircle(tab.customView)

            }
        })

        val tab = tabLayout.getTabAt(initialIndexPage)
        tab?.select()
    }

    private fun changeTabPager(position: Int) {
        viewPager.setCurrentItem(position, true)
        cacheManager.entry(KEY_TAB_POSITION, position)
        analytics.trackNotificationCenterTab(position)
    }

    private fun clearNotifCounter(position: Int) {
        if (position == INDEX_NOTIFICATION_UPDATE) {
            presenter.clearNotifCounter()
            resetCounterNotificationUpdate()
        }
    }


    override fun resetCounterNotificationUpdate() {
        updateCounter = 0
        setCounterNotificationUpdate()
    }

    private fun resetCircle(customView: View?) {
        var notif = customView?.findViewById<View>(R.id.circle)
        notif?.visibility = View.GONE
    }

    private fun setTabSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, Green_G500))
    }

    private fun setTabUnSelectedView(customView: View?) {
        val titleView = customView?.findViewById<TextView>(R.id.title)
        titleView?.setTextColor(MethodChecker.getColor(this, Neutral_N200))
    }

    private fun createCustomView(title: String): View? {
        val customView = LayoutInflater.from(this).inflate(R.layout.item_notification_tab_title, null)
        val titleView = customView.findViewById<TextView>(R.id.title)
        titleView.text = title
        return customView
    }

    override fun getViewPagerAdapter(): PagerAdapter? {
        fragmentAdapter = NotificationFragmentAdapter(supportFragmentManager)
        fragmentAdapter?.setItemList(tabList)

        return fragmentAdapter
    }

    override fun getLayoutRes(): Int = R.layout.activity_notification_page

    override fun getTabLayoutResourceId(): Int = R.id.indicator

    override fun getViewPagerResourceId(): Int = R.id.pager

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notifcenter_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.notif_settting -> openNotificationSettingPage()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openNotificationSettingPage(): Boolean {
        RouteManager.route(this, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING)
        return true
    }

    companion object {
        const val RED_DOT_GIMMICK_REMOTE_CONFIG_KEY = "android_red_dot_gimmick_view"
        private const val KEY_TAB_POSITION = "tab_position"

        const val INDEX_NOTIFICATION_ACTIVITY = 0
        const val INDEX_NOTIFICATION_UPDATE = 1

        fun start(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }

        fun createIntentUpdate(context: Context): Intent {
            val intent = Intent(context, NotificationActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(Intent.EXTRA_TITLE, INDEX_NOTIFICATION_UPDATE)
            intent.putExtras(bundle)
            return intent
        }
    }

}
