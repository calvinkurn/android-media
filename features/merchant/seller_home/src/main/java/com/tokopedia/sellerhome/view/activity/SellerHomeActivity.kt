package com.tokopedia.sellerhome.view.activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.sellerhome.SellerHomeApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.inappupdate.AppUpdateManagerWrapper
import com.tokopedia.internal_review.factory.createReviewHelper
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.requestStatusBarLight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifications.utils.NotificationUserSettingsTracker
import com.tokopedia.seller.active.common.plt.LoadTimeMonitoringListener
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoring
import com.tokopedia.seller.active.common.plt.som.SomListLoadTimeMonitoringActivity
import com.tokopedia.seller.active.common.worker.UpdateShopActiveWorker
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.SellerHomeRouter
import com.tokopedia.sellerhome.analytic.NavigationTracking
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.performance.HomeLayoutLoadTimeMonitoring
import com.tokopedia.sellerhome.common.DeepLinkHandler
import com.tokopedia.sellerhome.common.FragmentType
import com.tokopedia.sellerhome.common.PageFragment
import com.tokopedia.sellerhome.common.appupdate.UpdateCheckerHelper
import com.tokopedia.sellerhome.common.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.common.errorhandler.SellerHomeErrorHandler
import com.tokopedia.sellerhome.databinding.ActivitySahSellerHomeBinding
import com.tokopedia.sellerhome.di.component.DaggerHomeDashboardComponent
import com.tokopedia.sellerhome.di.component.HomeDashboardComponent
import com.tokopedia.sellerhome.di.module.SellerHomeModule
import com.tokopedia.sellerhome.view.FragmentChangeCallback
import com.tokopedia.sellerhome.view.StatusBarCallback
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.navigator.SellerHomeNavigator
import com.tokopedia.sellerhome.view.viewhelper.SellerHomeOnApplyInsetsListener
import com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav.BottomMenu
import com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav.IBottomClickListener
import com.tokopedia.sellerhome.view.viewmodel.SellerHomeActivityViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.accelerometer.orientation.AccelerometerOrientationListener
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

