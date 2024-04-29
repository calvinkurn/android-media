package com.tokopedia.navigation.presentation.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver
import com.tokopedia.abstraction.base.view.model.InAppCallback
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.perf.BlocksPerformanceTrace
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.appdownloadmanager.AppDownloadManagerHelper
import com.tokopedia.appdownloadmanager_common.presentation.util.AppDownloadManagerPermission
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.DOWNLOAD_MANAGER_APPLINK_PARAM
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.DOWNLOAD_MANAGER_PARAM_TRUE
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkRouter
import com.tokopedia.applink.DeeplinkDFMapper
import com.tokopedia.applink.FragmentConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.darkmodeconfig.common.DarkModeIntroductionLauncher
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.inappupdate.AppUpdateManagerWrapper
import com.tokopedia.navigation.GlobalNavAnalytics
import com.tokopedia.navigation.GlobalNavConstant
import com.tokopedia.navigation.databinding.ActivityMainParentBinding
import com.tokopedia.navigation.domain.model.Notification
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent
import com.tokopedia.navigation.presentation.di.GlobalNavModule
import com.tokopedia.navigation.presentation.model.BottomNavHomeId
import com.tokopedia.navigation.presentation.model.BottomNavHomeType
import com.tokopedia.navigation.presentation.model.supportedMainFragments
import com.tokopedia.navigation.presentation.presenter.MainParentViewModel
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.navigation_common.listener.HomeBottomNavListener
import com.tokopedia.navigation_common.listener.HomeCoachmarkListener
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener
import com.tokopedia.navigation_common.listener.MainParentStateListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.navigation_common.listener.RefreshNotificationListener
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.BottomNavItemId
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.ui.DynamicHomeNavBarView
import com.tokopedia.notifications.utils.NotificationUserSettingsTracker
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.telemetry.ITelemetryActivity
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import dagger.Lazy
import kotlinx.coroutines.FlowPreview
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.tokopedia.navigation.R as navigationR
import com.tokopedia.resources.common.R as resourcescommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@SuppressLint("DeprecatedMethod")
class NewMainParentActivity : BaseActivity(),
    CartNotifyListener,
    RefreshNotificationListener,
    MainParentStatusBarListener,
    HomePerformanceMonitoringListener,
    MainParentStateListener,
    ITelemetryActivity,
    InAppCallback,
    HomeCoachmarkListener,
    HomeBottomNavListener
{

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory>

    @Inject
    lateinit var globalNavAnalytics: Lazy<GlobalNavAnalytics>

    @Inject
    lateinit var userSession: Lazy<UserSessionInterface>

    private val pltPerformanceCallback by lazy {
        PageLoadTimePerformanceCallback(
            HOME_PERFORMANCE_MONITORING_PREPARE_METRICS,
            HOME_PERFORMANCE_MONITORING_NETWORK_METRICS,
            HOME_PERFORMANCE_MONITORING_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )
    }

    @OptIn(FlowPreview::class)
    private val performanceTrace: BlocksPerformanceTrace? by lazy {
        runCatching {
            BlocksPerformanceTrace(
                applicationContext,
                PERFORMANCE_TRACE_HOME,
                this.lifecycleScope,
                this,
                null,
                null
            )
        }.onFailure { it.printStackTrace() }.getOrNull()
    }

    private val cacheManager by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    private val viewModel by viewModels<MainParentViewModel>(
        factoryProducer = { viewModelFactory.get() }
    )

    private var appDownloadManagerHelper: AppDownloadManagerHelper? = null

    private var isFirstNavigationImpression: Boolean = false

    private lateinit var binding: ActivityMainParentBinding

    private val handler = Handler()

    private var notification: Notification? = null

    private var isUserFirstTimeLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        //changes for triggering unittest checker
        startSelectedPagePerformanceMonitoring()
        startMainParentPerformanceMonitoring()
        pltPerformanceCallback.startCustomMetric(MAIN_PARENT_ON_CREATE_METRICS)

        super.onCreate(savedInstanceState)
        initInjector()
        if (savedInstanceState != null) {
            viewModel.isRecurringAppLink = savedInstanceState.getBoolean(KEY_IS_RECURRING_APPLINK, false)
        }
        initDownloadManagerDialog()
        createView(savedInstanceState)

        val executeEventsWeave = object : WeaveInterface {
            override fun execute(): Any {
                return sendOpenHomeEvent()
            }
        }
        Weaver.executeWeaveCoRoutineWithFirebase(executeEventsWeave, RemoteConfigKey.ENABLE_ASYNC_OPENHOME_EVENT, this, true)
        installDFonBackground()
        runRiskWorker()

        if (pltPerformanceCallback.customMetric.containsKey(MAIN_PARENT_ON_CREATE_METRICS)) {
            pltPerformanceCallback.stopCustomMetric(MAIN_PARENT_ON_CREATE_METRICS)
        }

        sendNotificationUserSetting()
        showDarkModeIntroBottomSheet()

        observeData()
        setupInitialData()
    }

    override fun onStart() {
        super.onStart()
        pltPerformanceCallback.startCustomMetric(MAIN_PARENT_ON_START_METRICS)

        run firstTimeUser@{
            if (GlobalConfig.ENABLE_MACROBENCHMARK_UTIL) return@firstTimeUser

            if (!userSession.get().isFirstTimeUser) return@firstTimeUser
            setDefaultShakeEnable()
            routeOnboarding()
        }

        if (pltPerformanceCallback.customMetric.containsKey(MAIN_PARENT_ON_START_METRICS)) {
            pltPerformanceCallback.stopCustomMetric(MAIN_PARENT_ON_START_METRICS)
        }
    }

    override fun onResume() {
        super.onResume()
        val hasRunOnResumePlt = intent.getBooleanExtra(ARGS_HAS_RUN_ON_RESUME_PLT, false)
        if (!hasRunOnResumePlt) {
            pltPerformanceCallback.startCustomMetric(MAIN_PARENT_ON_RESUME_METRICS)
        }
        // if user is downloading the update (in app update feature),
        // check if the download is finished or is in progress
        checkForInAppUpdateInProgressOrCompleted()
        showDownloadManagerBottomSheet()
        viewModel.fetchNotificationData()

        run firstTimeLogin@{
            if (!userSession.get().isLoggedIn || isUserFirstTimeLogin) return@firstTimeLogin
//            reloadPage()
        }
        isUserFirstTimeLogin = !userSession.get().isLoggedIn

        addShortcutsAsync()

//        if (currentFragment != null) {
//            configureStatusBarBasedOnFragment(currentFragment)
//            onFragmentSelected(currentFragment)
//        }

        if (pltPerformanceCallback.customMetric.containsKey(MAIN_PARENT_ON_RESUME_METRICS) && !hasRunOnResumePlt) {
            pltPerformanceCallback.stopCustomMetric(MAIN_PARENT_ON_RESUME_METRICS)
            intent.putExtra(ARGS_HAS_RUN_ON_RESUME_PLT, true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appDownloadManagerHelper?.activityRef?.clear()
        appDownloadManagerHelper = null
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        saveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        runCatching {
            super.onRestoreInstanceState(savedInstanceState)
        }.onFailure {
            TODO()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkAppLinkCouponCode(intent)
        checkAgeVerificationExtra(intent)

        setIntent(intent)
        setDownloadManagerParameter()
        showSelectedPage()
        //handleAppLinkBottomNavigation(false);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkWritePermissionResultAndInstallApk(requestCode, grantResults)
    }

    override fun startHomePerformanceMonitoring() {
        pageLoadTimePerformanceInterface.startMonitoring(HOME_PERFORMANCE_MONITORING_KEY)
        pageLoadTimePerformanceInterface.startPreparePagePerformanceMonitoring()
    }

    override fun stopHomePerformanceMonitoring(isCache: Boolean) {
        pageLoadTimePerformanceInterface.addAttribution(
            PERFORMANCE_MONITORING_CACHE_ATTRIBUTION,
            if (isCache) {
                PERFORMANCE_MONITORING_CACHE_VALUE
            } else {
                PERFORMANCE_MONITORING_NETWORK_VALUE
            }
        )
        pageLoadTimePerformanceInterface.stopRenderPerformanceMonitoring()
        pageLoadTimePerformanceInterface.stopMonitoring(null)
    }

    override fun getPageLoadTimePerformanceInterface(): PageLoadTimePerformanceInterface {
        return pltPerformanceCallback
    }

    @OptIn(FlowPreview::class)
    override fun getBlocksPerformanceMonitoring(): BlocksPerformanceTrace? {
        return performanceTrace
    }

    override fun requestStatusBarDark() {
        //for tokopedia lightmode, triggered when in top page
        //for tokopedia darkmode, triggered when not in top page
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            //to trigger white text when tokopedia darkmode not on top page
            requestStatusBarLight()
        } else {
            forceRequestStatusBarDark()
        }
    }

    override fun requestStatusBarLight() {
        //for tokopedia lightmode, triggered when not in top page
        //for tokopedia darkmode, triggered when in top page
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * Force status bar texts to dark without UI mode checking.
     * For safe request dark status bar, use requestStatusBarDark()
     */
    override fun forceRequestStatusBarDark() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun onNotifyCart() {
        viewModel.fetchNotificationData()
    }

    override fun onRefreshNotification() {
        viewModel.fetchNotificationData()
    }

    override fun currentVisibleFragment(): String {
//        TODO("Not yet implemented")
        return ""
    }

    override fun getTelemetrySectionName(): String {
        return "home"
    }

    override fun onPositiveButtonInAppClicked(detailUpdate: DetailUpdate) {
        globalNavAnalytics.get().eventClickAppUpdate(detailUpdate.isForceUpdate)
    }

    override fun onNegativeButtonInAppClicked(detailUpdate: DetailUpdate) {
        globalNavAnalytics.get().eventClickCancelAppUpdate(detailUpdate.isForceUpdate)
    }

    override fun onNotNeedUpdateInApp() {
        //noop
    }

    override fun onNeedUpdateInApp(detailUpdate: DetailUpdate) {
        globalNavAnalytics.get().eventImpressionAppUpdate(detailUpdate.isForceUpdate)
    }

    override fun onHomeCoachMarkFinished() {
//        TODO()
    }

    override fun setForYouToHomeMenuTabSelected() {
        TODO("Not yet implemented")
    }

    override fun setHomeToForYouTabSelected() {
        TODO("Not yet implemented")
    }

    override fun isIconJumperEnabled(): Boolean {
        return HomeRollenceController.isIconJumper()
    }

    private fun initInjector() {
        DaggerGlobalNavComponent.builder()
            .baseAppComponent(getApplicationComponent())
            .globalNavModule(GlobalNavModule())
            .build()
            .inject(this)
    }

    private fun getApplicationComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    private fun saveInstanceState(outState: Bundle) {
        val intent = this.intent ?: return
        outState.putBoolean(KEY_IS_RECURRING_APPLINK, viewModel.isRecurringAppLink)

        //only save position when feed page is active and remove only if it is not feed
//        boolean isCurrentFragmentFeed = currentFragment.getClass().getSimpleName().equalsIgnoreCase(FEED_PAGE);
//        if (!isCurrentFragmentFeed) {
//            if (getIntent().getIntExtra(ARGS_TAB_POSITION, 0) != FEED_MENU) return;
//            getIntent().removeExtra(ARGS_TAB_POSITION);
//        } else {
//            getIntent().putExtra(ARGS_TAB_POSITION, FEED_MENU);
//        }
    }

    private fun observeData() {
        viewModel.notification.observe(this) { notification ->
            this.notification = notification

            val activeFragment = getCurrentActiveFragment() ?: return@observe
            setBadgeNotificationCounter(activeFragment)
        }

        viewModel.dynamicBottomNav.observe(this) { bottomNav ->
            binding.dynamicNavbar.setModelList(bottomNav)
        }
    }

    private fun setupInitialData() {
        viewModel.fetchDynamicBottomNavBar()
    }

    private fun startSelectedPagePerformanceMonitoring() {
        val tabId = getTabIdFromIntent()
        if (tabId == BottomNavHomeId) startHomePerformanceMonitoring()
    }

    private fun startMainParentPerformanceMonitoring() {
        PerformanceMonitoring.start(MAIN_PARENT_PERFORMANCE_MONITORING_KEY);
    }

    private fun getTabIdFromIntent(): BottomNavItemId {
        return intent?.extras?.let(::getTabIdFromIntentExtras)
            ?: intent?.data?.let(::getTabIdFromQueryParameter)
            ?: BottomNavHomeId
    }

    private fun getTabIdFromIntentExtras(extras: Bundle): BottomNavItemId {
        return BottomNavItemId(extras.getString(ARGS_TAB_ID, TAB_TYPE_HOME))
    }

    private fun getTabIdFromQueryParameter(data: Uri): BottomNavItemId {
        return BottomNavItemId(data.getQueryParameter(ARGS_TAB_ID) ?: TAB_TYPE_HOME)
    }

    private fun initDownloadManagerDialog() {
        appDownloadManagerHelper = AppDownloadManagerHelper(WeakReference(this))
        setDownloadManagerParameter()
    }

    private fun setDownloadManagerParameter() {
        appDownloadManagerHelper?.setIsTriggeredViaApplink(checkAppLinkShowDownloadManager())
    }

    private fun checkAppLinkShowDownloadManager(): Boolean {
        val uri = intent?.data ?: return false
        val paramValue = uri.getQueryParameter(DOWNLOAD_MANAGER_APPLINK_PARAM)
        return paramValue != null && paramValue.equals(DOWNLOAD_MANAGER_PARAM_TRUE, true)
    }

    //TODO()
    private fun createView(savedInstanceState: Bundle?) {
        isFirstNavigationImpression = true
        binding = ActivityMainParentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstTimeWeave = object : WeaveInterface {
            override fun execute(): Any {
                return executeFirstTimeEvent()
            }
        }
        Weaver.executeWeaveCoRoutineWithFirebase(firstTimeWeave, RemoteConfigKey.ENABLE_ASYNC_FIRSTTIME_EVENT, this, true)
        checkAppLinkCouponCode(intent)
        showSelectedPage()

        setupBottomNavigation()
//        populateBottomNavigationView()
//        bottomNavigation.setMenuClickListener(this)
//        bottomNavigation.setHomeForYouMenuClickListener(this)
    }

    private fun executeFirstTimeEvent(): Boolean {
        if (isFirstTime()) {
            globalNavAnalytics.get().trackFirstTime(this)
        }
        return true
    }

    private fun isFirstTime(): Boolean {
        val cache = LocalCacheHandler(this, GlobalNavConstant.Cache.KEY_FIRST_TIME)
        return cache.getBoolean(GlobalNavConstant.Cache.KEY_IS_FIRST_TIME, false)
    }

    private fun checkAppLinkCouponCode(intent: Intent) {
        val appLink = intent.getStringExtra(ApplinkRouter.EXTRA_APPLINK)
        if (viewModel.isRecurringAppLink || TextUtils.isEmpty(appLink)) return

        run moEngageCoupon@{
            val moEngageCouponCode = intent.getStringExtra(KEY_MO_ENGAGE_COUPON_CODE)
            if (moEngageCouponCode == null || TextUtils.isEmpty(moEngageCouponCode)) return@moEngageCoupon

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(
                getString(navigationR.string.coupon_copy_text),
                moEngageCouponCode
            )
            clipboard?.run { setPrimaryClip(clip) }

            runCatching {
                if (isFinishing) return@moEngageCoupon
                Toast.makeText(
                    this,
                    resources.getString(navigationR.string.coupon_copy_text),
                    Toast.LENGTH_LONG
                ).show()
            }.onFailure { it.printStackTrace() }
        }

        RouteManager.route(this, appLink)
        viewModel.isRecurringAppLink = true
    }

    private fun checkAgeVerificationExtra(intent: Intent) {
        if (!intent.hasExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS)) return
        if (isFinishing) return

        Toaster.showErrorWithAction(
            findViewById(android.R.id.content),
            intent.getStringExtra(ApplinkConstInternalCategory.PARAM_EXTRA_SUCCESS).orEmpty(),
            Snackbar.LENGTH_INDEFINITE,
            getString(resourcescommonR.string.general_label_ok),
        ) {}
    }

    private fun showSelectedPage() {
        val tabId = run {
            val tabIdFromIntent = getTabIdFromIntent()
            if (viewModel.hasTabType(tabIdFromIntent.type)) tabIdFromIntent
            else BottomNavHomeId
        }
        val fragment = getFragmentById(tabId) ?: return

        run feedPlusArguments@{
            if (!fragment::class.java.name.equals(FragmentConst.FEED_PLUS_CONTAINER_FRAGMENT, true)) return@feedPlusArguments
            runCatching {
                val oldArgs = fragment.arguments ?: Bundle()
                intent.extras?.let { extras -> oldArgs.putAll(extras) }
                fragment.arguments = oldArgs
            }.onFailure { it.printStackTrace() }
        }

        selectFragment(fragment, tabId)
    }

    private fun getFragmentById(id: BottomNavItemId): Fragment? {
//        TODO()
//        return HomeInternalRouter.getHomeFragment(
//            intent.getBooleanExtra(SCROLL_RECOMMEND_LIST, false)
//        )
        return supportFragmentManager.findFragmentByTag(id.value) ?: run createFragment@{
            val fragmentCreator = supportedMainFragments[id.type] ?: return null
            supportFragmentManager.fragmentCreator(this, id.discoId)
        }
    }

    private fun selectFragment(fragment: Fragment, itemId: BottomNavItemId) {
        configureStatusBarBasedOnFragment(fragment)
        configureNavigationBarBasedOnFragment(fragment)
        openFragment(fragment, itemId)
        setBadgeNotificationCounter(fragment)
    }

    private fun configureStatusBarBasedOnFragment(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setupStatusBarInMarshmallowAbove(fragment)
        } else {
            setupStatusBar()
        }
    }

    private fun configureNavigationBarBasedOnFragment(fragment: Fragment) {
        val isForceDarkMode = isFragmentForceDarkModeNavigationBar(fragment)
        binding.dynamicNavbar.forceDarkMode(isForceDarkMode)

        val lineColorRes =
            if (isForceDarkMode) navigationR.color.navigation_dms_line_bottom_nav_darkmode else unifyprinciplesR.color.Unify_NN50
        binding.lineBottomNav.setBackgroundResource(lineColorRes)
    }

    private fun setupStatusBarInMarshmallowAbove(fragment: Fragment) {
        if (isFragmentLightStatusBar(fragment)) {
            requestStatusBarLight()
        } else {
            requestStatusBarDark()
        }
    }

    //TODO()
    private fun setupStatusBar() {
        //apply inset to allow recyclerview scrolling behind status bar
        binding.container.fitsSystemWindows = false
        binding.container.requestApplyInsets()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val flags = binding.root.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            binding.container.systemUiVisibility = flags
            window.statusBarColor = ContextCompat.getColor(this, unifyprinciplesR.color.Unify_NN0)
        }

        //make full transparent statusBar
        requestStatusBarLight()
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//        window.statusBarColor = Color.TRANSPARENT
    }

    private fun isFragmentLightStatusBar(fragment: Fragment): Boolean {
        return (fragment as? FragmentListener)?.isLightThemeStatusBar ?: false
    }

    private fun isFragmentForceDarkModeNavigationBar(fragment: Fragment): Boolean {
        return (fragment as? FragmentListener)?.isForceDarkModeNavigationBar ?: false
    }

    private fun openFragment(fragment: Fragment, itemId: BottomNavItemId) {
        val activeFragment = getCurrentActiveFragment()
        if (activeFragment == fragment) return

        supportFragmentManager.commit {
            run hideActiveFragment@{
                if (activeFragment == null) return@hideActiveFragment
                hide(activeFragment)
                FragmentLifecycleObserver.onFragmentUnSelected(activeFragment)
            }
            if (!fragment.isAdded) {
                add(binding.container.id, fragment, itemId.value)
            }
            run showFragment@{
                show(fragment)
                FragmentLifecycleObserver.onFragmentSelected(fragment)
            }
        }
    }

    private fun getCurrentActiveFragment(): Fragment? {
//        TODO()
        return supportFragmentManager.fragments.firstOrNull {
            it.isAdded && it.isVisible
        }
    }

    private fun setupBottomNavigation() {
        binding.dynamicNavbar.setListener(object : DynamicHomeNavBarView.Listener {
            override fun onItemSelected(
                view: DynamicHomeNavBarView,
                model: BottomNavBarUiModel,
                isReselected: Boolean
            ) {
                val fragment = getFragmentById(model.uniqueId)
                fragment?.let { selectFragment(it, model.uniqueId) }
            }
        })
    }

    private fun sendOpenHomeEvent(): Boolean {
        val value = DataLayer.mapOf(AppEventTracking.MOENGAGE.LOGIN_STATUS, userSession.get().isLoggedIn)
        TrackApp.getInstance().moEngage.sendTrackEvent(value, AppEventTracking.EventMoEngage.OPEN_BERANDA)
        return true
    }

    //TODO("Check if DF_ALPHA_TESTING is still required")
    private fun installDFonBackground() {
        val userSession = this.userSession.get()
        val moduleNameList = buildList {

            run loggedInDF@{
                if (!userSession.isLoggedIn) return@loggedInDF

                add(DeeplinkDFMapper.DF_PROMO_TOKOPOINTS)
                add(DeeplinkDFMapper.DF_USER_SETTINGS)
                add(DeeplinkDFMapper.DF_OPERATIONAL_CONTACT_US)
                add(DeeplinkDFMapper.DF_PROMO_GAMIFICATION)
                add(DeeplinkDFMapper.DF_MERCHANT_LOGIN)
            }

            if (userSession.hasShop()) {
                add(DeeplinkDFMapper.DF_MERCHANT_SELLER)
            }

            add(DeeplinkDFMapper.DF_TRAVEL)
            add(DeeplinkDFMapper.DF_ENTERTAINMENT)
            add(DeeplinkDFMapper.DF_TOKOPEDIA_NOW)
            add(DeeplinkDFMapper.DF_TOKOFOOD)
            add(DeeplinkDFMapper.DF_MERCHANT_NONLOGIN)
            add(DeeplinkDFMapper.DF_DILAYANI_TOKOPEDIA)

//            if (BuildConfig.VERSION_NAME.endsWith(SUFFIX_ALPHA) && remoteConfig.get().getBoolean(RemoteConfigKey.ENABLE_APLHA_OBSERVER, true)) {
//                moduleNameList.add(DeeplinkDFMapper.DF_ALPHA_TESTING);
//            }
        }
        DFInstaller.installOnBackground(application, moduleNameList, "Home")
    }

    private fun runRiskWorker() {
        // Most of workers do nothing if it has already succeed previously.
        SubmitDeviceWorker.scheduleWorker(applicationContext, false)
    }

    private fun sendNotificationUserSetting() {
        if (!userSession.get().isLoggedIn) return
        NotificationUserSettingsTracker(applicationContext).sendNotificationUserSettings()
    }

    private fun showDarkModeIntroBottomSheet() {
        DarkModeIntroductionLauncher
            .withToaster(intent, window.decorView)
            .launch(this, supportFragmentManager, userSession.get().isLoggedIn)
    }

    private fun setDefaultShakeEnable() {
        cacheManager.edit {
            putBoolean(getString(navigationR.string.pref_receive_shake_nav), true)
        }
    }

    private fun routeOnboarding() {
        RouteManager.route(this, ApplinkConstInternalMarketplace.ONBOARDING)
        finish()
    }

    /**
     * While refreshing the app update info, we also check whether we have updates in progress to
     * complete.
     *
     * <p>This is important, so the app doesn't forget about downloaded updates even if it gets killed
     * during the download or misses some notifications.
     */
    private fun checkForInAppUpdateInProgressOrCompleted() {
        AppUpdateManagerWrapper.checkUpdateInProgressOrCompleted(this)
    }

    private fun showDownloadManagerBottomSheet() {
        appDownloadManagerHelper?.showAppDownloadManagerBottomSheet()
    }

    private fun checkWritePermissionResultAndInstallApk(requestCode: Int, grantResults: IntArray) {
        if (!GlobalConfig.IS_NAKAMA_VERSION) return
        AppDownloadManagerPermission.checkRequestPermissionResult(grantResults, requestCode) { hasPermission ->
            if (!hasPermission) return@checkRequestPermissionResult
            appDownloadManagerHelper?.startDownloadApk()
        }
    }

    private fun addShortcutsAsync() {
        val shortcutsWeave = object : WeaveInterface {
            override fun execute(): Any {
                addShortcuts()
                return true
            }
        }
        Weaver.executeWeaveCoRoutineNow(shortcutsWeave)
    }

    private fun addShortcuts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
        runCatching {
            val shortcutManager = getSystemService(ShortcutManager::class.java) ?: return@runCatching
            shortcutManager.removeAllDynamicShortcuts()

            val args = Bundle().apply {
                putBoolean(GlobalNavConstant.EXTRA_APPLINK_FROM_PUSH, true)
                putBoolean(GlobalNavConstant.FROM_APP_SHORTCUTS, true)
            }

            val homeIntent = MainParentActivity.start(this)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setAction(RouteManager.INTERNAL_VIEW)

            val shortcutInfoList = buildList {
                add(buildSearchAutoCompleteShortcut(args, homeIntent))
                if (userSession.get().isLoggedIn) {
                    add(buildWishlistShortcut(args, homeIntent))
                }
                add(buildPayShortcut(args, homeIntent))
                if (userSession.get().isLoggedIn) {
                    add(buildSellShortcut(args, homeIntent))
                }
            }
            shortcutManager.addDynamicShortcuts(shortcutInfoList)
        }.onFailure {
            it.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildSearchAutoCompleteShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val searchIntent = RouteManager.getIntent(this, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
            .setAction(RouteManager.INTERNAL_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(this, Shortcut.Search.id)
            .setShortLabel(getString(navigationR.string.navigation_home_label_shortcut_search))
            .setLongLabel(getString(navigationR.string.navigation_home_label_shortcut_search))
            .setIcon(Icon.createWithResource(this, navigationR.drawable.main_parent_navigation_ic_search_shortcut))
            .setIntents(arrayOf(homeIntent, searchIntent))
            .setRank(Shortcut.Search.ordinal)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildWishlistShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val wishlistIntent = RouteManager.getIntent(this, ApplinkConst.NEW_WISHLIST)
            .setAction(Intent.ACTION_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(this, Shortcut.Wishlist.id)
            .setShortLabel(getString(navigationR.string.navigation_home_label_shortcut_wishlist))
            .setLongLabel(getString(navigationR.string.navigation_home_label_shortcut_wishlist))
            .setIcon(Icon.createWithResource(this, navigationR.drawable.ic_wishlist_shortcut))
            .setIntents(arrayOf(homeIntent, wishlistIntent))
            .setRank(Shortcut.Wishlist.ordinal)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildPayShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val digitalIntent = RouteManager.getIntent(this, ApplinkConst.RECHARGE_SUBHOMEPAGE_HOME_NEW)
            .setAction(Intent.ACTION_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(this, Shortcut.Pay.id)
            .setShortLabel(getString(navigationR.string.navigation_home_label_shortcut_pay))
            .setLongLabel(getString(navigationR.string.navigation_home_label_shortcut_pay))
            .setIcon(Icon.createWithResource(this, navigationR.drawable.ic_pay_shortcut))
            .setIntents(arrayOf(homeIntent, digitalIntent))
            .setRank(Shortcut.Pay.ordinal)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun buildSellShortcut(args: Bundle, homeIntent: Intent): ShortcutInfo {
        val shopIntent = if (userSession.get().hasShop()) {
            RouteManager.getIntent(this, ApplinkConstInternalMarketplace.SHOP_PAGE, userSession.get().shopId)
        } else {
            RouteManager.getIntent(this, ApplinkConstInternalMarketplace.OPEN_SHOP)
        }.setAction(Intent.ACTION_VIEW)
            .putExtras(args)

        return ShortcutInfo.Builder(this, Shortcut.Sell.id)
            .setShortLabel(getString(navigationR.string.navigation_home_label_shortcut_sell))
            .setLongLabel(getString(navigationR.string.navigation_home_label_shortcut_sell))
            .setIcon(Icon.createWithResource(this, navigationR.drawable.ic_sell_shortcut))
            .setIntents(arrayOf(homeIntent, shopIntent))
            .setRank(Shortcut.Sell.ordinal)
            .build()
    }

    /**
     * This is duplicated by StatusBarUtil function
     */
    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes

        if (on) winParams.flags = winParams.flags or bits
        else winParams.flags = winParams.flags and bits.inv()

        win.attributes = winParams
    }

    /**
     * Notification
     */
    private fun setBadgeNotificationCounter(fragment: Fragment?) {
        handler.post {
            if (fragment == null) return@post
            val notification = this.notification
            if (fragment is AllNotificationListener && notification != null) {
                val totalInbox = notification.totalInbox
                val totalNotification = notification.totalNotif
                fragment.onNotificationChanged(totalNotification, totalInbox, notification.totalCart)
            }
            invalidateOptionsMenu()
        }
    }

    private fun BottomNavBarItemType.getTag(discoId: DiscoId): String {
        return "tag_${value}_$discoId"
    }

    companion object {
        private const val ARGS_TAB_ID = "tab_id"
        private const val ARGS_HAS_RUN_ON_RESUME_PLT = "has_run_on_resume_plt"
        internal  const val SCROLL_RECOMMEND_LIST = "recommend_list"

        private const val TAB_TYPE_HOME = "home"

        private const val MAIN_PARENT_PERFORMANCE_MONITORING_KEY = "mp_slow_rendering_perf"
        private const val MAIN_PARENT_ON_CREATE_METRICS = "mp_main_parent_on_create_metrics"
        private const val MAIN_PARENT_ON_START_METRICS = "mp_main_parent_on_start_metrics"
        private const val MAIN_PARENT_ON_RESUME_METRICS = "mp_main_parent_on_resume_metrics"

        private const val HOME_PERFORMANCE_MONITORING_KEY = "mp_home"
        private const val HOME_PERFORMANCE_MONITORING_PREPARE_METRICS = "home_plt_start_page_metrics"
        private const val HOME_PERFORMANCE_MONITORING_NETWORK_METRICS = "home_plt_network_request_page_metrics"
        private const val HOME_PERFORMANCE_MONITORING_RENDER_METRICS = "home_plt_render_page_metrics"
        private const val PERFORMANCE_MONITORING_CACHE_ATTRIBUTION = "dataSource"
        private const val PERFORMANCE_MONITORING_CACHE_VALUE = "Cache"
        private const val PERFORMANCE_MONITORING_NETWORK_VALUE = "Network"
        private const val PERFORMANCE_TRACE_HOME = "home"

        private const val KEY_IS_RECURRING_APPLINK = "IS_RECURRING_APPLINK"
        private const val KEY_MO_ENGAGE_COUPON_CODE = "coupon_code"

        fun start(context: Context): Intent {
            return Intent(context, NewMainParentActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    enum class Shortcut(val id: String) {
        Search("Cari"),
        Wishlist("Wishlist"),
        Pay("Tagihan"),
        Sell("Jual")
    }
}
