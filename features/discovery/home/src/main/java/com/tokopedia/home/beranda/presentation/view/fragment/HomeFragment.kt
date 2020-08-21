package com.tokopedia.home.beranda.presentation.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.analytics.performance.fpi.FpiPerformanceData
import com.tokopedia.analytics.performance.fpi.FragmentFramePerformanceIndexMonitoring
import com.tokopedia.analytics.performance.fpi.FragmentFramePerformanceIndexMonitoring.OnFrameListener
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.design.bottomsheet.BottomSheetView
import com.tokopedia.design.bottomsheet.BottomSheetView.BottomSheetField.BottomSheetFieldBuilder
import com.tokopedia.design.countdown.CountDownView.CountDownListener
import com.tokopedia.design.keyboard.KeyboardHelper
import com.tokopedia.design.keyboard.KeyboardHelper.OnKeyboardVisibilityChangedListener
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getBannerClick
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getBannerImpression
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getOverlayBannerClick
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getOverlayBannerImpression
import com.tokopedia.home.analytics.HomePageTrackingV2.RecommendationList.getAddToCartOnDynamicListCarousel
import com.tokopedia.home.analytics.HomePageTrackingV2.RecommendationList.getAddToCartOnDynamicListCarouselHomeComponent
import com.tokopedia.home.analytics.HomePageTrackingV2.RecommendationList.getCloseClickOnDynamicListCarousel
import com.tokopedia.home.analytics.HomePageTrackingV2.RecommendationList.getRecommendationListImpression
import com.tokopedia.home.analytics.HomePageTrackingV2.SprintSale.getSprintSaleImpression
import com.tokopedia.home.analytics.v2.CategoryWidgetTracking
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.analytics.v2.MixTopTracking.getMixTopViewIris
import com.tokopedia.home.analytics.v2.MixTopTracking.mapChannelToProductTracker
import com.tokopedia.home.analytics.v2.PopularKeywordTracking
import com.tokopedia.home.analytics.v2.ProductHighlightTracking.getProductHighlightImpression
import com.tokopedia.home.beranda.di.BerandaComponent
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeFlag
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.helper.ViewHelper
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_INFLATE_HOME_FRAGMENT
import com.tokopedia.home.beranda.helper.isSuccess
import com.tokopedia.home.beranda.listener.*
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitableDiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomepageBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCarouselCardDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.HomeRecyclerDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.DynamicChannelViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder.PopularKeywordListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.customview.NestedRecyclerView
import com.tokopedia.home.beranda.presentation.view.helper.*
import com.tokopedia.home.beranda.presentation.view.listener.*
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.constant.BerandaUrl
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home.widget.FloatingTextButton
import com.tokopedia.home.widget.ToggleableSwipeRefreshLayout
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics.Companion.getInstance
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.loyalty.view.activity.PromoListActivity
import com.tokopedia.navigation_common.listener.*
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.permissionchecker.PermissionCheckerHelper.PermissionCheckListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerWidgetType
import com.tokopedia.promogamification.common.floating.view.fragment.FloatingEggButtonFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.searchbar.HomeMainToolbar
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo.TickerDetail
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.make
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import com.tokopedia.weaver.Weaver.Companion.executeWeaveCoRoutineWithFirebase
import dagger.Lazy
import org.jetbrains.annotations.NotNull
import rx.Observable
import rx.schedulers.Schedulers
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * @author by errysuprayogi on 11/27/17.
 */
