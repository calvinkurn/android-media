package com.tokopedia.notifcenter.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.getParamInt
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.data.consts.Resources.Unify_N0
import com.tokopedia.notifcenter.data.entity.NotificationTabItem
import com.tokopedia.notifcenter.data.entity.NotificationUpdateUnread
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.listener.NotificationUpdateListener
import com.tokopedia.notifcenter.presentation.adapter.NotificationFragmentAdapter
import com.tokopedia.notifcenter.presentation.contract.NotificationActivityContract
import com.tokopedia.notifcenter.presentation.fragment.NotificationTransactionFragment
import com.tokopedia.notifcenter.presentation.fragment.NotificationUpdateFragment
import com.tokopedia.notifcenter.presentation.presenter.NotificationActivityPresenter
import com.tokopedia.notifcenter.util.CacheManager
import com.tokopedia.notifcenter.widget.NotificationTabLayout
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by meta on 20/06/18.
 */

class NotificationActivity : BaseTabActivity(), HasComponent<BaseAppComponent>,
        NotificationActivityContract.View,
        NotificationUpdateListener {

    val notificationComponent by lazy { initInjector() }

    @Inject lateinit var presenter: NotificationActivityPresenter
    @Inject lateinit var analytics: NotificationUpdateAnalytics
    @Inject lateinit var userSession: UserSessionInterface
    @Inject lateinit var cacheManager: CacheManager

    private var fragmentAdapter: NotificationFragmentAdapter? = null
    private val tabList = ArrayList<NotificationTabItem>()

    /*
    * notification id for buyer info consume
    * the id comes from tokopedia://notif-center/{id}
    * notification id will be consuming on updateFragment
    * */
    var notificationId = ""

    /*
     * track mark all as read counter
     * counting notification item to as read
     * */
    private var updateCounter = 0L

    private val notificationLayout by lazy {
        NotificationTabLayout(this)
    }

    private val customTabView by lazy {
        tabLayout.getTabAt(INDEX_NOTIFICATION_UPDATE)?.customView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initTabLayoutItem()
        setWindowBackground()
        super.onCreate(savedInstanceState)

        notificationComponent.inject(this)
        presenter.attachView(this)

        onParameterHandler(savedInstanceState)
        dotGimmickRemoteConfig()
    }

    private fun onParameterHandler(bundle: Bundle?) {
        val intentData = intent?.data
        intentData?.path?.let {
            when {
                it.contains(PATH_BUYER_INFO) -> {
                    initViewTabLayout(INDEX_NOTIFICATION_UPDATE)

                    // set notification id
                    intentData.lastPathSegment?.let { id ->
                        if (id != PATH_BUYER_INFO) notificationId = id
                    }
                }
                else -> initView(bundle)
            }
        }?: initView(bundle)
    }

    private fun dotGimmickRemoteConfig() {
        baseContext?.let {
            val remoteConfig = FirebaseRemoteConfigImpl(it)
            val gimmickLocalStatus = cacheManager.isDisplayedGimmick
            val gimmickRemoteConfigStatus = remoteConfig.getBoolean(
                    RED_DOT_GIMMICK_REMOTE_CONFIG_KEY,
                    false
            )
            if (gimmickRemoteConfigStatus && !gimmickLocalStatus) {
                cacheManager.isDisplayedGimmick = true
                presenter.sendNotif(
                        ::onSuccessSendNotification,
                        ::onErrorSendNotification
                )
            }
        }
    }

    private fun initTabLayoutItem() {
        // transaction
        tabList.add(NotificationTabItem(
                getString(R.string.title_notification_transaction),
                NotificationTransactionFragment()
        ))

        // update
        tabList.add(NotificationTabItem(
                getString(R.string.title_notification_update),
                NotificationUpdateFragment()
        ))
    }

    private fun onSuccessSendNotification() {
        presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
    }

    private fun onErrorSendNotification() {
        presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
    }

    private fun initView(savedInstanceState: Bundle?) {
        val initialIndexPage = if (cacheManager.isExist(KEY_TAB_POSITION)) {
            cacheManager.read(KEY_TAB_POSITION)
        } else {
            getParamInt(Intent.EXTRA_TITLE,
                    intent.extras,
                    savedInstanceState,
                    INDEX_NOTIFICATION_ACTIVITY)
        }
        initViewTabLayout(initialIndexPage)
    }

    private fun initViewTabLayout(page: Int) {
        initTabLayout(page)
        presenter.getUpdateUnreadCounter(onSuccessGetUpdateUnreadCounter())
        presenter.getIsTabUpdate(this)
    }

    override fun goToUpdateTab() {
        if (cacheManager.isExist(KEY_TAB_POSITION)) {
            val position = cacheManager.read(KEY_TAB_POSITION)
            changeTabPager(position)
        } else {
            viewPager.currentItem = INDEX_NOTIFICATION_UPDATE
        }
    }

    override fun onSuccessLoadNotificationUpdate() {
        clearTabCounter(INDEX_NOTIFICATION_UPDATE)
    }

    private fun onSuccessGetUpdateUnreadCounter(): (NotificationUpdateUnread) -> Unit {
        return {
            customTabView
                    ?.findViewById<View>(R.id.circle)
                    ?.showWithCondition(
                            it.pojo.notifUnreadInt > 0
                    )
            updateCounter = it.pojo.notifUnreadInt
            setCounterNotificationUpdate()
        }
    }

    private fun initTabLayout(initialIndexPage: Int) {
        tabList.forEachIndexed { index, _ ->
            //add a new tab
            tabLayout.addTab(tabLayout.newTab())

            //define the tab content
            val titleTab = tabList[index].title
            val tabCurrentLayout = tabLayout.getTabAt(index)
            tabCurrentLayout?.customView = notificationLayout.init(titleTab)
            if (index == viewPager.currentItem) {
                notificationLayout.selected(tabCurrentLayout?.customView)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.elevation = 0f
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {}

            override fun onTabUnselected(tab: TabLayout.Tab) {
                notificationLayout.unselected(tab.customView)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                changeTabPager(tab.position)
                clearTabCounter(tab.position)
                notificationLayout.selected(tab.customView)
                notificationLayout.removeDot(tab.customView)
            }
        })

        tabLayout.setBackgroundResource(Unify_N0)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.getTabAt(initialIndexPage)?.select()
    }

    private fun changeTabPager(position: Int) {
        viewPager.setCurrentItem(position, true)
        cacheManager.entry(KEY_TAB_POSITION, position)
        analytics.trackNotificationCenterTab(position)
    }

    private fun clearTabCounter(position: Int) {
        if (position == INDEX_NOTIFICATION_UPDATE) {
            presenter.clearNotifCounter()
            resetCounterNotificationUpdate()
        }
    }

    override fun resetCounterNotificationUpdate() {
        updateCounter = 0
        setCounterNotificationUpdate()
    }

    override fun getViewPagerAdapter(): PagerAdapter? {
        fragmentAdapter = NotificationFragmentAdapter(supportFragmentManager)
        fragmentAdapter?.setItemList(tabList)
        return fragmentAdapter
    }

    override fun getComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    private fun setCounterNotificationUpdate() {
        val defaultTitle = getString(R.string.title_notification_update)
        var counter = ""
        if (updateCounter > 0) {
            counter = setCounterText(updateCounter.toString())
        } else if (updateCounter > 99) {
            counter = setCounterText(getString(R.string.exceed_ninety_nine))
        }

        val title = String.format("%s%s", defaultTitle, counter)
        setTabTitle(INDEX_NOTIFICATION_UPDATE, title)
    }

    private fun setTabTitle(tabIndex: Int, title: String) {
        val tabView = tabLayout.getTabAt(tabIndex)?.customView
        val textTab: TextView? = tabView?.findViewById(R.id.title)
        textTab?.text = title
    }

    private fun setCounterText(text: String): String {
        return getString(R.string.title_counter_update_notification, text)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notifcenter_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.notif_settting -> {
                analytics.trackTroubleshooterGearClicked(userSession.userId, userSession.shopId)
                openNotificationSettingPage()
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openNotificationSettingPage(): Boolean {
        RouteManager.route(this, USER_NOTIFICATION_SETTING)
        return true
    }

    private fun setWindowBackground() {
        try {
            window.decorView.setBackgroundColor(ContextCompat.getColor(this, Unify_N0))
        } catch (e: Exception) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    private fun initInjector(): NotificationComponent {
        return DaggerNotificationComponent.builder()
                .baseAppComponent(component)
                .commonModule(CommonModule(this))
                .build()
    }

    override fun getPageLimit(): Int = tabList.size + 1
    override fun getViewPagerResourceId(): Int = R.id.pager
    override fun getTabLayoutResourceId(): Int = R.id.indicator
    override fun getToolbarResourceID(): Int = R.id.toolbar
    override fun getLayoutRes(): Int = R.layout.activity_notification_page

    companion object {
        private const val KEY_TAB_POSITION = "tab_position"
        private const val PATH_BUYER_INFO = "notif-center"

        var INDEX_NOTIFICATION_ACTIVITY = 0
        var INDEX_NOTIFICATION_UPDATE = 1
        const val RED_DOT_GIMMICK_REMOTE_CONFIG_KEY = "android_red_dot_gimmick_view"

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