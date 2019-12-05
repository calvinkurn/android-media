package com.tokopedia.navigation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.kotlin.util.getParamInt
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.domain.pojo.NotifCenterSendNotifData
import com.tokopedia.navigation.domain.pojo.NotificationUpdateUnread
import com.tokopedia.navigation.listener.NotificationActivityListener
import com.tokopedia.navigation.presentation.adapter.NotificationFragmentAdapter
import com.tokopedia.navigation.presentation.di.notification.DaggerNotificationUpdateComponent
import com.tokopedia.navigation.presentation.fragment.NotificationFragment
import com.tokopedia.navigation.presentation.fragment.NotificationTransactionFragment
import com.tokopedia.navigation.presentation.fragment.NotificationUpdateFragment
import com.tokopedia.navigation.presentation.presenter.NotificationActivityPresenter
import com.tokopedia.navigation.presentation.view.listener.NotificationActivityContract
import com.tokopedia.navigation.util.CacheManager
import com.tokopedia.navigation.util.NotifPreference
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import javax.inject.Inject

/**
 * Created by meta on 20/06/18.
 */

class NotificationActivity : BaseTabActivity(), HasComponent<BaseAppComponent>, NotificationActivityContract.View,
        NotificationUpdateFragment.NotificationUpdateListener {

    @Inject lateinit var presenter: NotificationActivityPresenter
    @Inject lateinit var analytics: NotificationUpdateAnalytics
    @Inject lateinit var notifPreference: NotifPreference
    @Inject lateinit var cacheManager: CacheManager

    private val handler = Handler()

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
        var initialIndexPage = getParamInt(Intent.EXTRA_TITLE, intent.extras, null, INDEX_NOTIFICATION_ACTIVITY)
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
                showOnBoarding(tab.position)
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

    private fun showOnBoarding(position: Int) {
        val tag = javaClass.name + ".OnBoarding"
        if (position != INDEX_NOTIFICATION_UPDATE || hasBeenShown(tag)) return
        handler.post {
            val coachMarkItems = getCoachMarkItems()
            getNotificationUpdate()?.showOnBoarding(coachMarkItems, tag)
        }
    }

    private fun hasBeenShown(tag: String): Boolean {
        return CoachMarkPreference.hasShown(this, tag)
    }

    private fun getNotificationUpdate(): NotificationActivityListener? {
        val notificationUpdateFragment = tabList[INDEX_NOTIFICATION_UPDATE].fragment
        if (notificationUpdateFragment is NotificationActivityListener) {
            return notificationUpdateFragment
        }
        return null
    }

    private fun getCoachMarkItems(): ArrayList<CoachMarkItem> {
        val notificationSettingIcon = getNotificationSettingIconView()
        return arrayListOf(
                CoachMarkItem(
                        tabLayout,
                        getString(R.string.coachicon_title_tabs),
                        getString(R.string.coachicon_description_tabs)
                ),
                CoachMarkItem(
                        notificationSettingIcon,
                        getString(R.string.coachicon_title_notification_setting),
                        getString(R.string.coachicon_description_notification_setting)
                )
        )
    }

    private fun getNotificationSettingIconView(): View? {
        return findViewById(R.id.notif_settting)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notification_activity, menu)
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
        private const val KEY_TAB_POSITION = "tab_position"

        var INDEX_NOTIFICATION_ACTIVITY = 0
        var INDEX_NOTIFICATION_UPDATE = 1
        const val RED_DOT_GIMMICK_REMOTE_CONFIG_KEY = "android_red_dot_gimmick_view"

        fun start(context: Context): Intent {
            return Intent(context, NotificationActivity::class.java)
        }

        fun createIntentUpdate(context: Context): Intent {
            var intent = Intent(context, NotificationActivity::class.java)
            var bundle = Bundle()
            bundle.putInt(Intent.EXTRA_TITLE, INDEX_NOTIFICATION_UPDATE)
            intent.putExtras(bundle)
            return intent
        }
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.NOTIFICATION)
        @JvmStatic
        fun createIntent(context: Context, extras: Bundle) = Companion.start(context)

        @DeepLink(ApplinkConst.BUYER_INFO)
        @JvmStatic
        fun createIntentUpdate(context: Context, extras: Bundle) = Companion.createIntentUpdate(context)
    }
}