@SuppressLint("SyntheticAccessor")
open class HomeFragment : BaseDaggerFragment(),
        OnRefreshListener,
        HomeCategoryListener,
        CountDownListener,
        AllNotificationListener,
        FragmentListener,
        HomeEggListener,
        HomeTabFeedListener,
        HomeInspirationListener,
        HomeFeedsListener,
        HomeReviewListener,
        PopularKeywordListener,
        FramePerformanceIndexInterface,
        HomeAutoRefreshListener {

    companion object {
        private const val className = "com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment"
        private const val TOKOPOINTS_NOTIFICATION_TYPE = "drawer"
        private const val SCROLL_STATE_DRAG = 0
        private const val REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220
        private const val DEFAULT_WALLET_APPLINK_REQUEST_CODE = 111
        private const val REQUEST_CODE_REVIEW = 999
        private const val REQUEST_CODE_LOGIN_TOKOPOINTS = 120
        private const val VISITABLE_SIZE_WITH_DEFAULT_BANNER = 1
        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        private const val REVIEW_CLICK_AT = "rating"
        private const val UTM_SOURCE = "utm_source"
        private const val EXTRA_URL = "url"
        private const val EXTRA_TITLE = "core_web_view_extra_title"
        private const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        private const val EXTRA_TOTAL_VIEW = "EXTRA_TOTAL_VIEW"
        private const val EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID"
        private const val SEND_SCREEN_MIN_INTERVAL_MILLIS: Long = 1000
        private const val DEFAULT_UTM_SOURCE = "home_notif"
        private const val SEE_ALL_CARD = "android_mainapp_home_see_all_card_config"
        private const val REQUEST_CODE_PLAY_ROOM = 256
        private const val PERFORMANCE_PAGE_NAME_HOME = "home"
        private var lock = Object()
        private const val ENABLE_ASYNC_HOME_DAGGER = "android_async_home_dagger"

        var HIDE_TICKER = false
        private var HIDE_GEO = false
        private const val SOURCE_ACCOUNT = "account"
        private const val SCROLL_RECOMMEND_LIST = "recommend_list"
        private const val KEY_IS_LIGHT_THEME_STATUS_BAR = "is_light_theme_status_bar"
        private const val CLICK_TIME_INTERVAL: Long = 500

        @JvmStatic
        fun newInstance(scrollToRecommendList: Boolean): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putBoolean(SCROLL_RECOMMEND_LIST, scrollToRecommendList)
            fragment.arguments = args
            return fragment
        }
    }

    override val eggListener: HomeEggListener
        get() = this
    override val childsFragmentManager: FragmentManager
        get() = childFragmentManager

    override val userId: String
        get() = getHomeViewModel().getUserId()

    override val windowHeight: Int
        get() = if (activity != null) {
            root.height
        } else {
            0
        }

    @Inject
    lateinit var permissionCheckerHelper: Lazy<PermissionCheckerHelper>

    protected var trackingQueue: TrackingQueue? = null

    //Get Viewmodel using getHomeViewModel() method
    @Inject
    lateinit var viewModel: Lazy<HomeViewModel>
    private lateinit var remoteConfig: RemoteConfig
    private lateinit var userSession: UserSessionInterface
    private lateinit var root: FrameLayout
    private lateinit var refreshLayout: ToggleableSwipeRefreshLayout
    private lateinit var floatingTextButton: FloatingTextButton
    private lateinit var stickyLoginView: StickyLoginView
    private lateinit var onEggScrollListener: RecyclerView.OnScrollListener
    private lateinit var irisAnalytics: Iris
    private lateinit var irisSession: IrisSession
    private lateinit var statusBarBackground: View
    private lateinit var tickerDetail: TickerDetail
    private lateinit var sharedPrefs: SharedPreferences
    private var homeRecyclerView: NestedRecyclerView? = null
    private var homeMainToolbar: HomeMainToolbar? = null
    private var homeSnackbar: Snackbar? = null
    private var component: BerandaComponent? = null
    private var adapter: HomeRecycleAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var messageSnackbar: SnackbarRetry? = null
    private var activityStateListener: ActivityStateListener? = null
    private var mainParentStatusBarListener: MainParentStatusBarListener? = null
    private var homePerformanceMonitoringListener: HomePerformanceMonitoringListener? = null
    private var showRecomendation = false
    private var mShowTokopointNative = false
    private var showSeeAllCard = true
    private var isShowFirstInstallSearch = false
    private var scrollToRecommendList = false
    private var isFeedLoaded = false
    private var startToTransitionOffset = 0
    private var searchBarTransitionRange = 0
    private var lastSendScreenTimeMillis: Long = 0
    private var positionSticky: IntArray? = IntArray(2)
    private var isLightThemeStatusBar = true
    private val impressionScrollListeners: MutableMap<String, RecyclerView.OnScrollListener> = HashMap()
    private var mLastClickTime = System.currentTimeMillis()
    private val fragmentFramePerformanceIndexMonitoring = FragmentFramePerformanceIndexMonitoring()
    private var pageLoadTimeCallback: PageLoadTimePerformanceInterface? = null
    private var isOnRecylerViewLayoutAdded = false
    private var fragmentCreatedForFirstTime = false
    private var shouldPausePlay = true
    private var lock = Object()
    private var autoRefreshFlag = HomeFlag()
    private var autoRefreshHandler = Handler()
    private var autoRefreshRunnable: TimerRunnable = TimerRunnable(listener = this)
    private var serverOffsetTime: Long = 0L


    override fun onAttach(context: Context) {
        super.onAttach(context)
        createDaggerComponent()
        mainParentStatusBarListener = context as MainParentStatusBarListener
        homePerformanceMonitoringListener = castContextToHomePerformanceMonitoring(context)
        requestStatusBarDark()
    }

    private fun createDaggerComponent(){
        val enableAsyncDaggerCompInit = getRemoteConfig().getBoolean(ENABLE_ASYNC_HOME_DAGGER, false)
        if(enableAsyncDaggerCompInit) {
            var homeDaggerWeave = object : WeaveInterface {
                override fun execute(): Any {
                    return initHomePageFlows()
                }
            }
            Weaver.executeWeaveCoRoutineNow(homeDaggerWeave)
        }
        else{
            initHomePageFlows()
        }
    }

    private fun requestStatusBarDark() {
        isLightThemeStatusBar = false
        mainParentStatusBarListener?.requestStatusBarDark()
    }

    private fun requestStatusBarLight() {
        isLightThemeStatusBar = true
        mainParentStatusBarListener?.requestStatusBarLight()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCreatedForFirstTime = true
        searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.home_searchbar_transition_range)
        startToTransitionOffset = resources.getDimensionPixelSize(R.dimen.banner_background_height) / 2
    }

    fun callSubordinateTasks() {
        injectCouponTimeBased()
        setGeolocationPermission()
        needToShowGeolocationComponent()
        stickyContent
    }


    protected open fun initHomePageFlows():Boolean{
        initInjectorHome()
        return true
    }

    private fun getUserSession() : UserSessionInterface{
        if(!::userSession.isInitialized){
            activity?.let {
                userSession = UserSession(it)
            }
        }
        return userSession
    }

    private fun getIrisAnalytics() : Iris{
        if(!::irisAnalytics.isInitialized){
            activity?.let {
                irisAnalytics = getInstance(it)
            }
        }
        return irisAnalytics
    }

    private fun getIrisSession() : IrisSession{
        if(!::irisSession.isInitialized){
            activity?.let {
                irisSession = IrisSession(it)
            }
        }
        return irisSession
    }

    private fun getRemoteConfig() : RemoteConfig{
        if(!::remoteConfig.isInitialized){
            activity?.let {
                remoteConfig = FirebaseRemoteConfigImpl(it)
            }
        }
        return remoteConfig
    }

    override fun getTrackingQueueObj() : TrackingQueue?{
        if(trackingQueue == null){
            activity?.let {
                trackingQueue = TrackingQueue(it)
            }
        }
        return trackingQueue
    }

    override fun getScreenName(): String {
        return ConstantKey.Analytics.AppScreen.UnifyTracking.SCREEN_UNIFY_HOME_BERANDA
    }

    override fun initInjector() {
    }

    fun initInjectorHome() {
        synchronized(lock) {
            if (activity != null) {
                if (component == null) {
                    component = initBuilderComponent().build()
                    component!!.inject(this)
                }
            }
        }
    }

    protected open fun initBuilderComponent(): DaggerBerandaComponent.Builder {
        return DaggerBerandaComponent.builder().baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
    }

    private fun fetchRemoteConfig() {
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(activity)
        firebaseRemoteConfig.let {
            showRecomendation = it.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_RECOMENDATION_BUTTON, false)
            mShowTokopointNative = it.getBoolean(ConstantKey.RemoteConfigKey.APP_SHOW_TOKOPOINT_NATIVE, false)
            isShowFirstInstallSearch = it.getBoolean(ConstantKey.RemoteConfigKey.REMOTE_CONFIG_KEY_FIRST_INSTALL_SEARCH, false)
            showSeeAllCard = it.getBoolean(SEE_ALL_CARD, true)
        }
    }

    private fun castContextToHomePerformanceMonitoring(context: Context): HomePerformanceMonitoringListener? {
        return if (context is HomePerformanceMonitoringListener) {
            context
        } else null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        BenchmarkHelper.beginSystraceSection(TRACE_INFLATE_HOME_FRAGMENT)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        BenchmarkHelper.endSystraceSection()
        fragmentFramePerformanceIndexMonitoring.init(
                "home", this, object : OnFrameListener {
            override fun onFrameRendered(fpiPerformanceData: FpiPerformanceData) {}
        }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewLifecycleOwner.lifecycle.addObserver(fragmentFramePerformanceIndexMonitoring)
        }
        homeMainToolbar = view.findViewById(R.id.toolbar)
        homeMainToolbar?.setAfterInflationCallable(afterInflationCallable)
        statusBarBackground = view.findViewById(R.id.status_bar_bg)
        homeRecyclerView = view.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerView?.setHasFixedSize(true)
        refreshLayout = view.findViewById(R.id.home_swipe_refresh_layout)
        floatingTextButton = view.findViewById(R.id.recom_action_button)
        stickyLoginView = view.findViewById(R.id.sticky_login_text)
        root = view.findViewById(R.id.root)
        if (arguments != null) {
            scrollToRecommendList = arguments!!.getBoolean(SCROLL_RECOMMEND_LIST)
        }
        homeSnackbar = Snackbar.make(root, "", Snackbar.LENGTH_SHORT)
        fetchRemoteConfig()
        setupStatusBar()
        setupHomeRecyclerView()
        initEggDragListener()
        return view
    }

    private val afterInflationCallable: Callable<Any?>
        private get() = Callable<Any?> {
            calculateSearchbarView(0)
            observeSearchHint()
            null
        }

    private fun setupHomeRecyclerView() { //giving recyclerview larger cache to prevent lag, we can implement this because home dc content
//is finite
        homeRecyclerView?.setItemViewCacheSize(20)
        homeRecyclerView?.itemAnimator = null
        if (homeRecyclerView?.itemDecorationCount == 0) {
            homeRecyclerView?.addItemDecoration(HomeRecyclerDecoration(resources.getDimensionPixelSize(R.dimen.home_recyclerview_item_spacing)))
        }
        homeRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                evaluateHomeComponentOnScroll(recyclerView)
                //calculate transparency of homeMainToolbar based on rv offset
                calculateSearchbarView(recyclerView.computeVerticalScrollOffset())
            }
        })
    }

    private fun setupStatusBar() {
        activity?.let {
            statusBarBackground.background = ColorDrawable(
                    ContextCompat.getColor(it, R.color.green_600)
            )
        }
        //status bar background compability, we show view background for android >= Kitkat
//because in that version, status bar can't forced to dark mode, we must set background
//to keep status bar icon visible
        statusBarBackground.layoutParams.height = ViewHelper.getStatusBarHeight(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarBackground.visibility = View.INVISIBLE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBarBackground.visibility = View.VISIBLE
        } else {
            statusBarBackground.visibility = View.GONE
        }
        //initial condition for status and searchbar
        setStatusBarAlpha(0f)
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView) { //set refresh layout to only enabled when reach 0 offset
//because later we will disable scroll up for this parent recyclerview
//and makes refresh layout think we can't scroll up (which actually can! we only disable
//scroll so that feed recommendation section can scroll its content)
        if (recyclerView.computeVerticalScrollOffset() == 0) {
            refreshLayout.setCanChildScrollUp(false)
        } else {
            refreshLayout.setCanChildScrollUp(true)
        }
        if (recyclerView.canScrollVertically(1)) {
            if (homeMainToolbar != null && homeMainToolbar?.getViewHomeMainToolBar() != null) {
                homeMainToolbar?.showShadow()
            }
            showFeedSectionViewHolderShadow(false)
            homeRecyclerView?.setNestedCanScroll(false)
        } else { //home feed now can scroll up, so hide maintoolbar shadow
            if (homeMainToolbar != null && homeMainToolbar?.getViewHomeMainToolBar() != null) {
                homeMainToolbar?.hideShadow()
            }
            showFeedSectionViewHolderShadow(true)
            homeRecyclerView?.setNestedCanScroll(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initRefreshLayout()
        subscribeHome()
        initEggTokenScrollListener()
        registerBroadcastReceiverTokoCash()
        floatingTextButton.setOnClickListener { view: View? ->
            scrollToRecommendList()
            HomePageTracking.eventClickJumpRecomendation(activity)
        }
        KeyboardHelper.setKeyboardVisibilityChangedListener(root, object : OnKeyboardVisibilityChangedListener {
            override fun onKeyboardShown() {
                floatingTextButton.forceHide()
            }

            override fun onKeyboardHide() {
                floatingTextButton.resetState()
            }
        })
        stickyLoginView?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _  ->
            val floatingEggButtonFragment = floatingEggButtonFragment
            if (stickyLoginView.isShowing()) {
                positionSticky = stickyLoginView.getLocation()
            }
            floatingEggButtonFragment?.let {
                updateEggBottomMargin(it)
            }
        }
        stickyLoginView?.setOnClickListener { v: View? ->
            if (stickyLoginView.isLoginReminder()) {
                stickyLoginView?.trackerLoginReminder.clickOnLogin(StickyLoginConstant.Page.HOME)
            } else {
                stickyLoginView?.tracker.clickOnLogin(StickyLoginConstant.Page.HOME)
            }
            onGoToLogin()
        }
        stickyLoginView?.setOnDismissListener(View.OnClickListener { v: View? ->
            stickyLoginView?.dismiss(StickyLoginConstant.Page.HOME)
            if (stickyLoginView.isLoginReminder()) {
                stickyLoginView?.trackerLoginReminder.clickOnDismiss(StickyLoginConstant.Page.HOME)
            } else {
                stickyLoginView?.tracker.clickOnDismiss(StickyLoginConstant.Page.HOME)
            }
            val floatingEggButtonFragment = floatingEggButtonFragment
            floatingEggButtonFragment?.let { updateEggBottomMargin(it) }
        })
    }

    private fun scrollToRecommendList() {
        homeRecyclerView?.smoothScrollToPosition(getHomeViewModel().getRecommendationFeedSectionPosition())
        scrollToRecommendList = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LIGHT_THEME_STATUS_BAR, isLightThemeStatusBar)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            isLightThemeStatusBar = savedInstanceState.getBoolean(KEY_IS_LIGHT_THEME_STATUS_BAR)
        }
    }

    override fun onResume() {
        super.onResume()
        shouldPausePlay = true
        createAndCallSendScreen()
        if (!shouldPausePlay) adapter?.onResume()
        conditionalViewModelRefresh()
        if (activityStateListener != null) {
            activityStateListener!!.onResume()
        }
        adjustStatusBarColor()
        if (isEnableToAutoRefresh(autoRefreshFlag)) {
            setAutoRefreshOnHome(autoRefreshFlag)
        }
    }

    private fun conditionalViewModelRefresh(){
        if(!fragmentCreatedForFirstTime) {
            getHomeViewModel().refresh(isFirstInstall())
        }
    }

    private fun adjustStatusBarColor() {
        if (homeRecyclerView != null) {
            calculateSearchbarView(homeRecyclerView!!.computeVerticalScrollOffset())
        } else {
            calculateSearchbarView(0)
        }
    }

    private fun createAndCallSendScreen() {
        val sendScrWeave: WeaveInterface = object : WeaveInterface {
            override fun execute(): Any {
                return sendScreen()
            }
        }
        executeWeaveCoRoutineWithFirebase(sendScrWeave, RemoteConfigKey.ENABLE_ASYNC_HOME_SNDSCR, context?.applicationContext)
    }

    override fun onPause() {
        super.onPause()
        if(shouldPausePlay) adapter?.onPause()
        getTrackingQueueObj()?.sendAll()
        if (activityStateListener != null) {
            activityStateListener!!.onPause()
        }
        if (isEnableToAutoRefresh(autoRefreshFlag)) {
            stopAutoRefreshJob(autoRefreshHandler, autoRefreshRunnable)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.onDestroy()
        homeRecyclerView?.adapter = null
        adapter = null
        homeRecyclerView?.layoutManager = null
        layoutManager = null
        getHomeViewModel().onCloseChannel()
        unRegisterBroadcastReceiverTokoCash()
    }

    private fun initRefreshLayout() {
        refreshLayout.post {
            /*
             * set notification gimmick
             */if (homeMainToolbar != null && homeMainToolbar?.getViewHomeMainToolBar() != null) {
            homeMainToolbar?.setNotificationNumber(0)
        }
        }
        refreshLayout.setOnRefreshListener(this)
    }

    private fun subscribeHome() {
        observeHomeData()
        observeUpdateNetworkStatusData()
        observeOneClickCheckout()
        observePopupIntroOvo()
        observeErrorEvent()
        observeSendLocation()
        observeStickyLogin()
        observeTrackingData()
        observeRequestImagePlayBanner()
        observeViewModelInitialized()
        observeHomeRequestNetwork()
        observeSalamWidget()
        observeRechargeRecommendation()
        observePlayReminder()
        observeIsNeedRefresh()
    }
          
    private fun observeIsNeedRefresh() {
        getHomeViewModel().isNeedRefresh.observe(viewLifecycleOwner, Observer { data: Event<Boolean> ->
            val isNeedRefresh = data.peekContent()
            if (isNeedRefresh) {
                adapter?.resetImpressionHomeBanner()
            }
        })
    }

    private fun observeHomeRequestNetwork() {
        getHomeViewModel().isRequestNetworkLiveData.observe(viewLifecycleOwner, Observer { data: Event<Boolean> ->
            val isRequestNetwork = data.peekContent()
            if (isRequestNetwork && getPageLoadTimeCallback() != null) {
                getPageLoadTimeCallback()?.startNetworkRequestPerformanceMonitoring()
            } else if (getPageLoadTimeCallback() != null) {
                getPageLoadTimeCallback()?.stopNetworkRequestPerformanceMonitoring()
                getPageLoadTimeCallback()?.startRenderPerformanceMonitoring()
            }
        })
    }

    private fun observeViewModelInitialized() {
        getHomeViewModel().isViewModelInitialized.observe(viewLifecycleOwner, Observer { data: Event<Boolean> ->
            val isViewModelInitialized = data.peekContent()
            if (isViewModelInitialized) {
                callSubordinateTasks();
                if(getPageLoadTimeCallback() != null) {
                    getPageLoadTimeCallback()?.stopPreparePagePerformanceMonitoring()
                }
            }
        })
    }

    private fun observeErrorEvent() {
        getHomeViewModel().errorEventLiveData.observe(viewLifecycleOwner, Observer { data: Event<String?>? -> showToaster(getString(R.string.home_error_connection), TYPE_ERROR) })
    }

    private fun observeHomeData() {
        getHomeViewModel().homeLiveData.observe(this, Observer { data: HomeDataModel? ->
            if (data != null) {
                if (data.list.size > VISITABLE_SIZE_WITH_DEFAULT_BANNER) {
                    configureHomeFlag(data.homeFlag)
                    setData(data.list as List<HomeVisitable>, data.isCache)
                } else if (!data.isCache) {
                    showToaster(getString(R.string.home_error_connection), TYPE_ERROR)
                }
            }
        })
    }

    private fun observeUpdateNetworkStatusData() {
        getHomeViewModel().updateNetworkLiveData.observe(this, Observer { (status) ->
            resetImpressionListener()
            if (status === Result.Status.SUCCESS) {
                hideLoading()
            } else if (status === Result.Status.ERROR) {
                hideLoading()
                showToaster(getString(R.string.home_error_connection), TYPE_ERROR)
            } else {
                showLoading()
            }
        })
    }

    private fun observeTrackingData() {
        getHomeViewModel().trackingLiveData.observe(this, Observer<Event<List<HomeVisitable?>>> { trackingData: Event<List<HomeVisitable?>> ->
            val homeVisitables = trackingData.getContentIfNotHandled()
            homeVisitables?.let {
                val visitables: List<Visitable<*>> = it as List<Visitable<*>>
                addImpressionToTrackingQueue(visitables)
                setupViewportImpression(visitables)
            }
        })
    }

    private fun observeSearchHint() {
        if (view != null && ::viewModel.isInitialized && !getHomeViewModel().searchHint.hasObservers() && homeMainToolbar != null && homeMainToolbar?.getViewHomeMainToolBar() != null) {
            getHomeViewModel().searchHint.observe(viewLifecycleOwner, Observer { data: SearchPlaceholder -> setHint(data) })
        }
    }

    private fun observeOneClickCheckout() {
        getHomeViewModel().oneClickCheckout.observe(viewLifecycleOwner, Observer { event: Event<Any> ->
            val data = event.peekContent()
            if (data is Throwable) { // error
                showToaster(getString(R.string.home_error_connection), TYPE_ERROR)
            } else {
                val dataMap = data as Map<*, *>
                sendEETracking(getAddToCartOnDynamicListCarousel(
                        (dataMap[HomeViewModel.CHANNEL] as DynamicHomeChannel.Channels?)!!,
                        (dataMap[HomeViewModel.GRID] as DynamicHomeChannel.Grid?)!!,
                        dataMap[HomeViewModel.POSITION] as Int,
                        (dataMap[HomeViewModel.ATC] as AddToCartDataModel?)!!.data.cartId,
                        "0",
                        viewModel.get().getUserId()
                ) as HashMap<String, Any>)
                RouteManager.route(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
            }
        })

        viewModel.get().oneClickCheckoutHomeComponent.observe(viewLifecycleOwner, Observer { event: Event<Any> ->
            val data = event.peekContent()
            if (data is Throwable) { // error
                showToaster(getString(R.string.home_error_connection), TYPE_ERROR)
            } else {
                val dataMap = data as Map<*, *>
                sendEETracking(getAddToCartOnDynamicListCarouselHomeComponent(
                        (dataMap[HomeViewModel.CHANNEL] as ChannelModel),
                        (dataMap[HomeViewModel.GRID] as ChannelGrid),
                        dataMap[HomeViewModel.POSITION] as Int,
                        (dataMap[HomeViewModel.ATC] as AddToCartDataModel?)!!.data.cartId,
                        "0",
                        getHomeViewModel().getUserId()
                ) as HashMap<String, Any>)
                RouteManager.route(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
            }
        })
    }

    private fun observePlayReminder(){
        getHomeViewModel().reminderPlayLiveData.observe(this, Observer {
            if(it.isSuccess()){
                showToaster(
                        if(it.data == true) getString(R.string.home_page_play_card_success_add_reminder)
                        else getString(R.string.home_page_play_card_success_remove_reminder),
                        Toaster.TYPE_NORMAL
                )
            } else {
                showToaster(getString(R.string.home_error_connection), TYPE_ERROR)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearchHint()
    }

    private fun observeSendLocation() {
        getHomeViewModel().sendLocationLiveData.observe(viewLifecycleOwner, Observer { data: Event<Any?>? -> detectAndSendLocation() })
    }

    private fun observePopupIntroOvo() {
        getHomeViewModel().popupIntroOvoLiveData.observe(viewLifecycleOwner, Observer { data: Event<String?> ->
            if (RouteManager.isSupportApplink(activity, data.peekContent())) {
                val intentBalanceWallet = RouteManager.getIntent(activity, data.peekContent())
                Objects.requireNonNull(context)?.startActivity(intentBalanceWallet)
                activity?.let {
                    it.overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_page_stay)
                }
            }
        })
    }

    private fun observeStickyLogin() {
        getHomeViewModel().stickyLogin.observe(viewLifecycleOwner, Observer { (status, data) ->
            if (status === Result.Status.SUCCESS) {
                setStickyContent(data!!)
            } else {
                hideStickyLogin()
            }
        })
    }

    @VisibleForTesting
    private fun observeRequestImagePlayBanner() {
        context?.let {
            getHomeViewModel().requestImageTestLiveData.observe(this, Observer { playCardViewModelEvent: Event<PlayCardDataModel> ->
                Glide.with(it)
                        .asBitmap()
                        .load(playCardViewModelEvent.peekContent().playCardHome?.coverUrl)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                getHomeViewModel().setPlayBanner(playCardViewModelEvent.peekContent())
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                getHomeViewModel().clearPlayBanner()
                            }
                        })
            })
        }
    }

    private fun observeSalamWidget(){
        context?.let{
            getHomeViewModel().salamWidgetLiveData.observe(this, Observer {
                getHomeViewModel().insertSalamWidget(it.peekContent())
            })
        }
    }

    private fun observeRechargeRecommendation(){
        context?.let {
            getHomeViewModel().rechargeRecommendationLiveData.observe(this, Observer {
                getHomeViewModel().insertRechargeRecommendation(it.peekContent())
            })
        }
    }

    private fun setData(data: List<HomeVisitable?>, isCache: Boolean) {
        if(!data.isEmpty()) {
            if (needToPerformanceMonitoring() && getPageLoadTimeCallback() != null) {
                setOnRecyclerViewLayoutReady(isCache);
                adapter?.submitList(data);
            } else {
                adapter?.submitList(data);
            }

            if (isDataValid(data)) {
                removeNetworkError();
            } else {
                showToaster(getString(R.string.home_error_connection), TYPE_ERROR);
            }
        }
    }

    private fun isDataValid(visitables: List<HomeVisitable?>): Boolean {
        return containsInstance(visitables, HomepageBannerDataModel::class.java)
    }

    private fun <T> containsInstance(list: List<T>, type: Class<*>): Boolean {
        val instance = list.filterIsInstance(type)
        return instance.isNotEmpty()
    }

    private fun loadEggData() {
        val floatingEggButtonFragment = floatingEggButtonFragment
        if (floatingEggButtonFragment != null) {
            updateEggBottomMargin(floatingEggButtonFragment)
            floatingEggButtonFragment.loadEggData()
        }
    }

    private fun calculateSearchbarView(offset: Int) {
        val endTransitionOffset = startToTransitionOffset + searchBarTransitionRange
        val maxTransitionOffset = endTransitionOffset - startToTransitionOffset
        //mapping alpha to be rendered per pixel for x height
        var offsetAlpha = 255f / maxTransitionOffset * (offset - startToTransitionOffset)
        //2.5 is maximum
        if (offsetAlpha < 0) {
            offsetAlpha = 0f
        }
        if (homeMainToolbar != null && homeMainToolbar?.getViewHomeMainToolBar() != null) {
            if (offsetAlpha >= 150) {
                homeMainToolbar?.switchToDarkToolbar()
                if (isLightThemeStatusBar) requestStatusBarDark()
            } else {
                homeMainToolbar?.switchToLightToolbar()
                if (!isLightThemeStatusBar) requestStatusBarLight()
            }
        }
        if (offsetAlpha >= 255) {
            offsetAlpha = 255f
        }
        if (homeMainToolbar != null && homeMainToolbar?.getViewHomeMainToolBar() != null) {
            if (offsetAlpha >= 0 && offsetAlpha <= 255) {
                homeMainToolbar?.setBackgroundAlpha(offsetAlpha)
                setStatusBarAlpha(offsetAlpha)
            }
        }
    }

    private fun setStatusBarAlpha(alpha: Float) {
        val drawable = statusBarBackground.background
        drawable.alpha = alpha.toInt()
        statusBarBackground.background = drawable
    }

    private fun hideEggFragmentOnScrolling() {
        val floatingEggButtonFragment = floatingEggButtonFragment
        floatingEggButtonFragment?.hideOnScrolling()
    }

    override fun isShowSeeAllCard(): Boolean {
        return showSeeAllCard
    }

    private fun initEggTokenScrollListener() {
        onEggScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy == 0) {
                    return
                }
                val floatingEggButtonFragment = floatingEggButtonFragment
                if (floatingEggButtonFragment != null) {
                    updateEggBottomMargin(floatingEggButtonFragment)
                    floatingEggButtonFragment.hideOnScrolling()
                }
            }
        }
        homeRecyclerView?.removeOnScrollListener(onEggScrollListener)
        homeRecyclerView?.addOnScrollListener(onEggScrollListener)
    }

    // https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
    private val floatingEggButtonFragment: FloatingEggButtonFragment?
        get() =// https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
            if (activity != null && isAdded && childsFragmentManager != null) {
                childFragmentManager.findFragmentById(R.id.floating_egg_fragment) as FloatingEggButtonFragment?
            } else null

    private fun initAdapter() {
        layoutManager = LinearLayoutManager(context)
        homeRecyclerView?.layoutManager = layoutManager
        val adapterFactory = HomeAdapterFactory(
                this,
                this,
                this,
                this,
                this,
                homeRecyclerView?.recycledViewPool?: RecyclerView.RecycledViewPool(),
                this,
                HomeComponentCallback(getHomeViewModel()),
                DynamicLegoBannerComponentCallback(context, this),
                RecommendationListCarouselComponentCallback(getHomeViewModel(), this),
                MixLeftComponentCallback(this),
                MixTopComponentCallback(this),
                HomeReminderWidgetCallback(RechargeRecommendationCallback(context,getHomeViewModel(),this),
                        SalamWidgetCallback(context,getHomeViewModel(),this, getUserSession())),
                ProductHighlightComponentCallback(this),
                Lego4AutoBannerComponentCallback(context, this)
        )
        val asyncDifferConfig = AsyncDifferConfig.Builder(HomeVisitableDiffUtil())
                .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
                .build()
        adapter = HomeRecycleAdapter(asyncDifferConfig, adapterFactory, ArrayList<HomeVisitable>())
        homeRecyclerView?.adapter = adapter
    }

    override fun onSectionItemClicked(actionLink: String) {
        onActionLinkClicked(actionLink)
    }

    override fun onSpotlightItemClicked(actionLink: String) {
        onActionLinkClicked(actionLink)
    }

    private fun configureHomeFlag(homeFlag: HomeFlag) {
        floatingTextButton.visibility = if (homeFlag.getFlag(HomeFlag.TYPE.HAS_RECOM_NAV_BUTTON) && showRecomendation) View.VISIBLE else View.GONE
        initAutoRefreshHandler()
        if (isEnableToAutoRefresh(homeFlag)) {
            autoRefreshFlag = homeFlag
            serverOffsetTime = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(homeFlag.serverTime)
            setAutoRefreshOnHome(homeFlag)
        }
    }

    private fun isEnableToAutoRefresh(homeFlag: HomeFlag): Boolean {
        return homeFlag.getFlag(HomeFlag.TYPE.IS_AUTO_REFRESH)
                && homeFlag.serverTime != 0L
                && homeFlag.eventTime.isNotEmpty()
    }

    private fun initAutoRefreshHandler() {
        stopAutoRefreshJob(autoRefreshHandler, autoRefreshRunnable)
        autoRefreshRunnable = TimerRunnable(listener = this)
        autoRefreshHandler = Handler()
    }

    private fun setAutoRefreshOnHome(autoRefreshFlag: HomeFlag)  {
        initAutoRefreshHandler()
        val expiredTime = DateHelper.getExpiredTime(autoRefreshFlag.eventTime)
        autoRefreshRunnable = getAutoRefreshRunnableThread(serverOffsetTime, expiredTime, autoRefreshHandler, this)
        runAutoRefreshJob(autoRefreshHandler, autoRefreshRunnable)
    }

    override fun onHomeAutoRefreshTriggered() {
        doHomeDataRefresh()
    }

    private fun doHomeDataRefresh() {
        getHomeViewModel().refreshHomeData()
    }

    private fun onGoToSell() {
        if (isUserLoggedIn) {
            val shopId = userShopId
            if (shopId != "0") {
                onGoToShop(shopId)
            } else {
                onGoToCreateShop()
            }
        } else {
            onGoToLogin()
        }
    }

    private fun onGoToLogin() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, SOURCE_ACCOUNT)
        val intentHome = RouteManager.getIntent(activity, ApplinkConst.HOME)
        intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity?.let {
            it.startActivities(arrayOf(intentHome, intent))
            it.finish()
        }
    }

    private fun onGoToCreateShop() {
        val intent = RouteManager.getIntent(activity, ApplinkConst.CREATE_SHOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity?.startActivity(intent)
    }

    private fun onGoToShop(shopId: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.SHOP)
        intent.putExtra(EXTRA_SHOP_ID, shopId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity?.startActivity(intent)
    }

    override fun openShop() {
        onGoToSell()
    }

    override fun actionAppLinkWalletHeader(appLinkBalance: String) {
        goToOvo(appLinkBalance)
    }

    private fun goToOvo(appLinkScheme: String?) {
        val intent = if (appLinkScheme == null || appLinkScheme.isEmpty()) RouteManager.getIntent(activity, ApplinkConst.WEBVIEW).putExtra("EXTRA_URL", appLinkScheme) else if (RouteManager.isSupportApplink(activity, appLinkScheme)) RouteManager.getIntent(activity, appLinkScheme).setData(Uri.parse(appLinkScheme)) else RouteManager.getIntent(activity, ApplinkConst.WEBVIEW).putExtra("EXTRA_URL", appLinkScheme)
        startActivityForResult(intent, DEFAULT_WALLET_APPLINK_REQUEST_CODE)
    }

    override fun onRequestPendingCashBack() {
        getHomeViewModel().getTokocashPendingBalance()
    }

    override fun actionInfoPendingCashBackTokocash(cashBackData: CashBackData,
                                                   appLinkActionButton: String) {
        activity?.let {
            val bottomSheetDialogTokoCash = BottomSheetView(it)
            bottomSheetDialogTokoCash.setListener(object : BottomSheetView.ActionListener {
                override fun clickOnTextLink(url: String) {}
                override fun clickOnButton(url: String, appLink: String) {
                    goToOvo(appLink)
                }
            })
            bottomSheetDialogTokoCash.renderBottomSheet(BottomSheetFieldBuilder()
                    .setTitle(getString(R.string.toko_cash_pending_title))
                    .setBody(String.format(getString(R.string.toko_cash_pending_body),
                            cashBackData.amountText))
                    .setImg(R.drawable.ic_box)
                    .setUrlButton("",
                            appLinkActionButton,
                            getString(R.string.toko_cash_pending_proceed_button))
                    .build())
            bottomSheetDialogTokoCash.show()
        }
    }

    override fun actionTokoPointClicked(appLink: String, tokoPointUrl: String, pageTitle: String) {
        if (mShowTokopointNative) {
            openApplink(appLink, "")
        } else {
            if (TextUtils.isEmpty(pageTitle)) RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW, tokoPointUrl) else RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW_TITLE, pageTitle, tokoPointUrl)
        }
    }

    override fun onPageDragStateChanged(isDragged: Boolean) {
        if (refreshLayout != null) {
            refreshLayout.isEnabled = !isDragged
        }
    }

    override fun onPromoClick(position: Int, slidesModel: BannerSlidesModel) { // tracking handler
        if (slidesModel.type == BannerSlidesModel.TYPE_BANNER_PERSO) {
            sendEETracking(getOverlayBannerClick(slidesModel) as HashMap<String, Any>)
        } else {
            sendEETracking(getBannerClick(slidesModel) as HashMap<String, Any>)
        }
        HomeTrackingUtils.homeSlidingBannerClick(context, slidesModel, position)
        if (activity != null && RouteManager.isSupportApplink(activity, slidesModel.applink)) {
            openApplink(slidesModel.applink, "")
        } else {
            openWebViewURL(slidesModel.redirectUrl, activity)
        }
        if (slidesModel.redirectUrl.isNotEmpty()) {
            TopAdsUrlHitter(className).hitClickUrl(getContext(),
                    slidesModel.redirectUrl, slidesModel.id.toString(),
                    slidesModel.title + " : " + slidesModel.creativeName,
                    slidesModel.imageUrl)
        }
    }

    override fun onPromoAllClick() {
        HomePageTracking.eventClickViewAllPromo(activity)
        HomeTrackingUtils.homeViewAllPromotions(activity, "PromoListActivity")
        val remoteConfigEnable: Boolean
        val remoteConfig = FirebaseRemoteConfigImpl(activity)
        remoteConfigEnable = remoteConfig.getBoolean(
                ConstantKey.RemoteConfigKey.MAINAPP_NATIVE_PROMO_LIST
        )
        if (activity != null && remoteConfigEnable) {
            activity?.startActivity(PromoListActivity.newInstance(
                    activity,
                    PromoListActivity.DEFAULT_AUTO_SELECTED_MENU_ID,
                    PromoListActivity.DEFAULT_AUTO_SELECTED_CATEGORY_ID
            ))
        } else {
            if (activity != null) {
                showBannerWebViewOnAllPromoClickFromHomeIntent(BerandaUrl.PROMO_URL + BerandaUrl.FLAG_APP, getString(R.string.title_activity_promo))
            }
        }
    }

    private fun showBannerWebViewOnAllPromoClickFromHomeIntent(url: String, title: String) {
        val intent = RouteManager.getIntent(activity, ApplinkConst.PROMO)
        intent.putExtra(EXTRA_URL, url)
        intent.putExtra(EXTRA_TITLE, title)
        startActivity(intent)
    }

    override fun onCloseTicker() {
        HIDE_TICKER = true
        getHomeViewModel().onCloseTicker()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DIGITAL_PRODUCT_DETAIL -> if (data != null && data.hasExtra(EXTRA_MESSAGE)) {
                val message = data.getStringExtra(EXTRA_MESSAGE)
                if (!TextUtils.isEmpty(message)) {
                    NetworkErrorHelper.showSnackbar(activity, message)
                }
            }
            REQUEST_CODE_REVIEW -> {
                adapter?.notifyDataSetChanged()
                if (resultCode == Activity.RESULT_OK) {
                    getHomeViewModel().onRemoveSuggestedReview()
                }
            }
            REQUEST_CODE_PLAY_ROOM -> {
                if (data != null && data.hasExtra(EXTRA_TOTAL_VIEW) && data.hasExtra(EXTRA_CHANNEL_ID)) viewModel.get().updateBannerTotalView(data.getStringExtra(EXTRA_CHANNEL_ID), data.getStringExtra(EXTRA_TOTAL_VIEW))
            }
        }
    }

    override fun onRefresh() { //on refresh most likely we already lay out many view, then we can reduce
//animation to keep our performance
        adapter?.resetImpressionHomeBanner()
        resetFeedState()
        removeNetworkError()
        if (this::viewModel.isInitialized) {
            getHomeViewModel().getSearchHint(isFirstInstall())
            getHomeViewModel().refreshHomeData()
            stickyContent
        }
        if (activity is RefreshNotificationListener) {
            (activity as RefreshNotificationListener?)?.onRefreshNotification()
        }
        loadEggData()
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE)
    }

    private fun onNetworkRetry() { //on refresh most likely we already lay out many view, then we can reduce
//animation to keep our performance
        homeRecyclerView?.itemAnimator = null
        resetFeedState()
        removeNetworkError()
        homeRecyclerView?.isEnabled = false
        if(::viewModel.isInitialized) {
            getHomeViewModel().refresh(isFirstInstall())
            stickyContent
        }
        if (activity is RefreshNotificationListener) {
            (activity as RefreshNotificationListener?)?.onRefreshNotification()
        }
        loadEggData()
        fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE)
    }

    private fun resetFeedState() {
        isFeedLoaded = false
    }

    override fun onCountDownFinished() {
        getHomeViewModel().refreshHomeData()
    }

    private fun showLoading() {
        refreshLayout.isRefreshing = true
    }

    private val stickyContent: Unit
        private get(){
            val stickyContentWeave: WeaveInterface = object : WeaveInterface {
                @NotNull
                override fun execute(): Any {
                    return executeGetStickyContent()
                }
            }
            Weaver.executeWeaveCoRoutineNow(stickyContentWeave)
        }

    private fun executeGetStickyContent():Boolean{
        val isShowSticky = getRemoteConfig().getBoolean(StickyLoginConstant.KEY_STICKY_LOGIN_WIDGET_HOME, true)
        if (isShowSticky && !getUserSession().isLoggedIn) getHomeViewModel().getStickyContent()
        return true
    }

    private fun injectCouponTimeBased() {
         if(getUserSession().isLoggedIn) getHomeViewModel().injectCouponTimeBased();
     }

    private fun hideLoading() {
        refreshLayout.isRefreshing = false
        homeRecyclerView?.isEnabled = true
    }

    private fun setOnRecyclerViewLayoutReady(isCache: Boolean) {
        isOnRecylerViewLayoutAdded = true
        homeRecyclerView?.addOneTimeGlobalLayoutListener {
            homePerformanceMonitoringListener?.stopHomePerformanceMonitoring(isCache);
            homePerformanceMonitoringListener = null;
            fetchTokopointsNotification(TOKOPOINTS_NOTIFICATION_TYPE)
            if(fragmentCreatedForFirstTime){
                fragmentCreatedForFirstTime = false
                conditionalViewModelRefresh()
            }
        }
    }

    private fun needToShowGeolocationComponent() {
        val firebaseShowGeolocationComponent = getRemoteConfig().getBoolean(RemoteConfigKey.SHOW_HOME_GEOLOCATION_COMPONENT, true)
        if (!firebaseShowGeolocationComponent) {
            getHomeViewModel().setNeedToShowGeolocationComponent(false)
            return
        }
        var needToShowGeolocationComponent = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                val userHasDeniedPermissionBefore = ActivityCompat
                        .shouldShowRequestPermissionRationale(it,
                                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION)
                if (userHasDeniedPermissionBefore) {
                    getHomeViewModel().setNeedToShowGeolocationComponent(false)
                    return
                }
            }
        }
        if (activity != null) {
            if (getHomeViewModel().hasGeolocationPermission()) {
                needToShowGeolocationComponent = false
            }
        }
        if (needToShowGeolocationComponent && HIDE_GEO) {
            getHomeViewModel().setNeedToShowGeolocationComponent(false)
            return
        }
        getHomeViewModel().setNeedToShowGeolocationComponent(needToShowGeolocationComponent)
    }

    private fun setGeolocationPermission() {
        if (activity == null) getHomeViewModel().setGeolocationPermission(false)
        else getHomeViewModel().setGeolocationPermission(permissionCheckerHelper.get().hasPermission(
                activity!!,
                arrayOf(PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION)))
    }

    private fun promptGeolocationPermission() {
        permissionCheckerHelper.get().checkPermission(this,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                object : PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        HomePageTracking.eventClickNotAllowGeolocation(activity)
                        getHomeViewModel().onCloseGeolocation()
                        showNotAllowedGeolocationSnackbar()
                    }

                    override fun onNeverAskAgain(permissionText: String) {}
                    override fun onPermissionGranted() {
                        HomePageTracking.eventClickAllowGeolocation(activity)
                        detectAndSendLocation()
                        getHomeViewModel().onCloseGeolocation()
                        showAllowedGeolocationSnackbar()
                    }
                }, "")
    }

    private fun detectAndSendLocation() {
        activity?.let {
            Observable.just(true).map { aBoolean: Boolean? ->
                val locationDetectorHelper = LocationDetectorHelper(
                        permissionCheckerHelper.get(),
                        LocationServices.getFusedLocationProviderClient(it.getApplicationContext()),
                        it.applicationContext)
                locationDetectorHelper.getLocation(onGetLocation(), it,
                        LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                        "")
                true
            }.subscribeOn(Schedulers.io()).subscribe({ aBoolean: Boolean? -> }) { throwable: Throwable? -> }
        }
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude) ->
            saveLocation(activity, latitude, longitude)
            getHomeViewModel().sendGeolocationData()
            null
        }
    }

    fun saveLocation(context: Context?, latitude: Double, longitude: Double) {
        val editor: SharedPreferences.Editor
        if (context != null && !TextUtils.isEmpty(ConstantKey.LocationCache.KEY_LOCATION)) {
            sharedPrefs = context.getSharedPreferences(ConstantKey.LocationCache.KEY_LOCATION, Context.MODE_PRIVATE)
            editor = sharedPrefs.edit()
        } else {
            return
        }
        editor.putString(ConstantKey.LocationCache.KEY_LOCATION_LAT, latitude.toString())
        editor.putString(ConstantKey.LocationCache.KEY_LOCATION_LONG, longitude.toString())
        editor.apply()
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs.edit().putLong(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_TIME_SEARCH, 0).apply()
        }
    }

    private fun isFirstInstall(): Boolean {
        context?.let {
            if (!getUserSession().isLoggedIn &&
                    isShowFirstInstallSearch) {
                sharedPrefs = it.getSharedPreferences(
                        ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
                var firstInstallCacheValue = sharedPrefs.getLong(
                        ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_TIME_SEARCH, 0)
                if (firstInstallCacheValue == 0L) return false
                firstInstallCacheValue += (30 * 60000).toLong()
                val now = Date()
                val firstInstallTime = Date(firstInstallCacheValue)
                if (now.compareTo(firstInstallTime) <= 0) {
                    return true
                } else {
                    saveFirstInstallTime()
                    return false
                }
            } else {
                return false
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.let {
            permissionCheckerHelper.get().onRequestPermissionsResult(it, requestCode, permissions, grantResults)
        }
    }

    private fun setHint(searchPlaceholder: SearchPlaceholder) {
        if (searchPlaceholder.data != null && searchPlaceholder.data.placeholder != null && searchPlaceholder.data.keyword != null) {
            homeMainToolbar?.setHint(
                    searchPlaceholder.data.placeholder,
                    searchPlaceholder.data.keyword,
                    isFirstInstall())
        }
    }

    private fun addImpressionToTrackingQueue(visitables: List<Visitable<*>>) {
        if (visitables != null) {
            val combinedTracking: MutableList<Any> = ArrayList()
            for (visitable in visitables) {
                if (visitable is HomeVisitable) {
                    val homeVisitable = visitable
                    if (homeVisitable.isTrackingCombined && homeVisitable.trackingDataForCombination != null) {
                        getTrackingQueueObj()?.putEETracking(
                                LegoBannerTracking.getHomeBannerImpression(homeVisitable.trackingDataForCombination) as HashMap<String, Any>
                        )
                    } else if (!homeVisitable.isTrackingCombined && homeVisitable.trackingData != null) {
                        HomePageTracking.eventEnhancedImpressionWidgetHomePage(getTrackingQueueObj(), homeVisitable.trackingData)
                    }
                }
            }
            if (!combinedTracking.isEmpty()) {
                HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(getTrackingQueueObj(), combinedTracking)
            }
        }
    }

    override fun showNetworkError(message: String) {
        if (isAdded && activity != null && adapter != null) {
            if (adapter!!.itemCount > 0) {
                if (messageSnackbar == null) {
                    messageSnackbar = NetworkErrorHelper.createSnackbarWithAction(
                            activity, getString(R.string.msg_network_error)) { onNetworkRetry() }
                }
                messageSnackbar?.showRetrySnackbar()
            } else {
                NetworkErrorHelper.showEmptyState(activity, root, message) { onRefresh() }
            }
        }
    }

    override fun onDynamicChannelClicked(actionLink: String) {
        onActionLinkClicked(actionLink)
    }

    override fun updateExpiredChannel(dynamicChannelDataModel: DynamicChannelDataModel, position: Int) {
        getHomeViewModel().getDynamicChannelData(dynamicChannelDataModel, position)
    }

    override fun onBuyAgainCloseChannelClick(channel: DynamicHomeChannel.Channels, position: Int) {
        getHomeViewModel().onCloseBuyAgain(channel.id, position)
        TrackApp.getInstance().gtm.sendGeneralEvent(getCloseClickOnDynamicListCarousel(channel, getHomeViewModel().getUserId()))
    }

    private fun onActionLinkClicked(actionLink: String, trackingAttribution: String = "") {
        val now = System.currentTimeMillis()
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return
        }
        mLastClickTime = now
        if (TextUtils.isEmpty(actionLink)) {
            return
        }
        if (activity != null
                && RouteManager.isSupportApplink(activity, actionLink)) {
            openApplink(actionLink, trackingAttribution)
        } else {
            openWebViewURL(actionLink, activity)
        }
    }

    override fun onPlayBannerCarouselRefresh(playCarouselCardDataModel: PlayCarouselCardDataModel, position: Int) {
        getHomeViewModel().getPlayBannerCarousel(position)
    }

    override fun onPlayBannerReminderClick(playBannerCarouselItemDataModel: PlayBannerCarouselItemDataModel) {
        getHomeViewModel().setToggleReminderPlayBanner(playBannerCarouselItemDataModel.channelId, playBannerCarouselItemDataModel.remindMe)
    }

    override fun onPlayV2Click(playBannerCarouselItemDataModel: PlayBannerCarouselItemDataModel) {
        if(playBannerCarouselItemDataModel.widgetType != PlayBannerWidgetType.UPCOMING && playBannerCarouselItemDataModel.widgetType != PlayBannerWidgetType.NONE) {
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalContent.PLAY_DETAIL, playBannerCarouselItemDataModel.channelId)
            startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM)
        } else {
            RouteManager.route(context, playBannerCarouselItemDataModel.applink)
        }
    }

    private fun openApplink(applink: String, trackingAttribution: String) {
        var applink = applink
        if (!TextUtils.isEmpty(applink)) {
            applink = appendTrackerAttributionIfNeeded(applink, trackingAttribution)
            RouteManager.route(activity, applink)
        }
    }

    private fun appendTrackerAttributionIfNeeded(applink: String, trackingAttribution: String): String {
        var trackingAttribution = trackingAttribution
        if (TextUtils.isEmpty(trackingAttribution)) {
            return applink
        }
        trackingAttribution = try {
            URLEncoder.encode(trackingAttribution, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            trackingAttribution.replace(" ".toRegex(), "%20")
        }
        return if (applink.contains("?") || applink.contains("%3F") || applink.contains("%3f")) {
            "$applink&tracker_attribution=$trackingAttribution"
        } else {
            "$applink?tracker_attribution=$trackingAttribution"
        }
    }

    private fun removeNetworkError() {
        NetworkErrorHelper.removeEmptyState(root)
        if (messageSnackbar != null && messageSnackbar!!.isShown) {
            messageSnackbar?.hideRetrySnackbar()
            messageSnackbar = null
        }
    }

    override fun removeViewHolderAtPosition(position: Int) {
        getHomeViewModel().removeViewHolderAtPosition(position)
    }

    fun openWebViewURL(url: String?, context: Context?) {
        if (!TextUtils.isEmpty(url) && context != null) {
            val intent = RouteManager.getIntent(context, ApplinkConst.PROMO)
            intent.putExtra(EXTRA_URL, url)
            try {
                startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                exception.printStackTrace()
            }
        }
    }

    override fun getTabBusinessWidget(position: Int) {
        getHomeViewModel().getBusinessUnitTabData(position)
    }

    override fun getBusinessUnit(tabId: Int, position: Int, tabName: String) {
        getHomeViewModel().getBusinessUnitData(tabId, position, tabName)
    }

    override fun getPlayChannel(position: Int) {
        getHomeViewModel().getPlayBanner(position)
    }

    override fun onRefreshTokoPointButtonClicked() {
        getHomeViewModel().onRefreshTokoPoint()
    }

    override fun onRefreshTokoCashButtonClicked() {
        getHomeViewModel().onRefreshTokoCash()
    }

    override fun onLegoBannerClicked(actionLink: String, trackingAttribution: String) {
        onActionLinkClicked(actionLink, trackingAttribution)
    }

    override fun onPromoScrolled(bannerSlidesModel: BannerSlidesModel) {
        HomeTrackingUtils.homeSlidingBannerImpression(context, bannerSlidesModel, bannerSlidesModel.position)
        if (bannerSlidesModel.type == BannerSlidesModel.TYPE_BANNER_PERSO) {
            putEEToTrackingQueue(getOverlayBannerImpression(bannerSlidesModel) as HashMap<String, Any>)
        } else {
            if (bannerSlidesModel.topadsViewUrl.isNotEmpty()) {
                TopAdsUrlHitter(className).hitImpressionUrl(context, bannerSlidesModel.topadsViewUrl,
                        bannerSlidesModel.id.toString(),
                        bannerSlidesModel.title + " : " + bannerSlidesModel.creativeName,
                        bannerSlidesModel.imageUrl)
            }
            val dataLayer = getBannerImpression(bannerSlidesModel) as HashMap<String, Any>
            dataLayer[KEY_SESSION_IRIS] = getIrisSession().getSessionId()
            putEEToTrackingQueue(dataLayer)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        resetAutoPlay(isVisibleToUser)
        trackScreen(isVisibleToUser)
        conditionalViewModelRefresh()
    }

    private fun restartBanner(isVisibleToUser: Boolean) {
        if (isVisibleToUser && view != null && adapter != null) {
            adapter?.notifyDataSetChanged()
        }
    }

    private fun resetAutoPlay(isVisibleToUser: Boolean){
        shouldPausePlay = !isVisibleToUser
        if(shouldPausePlay && view != null && adapter != null) adapter?.onPause()
        else adapter?.onResume()
    }

    private fun trackScreen(isVisibleToUser: Boolean) {
        if (isVisibleToUser && isAdded && activity != null) {
            createAndCallSendScreen()
        }
    }

    private fun sendScreen(): Boolean {
        if (activity != null && System.currentTimeMillis() > lastSendScreenTimeMillis + SEND_SCREEN_MIN_INTERVAL_MILLIS) {
            lastSendScreenTimeMillis = System.currentTimeMillis()
            HomePageTracking.sendScreen(activity, screenName, getUserSession().isLoggedIn)
        }
        return true
    }

    override val isMainViewVisible: Boolean
        get() = userVisibleHint

    override val parentPool: RecyclerView.RecycledViewPool
        get() = homeRecyclerView?.recycledViewPool?: RecyclerView.RecycledViewPool()

    override val isHomeFragment: Boolean
        get() {
            if (activity == null) {
                return false
            }
            val fragment = activity!!.supportFragmentManager.findFragmentById(R.id.container)
            return fragment is HomeFragment
        }

    override fun setActivityStateListener(activityStateListener: ActivityStateListener) {
        this.activityStateListener = activityStateListener
    }

    override fun onScrollToTop() {
        homeRecyclerView?.smoothScrollToPosition(0)
    }

    override fun isLightThemeStatusBar(): Boolean {
        return isLightThemeStatusBar
    }

    override fun sendIrisTrackerHashMap(tracker: HashMap<String, Any>) {
        putEEToIris(tracker)
    }

    override fun onPopularKeywordSectionReloadClicked(position: Int, channel: DynamicHomeChannel.Channels) {
        getHomeViewModel().getPopularKeywordData()
        PopularKeywordTracking.sendPopularKeywordClickReload(channel)
    }

    override fun onPopularKeywordItemImpressed(channel: DynamicHomeChannel.Channels, position: Int, keyword: String, positionInWidget: Int) {
        getTrackingQueueObj()?.putEETracking(PopularKeywordTracking.getPopularKeywordImpressionItem(channel, position, keyword, positionInWidget) as HashMap<String, Any>)
    }

    override fun onPopularKeywordItemClicked(applink: String, channel: DynamicHomeChannel.Channels, position: Int, keyword: String, positionInWidget: Int) {
        RouteManager.route(context, applink)
        PopularKeywordTracking.sendPopularKeywordClickItem(channel, position, keyword, positionInWidget)
    }

    protected fun registerBroadcastReceiverTokoCash() {
        if (activity == null) return
        activity?.let {
            it.registerReceiver(
                    tokoCashBroadcaseReceiver,
                    IntentFilter(it.getString(R.string.broadcast_wallet))
            )
        }

    }

    protected fun unRegisterBroadcastReceiverTokoCash() {
        if (activity == null) return
        activity?.unregisterReceiver(tokoCashBroadcaseReceiver)
    }

    private val tokoCashBroadcaseReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val extras = intent.extras
            if (extras != null) {
                activity?.let {
                    val data = extras.getString(it.getString(R.string.broadcast_wallet))
                    if (data != null && !data.isEmpty()) getHomeViewModel().getHeaderData() // update header data
                }
            }
        }
    }

    override fun onRetryLoadFeeds() {
        getHomeViewModel().getFeedTabData()
    }

    private val isUserLoggedIn: Boolean
        private get() = getUserSession().isLoggedIn

    private val userShopId: String
        private get() = getUserSession().shopId

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        userVisibleHint = !hidden
        fragmentFramePerformanceIndexMonitoring?.onFragmentHidden(hidden)
    }

    private fun fetchTokopointsNotification(type: String) {
        if (activity != null) {
            TokoPointsNotificationManager.fetchNotification(activity, type, childsFragmentManager)
        }
    }

    override fun hideEggOnScroll() {
        hideEggFragmentOnScrolling()
    }

    override fun onFeedContentScrolled(dy: Int, totalScrollY: Int) {}
    override fun onFeedContentScrollStateChanged(newState: Int) {}
    override fun onGoToProductDetailFromInspiration(productId: String, imageSource: String, name: String, price: String) {
        goToProductDetail(productId, imageSource, name, price)
    }

    private fun goToProductDetail(productId: String, imageSourceSingle: String, name: String, price: String) {
        activity?.startActivity(getProductIntent(productId))
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (activity != null) {
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int) {
        if (homeMainToolbar != null && homeMainToolbar?.getViewHomeMainToolBar() != null) {
            homeMainToolbar?.setNotificationNumber(notificationCount)
            homeMainToolbar?.setInboxNumber(inboxCount)
        }
    }
    

    override val homeMainToolbarHeight: Int
        get() {
            var height = 0
            if (homeMainToolbar != null) {
                height = homeMainToolbar?.height?:0
                homeMainToolbar?.let {
                    if (!it.isShadowApplied()) {
                        height += resources.getDimensionPixelSize(R.dimen.dp_8)
                    }
                }
            }
            return height
        }

    private fun showFeedSectionViewHolderShadow(show: Boolean) {
        val feedViewHolder = homeRecyclerView?.findViewHolderForAdapterPosition(
                getHomeViewModel().getRecommendationFeedSectionPosition()
        )
        if (feedViewHolder is HomeRecommendationFeedViewHolder) {
            feedViewHolder.showFeedTabShadow(show)
        }
    }

    private fun inheritScrollVelocityToRecommendation(velocity: Int) {
        val feedViewHolder = homeRecyclerView?.findViewHolderForAdapterPosition(
                getHomeViewModel().getRecommendationFeedSectionPosition()
        )
        if (feedViewHolder is HomeRecommendationFeedViewHolder) {
            feedViewHolder.scrollByVelocity(velocity)
        }
    }

    private fun initEggDragListener() {
        val floatingEggButtonFragment = floatingEggButtonFragment
        floatingEggButtonFragment?.setOnDragListener(object : FloatingEggButtonFragment.OnDragListener {
            override fun onDragStart() {
                refreshLayout.setCanChildScrollUp(true)
            }

            override fun onDragEnd() {
                refreshLayout.setCanChildScrollUp(false)
            }
        })
    }

    override fun launchPermissionChecker() {
        promptGeolocationPermission()
    }

    override fun onCloseGeolocationView() {
        HIDE_GEO = true
        getHomeViewModel().onCloseGeolocation()
    }

    private fun getSnackbar(text: String, duration: Int): Snackbar {
        if (homeSnackbar != null) {
            homeSnackbar!!.dismiss()
            homeSnackbar = null
        }
        homeSnackbar = Snackbar.make(root, text, duration)
        return homeSnackbar!!
    }

    fun showNotAllowedGeolocationSnackbar() {
        getSnackbar(getString(R.string.discovery_home_snackbar_geolocation_declined_permission),
                Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.discovery_home_snackbar_geolocation_setting)) { view: View? ->
                    HomePageTracking.eventClickOnAtur(activity)
                    goToApplicationDetailActivity()
                }.show()
    }

    fun showAllowedGeolocationSnackbar() {
        getSnackbar(getString(R.string.discovery_home_snackbar_geolocation_granted_permission),
                Snackbar.LENGTH_LONG).show()
    }

    private fun goToApplicationDetailActivity() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", activity!!.packageName, null)
        intent.data = uri
        activity?.startActivity(intent)
    }

    override fun onPromoDragStart() {}
    override fun onPromoDragEnd() {}
    override fun putEEToTrackingQueue(data: HashMap<String, Any>) {
        if (getTrackingQueueObj() != null) {
            getTrackingQueueObj()?.putEETracking(data)
        }
    }

    override fun sendEETracking(data: HashMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    override fun putEEToIris(data: HashMap<String, Any>) {
        getIrisAnalytics().saveEvent(data)
    }

    private fun setStickyContent(tickerDetail: TickerDetail) {
        this.tickerDetail = tickerDetail
        updateStickyState()
    }

    private fun hideStickyLogin() {
        stickyLoginView.visibility = View.GONE
    }

    private fun updateStickyState() {
        if (isUserLoggedIn) {
            hideStickyLogin()
            return
        }

        var isCanShowing = getRemoteConfig().getBoolean(StickyLoginConstant.KEY_STICKY_LOGIN_REMINDER_HOME, true)
        if (stickyLoginView.isLoginReminder() && isCanShowing) {
            stickyLoginView.showLoginReminder(StickyLoginConstant.Page.HOME)
            stickyLoginView?.trackerLoginReminder.viewOnPage(StickyLoginConstant.Page.HOME)
        } else {
            isCanShowing = getRemoteConfig().getBoolean(StickyLoginConstant.KEY_STICKY_LOGIN_WIDGET_HOME, true)
            if (!isCanShowing) {
                hideStickyLogin()
                return
            }
            stickyLoginView.setContent(tickerDetail)
            stickyLoginView.show(StickyLoginConstant.Page.HOME)
            if (stickyLoginView.isShowing()) {
                stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.HOME)
            }
        }
    }

    override fun onReviewClick(position: Int, clickReviewAt: Int, delay: Long, applink: String) {
        Handler().postDelayed({
            val newAppLink = Uri.parse(applink)
                    .buildUpon()
                    .appendQueryParameter(REVIEW_CLICK_AT, clickReviewAt.toString())
                    .appendQueryParameter(UTM_SOURCE, DEFAULT_UTM_SOURCE)
                    .build().toString()
            val intent = RouteManager.getIntent(context, newAppLink)
            startActivityForResult(intent, REQUEST_CODE_REVIEW)
        }, delay)
    }

    override fun onCloseClick() {
        getHomeViewModel().dismissReview()
    }

    private fun updateEggBottomMargin(floatingEggButtonFragment: FloatingEggButtonFragment) {
        val params = floatingEggButtonFragment.view?.layoutParams as FrameLayout.LayoutParams
        if (stickyLoginView.isShowing()) {
            params.setMargins(0, 0, 0, stickyLoginView.height)
            val positionEgg = IntArray(2)
            val eggHeight = floatingEggButtonFragment.egg.height
            floatingEggButtonFragment.egg.getLocationOnScreen(positionEgg)
            if (positionEgg[1] + eggHeight > positionSticky?.get(1) ?: 0) {
                floatingEggButtonFragment.moveEgg(positionSticky!![1] - eggHeight)
            }
        } else {
            params.setMargins(0, 0, 0, 0)
        }
    }

    override fun getWindowWidth(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    private fun resetImpressionListener() {
        for ((_, value) in impressionScrollListeners) {
            homeRecyclerView?.removeOnScrollListener(value)
        }
        impressionScrollListeners.clear()
    }

    override fun refreshHomeData() {
        refreshLayout.isRefreshing = true
        onNetworkRetry()
    }

    override fun onTokopointCheckNowClicked(applink: String) {
        activity?.let {
            if (!TextUtils.isEmpty(applink)) {
                RouteManager.route(activity, applink)
            }
        }
    }

    override fun onOpenPlayChannelList(appLink: String) {
        openApplink(appLink, "")
    }

    override fun onOpenPlayActivity(root: View, channelId: String?) {
        shouldPausePlay = false
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalContent.PLAY_DETAIL, channelId)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                Pair.create(root.findViewById(R.id.exo_content_frame), getString(R.string.home_transition_video))
        )
        startActivityForResult(intent, REQUEST_CODE_PLAY_ROOM, options.toBundle())
    }

    private fun needToPerformanceMonitoring(): Boolean {
        return homePerformanceMonitoringListener != null && !isOnRecylerViewLayoutAdded
    }

    private fun showToaster(message: String, typeToaster: Int) {
        showToasterWithAction(message, typeToaster, "", View.OnClickListener { v: View? -> })
    }

    private fun showToasterWithAction(message: String, typeToaster: Int, actionText: String, clickListener: View.OnClickListener) {
        make(root, message, Snackbar.LENGTH_LONG, typeToaster, actionText, clickListener)
    }

    private fun addRecyclerViewScrollImpressionListener(dynamicChannelDataModel: DynamicChannelDataModel, adapterPosition: Int) {
        if (!impressionScrollListeners.containsKey(dynamicChannelDataModel.channel?.id)) {
            val listener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (layoutManager!!.findLastVisibleItemPosition() >= adapterPosition) {
                        sendIrisTracker(DynamicChannelViewHolder.Companion.getLayoutType(dynamicChannelDataModel.channel!!),
                                dynamicChannelDataModel.channel!!,
                                adapterPosition);
                        homeRecyclerView?.removeOnScrollListener(this);
                    }
                }
            }
            impressionScrollListeners.put(dynamicChannelDataModel.channel?.id!!, listener);
            homeRecyclerView?.addOnScrollListener(listener);
        }
    }

    private fun sendIrisTracker(layoutType: Int, channel: DynamicHomeChannel.Channels, position: Int) {
        when (layoutType) {
            DynamicChannelViewHolder.TYPE_SPRINT_SALE -> putEEToIris(
                    HomePageTracking.getEnhanceImpressionSprintSaleHomePage(
                            channel.id, channel.grids, position
                    )
            )
            DynamicChannelViewHolder.TYPE_ORGANIC -> putEEToIris(
                    HomePageTracking.getIrisEnhanceImpressionDynamicSprintLegoHomePage(
                            channel.id, channel.grids, channel.header.name
                    )
            )
            DynamicChannelViewHolder.TYPE_SPRINT_LEGO -> putEEToIris(
                    getSprintSaleImpression(channel, true) as HashMap<String, Any>
            )
            DynamicChannelViewHolder.TYPE_GIF_BANNER -> putEEToIris(
                    HomePageTracking.getEnhanceImpressionPromoGifBannerDC(channel))
            DynamicChannelViewHolder.TYPE_RECOMMENDATION_LIST -> putEEToIris(getRecommendationListImpression(channel, true, viewModel.get().getUserId()) as HashMap<String, Any>)
            DynamicChannelViewHolder.TYPE_PRODUCT_HIGHLIGHT -> putEEToIris(getProductHighlightImpression(
                    channel, getHomeViewModel().getUserId(), true
            ) as HashMap<String, Any>)
            DynamicChannelViewHolder.TYPE_CATEGORY_WIDGET -> putEEToIris(CategoryWidgetTracking.getCategoryWidgetBanneImpression(
                    channel.grids.toList(),
                    getHomeViewModel().getUserId(),
                    true,
                    channel
            ) as HashMap<String, Any>)
        }
    }

    private fun setupViewportImpression(visitables: List<Visitable<*>>) {
        for ((index, visitable) in visitables.withIndex()) {
            if (visitable is DynamicChannelDataModel) {
                if (!visitable.isCache && !visitable.channel!!.isInvoke) {
                    addRecyclerViewScrollImpressionListener(visitable, index)
                }
            }
        }
    }

    private fun getPageLoadTimeCallback(): PageLoadTimePerformanceInterface? {
        if (homePerformanceMonitoringListener != null && homePerformanceMonitoringListener?.pageLoadTimePerformanceInterface != null) {
           pageLoadTimeCallback =  homePerformanceMonitoringListener?.pageLoadTimePerformanceInterface
        }
        return pageLoadTimeCallback
    }

    override fun getFramePerformanceIndexData(): FragmentFramePerformanceIndexMonitoring {
        return fragmentFramePerformanceIndexMonitoring
    }

    override fun onDetach() {
        if(this::viewModel.isInitialized){
            this.viewModel.get().onCleared()
        }
        super.onDetach()
    }

    fun getHomeViewModel(): HomeViewModel{
        if(!this::viewModel.isInitialized){
            initInjectorHome()
        }
        return viewModel.get()
    }
}
