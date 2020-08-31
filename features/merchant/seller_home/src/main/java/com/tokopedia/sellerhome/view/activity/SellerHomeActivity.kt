package com.tokopedia.sellerhome.view.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhome.analytic.performance.SellerHomeLoadTimeMonitoringListener
import com.tokopedia.sellerhome.common.DeepLinkHandler
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE
import com.tokopedia.sellerhome.common.StatusbarHelper
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhome.view.model.NotificationCenterUnreadUiModel
import com.tokopedia.sellerhome.view.model.NotificationChatUiModel
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.navigator.SellerHomeNavigator
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.sellerhome.view.widget.toolbar.NotificationDotBadge
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_sah_seller_home.*
import javax.inject.Inject

class SellerHomeActivity : BaseActivity(), SellerHomeFragment.Listener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)

        private const val DOUBLE_TAB_EXIT_DELAY = 2000L

        private const val SHOP_PAGE_PREFIX = "tokopedia://shop/"
    }

    @Inject lateinit var userSession: UserSessionInterface
    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var remoteConfig: SellerHomeRemoteConfig

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val homeViewModel by lazy { viewModelProvider.get(SellerHomeActivityViewModel::class.java) }

    private val sellerHomeRouter: SellerHomeRouter? by lazy {
        val applicationContext = applicationContext
        return@lazy if (applicationContext is SellerHomeRouter)
            applicationContext
        else
            null
    }

    private var canExitApp = false
    private var lastProductManagePage = PageFragment(FragmentType.PRODUCT)
    private var lastSomTab = PageFragment(FragmentType.ORDER) //by default show tab "Semua Pesanan"
    private var navigator: SellerHomeNavigator? = null

    private var statusBarCallback: StatusBarCallback? = null
    private var performanceMonitoringSellerHomelayout: PerformanceMonitoring? = null

    var performanceMonitoringSellerHomeLayoutPlt: HomeLayoutLoadTimeMonitoring? = null
    var sellerHomeLoadTimeMonitoringListener: SellerHomeLoadTimeMonitoringListener? = null

    private var shouldMoveToReview: Boolean = false
    private var shouldMoveToCentralizedPromo: Boolean = false
    private var shouldMoveToShopPage: Boolean = false
    private var shouldMoveToBalance: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        if(startOldSellerHomeIfEnabled()) {
            super.onCreate(savedInstanceState)
            return
        }
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_seller_home)

        with (intent?.getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)?.firstOrNull().orEmpty()) {
            shouldMoveToReview = this == ApplinkConst.REPUTATION
            shouldMoveToCentralizedPromo = this == ApplinkConstInternalSellerapp.CENTRALIZED_PROMO
            shouldMoveToShopPage = this.startsWith(SHOP_PAGE_PREFIX)
            shouldMoveToBalance = this == ApplinkConstInternalGlobal.SALDO_DEPOSIT
        }
        val isRedirectedFromSellerMigration = intent?.hasExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA) ?: false ||
                intent?.hasExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME) ?: false

        setupToolbar()
        setupStatusBar()
        setupNavigator()
        setupDefaultPage()
        setupBottomNav()
        UpdateCheckerHelper.checkAppUpdate(this, isRedirectedFromSellerMigration)
        observeNotificationsLiveData()
        observeShopInfoLiveData()
    }

    override fun onStart() {
        super.onStart()
        val appLinks = ArrayList(intent?.getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA).orEmpty())
        if (appLinks.isNotEmpty()) {
            val appLinkToOpen = appLinks.firstOrNull().orEmpty()
            if (shouldMoveToReview || shouldMoveToCentralizedPromo || shouldMoveToShopPage || shouldMoveToBalance) {
                shouldMoveToReview = false
                shouldMoveToCentralizedPromo = false
                shouldMoveToShopPage = false
                shouldMoveToBalance = false
                RouteManager.getIntent(this, appLinkToOpen).apply {
                    replaceExtras(this@SellerHomeActivity.intent.extras)
                    appLinks.find { it != ApplinkConst.REPUTATION &&
                                    it != ApplinkConstInternalSellerapp.CENTRALIZED_PROMO &&
                                    !it.startsWith(SHOP_PAGE_PREFIX) &&
                                    it != ApplinkConstInternalGlobal.SALDO_DEPOSIT }?.let { nextDestinationApplink ->
                        putExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, nextDestinationApplink)
                    }
                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        clearNotification()
        homeViewModel.getNotifications()

        if (!userSession.isLoggedIn) {
            RouteManager.route(this, ApplinkConstInternalSellerapp.WELCOME)
            finish()
        } else if (!userSession.hasShop()) {
            RouteManager.route(this, ApplinkConst.CREATE_SHOP)
            finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleAppLink(intent)
    }

    override fun onBackPressed() {
        doubleTapToExit()
    }

    override fun getShopInfo() {
        homeViewModel.getShopInfo()
    }

    fun attachCallback(callback: StatusBarCallback) {
        statusBarCallback = callback
    }

    fun stopPerformanceMonitoringSellerHomeLayout() {
        performanceMonitoringSellerHomelayout?.stopTrace()
    }

    private fun startOldSellerHomeIfEnabled(): Boolean {
        if(remoteConfig.isNewSellerHomeDisabled()) {
            val oldSellerHome = com.tokopedia.sellerhome.view.oldactivity.SellerHomeActivity.createIntent(this)
            oldSellerHome.data = intent.data
            startActivity(oldSellerHome)
            finish()

            return true
        }
        return false
    }

    private fun setupToolbar() {
        setSupportActionBar(sahToolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val statusBarHeight = StatusbarHelper.getStatusBarHeight(this)
            val layoutParams = statusBarBackground?.layoutParams
            layoutParams?.let {
                if (it is LinearLayout.LayoutParams) {
                    it.height = statusBarHeight
                    statusBarBackground?.layoutParams = it
                    statusBarBackground?.requestLayout()
                }
            }
        }
    }

    private fun setupDefaultPage() {
        if(intent?.data == null) {
            showToolbar()
            showSellerHome()
        } else {
            handleAppLink(intent)
        }
    }

    private fun showSellerHome() {
        val home = FragmentType.HOME
        setCurrentFragmentType(home)
        sahBottomNav.currentItem = home
        navigator?.start(home)
    }

    private fun handleAppLink(intent: Intent?) {
        DeepLinkHandler.handleAppLink(intent) { page ->
            val pageType = page.type

            when (pageType) {
                FragmentType.ORDER -> lastSomTab = page
                FragmentType.PRODUCT -> lastProductManagePage = page
            }

            showToolbar(pageType)
            setCurrentFragmentType(pageType)
            sahBottomNav.currentItem = pageType
            navigator?.navigateFromAppLink(page)
        }
    }

    private fun doubleTapToExit() {
        if (canExitApp) {
            finish()
        } else {
            canExitApp = true
            Toast.makeText(this, R.string.sah_exit_message, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                canExitApp = false
            }, DOUBLE_TAB_EXIT_DELAY)
        }
    }

    private fun initInjector() {
        DaggerSellerHomeComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun setupNavigator() {
        navigator = SellerHomeNavigator(this, supportFragmentManager, sellerHomeRouter)
    }

    private fun setupBottomNav() {
        sahBottomNav.itemIconTintList = null
        sahBottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        sahBottomNav.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_sah_home -> {
                    UpdateShopActiveService.startService(this)
                    onBottomNavSelected(PageFragment(FragmentType.HOME), TrackingConstant.CLICK_HOME)
                    showToolbarNotificationBadge()
                }
                R.id.menu_sah_product -> {
                    UpdateShopActiveService.startService(this)
                    onBottomNavSelected(lastProductManagePage, TrackingConstant.CLICK_PRODUCT)
                }
                R.id.menu_sah_chat -> {
                    UpdateShopActiveService.startService(this)
                    onBottomNavSelected(PageFragment(FragmentType.CHAT), TrackingConstant.CLICK_CHAT)
                }
                R.id.menu_sah_order -> {
                    UpdateShopActiveService.startService(this)
                    onBottomNavSelected(lastSomTab, TrackingConstant.CLICK_ORDER)
                }
                R.id.menu_sah_other -> {
                    UpdateShopActiveService.startService(this)
                    showOtherSettingsFragment()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun showToolbarNotificationBadge() {
        sahToolbar?.menu?.findItem(SellerHomeFragment.NOTIFICATION_MENU_ID)?.let {
            NotificationDotBadge(this).showBadge(it)
        }
    }

    private fun onBottomNavSelected(page: PageFragment, trackingAction: String) {
        val pageType = page.type

        setupStatusBar()
        showToolbar(pageType)
        setCurrentFragmentType(pageType)
        resetPages(page)

        navigator?.showPage(pageType)
        trackClickBottomNavigation(trackingAction)
    }

    private fun showToolbar(@FragmentType pageType: Int = FragmentType.HOME) {
        val pageTitle = navigator?.getPageTitle(pageType)
        supportActionBar?.title = pageTitle
        sahToolbar?.show()
    }

    private fun trackClickBottomNavigation(trackingAction: String) {
        NavigationTracking.sendClickBottomNavigationMenuEvent(trackingAction)
    }

    private fun resetPages(page: PageFragment) {
        when(page.type) {
            FragmentType.PRODUCT -> lastProductManagePage = PageFragment(FragmentType.PRODUCT)
            FragmentType.ORDER -> lastSomTab = PageFragment(FragmentType.ORDER)
        }
    }

    private fun clearNotification() {
        if (remoteConfig.isNotificationTrayClear()) {
            (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)?.cancelAll()
            NotificationManagerCompat.from(this).cancelAll()
        }
    }

    private fun showOtherSettingsFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarBackground?.hide()
            statusBarCallback?.setStatusBar()
        }
        sahToolbar?.hide()

        val type = FragmentType.OTHER
        setCurrentFragmentType(type)
        navigator?.showPage(type)

        trackClickBottomNavigation(TrackingConstant.CLICK_OTHERS)
    }

    private fun setCurrentFragmentType(@FragmentType pageType: Int) {
        statusBarCallback?.setCurrentFragmentType(pageType)
    }

    private fun observeNotificationsLiveData() {
        homeViewModel.notifications.observe(this, Observer {
            if (it is Success) {
                showNotificationBadge(it.data.notifCenterUnread)
                showChatNotificationCounter(it.data.chat)
                showOrderNotificationCounter(it.data.sellerOrderStatus)
            }
        })
    }

    private fun observeShopInfoLiveData() {
        homeViewModel.shopInfo.observe(this, Observer {
            if (it is Success) {
                navigator?.run {
                    val shopName = it.data.shopName
                    val shopAvatar = it.data.shopAvatar

                    // update userSession
                    userSession.shopName = shopName
                    userSession.shopAvatar = shopAvatar

                    if(isHomePageSelected()) {
                        supportActionBar?.title = shopName
                    }

                    setHomeTitle(shopName)
                }
            }
        })
        homeViewModel.getShopInfo()
    }

    private fun showNotificationBadge(notifCenter: NotificationCenterUnreadUiModel) {
        val homeFragment = navigator?.getHomeFragment()
        homeFragment?.setNotifCenterCounter(notifCenter.notifUnreadInt)
    }

    private fun showChatNotificationCounter(chat: NotificationChatUiModel) {
        sahBottomNav.setNotification(chat.unreadsSeller, FragmentType.CHAT)
    }

    private fun showOrderNotificationCounter(orderStatus: NotificationSellerOrderStatusUiModel) {
        val notificationCount = orderStatus.newOrder.plus(orderStatus.readyToShip)
        sahBottomNav.setNotification(notificationCount, FragmentType.ORDER)
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestStatusBarDark()
            statusBarBackground?.show()
        }
    }

    private fun initPerformanceMonitoring(){
        performanceMonitoringSellerHomelayout = PerformanceMonitoring.start(SELLER_HOME_LAYOUT_TRACE)
        performanceMonitoringSellerHomeLayoutPlt = HomeLayoutLoadTimeMonitoring()
        performanceMonitoringSellerHomeLayoutPlt?.initPerformanceMonitoring()
    }
}