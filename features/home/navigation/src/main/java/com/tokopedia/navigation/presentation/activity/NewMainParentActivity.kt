package com.tokopedia.navigation.presentation.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate
import com.tokopedia.abstraction.base.view.fragment.lifecycle.FragmentLifecycleObserver
import com.tokopedia.abstraction.base.view.model.InAppCallback
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.AppLogParam.IS_MAIN_PARENT
import com.tokopedia.analytics.byteio.AppLogParam.PAGE_NAME
import com.tokopedia.analytics.byteio.EnterMethod
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.darkmodeconfig.common.DarkModeIntroductionLauncher
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.inappupdate.AppUpdateManagerWrapper
import com.tokopedia.navigation.GlobalNavAnalytics
import com.tokopedia.navigation.GlobalNavConstant
import com.tokopedia.navigation.databinding.ActivityNewMainParentBinding
import com.tokopedia.navigation.domain.model.Notification
import com.tokopedia.navigation.presentation.di.DaggerGlobalNavComponent
import com.tokopedia.navigation.presentation.model.BottomNavFeedId
import com.tokopedia.navigation.presentation.model.BottomNavHomeId
import com.tokopedia.navigation.presentation.model.BottomNavMePageId
import com.tokopedia.navigation.presentation.model.BottomNavMePageType
import com.tokopedia.navigation.presentation.model.putDiscoId
import com.tokopedia.navigation.presentation.model.putQueryParams
import com.tokopedia.navigation.presentation.model.putShouldShowGlobalNav
import com.tokopedia.navigation.presentation.model.supportedMainFragments
import com.tokopedia.navigation.presentation.presenter.MainParentViewModel
import com.tokopedia.navigation.presentation.util.GlobalNavAnalyticsProcessor
import com.tokopedia.navigation.presentation.util.TabSelectedListener
import com.tokopedia.navigation.presentation.util.VisitFeedProcessor
import com.tokopedia.navigation.presentation.util.awaitLayout
import com.tokopedia.navigation.presentation.util.createTabSelectedListener
import com.tokopedia.navigation.util.AssetPreloadManager
import com.tokopedia.navigation.util.MePageCoachMark
import com.tokopedia.navigation.util.TokopediaShortcutManager
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.CartNotifyListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.navigation_common.listener.HomeBottomNavListener
import com.tokopedia.navigation_common.listener.HomeCoachmarkListener
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener
import com.tokopedia.navigation_common.listener.HomeScrollViewListener
import com.tokopedia.navigation_common.listener.MainParentStateListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.navigation_common.listener.RefreshNotificationListener
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.BottomNavBarView
import com.tokopedia.navigation_common.ui.BottomNavItemId
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.util.inDarkMode
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.coroutines.resume
import com.tokopedia.navigation.R as navigationR
import com.tokopedia.resources.common.R as resourcescommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

