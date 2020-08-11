package com.tokopedia.sellerhome.view.oldactivity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhome.common.DeepLinkHandler
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.SellerHomePerformanceMonitoringConstant.SELLER_HOME_LAYOUT_TRACE
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.di.component.DaggerSellerHomeComponent
import com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.fragment.ContainerFragment
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhome.view.model.NotificationCenterUnreadUiModel
import com.tokopedia.sellerhome.view.model.NotificationChatUiModel
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.sellerhome.view.viewmodel.SharedViewModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.activity_sah_seller_home_old.*
import javax.inject.Inject

class SellerHomeActivity : BaseActivity(), SellerHomeFragment.Listener {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, SellerHomeActivity::class.java)

        private const val DOUBLE_TAB_EXIT_DELAY = 2000L
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val homeViewModel by lazy { viewModelProvider.get(SellerHomeActivityViewModel::class.java) }
    private val sharedViewModel by lazy { viewModelProvider.get(SharedViewModel::class.java) }

    private val handler = Handler() //create handler to make sure when showing fragment is on UI thread
    private val containerFragment by lazy {
        ContainerFragment.newInstance().apply {
            setSellerHomeListener(this@SellerHomeActivity)
        }
    }

    private val otherSettingsFragment by lazy {
        OtherMenuFragment.createInstance()
    }

    @FragmentType
    private var currentSelectedMenu = FragmentType.NONE
    private var canExitApp = false
    private var lastProductMangePage = PageFragment(FragmentType.PRODUCT)
    private var lastSomTab = PageFragment(FragmentType.ORDER) //by default show tab "Semua Pesanan"

    private var statusBarCallback: StatusBarCallback? = null
    private var performanceMonitoringSellerHomelayout: PerformanceMonitoring? = null

    var performanceMonitoringSellerHomeLayoutPlt: HomeLayoutLoadTimeMonitoring? = null

    private var shouldMoveToReview: Boolean = false
    private var shouldMoveToCentralizedPromo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_seller_home_old)

        with (intent?.getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)?.firstOrNull().orEmpty()) {
            shouldMoveToReview = this == ApplinkConst.REPUTATION
            shouldMoveToCentralizedPromo = this == ApplinkConst.REPUTATION
        }
        val isRedirectedFromSellerMigration = intent?.hasExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA) ?: false ||
                intent?.hasExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME) ?: false

        initInjector()
        setupBottomNav()
        setupDefaultPage()
        UpdateCheckerHelper.checkAppUpdate(this, isRedirectedFromSellerMigration)
        observeNotificationsLiveData()
        observeShopInfoLiveData()
        observeCurrentSelectedPageLiveData()
        setupStatusBar()
    }

    override fun onStart() {
        super.onStart()
        val appLinks = ArrayList(intent?.getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA).orEmpty())
        if (appLinks.isNotEmpty()) {
            val appLinkToOpen = appLinks.firstOrNull().orEmpty()
            if (shouldMoveToReview || shouldMoveToCentralizedPromo) {
                shouldMoveToReview = false
                shouldMoveToCentralizedPromo = false
                RouteManager.getIntent(this, appLinkToOpen).apply {
                    replaceExtras(this@SellerHomeActivity.intent.extras)
                    startActivity(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

    private fun setupDefaultPage() {
        if (intent?.data == null) {
            val homePage = PageFragment(FragmentType.HOME)
            sharedViewModel.setCurrentSelectedPage(homePage)
            showFragment(containerFragment)
        } else {
            handleAppLink(intent)
        }
    }

    private fun handleAppLink(intent: Intent?) {
        DeepLinkHandler.handleAppLink(intent) { page ->
            when(page.type) {
                FragmentType.ORDER -> lastSomTab = page
                FragmentType.PRODUCT -> lastProductMangePage = page
            }
            sharedViewModel.setCurrentSelectedPage(page)
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

    private fun setupBottomNav() {
        sahBottomNav.itemIconTintList = null
        sahBottomNav.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        sahBottomNav.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_sah_home -> {
                    UpdateShopActiveService.startService(this)
                    showContainerFragment(PageFragment(FragmentType.HOME), TrackingConstant.CLICK_HOME)
                }
                R.id.menu_sah_product -> {
                    UpdateShopActiveService.startService(this)
                    showContainerFragment(lastProductMangePage, TrackingConstant.CLICK_PRODUCT)
                }
                R.id.menu_sah_chat -> {
                    UpdateShopActiveService.startService(this)
                    showContainerFragment(PageFragment(FragmentType.CHAT), TrackingConstant.CLICK_CHAT)
                }
                R.id.menu_sah_order -> {
                    UpdateShopActiveService.startService(this)
                    showContainerFragment(lastSomTab, TrackingConstant.CLICK_ORDER)
                }
                R.id.menu_sah_other -> {
                    UpdateShopActiveService.startService(this)
                    showOtherSettingsFragment()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun showContainerFragment(page: PageFragment, trackingAction: String) {
        if (currentSelectedMenu == page.type) return
        currentSelectedMenu = page.type

        setupStatusBar()
        sharedViewModel.setCurrentSelectedPage(page)
        showFragment(containerFragment)
        resetPages(page)

        NavigationTracking.sendClickBottomNavigationMenuEvent(trackingAction)
    }

    private fun resetPages(page: PageFragment) {
        when(page.type) {
            FragmentType.PRODUCT -> lastProductMangePage = PageFragment(FragmentType.PRODUCT)
            FragmentType.ORDER -> lastSomTab = PageFragment(FragmentType.ORDER)
        }
    }

    private fun showOtherSettingsFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarCallback?.setStatusBar()
        }
        val type = FragmentType.OTHER
        if (currentSelectedMenu == type) return
        currentSelectedMenu = type

        showFragment(otherSettingsFragment)
        sharedViewModel.setCurrentSelectedPage(PageFragment(type))

        NavigationTracking.sendClickBottomNavigationMenuEvent(TrackingConstant.CLICK_OTHERS)
    }

    private fun showFragment(fragment: Fragment) {
        handler.post {
            val fragmentName = fragment.javaClass.name
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val isFragmentHasAttached = null != manager.findFragmentByTag(fragmentName)

            if (isFragmentHasAttached && manager.fragments.isNotEmpty()) {
                manager.fragments.forEach { fmt ->
                    if (fmt.javaClass.name == fragmentName) {
                        transaction.show(fmt)
                    } else {
                        transaction.hide(fmt)
                    }
                }
            } else {
                transaction.add(R.id.sahContainer, fragment, fragmentName)
            }
            transaction.commitNowAllowingStateLoss()
        }
    }

    private fun observeCurrentSelectedPageLiveData() {
        sharedViewModel.currentSelectedPage.observe(this, Observer {
            sahBottomNav.currentItem = it.type
            statusBarCallback?.setCurrentFragmentType(it.type)
        })
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
                containerFragment.showShopName(it.data.shopName)
            }
        })
        homeViewModel.getShopInfo()
    }

    private fun showNotificationBadge(notifCenter: NotificationCenterUnreadUiModel) {
        containerFragment.showNotifCenterBadge(notifCenter)
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
        }
    }

    private fun initPerformanceMonitoring(){
        performanceMonitoringSellerHomelayout = PerformanceMonitoring.start(SELLER_HOME_LAYOUT_TRACE)
        performanceMonitoringSellerHomeLayoutPlt = HomeLayoutLoadTimeMonitoring()
        performanceMonitoringSellerHomeLayoutPlt?.initPerformanceMonitoring()
    }

    fun stopPerformanceMonitoringSellerHomeLayout() {
        performanceMonitoringSellerHomelayout?.stopTrace()
    }
}