open class SellerHomeActivity :
    BaseActivity(),
    SellerHomeFragment.Listener,
    IBottomClickListener,
    SomListLoadTimeMonitoringActivity,
    HasComponent<HomeDashboardComponent> {

    companion object {

        private const val DOUBLE_TAB_EXIT_DELAY = 2000L
        private const val BOTTOM_NAV_ENTER_ANIM_DURATION = 4f
        private const val BOTTOM_NAV_EXIT_ANIM_DURATION = 1f

        private const val ACTION_GET_ALL_APP_WIDGET_DATA =
            "com.tokopedia.sellerappwidget.GET_ALL_APP_WIDGET_DATA"
        private const val NAVIGATION_OTHER_MENU_POSITION = 4
        private const val NAVIGATION_HOME_MENU_POSITION = 0
        private const val WEAR_PREF_NAME = "WearPopupSharedPref"

        private const val WEAR_POPUP_KEY = "isWearPopupShown"
        private const val TOKOPEDIA_MARKET_WEAR_APP = "market://details?id=com.tokopedia.sellerapp"
        const val LAST_FRAGMENT_TYPE_KEY = "last_fragment"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var remoteConfig: SellerHomeRemoteConfig

    private val sellerReviewHelper by lazy { createReviewHelper(applicationContext) }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val homeViewModel by lazy { viewModelProvider[SellerHomeActivityViewModel::class.java] }
    private val sellerHomeRouter by lazy { applicationContext as? SellerHomeRouter }

    private val menu = mutableListOf<BottomMenu>()

    private var canExitApp = false
    private var lastProductManagePage = PageFragment(FragmentType.PRODUCT)
    private var lastSomTab = PageFragment(FragmentType.ORDER) // by default show tab "Semua Pesanan"
    private var navigator: SellerHomeNavigator? = null
    private val accelerometerOrientationListener: AccelerometerOrientationListener by lazy {
        AccelerometerOrientationListener(contentResolver) {
            onAccelerometerOrientationSettingChange(it)
        }
    }

    private var statusBarCallback: StatusBarCallback? = null
    private var otherMenuFragmentChangeCallback: FragmentChangeCallback? = null
    private var binding: ActivitySahSellerHomeBinding? = null
    private val wearSharedPreference: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(WEAR_PREF_NAME, MODE_PRIVATE)
    }

    override var loadTimeMonitoringListener: LoadTimeMonitoringListener? = null

    override var performanceMonitoringSomListPlt: SomListLoadTimeMonitoring? = null

    var performanceMonitoringSellerHomeLayoutPlt: HomeLayoutLoadTimeMonitoring? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setActivityOrientation()
        initInjector()
        initSellerHomePlt()
        super.onCreate(savedInstanceState)
        setContentView()
        setupNavigator()

        setupBackground()
        setupToolbar()
        setupStatusBar()
        setupBottomNav()

        setupDefaultPage(savedInstanceState)

        checkAppUpdate()
        observeNotificationsLiveData()
        observeShopInfoLiveData()
        observeIsRoleEligible()
        fetchSellerAppWidget()
        setupSellerHomeInsetListener()
        sendNotificationUserSetting()
        observeWearDialog()
        installDFonBackground()
    }

    override fun getComponent(): HomeDashboardComponent {
        return DaggerHomeDashboardComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .sellerHomeModule(SellerHomeModule(this))
            .build()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding?.sahBottomNav?.addOneTimeGlobalLayoutListener {
            navigator?.navigateOnRestored(savedInstanceState, lifecycleScope)
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getNotifications()
        homeViewModel.getAdminInfo()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && remoteConfig.isWatchAppCheckingEnabled()) {
            homeViewModel.checkIfWearHasCompanionApp()
        }

        if (!userSession.isLoggedIn) {
            RouteManager.route(this, ApplinkConstInternalSellerapp.WELCOME)
            finish()
        } else if (!userSession.hasShop()) {
            RouteManager.route(this, ApplinkConst.CREATE_SHOP)
            finish()
        }
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.register()
        }
        navigator?.setSelectedPageSellerFeedback()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleAppLink(intent)

        if (navigator?.isHomePageSelected().orFalse()) {
            handleSellerPersona(intent)
            handleToaster(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AppUpdateManagerWrapper.onActivityResult(this, requestCode, resultCode)
        val sellerHomeLifecycleState = navigator?.getHomeFragment()?.lifecycle?.currentState
        if (sellerHomeLifecycleState?.isAtLeast(Lifecycle.State.CREATED) == true) {
            navigator?.getHomeFragment()?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is TkpdBaseV4Fragment) {
                if (it.onFragmentBackPressed()) return
            }
        }
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

    override fun onPause() {
        super.onPause()
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.unregister()
        }
    }

    override fun onDestroy() {
        navigator?.cleanupNavigator()
        navigator = null
        binding = null
        otherMenuFragmentChangeCallback = null
        super.onDestroy()
    }

    override fun menuClicked(position: Int, id: Int): Boolean {
        when (position) {
            FragmentType.HOME -> {
                UpdateShopActiveWorker.execute(this)
                onBottomNavSelected(PageFragment(FragmentType.HOME), TrackingConstant.CLICK_HOME)
                showToolbarNotificationBadge()
                checkForSellerAppReview(FragmentType.HOME)
            }
            FragmentType.PRODUCT -> {
                UpdateShopActiveWorker.execute(this)
                onBottomNavSelected(lastProductManagePage, TrackingConstant.CLICK_PRODUCT)
            }
            FragmentType.CHAT -> {
                UpdateShopActiveWorker.execute(this)
                onBottomNavSelected(PageFragment(FragmentType.CHAT), TrackingConstant.CLICK_CHAT)
            }
            FragmentType.ORDER -> {
                if (navigator?.getCurrentSelectedPage() != FragmentType.ORDER) {
                    initSomListLoadTimeMonitoring()
                }
                UpdateShopActiveWorker.execute(this)
                onBottomNavSelected(lastSomTab, TrackingConstant.CLICK_ORDER)
            }
            FragmentType.OTHER -> {
                UpdateShopActiveWorker.execute(this)
                showOtherSettingsFragment()
                showToolbar(FragmentType.OTHER)
            }
        }
        return true
    }

    override fun menuReselected(position: Int, id: Int) {
    }

    override fun initSomListLoadTimeMonitoring() {
        performanceMonitoringSomListPlt = SomListLoadTimeMonitoring()
        performanceMonitoringSomListPlt?.initPerformanceMonitoring()
    }

    override fun getSomListLoadTimeMonitoring() = performanceMonitoringSomListPlt

    fun attachCallback(callback: StatusBarCallback) {
        statusBarCallback = callback
    }

    fun attachOtherMenuFragmentChangeCallback(callback: FragmentChangeCallback) {
        otherMenuFragmentChangeCallback = callback
    }

    private fun sendNotificationUserSetting() {
        if (userSession.isLoggedIn) {
            NotificationUserSettingsTracker(applicationContext).sendNotificationUserSettings()
        }
    }

    private fun fetchSellerAppWidget() {
        val broadcastIntent = Intent().apply {
            action = ACTION_GET_ALL_APP_WIDGET_DATA
            setPackage(packageName)
        }
        sendBroadcast(broadcastIntent)
    }

    private fun setContentView() {
        binding = ActivitySahSellerHomeBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private fun setupBackground() {
        window.decorView.setBackgroundColor(
            getResColor(unifyprinciplesR.color.Unify_Background)
        )
    }

    private fun setupToolbar() {
        binding?.run {
            setSupportActionBar(sahToolbar)
        }
    }

    private fun setupDefaultPage(savedInstanceState: Bundle?) {
        if (intent?.data == null || savedInstanceState != null) {
            val initialPageType =
                savedInstanceState?.getInt(LAST_FRAGMENT_TYPE_KEY) ?: FragmentType.HOME
            showToolbar(initialPageType)
            showInitialPage(initialPageType)
            checkForSellerAppReview(initialPageType)
        } else {
            handleAppLink(intent)
        }
    }

    private fun showInitialPage(pageType: Int) {
        setCurrentFragmentType(pageType)
        binding?.sahBottomNav?.setSelected(pageType)

        if (pageType == FragmentType.OTHER) {
            hideToolbarAndStatusBar()
        }
        navigator?.start(pageType)
    }

    private fun handleAppLink(intent: Intent?) {
        DeepLinkHandler.handleAppLink(intent) { page ->
            val pageType = page.type

            when (pageType) {
                FragmentType.ORDER -> {
                    initSomListLoadTimeMonitoring()
                    lastSomTab = page
                }

                FragmentType.PRODUCT -> lastProductManagePage = page
            }

            showToolbar(pageType)
            setCurrentFragmentType(pageType)
            binding?.sahBottomNav?.setSelected(pageType)
            navigator?.navigateFromAppLink(page)
            checkForSellerAppReview(pageType)
        }
    }

    private fun doubleTapToExit() {
        if (canExitApp) {
            finish()
        } else {
            canExitApp = true
            Toast.makeText(this, R.string.sah_exit_message, Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                canExitApp = false
            }, DOUBLE_TAB_EXIT_DELAY)
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun setupNavigator() {
        val navigationHomeMenuView =
            binding?.sahBottomNav?.getMenuViewByIndex(NAVIGATION_HOME_MENU_POSITION)
        navigator = SellerHomeNavigator(
            this,
            lifecycleScope,
            supportFragmentManager,
            sellerHomeRouter,
            userSession,
            navigationHomeMenuView
        )
        setNavigationView()
    }

    private fun setNavigationView() {
        val navigationOtherMenuView = binding
            ?.sahBottomNav?.getMenuViewByIndex(NAVIGATION_OTHER_MENU_POSITION)
        navigator?.getHomeFragment()?.setNavigationNavigationView(
            navigationView = binding?.sahBottomNav,
            otherMenuView = navigationOtherMenuView
        )
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
        binding?.run {
            when (pageType) {
                FragmentType.HOME, FragmentType.PRODUCT, FragmentType.CHAT -> {
                    val pageTitle = navigator?.getPageTitle(pageType)
                    supportActionBar?.title = pageTitle
                    sahToolbar.show()
                    val showStatusBar = pageType == FragmentType.HOME
                    statusBarShadow?.isVisible = showStatusBar
                }

                else -> {
                    if (!DeviceScreenInfo.isTablet(this@SellerHomeActivity)) {
                        statusBarShadow?.gone()
                    } else {
                        statusBarShadow?.show()
                    }
                    sahToolbar.gone()
                }
            }
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

    private fun showOtherSettingsFragment() {
        hideToolbarAndStatusBar()

        val type = FragmentType.OTHER
        setCurrentFragmentType(type)
        navigator?.showPage(type)

        trackClickBottomNavigation(TrackingConstant.CLICK_OTHERS)
    }

    private fun setCurrentFragmentType(@FragmentType pageType: Int) {
        otherMenuFragmentChangeCallback?.setCurrentFragmentType(pageType)
    }

    private fun observeNotificationsLiveData() {
        homeViewModel.notifications.observe(this) {
            if (it is Success) {
                showNotificationBadge(it.data.notifCenterUnread)
                showChatNotificationCounter(it.data.chat)
                showOrderNotificationCounter(it.data.sellerOrderStatus)
            }
        }
    }

    private fun observeShopInfoLiveData() {
        homeViewModel.shopInfo.observe(this) {
            when (it) {
                is Success -> {
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
                is Fail -> {
                    SellerHomeErrorHandler.logException(
                        it.throwable,
                        SellerHomeErrorHandler.SHOP_INFO
                    )

                    SellerHomeErrorHandler.logExceptionToServer(
                        SellerHomeErrorHandler.SELLER_HOME_TAG,
                        it.throwable,
                        SellerHomeErrorHandler.SHOP_INFO,
                        SellerHomeErrorHandler.SHOP_INFO
                    )
                    navigator?.run {
                        if (isHomePageSelected()) {
                            supportActionBar?.title = userSession.shopName
                        }

                        setHomeTitle(userSession.shopName)
                    }
                }
            }
        }
        homeViewModel.getShopInfo()
    }

    private fun observeIsRoleEligible() {
        homeViewModel.isRoleEligible.observe(this) { result ->
            if (result is Success) {
                result.data.let { isRoleEligible ->
                    if (!isRoleEligible) {
                        RouteManager.route(this, ApplinkConstInternalUserPlatform.LOGOUT)
                        finish()
                    }
                }
            }
        }
    }

    private fun showNotificationBadge(notifUnreadInt: Int) {
        val homeFragment = navigator?.getHomeFragment()
        homeFragment?.setNotifCenterCounter(notifUnreadInt)
    }

    private fun showChatNotificationCounter(unreadSeller: Int) {
        val badgeVisibility = if (unreadSeller <= Int.ZERO) View.GONE else View.VISIBLE
        binding?.sahBottomNav?.setBadge(unreadSeller, FragmentType.CHAT, badgeVisibility)
    }

    private fun showOrderNotificationCounter(orderStatus: NotificationSellerOrderStatusUiModel) {
        val notificationCount = orderStatus.newOrder.plus(orderStatus.readyToShip)
        val badgeVisibility = if (notificationCount <= Int.ZERO) View.GONE else View.VISIBLE
        binding?.sahBottomNav?.setBadge(notificationCount, FragmentType.ORDER, badgeVisibility)
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isDarkMode()) {
                requestStatusBarLight()
            } else {
                requestStatusBarDark()
            }
            binding?.statusBarBackground?.show()
        }
    }

    private fun hideToolbarAndStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding?.statusBarBackground?.hide()
            statusBarCallback?.setStatusBar()
        }
        binding?.sahToolbar?.hide()
    }

    private fun setupBottomNav() {
        binding?.sahBottomNav?.setBackgroundColor(getResColor(android.R.color.transparent))

        menu.add(
            BottomMenu(
                R.id.menu_home,
                resources.getString(R.string.sah_home),
                R.raw.anim_bottom_nav_home,
                R.raw.anim_bottom_nav_home_to_enabled,
                R.drawable.ic_sah_bottom_nav_home_active,
                R.drawable.ic_sah_bottom_nav_home_inactive,
                unifyprinciplesR.color.Unify_GN600,
                false,
                BOTTOM_NAV_EXIT_ANIM_DURATION,
                BOTTOM_NAV_ENTER_ANIM_DURATION
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_product,
                resources.getString(R.string.sah_product),
                R.raw.anim_bottom_nav_product,
                R.raw.anim_bottom_nav_product_to_enabled,
                R.drawable.ic_sah_bottom_nav_product_active,
                R.drawable.ic_sah_bottom_nav_product_inactive,
                unifyprinciplesR.color.Unify_GN600,
                false,
                BOTTOM_NAV_EXIT_ANIM_DURATION,
                BOTTOM_NAV_ENTER_ANIM_DURATION
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_chat,
                resources.getString(R.string.sah_chat),
                R.raw.anim_bottom_nav_chat,
                R.raw.anim_bottom_nav_chat_to_enabled,
                R.drawable.ic_sah_bottom_nav_chat_active,
                R.drawable.ic_sah_bottom_nav_chat_inactive,
                unifyprinciplesR.color.Unify_GN600,
                true,
                BOTTOM_NAV_EXIT_ANIM_DURATION,
                BOTTOM_NAV_ENTER_ANIM_DURATION
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_order,
                resources.getString(R.string.sah_order),
                R.raw.anim_bottom_nav_order,
                R.raw.anim_bottom_nav_order_to_enabled,
                R.drawable.ic_sah_bottom_nav_order_active,
                R.drawable.ic_sah_bottom_nav_order_inactive,
                unifyprinciplesR.color.Unify_GN600,
                true,
                BOTTOM_NAV_EXIT_ANIM_DURATION,
                BOTTOM_NAV_ENTER_ANIM_DURATION
            )
        )
        menu.add(
            BottomMenu(
                R.id.menu_other,
                resources.getString(R.string.sah_others),
                R.raw.anim_bottom_nav_other,
                R.raw.anim_bottom_nav_other_to_enabled,
                R.drawable.ic_sah_bottom_nav_other_active,
                R.drawable.ic_sah_bottom_nav_other_inactive,
                unifyprinciplesR.color.Unify_GN600,
                false,
                BOTTOM_NAV_EXIT_ANIM_DURATION,
                BOTTOM_NAV_ENTER_ANIM_DURATION
            )
        )
        binding?.sahBottomNav?.setMenu(menu)
        binding?.sahBottomNav?.setMenuClickListener(this)
    }

    private fun initSellerHomePlt() {
        if (intent.data == null) {
            initPerformanceMonitoringSellerHome()
        } else {
            DeepLinkHandler.handleAppLink(intent) {
                if (it.type == FragmentType.HOME) {
                    initPerformanceMonitoringSellerHome()
                } else if (it.type == FragmentType.ORDER) {
                    initSomListLoadTimeMonitoring()
                }
            }
        }
    }

    private fun initPerformanceMonitoringSellerHome() {
        performanceMonitoringSellerHomeLayoutPlt = HomeLayoutLoadTimeMonitoring()
        performanceMonitoringSellerHomeLayoutPlt?.initPerformanceMonitoring()
    }

    private fun checkForSellerAppReview(pageType: Int) {
        if (pageType == FragmentType.HOME) {
            lifecycleScope.launch(Dispatchers.IO) {
                sellerReviewHelper?.checkForSellerReview(
                    this@SellerHomeActivity,
                    supportFragmentManager
                )
            }
        }
    }

    private fun setActivityOrientation() {
        if (DeviceScreenInfo.isTablet(this)) {
            val isAccelerometerRotationEnabled = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            ) == 1
            requestedOrientation =
                if (isAccelerometerRotationEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun onAccelerometerOrientationSettingChange(isEnabled: Boolean) {
        if (DeviceScreenInfo.isTablet(this)) {
            requestedOrientation =
                if (isEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun setupSellerHomeInsetListener() {
        binding?.run {
            sahRootLayout.setOnApplyWindowInsetsListener(
                SellerHomeOnApplyInsetsListener(sahContainer, sahBottomNav)
            )
        }
    }

    private fun checkAppUpdate() {
        // if redirected from any seller migration entry point, no need to show the update dialog
        val isRedirectedFromSellerMigrationEntryPoint =
            !intent.data?.getQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME)
                .isNullOrBlank()

        UpdateCheckerHelper.checkAppUpdate(this, isRedirectedFromSellerMigrationEntryPoint)
    }

    private fun observeWearDialog() {
        homeViewModel.shouldAskInstallCompanionApp.observe(this) {
            val isWearPopupShown: Boolean =
                wearSharedPreference.getBoolean(WEAR_POPUP_KEY, false)
            if (it && !isWearPopupShown) {
                val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                dialog.apply {
                    setTitle(resources.getString(R.string.wearos_install_popup_title))
                    setDescription(resources.getString(R.string.wearos_install_popup_description))
                    setPrimaryCTAText(resources.getString(R.string.wearos_install_popup_install))
                    setSecondaryCTAText(resources.getString(R.string.wearos_install_popup_later))
                    setPrimaryCTAClickListener {
                        val marketIntent = Intent(Intent.ACTION_VIEW)
                            .addCategory(Intent.CATEGORY_BROWSABLE)
                            .setData(Uri.parse(TOKOPEDIA_MARKET_WEAR_APP))
                        homeViewModel.launchMarket(marketIntent)
                        dialog.dismiss()
                        Toast.makeText(
                            this@SellerHomeActivity,
                            resources.getString(R.string.wearos_toast_install),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    setSecondaryCTAClickListener {
                        dialog.dismiss()
                    }
                }
                dialog.show()
                wearSharedPreference.edit()
                    .putBoolean(WEAR_POPUP_KEY, true)
                    .apply()
            }
        }
    }

    private fun installDFonBackground() {
        val moduleNameList = listOf(
            DeeplinkDFMapper.DF_CONTENT_PLAY_BROADCASTER,
            DeeplinkDFMapper.DF_SELLER_SHOP_PAGE
        )
        DFInstaller.installOnBackground(this.application, moduleNameList, "Seller Home")
    }

    private fun handleToaster(intent: Intent?) {
        val uri = intent?.data ?: return
        val message = uri.getQueryParameter(
            SellerHomeApplinkConst.TOASTER_MESSAGE
        ).orEmpty()
        val actionText = uri.getQueryParameter(SellerHomeApplinkConst.TOASTER_CTA)
            .orEmpty()
        if (message.isBlank()) return

        val view = binding?.sahContainer ?: return
        Handler(Looper.getMainLooper()).postDelayed({
            Toaster.toasterCustomBottomHeight = dpToPx(88).toInt()
            Toaster.build(
                view,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                actionText
            ).show()
        }, TimeUnit.SECONDS.toMillis(1))
    }

    private fun handleSellerPersona(intent: Intent?) {
        val uri = intent?.data ?: return
        val checkPersonaBtmSheet = uri.getBooleanQueryParameter(SellerHomeApplinkConst.IS_PERSONA, false)
        if (checkPersonaBtmSheet.orFalse()) {
            navigator?.getHomeFragment()?.refreshPersona()
        }
    }
}
