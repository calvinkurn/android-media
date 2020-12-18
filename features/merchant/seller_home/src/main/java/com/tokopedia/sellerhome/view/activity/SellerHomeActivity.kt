package com.tokopedia.sellerhome.view.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.kotlin.extensions.view.getResColor
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
import com.tokopedia.sellerhome.common.StatusbarHelper
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.navigator.SellerHomeNavigator
import com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav.BottomMenu
import com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav.IBottomClickListener
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_sah_seller_home.*
import javax.inject.Inject

class SellerHomeActivity : BaseActivity(), SellerHomeFragment.Listener, IBottomClickListener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)

        private const val DOUBLE_TAB_EXIT_DELAY = 2000L

        private const val LAST_FRAGMENT_TYPE_KEY = "last_fragment"
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

    private val menu = mutableListOf<BottomMenu>()

    private var canExitApp = false
    private var lastProductManagePage = PageFragment(FragmentType.PRODUCT)
    private var lastSomTab = PageFragment(FragmentType.ORDER) //by default show tab "Semua Pesanan"
    private var navigator: SellerHomeNavigator? = null
    private var isOrderShopAdmin = true

    private var statusBarCallback: StatusBarCallback? = null

    var performanceMonitoringSellerHomeLayoutPlt: HomeLayoutLoadTimeMonitoring? = null
    var sellerHomeLoadTimeMonitoringListener: SellerHomeLoadTimeMonitoringListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        initSellerHomePlt()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_seller_home)

        setupBackground()
        setupToolbar()
        setupStatusBar()
        setupBottomNav()
        setupNavigator()

        val initialPage = savedInstanceState?.getInt(LAST_FRAGMENT_TYPE_KEY) ?: FragmentType.HOME
        setupDefaultPage(initialPage)

        // if redirected from any seller migration entry point, no need to show the update dialog
        val isRedirectedFromSellerMigrationEntryPoint = !intent.data?.getQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).isNullOrBlank()

        UpdateCheckerHelper.checkAppUpdate(this, isRedirectedFromSellerMigrationEntryPoint)
        observeNotificationsLiveData()
        observeShopInfoLiveData()
        observeIsRoleEligible()
        observeIsOrderShopAdmin()
    }

    override fun onResume() {
        super.onResume()
        clearNotification()
        homeViewModel.getNotifications()
        homeViewModel.getAdminInfo()

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

    override fun onSaveInstanceState(outState: Bundle) {
        navigator?.getCurrentSelectedPage()?.let { page ->
            outState.putInt(LAST_FRAGMENT_TYPE_KEY, page)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator?.cleanupNavigator()
    }

    override fun menuClicked(position: Int, id: Int): Boolean {
        when (position) {
            FragmentType.HOME -> {
                UpdateShopActiveService.startService(this)
                onBottomNavSelected(PageFragment(FragmentType.HOME), TrackingConstant.CLICK_HOME)
                showToolbarNotificationBadge()
            }
            FragmentType.PRODUCT -> {
                UpdateShopActiveService.startService(this)
                onBottomNavSelected(lastProductManagePage, TrackingConstant.CLICK_PRODUCT)
            }
            FragmentType.CHAT -> {
                UpdateShopActiveService.startService(this)
                onBottomNavSelected(PageFragment(FragmentType.CHAT), TrackingConstant.CLICK_CHAT)
            }
            FragmentType.ORDER -> {
                UpdateShopActiveService.startService(this)
                onBottomNavSelected(lastSomTab, TrackingConstant.CLICK_ORDER)
            }
            FragmentType.OTHER -> {
                UpdateShopActiveService.startService(this)
                showOtherSettingsFragment()
            }
        }
        return true
    }

    override fun menuReselected(position: Int, id: Int) {

    }

    fun attachCallback(callback: StatusBarCallback) {
        statusBarCallback = callback
    }

    private fun setupBackground() {
        window.decorView.setBackgroundColor(Color.WHITE)
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

    private fun setupDefaultPage(@FragmentType initialPageType: Int) {
        if (intent?.data == null) {
            showToolbar(initialPageType)
            showInitialPage(initialPageType)
        } else {
            handleAppLink(intent)
        }
    }

    private fun showInitialPage(pageType: Int) {
        setCurrentFragmentType(pageType)
        sahBottomNav.setSelected(pageType)

        if (pageType == FragmentType.OTHER) {
            hideToolbarAndStatusBar()
        }
        navigator?.start(pageType)
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
            sahBottomNav.setSelected(pageType)
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
        navigator = SellerHomeNavigator(this, supportFragmentManager, sellerHomeRouter, userSession)
    }

    private fun showToolbarNotificationBadge() {
        val homeFragment = navigator?.getHomeFragment()
        homeFragment?.showNotificationBadge()
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
        if (pageType != FragmentType.OTHER) {
            sahToolbar?.show()
        } else {
            sahToolbar?.hide()
        }
    }

    private fun trackClickBottomNavigation(trackingAction: String) {
        NavigationTracking.sendClickBottomNavigationMenuEvent(trackingAction)
    }

    private fun resetPages(page: PageFragment) {
        when (page.type) {
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
        hideToolbarAndStatusBar()

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
                if (isOrderShopAdmin) {
                    showOrderNotificationCounter(it.data.sellerOrderStatus)
                }
            }
        })
    }

    private fun observeShopInfoLiveData() {
        homeViewModel.shopInfo.observe(this, Observer {
            if (it is Success) {
                navigator?.run {
                    val shopName = MethodChecker.fromHtml(it.data.shopName).toString()
                    val shopAvatar = it.data.shopAvatar

                    // update userSession
                    userSession.shopName = shopName
                    userSession.shopAvatar = shopAvatar

                    if (isHomePageSelected()) {
                        supportActionBar?.title = shopName
                    }

                    setHomeTitle(shopName)
                }
            }
        })
        homeViewModel.getShopInfo()
    }

    private fun observeIsRoleEligible() {
        homeViewModel.isRoleEligible.observe(this) { result ->
            if (result is Success) {
                result.data.let { isRoleEligible ->
                    if (!isRoleEligible) {
                        RouteManager.route(this, ApplinkConstInternalGlobal.LOGOUT)
                        finish()
                    }
                }
            }
            // TODO: Add logic when admin info request fails. Still asking PM. For now we will only preserve current user session value
        }
    }

    private fun observeIsOrderShopAdmin() {
        homeViewModel.isOrderShopAdmin.observe(this) { isOrderShopAdmin ->
            this.isOrderShopAdmin = isOrderShopAdmin
            if (!isOrderShopAdmin) {
                sahBottomNav.run {
                    setBadge(0, FragmentType.ORDER, View.INVISIBLE)
                }
            }
        }
    }

    private fun showNotificationBadge(notifUnreadInt: Int) {
        val homeFragment = navigator?.getHomeFragment()
        homeFragment?.setNotifCenterCounter(notifUnreadInt)
    }

    private fun showChatNotificationCounter(unreadsSeller: Int) {
        val badgeVisibility = if (unreadsSeller <= 0) View.INVISIBLE else View.VISIBLE
        sahBottomNav.setBadge(unreadsSeller, FragmentType.CHAT, badgeVisibility)
    }

    private fun showOrderNotificationCounter(orderStatus: NotificationSellerOrderStatusUiModel) {
        val notificationCount = orderStatus.newOrder.plus(orderStatus.readyToShip)
        val badgeVisibility = if (notificationCount <= 0) View.INVISIBLE else View.VISIBLE
        sahBottomNav.setBadge(notificationCount, FragmentType.ORDER, badgeVisibility)
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestStatusBarDark()
            statusBarBackground?.show()
        }
    }

    private fun hideToolbarAndStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarBackground?.hide()
            statusBarCallback?.setStatusBar()
        }
        sahToolbar?.hide()
    }

    private fun setupBottomNav() {
        sahBottomNav.setBackgroundColor(this.getResColor(android.R.color.transparent))

        val animEnterDuration = 4f
        val animExitDuration = 1f
        menu.add(BottomMenu(R.id.menu_home, resources.getString(R.string.sah_home), R.raw.anim_bottom_nav_home, R.raw.anim_bottom_nav_home_to_enabled, R.drawable.ic_sah_bottom_nav_home_active, R.drawable.ic_sah_bottom_nav_home_inactive, R.color.color_active_bottom_nav, false, animExitDuration, animEnterDuration))
        menu.add(BottomMenu(R.id.menu_product, resources.getString(R.string.sah_product), R.raw.anim_bottom_nav_product, R.raw.anim_bottom_nav_product_to_enabled, R.drawable.ic_sah_bottom_nav_product_active, R.drawable.ic_sah_bottom_nav_product_inactive, R.color.color_active_bottom_nav, false, animExitDuration, animEnterDuration))
        menu.add(BottomMenu(R.id.menu_chat, resources.getString(R.string.sah_chat), R.raw.anim_bottom_nav_chat, R.raw.anim_bottom_nav_chat_to_enabled, R.drawable.ic_sah_bottom_nav_chat_active, R.drawable.ic_sah_bottom_nav_chat_inactive, R.color.color_active_bottom_nav, true, animExitDuration, animEnterDuration))
        menu.add(BottomMenu(R.id.menu_order, resources.getString(R.string.sah_sale), R.raw.anim_bottom_nav_order, R.raw.anim_bottom_nav_order_to_enabled, R.drawable.ic_sah_bottom_nav_order_active, R.drawable.ic_sah_bottom_nav_order_inactive, R.color.color_active_bottom_nav, true, animExitDuration, animEnterDuration))
        menu.add(BottomMenu(R.id.menu_other, resources.getString(R.string.sah_others), R.raw.anim_bottom_nav_other, R.raw.anim_bottom_nav_other_to_enabled, R.drawable.ic_sah_bottom_nav_other_active, R.drawable.ic_sah_bottom_nav_other_inactive, R.color.color_active_bottom_nav, false, animExitDuration, animEnterDuration))
        sahBottomNav.setMenu(menu)

        sahBottomNav.setMenuClickListener(this)
    }

    private fun initSellerHomePlt() {
        if (intent.data == null) {
            initPerformanceMonitoringSellerHome()
        } else {
            DeepLinkHandler.handleAppLink(intent) {
                if (it.type == FragmentType.HOME) {
                    initPerformanceMonitoringSellerHome()
                }
            }
        }
    }

    private fun initPerformanceMonitoringSellerHome() {
        performanceMonitoringSellerHomeLayoutPlt = HomeLayoutLoadTimeMonitoring()
        performanceMonitoringSellerHomeLayoutPlt?.initPerformanceMonitoring()
    }
}