@SuppressLint("DeprecatedMethod")
@Suppress("LateinitUsage")
class NewMainParentActivity :
    BaseActivity(),
    CartNotifyListener,
    RefreshNotificationListener,
    MainParentStatusBarListener,
    HomePerformanceMonitoringListener,
    MainParentStateListener,
    ITelemetryActivity,
    InAppCallback,
    HomeCoachmarkListener,
    HomeBottomNavListener {

    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelFactory>

    @Inject
    lateinit var globalNavAnalytics: Lazy<GlobalNavAnalytics>

    @Inject
    lateinit var userSession: Lazy<UserSessionInterface>

    @Inject
    lateinit var assetPreloadManager: Lazy<AssetPreloadManager>

    @Inject
    lateinit var dispatchers: Lazy<CoroutineDispatchers>

    @Inject
    lateinit var globalAnalyticsProcessor: Lazy<GlobalNavAnalyticsProcessor>

    @Inject
    lateinit var visitFeedProcessor: Lazy<VisitFeedProcessor>

    @Inject
    lateinit var shortcutManager: Lazy<TokopediaShortcutManager>

    @Inject
    lateinit var mePageCoachMark: Lazy<MePageCoachMark>

    private var coachMarkJob: Job? = null

    private val onTabSelected: List<TabSelectedListener> by lazy {
        listOf(
            createTabSelectedListener(globalAnalyticsProcessor.get(), true, { !it }, { false }),
            createTabSelectedListener(visitFeedProcessor.get()),
            createTabSelectedListener { updateAppLogPageData(it.uniqueId, false) },
            createTabSelectedListener { sendEnterPage(it.uniqueId) },
            createTabSelectedListener { if (it.uniqueId != BottomNavHomeId) mePageCoachMark.get().forceDismiss() }
        )
    }

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
                this,
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

    private lateinit var binding: ActivityNewMainParentBinding

    private val handler = Handler()

    private var notification: Notification? = null

    private var isUserFirstTimeLogin = false

    private var doubleTapExit = false

    private var selectJob: Job? = null

    private val darkModeContext by lazy { inDarkMode }

    override fun onCreate(savedInstanceState: Bundle?) {
        // changes for triggering unittest checker
        startSelectedPagePerformanceMonitoring()
        startMainParentPerformanceMonitoring()
        pltPerformanceCallback.startCustomMetric(MAIN_PARENT_ON_CREATE_METRICS)

        super.onCreate(savedInstanceState)
        HomeRollenceController.fetchIconJumperValue() // TODO("Is this still valid after revamp?")
        initInjector()
        if (savedInstanceState != null) {
            adjustIntentFromSavedState(savedInstanceState)
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
            if (!userSession.get().isLoggedIn || !isUserFirstTimeLogin) {
                intent.removeTargetTabIdExtra()
                return@firstTimeLogin
            }
            val targetId = run getFromExtra@{
                val targetTabIdExtra = intent.getTargetTabIdExtra()
                intent.removeTargetTabIdExtra()
                targetTabIdExtra
            } ?: run getCurrentActive@{
                getCurrentActiveFragment()?.tag?.let {
                    BottomNavItemId(it)
                } ?: BottomNavHomeId
            }

            reloadPage(targetId, true)
        }
        isUserFirstTimeLogin = !userSession.get().isLoggedIn

        addShortcutsAsync()

        val fragment = getCurrentActiveFragment()
        if (fragment != null) {
            configureBackgroundBasedOnFragment(fragment)
            configureStatusBarBasedOnFragment(fragment)
            FragmentLifecycleObserver.onFragmentSelected(fragment)
        }

        if (pltPerformanceCallback.customMetric.containsKey(MAIN_PARENT_ON_RESUME_METRICS) && !hasRunOnResumePlt) {
            pltPerformanceCallback.stopCustomMetric(MAIN_PARENT_ON_RESUME_METRICS)
            intent.putExtra(ARGS_HAS_RUN_ON_RESUME_PLT, true)
        }
    }

    override fun onPause() {
        super.onPause()
        coachMarkJob?.cancel()
        mePageCoachMark.get().forceDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        appDownloadManagerHelper?.activityRef?.clear()
        appDownloadManagerHelper = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        AppUpdateManagerWrapper.onActivityResult(this, requestCode, resultCode)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        saveInstanceState(outState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceState(outState)
    }

    private fun adjustIntentFromSavedState(savedInstanceState: Bundle) {
        val type = savedInstanceState.getString(ARGS_TAB_TYPE) ?: return
        val discoId = savedInstanceState.getString(ARGS_DISCO_ID, "")
        intent
            .putExtra(ARGS_TAB_TYPE, type)
            .putExtra(ARGS_DISCO_ID, discoId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        runCatching {
            super.onRestoreInstanceState(savedInstanceState)
        }.onFailure {
            reloadPage(BottomNavHomeId, false)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkAppLinkCouponCode(intent)
        checkAgeVerificationExtra(intent)

        setIntent(intent)
        setDownloadManagerParameter()
        showSelectedPage()
        handleAppLinkBottomNavigation(false)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkWritePermissionResultAndInstallApk(requestCode, grantResults)
    }

    override fun onBackPressed() {
        if (doubleTapExit) {
            finish()
        } else {
            doubleTapExit = true
            runCatching {
                if (!isFinishing) {
                    Toast.makeText(this, navigationR.string.exit_message, Toast.LENGTH_SHORT).show()
                }
                Handler().postDelayed({
                    doubleTapExit = false
                }, EXIT_DELAY_MILLIS)
            }.onFailure { it.printStackTrace() }
        }
    }

    private fun reloadPage(id: BottomNavItemId, isJustLoggedIn: Boolean) {
        val isFeed = id == BottomNavFeedId
        val intent = intent.putExtra(
            ApplinkConstInternalContent.UF_EXTRA_FEED_IS_JUST_LOGGED_IN,
            isFeed && isJustLoggedIn
        ).putTabIdExtra(id)

        if (isFeed) {
            recreate()
        } else {
            finish()
            startActivity(intent)
        }
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
        // for tokopedia lightmode, triggered when in top page
        // for tokopedia darkmode, triggered when not in top page
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // to trigger white text when tokopedia darkmode not on top page
            requestStatusBarLight()
        } else {
            forceRequestStatusBarDark()
        }
    }

    override fun requestStatusBarLight() {
        // for tokopedia lightmode, triggered when not in top page
        // for tokopedia darkmode, triggered when in top page
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
        val tag = getCurrentActiveFragment()?.tag ?: return ""
        val id = BottomNavItemId(tag)
        return viewModel.getModelById(id)?.title.orEmpty()
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
        // noop
    }

    override fun onNeedUpdateInApp(detailUpdate: DetailUpdate) {
        globalNavAnalytics.get().eventImpressionAppUpdate(detailUpdate.isForceUpdate)
    }

    override fun onHomeCoachMarkFinished() {
        // Feed Coachmark has been deprecated, so this is expected to be empty as of now
        coachMarkJob?.cancel()
        coachMarkJob = lifecycleScope.launch {
            val mePageView = binding.dynamicNavbar.findBottomNavItemViewById(BottomNavMePageId) ?: return@launch
            mePageView.awaitLayout()
            mePageCoachMark.get().show(mePageView)
        }
    }

    override fun setForYouToHomeMenuTabSelected() {
        binding.dynamicNavbar.setJumperForId(BottomNavHomeId, false)
    }

    override fun setHomeToForYouTabSelected() {
        binding.dynamicNavbar.setJumperForId(BottomNavHomeId, true)
    }

    override fun isIconJumperEnabled(): Boolean {
        val bottomNav = viewModel.dynamicBottomNav.value ?: return false
        return bottomNav.any { it.uniqueId == BottomNavHomeId && it.jumper != null }
    }

    private fun initInjector() {
        DaggerGlobalNavComponent.factory()
            .create(getApplicationComponent(), this)
            .inject(this)
    }

    private fun getApplicationComponent(): BaseAppComponent {
        return (application as BaseMainApplication).baseAppComponent
    }

    private fun saveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_IS_RECURRING_APPLINK, viewModel.isRecurringAppLink)

        val currentFragmentTag = getCurrentActiveFragment()?.tag ?: return
        val tabId = BottomNavItemId(currentFragmentTag)
        outState.putString(ARGS_TAB_TYPE, tabId.type.value)
        outState.putString(ARGS_DISCO_ID, tabId.discoId.value)
    }

    private fun observeData() {
        viewModel.notification.observe(this) { notification ->
            this.notification = notification

            val activeFragment = getCurrentActiveFragment() ?: return@observe
            setBadgeNotificationCounter(activeFragment)
        }

        viewModel.dynamicBottomNav.observe(this) { bottomNavList ->
            if (bottomNavList == null) return@observe
            binding.dynamicNavbar.setModelList(bottomNavList)

            // TODO("Ask Dave about this")
            if (bottomNavList.isEmpty()) return@observe
            val homeBottomNav = bottomNavList.firstOrNull { it.uniqueId == BottomNavHomeId } ?: return@observe
            if (homeBottomNav.jumper != null) setHomeToForYouTabSelected()
        }

        viewModel.nextDynamicBottomNav.observe(this) { bottomNavList ->
            if (bottomNavList == null) return@observe
            lifecycleScope.launch {
                assetPreloadManager.get().preloadByCacheTask(bottomNavList)
            }
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
        PerformanceMonitoring.start(MAIN_PARENT_PERFORMANCE_MONITORING_KEY)
    }

    private fun getTabIdFromIntent(): BottomNavItemId {
        return intent?.getTabIdExtra()
            ?: intent?.data?.let(::getTabIdFromQueryParameter)
            ?: BottomNavHomeId
    }

    private fun getTabIdFromQueryParameter(data: Uri): BottomNavItemId? {
        val type = data.getQueryParameter(ARGS_TAB_TYPE) ?: return null
        return BottomNavItemId(
            BottomNavBarItemType(type),
            DiscoId(data.getQueryParameter(ARGS_DISCO_ID).orEmpty())
        )
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

    private fun createView(savedInstanceState: Bundle?) {
        isFirstNavigationImpression = true
        binding = ActivityNewMainParentBinding.inflate(layoutInflater)
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
            getString(resourcescommonR.string.general_label_ok)
        ) {}
    }

    private fun showSelectedPage() {
        selectJob?.cancel()
        selectJob = lifecycleScope.launch {
            awaitNavData()
            val tabId = run {
                val tabIdFromIntent = getTabIdFromIntent()
                if (viewModel.hasTabType(tabIdFromIntent.type)) {
                    tabIdFromIntent
                } else {
                    BottomNavHomeId
                }
            }
            val fragment = getFragmentById(tabId) ?: return@launch
            val success = selectFragment(fragment, tabId)
            if (!success) return@launch
            binding.dynamicNavbar.select(tabId, forceSelect = true)
        }
    }

    private fun handleAppLinkBottomNavigation(isFirstInit: Boolean) {
        if (!::binding.isInitialized) return

        val tabId = getTabIdFromIntent()
        binding.dynamicNavbar.select(tabId)

        if (tabId == BottomNavHomeId) {
            setHomeNavSelected(tabId, isFirstInit)
        }
    }

    private fun setHomeNavSelected(tabId: BottomNavItemId, isFirstInit: Boolean) {
        if (!isFirstInit) return
        updateAppLogPageData(tabId, true)
        sendEnterPage(tabId)
    }

    private fun getFragmentById(id: BottomNavItemId, model: BottomNavBarUiModel? = null): Fragment? {
        return supportFragmentManager.findFragmentByTag(id.value) ?: run createFragment@{
            val fragmentCreator = supportedMainFragments[id.type] ?: return null
            val create = fragmentCreator.create

            val fragment = supportFragmentManager.create(
                this,
                Bundle().apply {
                    putDiscoId(id.discoId)
                    putShouldShowGlobalNav(!viewModel.hasTabType(BottomNavMePageType))

                    val navBarModel = model ?: return@apply
                    putQueryParams(navBarModel.queryParams)
                }
            )

            onFragmentCreated(fragment)
            fragment
        }
    }

    private fun onFragmentCreated(fragment: Fragment) {
        when (fragment.tag) {
            BottomNavFeedId.value -> onFeedFragmentCreated(fragment)
            else -> {}
        }
    }

    private fun onFeedFragmentCreated(fragment: Fragment) {
        runCatching {
            val oldArgs = fragment.arguments ?: Bundle()
            intent.extras?.let { extras -> oldArgs.putAll(extras) }
            fragment.arguments = oldArgs
        }.onFailure { it.printStackTrace() }

        intent.putExtra(ApplinkConstInternalContent.UF_EXTRA_FEED_ENTRY_POINT, ApplinkConstInternalContent.NAV_BUTTON_ENTRY_POINT)
    }

    private fun selectFragment(fragment: Fragment, itemId: BottomNavItemId): Boolean {
        if (checkShouldLogin(itemId)) return false

        configureBackgroundBasedOnFragment(fragment)
        configureStatusBarBasedOnFragment(fragment)
        configureNavigationBarBasedOnFragment(fragment)
        openFragment(fragment, itemId)
        setBadgeNotificationCounter(fragment)

        val model = viewModel.getModelById(itemId)
        if (model != null) onTabSelected.forEach { it.onSelected(model) }

        return true
    }

    private fun checkShouldLogin(id: BottomNavItemId): Boolean {
        val requireLogin = supportedMainFragments[id.type]?.requireLogin ?: false
        val shouldLogin = requireLogin && !userSession.get().isLoggedIn
        if (shouldLogin) {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
                .putExtra(ApplinkConstInternalUserPlatform.PARAM_CALLBACK_REGISTER, ApplinkConstInternalUserPlatform.EXPLICIT_PERSONALIZE)
                .putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, SOURCE_ACCOUNT)

            this@NewMainParentActivity.intent.putTargetTabIdExtra(id)

            startActivity(intent)
        }

        return shouldLogin
    }

    private fun configureBackgroundBasedOnFragment(fragment: Fragment) {
        val isForceDarkMode = isFragmentForceDarkModeNavigationBar(fragment)
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                if (isForceDarkMode) darkModeContext else this,
                unifyprinciplesR.color.Unify_NN0
            )
        )
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

    private fun setupStatusBar() {
        // apply inset to allow recyclerview scrolling behind status bar
        binding.container.fitsSystemWindows = false
        binding.container.requestApplyInsets()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val flags = binding.root.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            binding.container.systemUiVisibility = flags
            window.statusBarColor = ContextCompat.getColor(this, unifyprinciplesR.color.Unify_NN0)
        }

        // make full transparent statusBar
        requestStatusBarLight()
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

        supportFragmentManager.commit(allowStateLoss = true) {
            run hideActiveFragment@{
                if (activeFragment == null) return@hideActiveFragment
                hide(activeFragment)
                activeFragment.userVisibleHint = false
                FragmentLifecycleObserver.onFragmentUnSelected(activeFragment)
            }
            if (!fragment.isAdded) {
                add(binding.container.id, fragment, itemId.value)
            }
            run showFragment@{
                show(fragment)
                fragment.userVisibleHint = true
                FragmentLifecycleObserver.onFragmentSelected(fragment)
            }
        }
    }

    private fun getCurrentActiveFragment(): Fragment? {
        return supportFragmentManager.fragments.firstOrNull {
            it.isAdded && it.isVisible
        }
    }

    private fun setupBottomNavigation() {
        binding.dynamicNavbar.setListener(object : BottomNavBarView.Listener {
            override fun onItemSelected(
                view: BottomNavBarView,
                model: BottomNavBarUiModel,
                isReselected: Boolean,
                isJumper: Boolean
            ): Boolean {
                return if (isReselected) {
                    onItemReselected(model, isJumper)
                    true
                } else {
                    val fragment = getFragmentById(model.uniqueId, model)
                    val success = fragment?.let { selectFragment(it, model.uniqueId) }
                    success == true
                }
            }
        })
    }

    private fun onItemReselected(model: BottomNavBarUiModel, isJumper: Boolean) {
        if (model.jumper != null) {
            onJumperPressed(model, isJumper)
        } else {
            scrollToTop(model.uniqueId)
        }
    }

    private fun onJumperPressed(model: BottomNavBarUiModel, isJumper: Boolean) {
        when (model.uniqueId) {
            BottomNavHomeId -> onHomeJumperPressed(model, isJumper)
            else -> {}
        }
    }

    private fun onHomeJumperPressed(model: BottomNavBarUiModel, isJumper: Boolean) {
        val activeFragment = getCurrentActiveFragment() ?: return
        if (activeFragment.tag != BottomNavHomeId.value) return
        if (activeFragment !is HomeScrollViewListener) return

        if (isJumper) {
            activeFragment.getRecommendationForYouIndex() ?: return
            activeFragment.onScrollToRecommendationForYou()
        } else {
            activeFragment.onScrollToHomeHeader()
        }
    }

    private fun scrollToTop(id: BottomNavItemId) {
        val fragment = getFragmentById(id) ?: return
        if (!fragment.userVisibleHint || fragment !is FragmentListener) return
        fragment.onScrollToTop()
    }

    private fun sendOpenHomeEvent(): Boolean {
        val value = DataLayer.mapOf(AppEventTracking.MOENGAGE.LOGIN_STATUS, userSession.get().isLoggedIn)
        TrackApp.getInstance().moEngage.sendTrackEvent(value, AppEventTracking.EventMoEngage.OPEN_BERANDA)
        return true
    }

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

    private suspend fun awaitNavData() = suspendCancellableCoroutine { cont ->
        fun sendDataOnce() {
            if (cont.isActive) { cont.resume(Unit) }
        }

        if (viewModel.dynamicBottomNav.value != null) {
            sendDataOnce()
        }
        val observer = Observer<List<BottomNavBarUiModel>?> {
            if (it == null) return@Observer
            sendDataOnce()
        }
        viewModel.dynamicBottomNav.observe(this, observer)

        cont.invokeOnCancellation { viewModel.dynamicBottomNav.removeObserver(observer) }
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
        shortcutManager.get().addShortcuts()
    }

    /**
     * This is duplicated by StatusBarUtil function
     */
    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes

        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }

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

    private fun updateAppLogPageData(tabId: BottomNavItemId, isFirstInit: Boolean) {
        val fragment = getFragmentById(tabId) ?: return
        if (userSession.get().isFirstTimeUser || fragment !is AppLogInterface) return
        val currentPageName = AppLogAnalytics.getCurrentData(PAGE_NAME)
        if (currentPageName == null || fragment.getPageName() != currentPageName.toString()) {
            AppLogAnalytics.pushPageData(fragment)
            AppLogAnalytics.putPageData(IS_MAIN_PARENT, true)
        }
        handleAppLogEnterMethod(fragment, isFirstInit)
    }

    private fun handleAppLogEnterMethod(appLogInterface: AppLogInterface, isFirstInit: Boolean) {
        if (isFirstInit) {
            AppLogAnalytics.putEnterMethod(EnterMethod.CLICK_APP_ICON)
        } else {
            val pageName = appLogInterface.getPageName()
            if (AppLogAnalytics.pageDataList.isEmpty()) return

            when (pageName) {
                PageName.HOME -> {
                    AppLogAnalytics.putEnterMethod(EnterMethod.CLICK_HOME_ICON)
                }
                PageName.WISHLIST -> {
                    AppLogAnalytics.putEnterMethod(EnterMethod.CLICK_WISHLIST_ICON)
                }
                else -> {}
            }
        }
    }

    private fun sendEnterPage(tabId: BottomNavItemId) {
        val fragment = getFragmentById(tabId) ?: return
        if (userSession.get().isFirstTimeUser || fragment !is AppLogInterface || !fragment.shouldTrackEnterPage()) return
        AppLogRecommendation.sendEnterPageAppLog()
    }

    private fun Intent.putTabIdExtra(tabId: BottomNavItemId): Intent {
        return putExtra(ARGS_TAB_TYPE, tabId.type.value)
            .putExtra(ARGS_DISCO_ID, tabId.discoId.value)
    }

    private fun Intent.getTabIdExtra(): BottomNavItemId? {
        val targetTabType = getStringExtra(ARGS_TAB_TYPE) ?: return null
        return BottomNavItemId(
            BottomNavBarItemType(targetTabType),
            DiscoId(getStringExtra(ARGS_DISCO_ID).orEmpty())
        )
    }

    private fun Intent.putTargetTabIdExtra(tabId: BottomNavItemId): Intent {
        return putExtra(ARGS_TARGET_TAB_TYPE, tabId.type.value)
            .putExtra(ARGS_TARGET_DISCO_ID, tabId.discoId.value)
    }

    private fun Intent.getTargetTabIdExtra(): BottomNavItemId? {
        val targetTabType = getStringExtra(ARGS_TARGET_TAB_TYPE) ?: return null
        return BottomNavItemId(
            BottomNavBarItemType(targetTabType),
            DiscoId(getStringExtra(ARGS_TARGET_DISCO_ID).orEmpty())
        )
    }

    private fun Intent.removeTargetTabIdExtra() {
        removeExtra(ARGS_TARGET_TAB_TYPE)
        removeExtra(ARGS_TARGET_DISCO_ID)
    }

    companion object {
        private const val ARGS_TAB_TYPE = "tab_type"
        private const val ARGS_DISCO_ID = "disco_id"

        private const val ARGS_TARGET_TAB_TYPE = "target_tab_type"
        private const val ARGS_TARGET_DISCO_ID = "target_disco_id"

        private const val ARGS_HAS_RUN_ON_RESUME_PLT = "has_run_on_resume_plt"
        internal const val SCROLL_RECOMMEND_LIST = "recommend_list"

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

        private const val EXIT_DELAY_MILLIS = 2_000L

        private const val SOURCE_ACCOUNT = "account"

        fun start(context: Context): Intent {
            return Intent(context, NewMainParentActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
