
package com.tokopedia.home.beranda.presentation.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry
import com.tokopedia.analytics.performance.perf.*
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_REQUEST_CODE
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.microinteraction.navtoolbar.NavToolbarMicroInteraction
import com.tokopedia.discovery.common.microinteraction.navtoolbar.navToolbarMicroInteraction
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getBannerClick
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getBannerImpression
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getOverlayBannerClick
import com.tokopedia.home.analytics.HomePageTrackingV2.HomeBanner.getOverlayBannerImpression
import com.tokopedia.home.analytics.v2.BestSellerWidgetTracker
import com.tokopedia.home.analytics.v2.LoginWidgetTracking
import com.tokopedia.home.analytics.v2.PopularKeywordTracking
import com.tokopedia.home.analytics.v2.RecommendationListTracking
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.di.BerandaComponent
import com.tokopedia.home.beranda.di.DaggerBerandaComponent
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.SearchPlaceholder
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.helper.ViewHelper
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_INFLATE_HOME_FRAGMENT
import com.tokopedia.home.beranda.listener.ActivityStateListener
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeEggListener
import com.tokopedia.home.beranda.listener.HomeFeedsListener
import com.tokopedia.home.beranda.listener.HomeReviewListener
import com.tokopedia.home.beranda.listener.HomeTabFeedListener
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecycleAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitableDiffUtil
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.CashBackData
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceCoachmark
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeAdapterFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.CarouselPlayWidgetViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.HomeHeaderOvoViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder.PopularKeywordListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.BalanceWidgetView
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.customview.NestedRecyclerView
import com.tokopedia.home.beranda.presentation.view.helper.HomePrefController
import com.tokopedia.home.beranda.presentation.view.helper.HomeRollenceController
import com.tokopedia.home.beranda.presentation.view.helper.getPositionWidgetVertical
import com.tokopedia.home.beranda.presentation.view.helper.isHomeTokonowCoachmarkShown
import com.tokopedia.home.beranda.presentation.view.helper.isSubscriptionCoachmarkShown
import com.tokopedia.home.beranda.presentation.view.helper.setHomeTokonowCoachmarkShown
import com.tokopedia.home.beranda.presentation.view.helper.setSubscriptionCoachmarkShown
import com.tokopedia.home.beranda.presentation.view.listener.BannerComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.BestSellerWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.CMHomeWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.CampaignWidgetComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.CarouselPlayWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.CategoryWidgetV2Callback
import com.tokopedia.home.beranda.presentation.view.listener.ChooseAddressWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.CueWidgetComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.DynamicIconComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.DynamicLegoBannerComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.FeaturedShopComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.FlashSaleWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.HomeComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.HomePayLaterWidgetListener
import com.tokopedia.home.beranda.presentation.view.listener.HomeReminderWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.Lego6AutoBannerComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.LegoProductCallback
import com.tokopedia.home.beranda.presentation.view.listener.MerchantVoucherComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.MissionWidgetComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.MixLeftComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.MixTopComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.ProductHighlightComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.RechargeBUWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.RechargeRecommendationCallback
import com.tokopedia.home.beranda.presentation.view.listener.RecommendationListCarouselComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.SalamWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.ShopFlashSaleWidgetCallback
import com.tokopedia.home.beranda.presentation.view.listener.SpecialReleaseComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.SpecialReleaseRevampCallback
import com.tokopedia.home.beranda.presentation.view.listener.TodoWidgetComponentCallback
import com.tokopedia.home.beranda.presentation.view.listener.VpsWidgetComponentCallback
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.constant.BerandaUrl
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home.constant.ConstantKey.CATEGORY_ID
import com.tokopedia.home.constant.ConstantKey.ResetPassword.IS_SUCCESS_RESET
import com.tokopedia.home.constant.ConstantKey.ResetPassword.KEY_MANAGE_PASSWORD
import com.tokopedia.home.constant.HomePerformanceConstant
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.widget.ToggleableSwipeRefreshLayout
import com.tokopedia.home_component.HomeComponentRollenceController
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.home_component.customview.pullrefresh.ParentIconSwipeRefreshLayout
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics.Companion.getInstance
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.encodeToUtf8
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.locationmanager.DeviceLocation
import com.tokopedia.locationmanager.LocationDetectorHelper
import com.tokopedia.navigation_common.listener.AllNotificationListener
import com.tokopedia.navigation_common.listener.FragmentListener
import com.tokopedia.navigation_common.listener.HomeCoachmarkListener
import com.tokopedia.navigation_common.listener.HomePerformanceMonitoringListener
import com.tokopedia.navigation_common.listener.MainParentStatusBarListener
import com.tokopedia.navigation_common.listener.RefreshNotificationListener
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.const.PlayWidgetConst
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play_common.util.extension.getVisiblePortion
import com.tokopedia.promogamification.common.floating.view.fragment.FloatingEggButtonFragment
import com.tokopedia.recharge_component.model.WidgetSource
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationWidgetListener
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.unifycomponents.Toaster.build
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.common.helper.isRegisteredFromStickyLogin
import com.tokopedia.usercomponents.stickylogin.common.helper.saveIsRegisteredFromStickyLogin
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginAction
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginView
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.weaver.WeaveInterface
import com.tokopedia.weaver.Weaver
import com.tokopedia.weaver.Weaver.Companion.executeWeaveCoRoutineWithFirebase
import com.tokopedia.wishlistcommon.util.AddRemoveWishlistV2Handler
import dagger.Lazy
import kotlinx.coroutines.FlowPreview
import rx.Observable
import rx.schedulers.Schedulers
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.Date
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * @author by yoasfs on 12/14/17.
 */

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@SuppressLint("SyntheticAccessor")
open class HomeRevampFragment :
    BaseDaggerFragment(),
    OnRefreshListener,
    HomeCategoryListener,
    AllNotificationListener,
    FragmentListener,
    HomeEggListener,
    HomeTabFeedListener,
    HomeFeedsListener,
    HomeReviewListener,
    PopularKeywordListener,
    PlayWidgetListener,
    RecommendationWidgetListener,
    CMHomeWidgetCallback,
    HomePayLaterWidgetListener {

    companion object {
        private const val className = "com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment"
        private const val REQUEST_CODE_DIGITAL_PRODUCT_DETAIL = 220
        private const val DEFAULT_WALLET_APPLINK_REQUEST_CODE = 111
        private const val REQUEST_CODE_LOGIN_STICKY_LOGIN = 130
        private const val REQUEST_CODE_LOGIN = 131
        private const val REQUEST_CODE_LOGIN_WIDGET_LOGIN = 133
        private const val REQUEST_CODE_REVIEW = 999
        private const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"
        private const val REVIEW_CLICK_AT = "rating"
        private const val SOURCE = "source"
        private const val EXTRA_URL = "url"
        private const val EXTRA_TITLE = "core_web_view_extra_title"
        private const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        private const val SEND_SCREEN_MIN_INTERVAL_MILLIS: Long = 1000
        private const val DEFAULT_SOURCE = "home_notif"
        private const val SEE_ALL_CARD = "android_mainapp_home_see_all_card_config"
        private const val REQUEST_CODE_PLAY_ROOM_PLAY_WIDGET = 258
        private const val REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME = 257
        private const val ENABLE_ASYNC_HOME_DAGGER = "android_async_home_dagger"
        private const val PLAY_CAROUSEL_WIDGET_VISIBLE_PORTION_THRESHOLD = 0.3

        var HIDE_TICKER = false
        private const val SOURCE_ACCOUNT = "account"
        private const val SCROLL_RECOMMEND_LIST = "recommend_list"
        private const val KEY_IS_LIGHT_THEME_STATUS_BAR = "is_light_theme_status_bar"
        private const val CLICK_TIME_INTERVAL: Long = 500

        private const val PARAM_APPLINK_AUTOCOMPLETE = "?navsource={source}&hint={hint}&first_install={first_install}"
        private const val HOME_SOURCE = "home"

        private const val DELAY_TOASTER_RESET_PASSWORD = 5000
        private const val ITEM_VIEW_CACHE_SIZE = 20
        private const val EMPTY_TIME_MILLIS = 0L
        private const val MONTH_DAY_COUNT = 30
        private const val TIME_MILLIS_MINUTE = 60000
        private const val COACHMARK_FIRST_INDEX = 0
        private const val HOME_HEADER_POSITION = 0
        private const val VIEW_DEFAULT_HEIGHT = 0f
        private const val STATUS_BAR_DEFAULT_ALPHA = 0f
        private const val VERTICAL_SCROLL_FULL_BOTTOM_OFFSET = 0
        private const val RV_DIRECTION_TOP = 1
        private const val NOTIFICATION_NUMBER_DEFAULT = 0
        private const val DEFAULT_CART_QUANTITY = "0"
        private const val EGG_MINIMUM_DY = 0
        private const val INVALID_SERVER_TIME = 0L
        private const val NO_SHOP_VALUE = "0"
        private const val PROMO_LIST_ACTIVITY_TAG = "PromoListActivity"
        private const val RV_EMPTY_TRESHOLD = 0
        private const val UTF8_ENCODER = "UTF-8"
        private const val SPACE_CHAR = "%20"
        private const val QUERY_CHAR_1 = "?"
        private const val QUERY_CHAR_2 = "%3F"
        private const val POSITION_ARRAY_CONTAINER_SIZE = 2
        private const val DEFAULT_MARGIN_VALUE = 0
        private const val POSITION_ARRAY_Y = 1
        private const val isPageRefresh = true

        @JvmStatic
        fun newInstance(scrollToRecommendList: Boolean): HomeRevampFragment {
            val fragment = HomeRevampFragment()
            val args = Bundle()
            args.putBoolean(SCROLL_RECOMMEND_LIST, scrollToRecommendList)
            fragment.arguments = args
            return fragment
        }
    }

    private var isNeedToRotateTokopoints: Boolean = true
    private var errorToaster: Snackbar? = null
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

    // Get Viewmodel using getHomeViewModel() method
    @Inject
    lateinit var viewModel: Lazy<HomeRevampViewModel>
    private lateinit var remoteConfig: RemoteConfig
    private lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var homePrefController: HomePrefController
    private lateinit var root: FrameLayout
    private var refreshLayout: ParentIconSwipeRefreshLayout? = null
    private var refreshLayoutOld: ToggleableSwipeRefreshLayout? = null
    private lateinit var onEggScrollListener: RecyclerView.OnScrollListener
    private lateinit var irisAnalytics: Iris
    private lateinit var irisSession: IrisSession
    private lateinit var statusBarBackground: View
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var remoteConfigInstance: RemoteConfigInstance
    private var stickyLoginView: StickyLoginView? = null
    private var homeRecyclerView: NestedRecyclerView? = null
    private var navToolbar: NavToolbar? = null
    private var homeSnackbar: Snackbar? = null
    private var component: BerandaComponent? = null
    private var adapter: HomeRecycleAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var messageSnackbar: SnackbarRetry? = null
    private var activityStateListener: ActivityStateListener? = null
    private var mainParentStatusBarListener: MainParentStatusBarListener? = null
    private var homePerformanceMonitoringListener: HomePerformanceMonitoringListener? = null
    private var homeCoachmarkListener: HomeCoachmarkListener? = null
    private var showRecomendation = false
    private var mShowTokopointNative = false
    private var showSeeAllCard = true
    private var isShowFirstInstallSearch = false
    private var isUsingNewPullRefresh = true
    private var scrollToRecommendList = false
    private var isFeedLoaded = false
    private var startToTransitionOffset = 0
    private var searchBarTransitionRange = 0
    private var lastSendScreenTimeMillis: Long = 0
    private var isLightThemeStatusBar = false
    private val impressionScrollListeners: MutableMap<String, RecyclerView.OnScrollListener> = HashMap()
    private var mLastClickTime = System.currentTimeMillis()
    private var pageLoadTimeCallback: PageLoadTimePerformanceInterface? = null
    private var isOnRecyclerViewLayoutAdded = false
    private var fragmentCreatedForFirstTime = false
    private var recommendationWishlistItem: RecommendationItem? = null
    private var lock = Object()
    private var subscriptionCoachmarkIsShowing = false
    private var tokonowCoachmarkIsShowing = false
    private var coachmarkTokonow: CoachMark2? = null
    private var coachmarkSubscription: CoachMark2? = null
    private var tokonowIconRef: View? = null
    private var tokonowIconParentPosition: Int = -1
    private val coachMarkItemSubscription = ArrayList<CoachMark2Item>()
    private val coachMarkItemTokonow = ArrayList<CoachMark2Item>()
    private var scrollPositionY = 0
    private var positionWidgetSubscription = 0
    private var positionWidgetTokonow = 0

    private var bannerCarouselCallback: BannerComponentCallback? = null

    private lateinit var playWidgetCoordinator: PlayWidgetCoordinator
    private var chooseAddressWidgetInitialized: Boolean = false
    private var fragmentCurrentCacheState: Boolean = true
    private var fragmentCurrentVisitableCount: Int = -1
    private var fragmentCurrentScrollPosition: Int = -1

    private val navToolbarMicroInteraction: NavToolbarMicroInteraction? by navToolbarMicroInteraction()

    private var performanceTrace: BlocksPerformanceTrace? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createDaggerComponent()
        mainParentStatusBarListener = context as MainParentStatusBarListener
        homePerformanceMonitoringListener = castContextToHomePerformanceMonitoring(context)
        homeCoachmarkListener = castContextToHomeCoachmarkListener(context)
        performanceTrace = homePerformanceMonitoringListener?.blocksPerformanceMonitoring
        requestStatusBarDark()
    }

    private fun createDaggerComponent() {
        val enableAsyncDaggerCompInit = getRemoteConfig().getBoolean(ENABLE_ASYNC_HOME_DAGGER, false)
        if (enableAsyncDaggerCompInit) {
            val homeDaggerWeave = object : WeaveInterface {
                override fun execute(): Any {
                    return initHomePageFlows()
                }
            }
            Weaver.executeWeaveCoRoutineNow(homeDaggerWeave)
        } else {
            initHomePageFlows()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPageLoadTimeCallback()?.startCustomMetric(HomePerformanceConstant.KEY_PERFORMANCE_ON_CREATE_HOME)
        fragmentCreatedForFirstTime = true
        context?.run {
            searchBarTransitionRange = resources.getDimensionPixelSize(R.dimen.home_revamp_searchbar_transition_range)
        }
        startToTransitionOffset = 1f.toDpInt()
        getPageLoadTimeCallback()?.stopCustomMetric(HomePerformanceConstant.KEY_PERFORMANCE_ON_CREATE_HOME)
    }

    protected open fun initHomePageFlows(): Boolean {
        initInjectorHome()
        return true
    }

    private fun getUserSession(): UserSessionInterface {
        if (!::userSession.isInitialized) {
            activity?.let {
                userSession = UserSession(it)
            }
        }
        return userSession
    }

    private fun getIrisAnalytics(): Iris {
        if (!::irisAnalytics.isInitialized) {
            activity?.let {
                irisAnalytics = getInstance(it)
            }
        }
        return irisAnalytics
    }

    private fun getIrisSession(): IrisSession {
        if (!::irisSession.isInitialized) {
            activity?.let {
                irisSession = IrisSession(it)
            }
        }
        return irisSession
    }

    private fun getRemoteConfig(): RemoteConfig {
        if (!::remoteConfig.isInitialized) {
            activity?.let {
                remoteConfig = FirebaseRemoteConfigImpl(it)
            }
        }
        return remoteConfig
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    override fun getTrackingQueueObj(): TrackingQueue? {
        if (trackingQueue == null) {
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

    private fun initInjectorHome() {
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
        return DaggerBerandaComponent.builder().baseAppComponent((requireActivity().application as BaseMainApplication).baseAppComponent)
    }

    private fun isUseNewPullRefresh(): Boolean =
        getRemoteConfig().getBoolean(RemoteConfigKey.HOME_USE_NEW_PULL_REFRESH, true)

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
        } else {
            null
        }
    }

    private fun castContextToHomeCoachmarkListener(context: Context): HomeCoachmarkListener? =
        if (context is HomeCoachmarkListener) context else null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        BenchmarkHelper.beginSystraceSection(TRACE_INFLATE_HOME_FRAGMENT)
        isUsingNewPullRefresh = isUseNewPullRefresh()
        val view = inflater.inflate(
            if (isUsingNewPullRefresh) R.layout.fragment_home_revamp else R.layout.fragment_home_revamp_old_refresh,
            container,
            false
        )
        BenchmarkHelper.endSystraceSection()
        navToolbar = view.findViewById(R.id.navToolbar)
        performanceTrace?.addViewPerformanceBlocks(navToolbar)
        statusBarBackground = view.findViewById(R.id.status_bar_bg)
        homeRecyclerView = view.findViewById(R.id.home_fragment_recycler_view)
        homeRecyclerView?.setHasFixedSize(true)
        HomeComponentRollenceController.fetchHomeComponentRollenceValue()
        context?.let {
            HomeRollenceController.fetchAtfRollenceValue(it)
        }

        // show nav toolbar
        navToolbar?.visibility = View.VISIBLE
        activity?.let { navToolbar?.setupToolbarWithStatusBar(it) }
        navToolbar?.let {
            viewLifecycleOwner.lifecycle.addObserver(it)
            homeRecyclerView?.addOnScrollListener(
                NavRecyclerViewScrollListener(
                    navToolbar = it,
                    startTransitionPixel = homeMainToolbarHeight,
                    toolbarTransitionRangePixel = searchBarTransitionRange,
                    navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                        override fun onAlphaChanged(offsetAlpha: Float) {
                        }

                        override fun onSwitchToDarkToolbar() {
                            navToolbar?.hideShadow()
                            if (HomeRollenceController.isUsingAtf2Variant()) {
                                requestStatusBarDark()
                            } else {
                                requestStatusBarLight()
                            }
                        }

                        override fun onSwitchToLightToolbar() {
                            requestStatusBarDark()
                        }

                        override fun onYposChanged(yOffset: Int) {
                        }
                    },
                    isBackgroundColorDefaultColor = HomeRollenceController.isUsingAtf2Variant()
                )
            )
            val icons = IconBuilder(
                IconBuilderFlag(pageSource = NavSource.HOME)
            ).addIcon(getInboxIcon()) {}
            icons.addIcon(IconList.ID_NOTIFICATION) {}
            icons.apply {
                addIcon(IconList.ID_CART) {}
                addIcon(IconList.ID_NAV_GLOBAL) {}
            }
            it.setIcon(icons)
            it.setupMicroInteraction(navToolbarMicroInteraction)
        }
        onChooseAddressUpdated()
        getSearchPlaceHolderHint()

        if (isUsingNewPullRefresh) {
            refreshLayout = view.findViewById(R.id.home_swipe_refresh_layout)
        } else {
            refreshLayoutOld = view.findViewById(R.id.home_swipe_refresh_layout)
        }

        root = view.findViewById(R.id.root)
        if (arguments != null) {
            scrollToRecommendList = requireArguments().getBoolean(SCROLL_RECOMMEND_LIST)
        }
        homeSnackbar = Snackbar.make(root, "", Snackbar.LENGTH_SHORT)
        fetchRemoteConfig()
        setupStatusBar()
        setupHomeRecyclerView()
        initEggDragListener()
        return view
    }

    private fun getSearchPlaceHolderHint() {
        if (this::viewModel.isInitialized) {
            getHomeViewModel().getSearchHint(isFirstInstall())
        }
    }

    private fun getInboxIcon(): Int {
        return IconList.ID_MESSAGE
    }

    private fun ArrayList<CoachMark2Item>.buildSubscriptionCoachMark(subscriptionBalanceCoachMark: BalanceCoachmark) {
        context?.let { currentContext ->
            val subscriptionWidget = getSubscriptionBalanceWidgetView()
            subscriptionWidget?.let {
                if (this.isEmpty()) {
                    positionWidgetSubscription = it.getPositionWidgetVertical()
                    this.add(
                        CoachMark2Item(
                            it,
                            if (subscriptionBalanceCoachMark.title.isBlank()) "" else subscriptionBalanceCoachMark.title.parseAsHtml(),
                            if (subscriptionBalanceCoachMark.description.isBlank()) "" else subscriptionBalanceCoachMark.description.parseAsHtml()
                        )
                    )
                }
            }
        }
    }

    private fun ArrayList<CoachMark2Item>.buildTokonowCoachmark(tokonowIcon: View?) {
        context?.let { currentContext ->
            if (!isHomeTokonowCoachmarkShown(currentContext)) {
                tokonowIcon?.let { tokonowIcon ->
                    if (this.isEmpty()) {
                        positionWidgetTokonow = tokonowIcon.getPositionWidgetVertical()
                        this.add(
                            CoachMark2Item(
                                tokonowIcon,
                                getString(R.string.home_tokonow_coachmark_title),
                                getString(R.string.home_tokonow_coachmark_description),
                                position = CoachMark2.POSITION_TOP
                            )
                        )
                    }
                }
            }
        }
    }

    private fun showCoachMark(
        subscriptionBalanceCoachMark: BalanceCoachmark? = null
    ) {
        context?.let { ctx ->
            if (!isSubscriptionCoachmarkShown(ctx) && subscriptionBalanceCoachMark != null) {
                showSubscriptionEligibleCoachMark(subscriptionBalanceCoachMark)
            } else if (!isHomeTokonowCoachmarkShown(ctx)) {
                showTokonowCoachmark()
            } else {
                homeCoachmarkListener?.onHomeCoachMarkFinished()
            }
        }
    }

    private fun showSubscriptionEligibleCoachMark(subscriptionBalanceCoachMark: BalanceCoachmark) {
        context?.let {
            subscriptionBalanceCoachMark.let { subscriptionBalanceCoachMark ->
                coachMarkItemSubscription.buildSubscriptionCoachMark(subscriptionBalanceCoachMark)
                coachmarkSubscription?.let { subscriptionCoachMark ->
                    try {
                        if (coachMarkItemSubscription.isNotEmpty() && isValidToShowCoachMark() && !subscriptionCoachmarkIsShowing) {
                            subscriptionCoachMark.onDismissListener = {
                                setSubscriptionCoachmarkShown(it)
                                showTokonowCoachmark()
                            }
                            subscriptionCoachMark.showCoachMark(step = coachMarkItemSubscription, index = COACHMARK_FIRST_INDEX)
                            subscriptionCoachmarkIsShowing = true
                        }
                    } catch (e: Exception) {
                        subscriptionCoachmarkIsShowing = false
                    }
                }
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun showTokonowCoachmark() {
        if (!userVisibleHint) return

        context?.let {
            coachMarkItemTokonow.buildTokonowCoachmark(tokonowIconRef)
            coachmarkTokonow?.let { coachmarkTokonow ->
                try {
                    if (coachMarkItemTokonow.isNotEmpty() && isValidToShowCoachMark() && !tokonowCoachmarkIsShowing) {
                        coachmarkTokonow.onDismissListener = {
                            setHomeTokonowCoachmarkShown(it)
                            homeCoachmarkListener?.onHomeCoachMarkFinished()
                        }
                        coachmarkTokonow.showCoachMark(step = coachMarkItemTokonow, index = COACHMARK_FIRST_INDEX)
                        tokonowCoachmarkIsShowing = true
                    } else {
                        homeCoachmarkListener?.onHomeCoachMarkFinished()
                    }
                } catch (e: Exception) {
                    tokonowCoachmarkIsShowing = false
                    e.printStackTrace()
                    homeCoachmarkListener?.onHomeCoachMarkFinished()
                }
            }
        }
    }

    private fun getSubscriptionBalanceWidgetView(): View? {
        val view = homeRecyclerView?.findViewHolderForAdapterPosition(HOME_HEADER_POSITION)
        (view as? HomeHeaderOvoViewHolder)?.let {
            val balanceWidgetSubscriptionView = getBalanceWidgetViewSubscriptionOnly(it.itemView.findViewById(R.id.view_balance_widget))
            if (it.itemView.findViewById<BalanceWidgetView>(R.id.view_balance_widget).isShown && (
                (
                    balanceWidgetSubscriptionView?.y
                        ?: VIEW_DEFAULT_HEIGHT
                    ) > VIEW_DEFAULT_HEIGHT
                )
            ) {
                return balanceWidgetSubscriptionView
            }
        }
        return null
    }

    private fun getBalanceWidgetViewSubscriptionOnly(balanceWidgetView: BalanceWidgetView): View? {
        return balanceWidgetView.getSubscriptionView()
    }

    private fun isValidToShowCoachMark(): Boolean {
        activity?.let {
            return !it.isFinishing
        }
        return false
    }

    private fun setupHomeRecyclerView() {
        homeRecyclerView?.setItemViewCacheSize(ITEM_VIEW_CACHE_SIZE)
        homeRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollPositionY += dy
                evaluateHomeComponentOnScroll(recyclerView)
            }
        })
        setupEmbraceBreadcrumbListener()
        setupHomePlayWidgetListener()
    }

    private fun setupEmbraceBreadcrumbListener() {
        if (remoteConfig.getBoolean(RemoteConfigKey.HOME_ENABLE_SCROLL_EMBRACE_BREADCRUMB)) {
            homeRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    trackEmbraceBreadcrumbPosition()
                }
            })
        }
    }

    private fun setupHomePlayWidgetListener() {
        homeRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val contentRect = Rect(
                0,
                homeMainToolbarHeight,
                getScreenWidth(),
                getScreenHeight() - resources.getDimensionPixelOffset(
                    com.tokopedia.unifyprinciples.R.dimen.unify_space_48
                )
            )

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!::playWidgetCoordinator.isInitialized) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return
                val layoutManager = this@HomeRevampFragment.layoutManager ?: return

                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                val viewHolders = (firstVisible..lastVisible).map {
                    recyclerView.findViewHolderForAdapterPosition(it)
                }
                val playViewHolder = viewHolders.firstOrNull { it is CarouselPlayWidgetViewHolder }

                if (playViewHolder != null) {
                    val visiblePortion = playViewHolder.itemView.getVisiblePortion(contentRect)
                    if (visiblePortion[1] >= PLAY_CAROUSEL_WIDGET_VISIBLE_PORTION_THRESHOLD) {
                        playWidgetCoordinator.onVisible()
                    } else {
                        playWidgetCoordinator.onNotVisible()
                    }
                } else {
                    playWidgetCoordinator.onNotVisible()
                }
            }
        })
    }

    private fun trackEmbraceBreadcrumbPosition() {
        if (fragmentCurrentScrollPosition != layoutManager?.findLastVisibleItemPosition()) {
            fragmentCurrentScrollPosition = layoutManager?.findLastVisibleItemPosition() ?: -1
            HomeServerLogger.sendEmbraceBreadCrumb(
                fragment = this@HomeRevampFragment,
                isLoggedIn = userSession.isLoggedIn,
                isCache = fragmentCurrentCacheState,
                visitableListCount = fragmentCurrentVisitableCount,
                scrollPosition = fragmentCurrentScrollPosition
            )
        }
    }

    private fun setupStatusBar() {
        activity?.let {
            statusBarBackground.background = ColorDrawable(
                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
            )
        }
        // status bar background compability, we show view background for android >= Kitkat
// because in that version, status bar can't forced to dark mode, we must set background
// to keep status bar icon visible
        statusBarBackground.layoutParams.height = ViewHelper.getStatusBarHeight(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarBackground.visibility = View.INVISIBLE
        } else {
            statusBarBackground.visibility = View.VISIBLE
        }
        // initial condition for status and searchbar
        setStatusBarAlpha(STATUS_BAR_DEFAULT_ALPHA)
    }

    private fun evaluateScrollSubscriptionCoachmark() {
        coachmarkSubscription?.let { subscriptionCoachmark ->
            context?.let { ctx ->
                if (scrollPositionY > positionWidgetSubscription && subscriptionCoachmarkIsShowing) {
                    subscriptionCoachmark.hideCoachMark()
                } else if (!isSubscriptionCoachmarkShown(ctx) && !subscriptionCoachmark.isShowing) {
                    subscriptionCoachmark.showCoachMark(coachMarkItemSubscription)
                }
            }
        }
    }

    private fun evaluateScrollTokonowCoachmark() {
        coachmarkTokonow?.let { tokonowCoachmark ->
            context?.let { ctx ->
                if (scrollPositionY > positionWidgetTokonow && tokonowCoachmarkIsShowing) {
                    tokonowCoachmark.hideCoachMark()
                } else if (!isHomeTokonowCoachmarkShown(ctx)) {
                    tokonowCoachmark.showCoachMark(coachMarkItemTokonow)
                }
            }
        }
    }

    private fun evaluateShowCoachmark() {
        if (subscriptionCoachmarkIsShowing) {
            evaluateScrollSubscriptionCoachmark()
        } else if (tokonowCoachmarkIsShowing) {
            evaluateScrollTokonowCoachmark()
        }
    }

    private fun evaluateHomeComponentOnScroll(recyclerView: RecyclerView) { // set refresh layout to only enabled when reach 0 offset
// because later we will disable scroll up for this parent recyclerview
// and makes refresh layout think we can't scroll up (which actually can! we only disable
// scroll so that feed recommendation section can scroll its content)
        if (recyclerView.computeVerticalScrollOffset() == VERTICAL_SCROLL_FULL_BOTTOM_OFFSET) {
            refreshLayout?.setCanChildScrollUp(false)
            refreshLayoutOld?.setCanChildScrollUp(false)
        } else {
            refreshLayout?.setCanChildScrollUp(true)
            refreshLayoutOld?.setCanChildScrollUp(true)
        }
        if (recyclerView.canScrollVertically(RV_DIRECTION_TOP)) {
            navToolbar?.showShadow(lineShadow = true)
            showFeedSectionViewHolderShadow(false)
            homeRecyclerView?.setNestedCanScroll(false)
        } else { // home feed now can scroll up, so hide maintoolbar shadow
            navToolbar?.hideShadow(lineShadow = true)
            showFeedSectionViewHolderShadow(true)
            homeRecyclerView?.setNestedCanScroll(true)
        }

        evaluateShowCoachmark()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
        initCoachmark()
        initRefreshLayout()
        subscribeHome()
        initEggTokenScrollListener()
        initStickyLogin()

        context?.let {
            if (isRegisteredFromStickyLogin(it)) gotoNewUserZone()
        }

        if (isSuccessReset()) showSuccessResetPasswordDialog()
    }

    private fun initCoachmark() {
        context?.let {
            coachmarkTokonow = CoachMark2(it)
            coachmarkSubscription = CoachMark2(it)
        }
    }

    override fun onLoginWidgetClick() {
        LoginWidgetTracking.sendLoginClick()
        goToLogin(REQUEST_CODE_LOGIN_WIDGET_LOGIN)
    }

    fun goToLogin(requestCode: Int) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
            startActivityForResult(intent, requestCode)
        }
    }

    private fun initStickyLogin() {
        stickyLoginView = if(!HomeRollenceController.isUsingAtf2Variant())
            view?.findViewById(R.id.sticky_login_text) else null
        if(stickyLoginView == null) return
        stickyLoginView?.page = StickyLoginConstant.Page.HOME
        stickyLoginView?.lifecycleOwner = viewLifecycleOwner
        stickyLoginView?.setStickyAction(object : StickyLoginAction {
            override fun onClick() {
                goToLogin(REQUEST_CODE_LOGIN_STICKY_LOGIN)
            }

            override fun onDismiss() {
                floatingEggButtonFragment?.let {
                    updateEggBottomMargin(it)
                }
            }

            override fun onViewChange(isShowing: Boolean) {
                floatingEggButtonFragment?.let {
                    updateEggBottomMargin(it)
                }
            }
        })

        stickyLoginView?.hide()
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
        if (getHomeViewModel().isFirstLoad) {
            getPageLoadTimeCallback()?.startCustomMetric(HomePerformanceConstant.KEY_PERFORMANCE_ON_RESUME_HOME)
        }
        playWidgetOnVisibilityChanged(isViewResumed = true)
        super.onResume()
        createAndCallSendScreen()
        adapter?.onResumeBanner()
        conditionalViewModelRefresh()
        if (activityStateListener != null) {
            activityStateListener!!.onResume()
        }
        if (getHomeViewModel().isFirstLoad) {
            getPageLoadTimeCallback()?.stopCustomMetric(HomePerformanceConstant.KEY_PERFORMANCE_ON_RESUME_HOME)
            getHomeViewModel().isFirstLoad = false
        }

        adapter?.onResumeSpecialRelease()

        // refresh home-to-do-widget data if needed
        getHomeViewModel().getCMHomeWidgetData(false)
    }

    private fun conditionalViewModelRefresh() {
        if (!validateChooseAddressWidget()) {
            getHomeViewModel().refreshWithThreeMinsRules(isFirstInstall = isFirstInstall())
        }
    }

    private fun validateChooseAddressWidget(): Boolean {
        context?.let { currentContext ->
            var isAddressChanged = false
            getHomeViewModel().getAddressData().toLocalCacheModel().let {
                isAddressChanged = ChooseAddressUtils.isLocalizingAddressHasUpdated(currentContext, it)
            }

            if (isAddressChanged) {
                chooseAddressWidgetInitialized = false
                val localChooseAddressData = ChooseAddressUtils.getLocalizingAddressData(currentContext)
                val updatedChooseAddressData = HomeChooseAddressData(isActive = true)
                    .setLocalCacheModel(localChooseAddressData)
                getHomeViewModel().updateChooseAddressData(updatedChooseAddressData)
                getHomeViewModel().refreshHomeData()
            }

            return isAddressChanged
        }
        return false
    }

    private fun createAndCallSendScreen() {
        val sendScrWeave: WeaveInterface = object : WeaveInterface {
            override fun execute(): Any {
                return sendScreen()
            }
        }
        executeWeaveCoRoutineWithFirebase(sendScrWeave, RemoteConfigKey.ENABLE_ASYNC_HOME_SNDSCR, context?.applicationContext, true)
    }

    override fun onPause() {
        playWidgetOnVisibilityChanged(isViewResumed = false)
        super.onPause()
        adapter?.onPauseBanner()
        getTrackingQueueObj()?.sendAll()
        if (activityStateListener != null) {
            activityStateListener!!.onPause()
        }
        performanceTrace?.finishOnPaused()
    }

    override fun onStop() {
        super.onStop()
        chooseAddressWidgetInitialized = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::playWidgetCoordinator.isInitialized) {
            playWidgetCoordinator.onDestroy()
        }
        Toaster.onCTAClick = View.OnClickListener { }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeRecyclerView?.adapter = null
        adapter = null
        homeRecyclerView?.layoutManager = null
        layoutManager = null
    }

    private fun initRefreshLayout() {
        refreshLayout?.post {
            /*
             * set notification gimmick
             */
            navToolbar?.setBadgeCounter(IconList.ID_NOTIFICATION, NOTIFICATION_NUMBER_DEFAULT)
        }
        refreshLayout?.setEnableSwipeRefreshDistancePx(navToolbar?.height ?: 0)
        refreshLayout?.setOnRefreshListener { onRefresh() }

        refreshLayoutOld?.post {
            /*
             * set notification gimmick
             */
            navToolbar?.setBadgeCounter(IconList.ID_NOTIFICATION, NOTIFICATION_NUMBER_DEFAULT)
        }
        refreshLayoutOld?.setOnRefreshListener { onRefresh() }
    }

    private fun subscribeHome() {
        observeHomeData()
        observeUpdateNetworkStatusData()
        observeOneClickCheckout()
        observeErrorEvent()
        observeTrackingData()
        observeHomeRequestNetwork()
        observeIsNeedRefresh()
        observeSearchHint()
        observePlayWidgetReminder()
        observePlayWidgetReminderEvent()
        observeResetNestedScrolling()
        observeSearchHint()
    }

    private fun observeResetNestedScrolling() {
        getHomeViewModel().resetNestedScrolling.observe(
            viewLifecycleOwner,
            Observer { data: Event<Boolean> ->
                val isResetNestedScrolling = data.peekContent()
                if (isResetNestedScrolling) {
                    homeRecyclerView?.setNestedCanScroll(false)
                }
            }
        )
    }

    private fun observeIsNeedRefresh() {
        getHomeViewModel().isNeedRefresh.observe(
            viewLifecycleOwner,
            Observer { data: Event<Boolean> ->
                val isNeedRefresh = data.peekContent()
                if (isNeedRefresh) {
                    bannerCarouselCallback?.resetImpression()
                }
            }
        )
    }

    private fun observeHomeRequestNetwork() {
        getHomeViewModel().isRequestNetworkLiveData.observe(
            viewLifecycleOwner,
            Observer { data: Event<Boolean> ->
                val isRequestNetwork = data.peekContent()
                if (isRequestNetwork && getPageLoadTimeCallback() != null) {
                    getPageLoadTimeCallback()?.startNetworkRequestPerformanceMonitoring()
                }
            }
        )
    }

    private fun observeErrorEvent() {
        getHomeViewModel().errorEventLiveData.observe(
            viewLifecycleOwner,
            Observer { data: Event<Throwable> ->
                showToaster(getErrorString(data.peekContent()), TYPE_ERROR)
            }
        )
    }

    private fun observeHomeData() {
        getHomeViewModel().homeLiveDynamicChannel.observe(
            viewLifecycleOwner,
            Observer { dynamicChannel: HomeDynamicChannelModel? ->
                dynamicChannel?.let {
                    if (dynamicChannel.list.isNotEmpty()) {
                        setData(dynamicChannel.list, dynamicChannel.isCache)
                    }
                }
            }
        )
    }

    private fun observeUpdateNetworkStatusData() {
        getHomeViewModel().updateNetworkLiveData.observe(
            viewLifecycleOwner,
            Observer { (status, _, throwable) ->
                resetImpressionListener()
                when {
                    status === Result.Status.SUCCESS -> {
                        hideLoading()
                    }
                    status === Result.Status.ERROR -> {
                        hideLoading()
                        showNetworkError(getErrorStringWithDefault(throwable))
                        onPageLoadTimeEnd()
                        performanceTrace?.setPageState(BlocksPerformanceTrace.BlocksPerfState.STATE_PARTIALLY_ERROR)
                    }
                    status === Result.Status.ERROR_PAGINATION -> {
                        hideLoading()
                        showNetworkError(getErrorStringWithDefault(throwable))
                        performanceTrace?.setPageState(BlocksPerformanceTrace.BlocksPerfState.STATE_PARTIALLY_ERROR)
                    }
                    status === Result.Status.ERROR_ATF -> {
                        hideLoading()
                        showNetworkError(getErrorStringWithDefault(throwable))
                        adapter?.resetChannelErrorState()
                        adapter?.resetAtfErrorState()
                        performanceTrace?.setPageState(BlocksPerformanceTrace.BlocksPerfState.STATE_PARTIALLY_ERROR)
                    }
                    status == Result.Status.ERROR_GENERAL -> {
                        val errorString = getErrorStringWithDefault(throwable)
                        showNetworkError(errorString)
                        NetworkErrorHelper.showEmptyState(activity, root, errorString) { onRefresh() }
                        onPageLoadTimeEnd()
                        performanceTrace?.setPageState(BlocksPerformanceTrace.BlocksPerfState.STATE_ERROR)
                    }
                    else -> {
                        showLoading()
                    }
                }
            }
        )
        getHomeViewModel().hideShowLoading.observe(
            viewLifecycleOwner,
            Observer {
                hideLoading()
            }
        )
    }

    private fun observeTrackingData() {
        getHomeViewModel().trackingLiveData.observe(
            viewLifecycleOwner,
            Observer<Event<List<HomeVisitable?>>> { trackingData: Event<List<HomeVisitable?>> ->
                val homeVisitables = trackingData.getContentIfNotHandled()
                homeVisitables?.let {
                    val visitables = it as List<Visitable<*>>
                    addImpressionToTrackingQueue(visitables)
                }
            }
        )
    }

    private fun observeSearchHint() {
        if (view != null && ::viewModel.isInitialized && !getHomeViewModel().searchHint.hasObservers()) {
            getHomeViewModel().searchHint.observe(
                viewLifecycleOwner,
                Observer { data: SearchPlaceholder ->
                    if (userVisibleHint) {
                        setHint(data)
                    }
                }
            )
        }
    }

    private fun observeOneClickCheckout() {
        viewModel.get().oneClickCheckoutHomeComponent.observe(
            viewLifecycleOwner,
            Observer { event: Event<Any> ->
                val data = event.peekContent()
                if (data is Throwable) { // error
                    showToaster(getErrorString(MessageErrorException(data.message)), TYPE_ERROR)
                } else {
                    val dataMap = data as Map<*, *>
                    sendEETracking(
                        RecommendationListTracking.getAddToCartOnDynamicListCarouselHomeComponent(
                            (dataMap[HomeRevampViewModel.CHANNEL] as ChannelModel),
                            (dataMap[HomeRevampViewModel.GRID] as ChannelGrid),
                            dataMap[HomeRevampViewModel.POSITION] as Int,
                            (dataMap[HomeRevampViewModel.ATC] as? AddToCartDataModel)!!.data.cartId,
                            DEFAULT_CART_QUANTITY,
                            getHomeViewModel().getUserId()
                        ) as HashMap<String, Any>
                    )
                    RouteManager.route(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
                }
            }
        )
    }

    private fun observePlayWidgetReminder() {
        getHomeViewModel().playWidgetReminderObservable.observe(
            viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Result.Status.SUCCESS -> if (it.data != null) {
                        showToaster(
                            if (it.data.reminded) {
                                getString(com.tokopedia.play.widget.R.string.play_widget_success_add_reminder)
                            } else {
                                getString(com.tokopedia.play.widget.R.string.play_widget_success_remove_reminder)
                            },
                            TYPE_NORMAL
                        )
                    }
                    Result.Status.ERROR -> showToaster(getErrorString(MessageErrorException(getString(com.tokopedia.play.widget.R.string.play_widget_error_reminder))), TYPE_ERROR)
                    else -> {
                    }
                }
            }
        )
    }

    private fun observePlayWidgetReminderEvent() {
        getHomeViewModel().playWidgetReminderEvent.observe(
            viewLifecycleOwner,
            Observer {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearchHint()
    }

    private fun setData(data: List<Visitable<*>>, isCache: Boolean) {
        if (data.isNotEmpty()) {
            if (needToPerformanceMonitoring(data) && getPageLoadTimeCallback() != null) {
                finishPageLoadTime(isCache)
            }
            this.fragmentCurrentCacheState = isCache
            this.fragmentCurrentVisitableCount = data.size

            HomeServerLogger.sendEmbraceBreadCrumb(
                fragment = this,
                isLoggedIn = userSession.isLoggedIn,
                isCache = isCache,
                visitableListCount = data.size,
                scrollPosition = layoutManager?.findLastVisibleItemPosition()
            )
            performanceTrace?.setBlock(data)
            adapter?.submitList(data)
        }
    }

    private fun finishPageLoadTime(isCache: Boolean) {
        getPageLoadTimeCallback()?.stopNetworkRequestPerformanceMonitoring()
        getPageLoadTimeCallback()?.startRenderPerformanceMonitoring()
        setOnRecyclerViewLayoutReady(isCache)
    }

    private fun <T> containsInstance(list: List<T>, type: Class<*>): Boolean {
        val instance = list.filterIsInstance(type)
        return instance.isNotEmpty()
    }

    private fun loadEggData(isPageRefresh: Boolean = false) {
        val floatingEggButtonFragment = floatingEggButtonFragment
        if (floatingEggButtonFragment != null) {
            updateEggBottomMargin(floatingEggButtonFragment)
            floatingEggButtonFragment.loadEggData(isPageRefresh)
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

    private object FixedTheme {
        const val TOOLBAR_DARK_TYPE = 0
        const val TOOLBAR_LIGHT_TYPE = 1
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

    override fun onTokonowViewCaptured(tokonowView: View?, parentPosition: Int) {
        this.tokonowIconRef = tokonowView
        this.tokonowIconParentPosition = parentPosition
    }

    override fun dismissTokonowCoachmark(parentPosition: Int) {
        if (tokonowIconParentPosition == parentPosition) {
            coachmarkTokonow?.let {
                if (it.isShowing) {
                    it.dismissCoachMark()
                }
            }
        }
    }

    private fun initEggTokenScrollListener() {
        onEggScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy == EGG_MINIMUM_DY) {
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
        get() = // https://stackoverflow.com/questions/28672883/java-lang-illegalstateexception-fragment-not-attached-to-activity
            if (activity != null && isAdded) {
                childFragmentManager.findFragmentById(R.id.floating_egg_fragment) as FloatingEggButtonFragment?
            } else {
                null
            }

    private fun initAdapter() {
        if (!this::homePrefController.isInitialized) {
            initInjectorHome()
        }
        layoutManager = LinearLayoutManager(context)
        homeRecyclerView?.layoutManager = layoutManager
        setupPlayWidgetCoordinator()
        bannerCarouselCallback = BannerComponentCallback(context, this)
        val adapterFactory = HomeAdapterFactory(
            this,
            this,
            this,
            homeRecyclerView?.recycledViewPool ?: RecyclerView.RecycledViewPool(),
            this,
            HomeComponentCallback(this),
            DynamicLegoBannerComponentCallback(context, this, userId),
            RecommendationListCarouselComponentCallback(this),
            MixLeftComponentCallback(this),
            MixTopComponentCallback(this),
            HomeReminderWidgetCallback(
                RechargeRecommendationCallback(context, this),
                SalamWidgetCallback(context, this, getUserSession())
            ),
            ProductHighlightComponentCallback(this),
            FeaturedShopComponentCallback(context, this),
            playWidgetCoordinator,
            this,
            RechargeBUWidgetCallback(context, this),
            bannerCarouselCallback,
            DynamicIconComponentCallback(context, this, homePrefController),
            Lego6AutoBannerComponentCallback(context, this),
            CampaignWidgetComponentCallback(context, this),
            this,
            this,
            SpecialReleaseComponentCallback(context, this),
            MerchantVoucherComponentCallback(this),
            CueWidgetComponentCallback(this),
            VpsWidgetComponentCallback(this),
            CategoryWidgetV2Callback(context, this),
            MissionWidgetComponentCallback(this, getHomeViewModel()),
            LegoProductCallback(this),
            TodoWidgetComponentCallback(this, getHomeViewModel()),
            FlashSaleWidgetCallback(this),
            CarouselPlayWidgetCallback(getTrackingQueueObj(), userSession, this),
            BestSellerWidgetCallback(context, this, getHomeViewModel()),
            SpecialReleaseRevampCallback(this),
            ShopFlashSaleWidgetCallback(this, getHomeViewModel())
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

    private fun doHomeDataRefresh() {
        getHomeViewModel().refreshHomeData()
    }

    private fun onGoToSell() {
        if (isUserLoggedIn) {
            val shopId = userShopId
            if (shopId != NO_SHOP_VALUE) {
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
        startActivityForResult(intent, REQUEST_CODE_LOGIN)
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
    }

    override fun actionInfoPendingCashBackTokocash(
        cashBackData: CashBackData,
        appLinkActionButton: String
    ) {
        activity?.let { context ->
            val bottomSheetDialogTokoCash = BottomSheetUnify()
            bottomSheetDialogTokoCash.run {
                setTitle(getString(R.string.toko_cash_pending_title))
                val pendingCashbackView = View.inflate(context, R.layout.home_pending_cashback_tokocash, null)
                pendingCashbackView.findViewById<Typography>(R.id.body_bottom_sheet)?.text = String.format(
                    getString(R.string.toko_cash_pending_body),
                    cashBackData.amountText
                )
                pendingCashbackView.findViewById<ImageView>(R.id.img_bottom_sheet)?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_box))
                pendingCashbackView.findViewById<Typography>(R.id.button_opsi_bottom_sheet)?.run {
                    setOnClickListener {
                        goToOvo(appLinkActionButton)
                    }
                    text = getString(R.string.toko_cash_pending_proceed_button)
                }
                setChild(pendingCashbackView)
            }
            childsFragmentManager.let { bottomSheetDialogTokoCash.show(it, "") }
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
        refreshLayout?.isEnabled = !isDragged
        refreshLayoutOld?.isEnabled = !isDragged
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
            TopAdsUrlHitter(context).hitClickUrl(
                className,
                slidesModel.redirectUrl,
                slidesModel.id.toString(),
                slidesModel.title + " : " + slidesModel.creativeName,
                slidesModel.imageUrl
            )
        }
    }

    override fun onPromoAllClick() {
        HomePageTracking.eventClickViewAllPromo()
        HomeTrackingUtils.homeViewAllPromotions(activity, PROMO_LIST_ACTIVITY_TAG)
        val remoteConfigEnable: Boolean
        val remoteConfig = FirebaseRemoteConfigImpl(activity)
        remoteConfigEnable = remoteConfig.getBoolean(
            ConstantKey.RemoteConfigKey.MAINAPP_NATIVE_PROMO_LIST
        )
        if (activity != null && remoteConfigEnable) {
            RouteManager.route(requireActivity(), ApplinkConstInternalPromo.PROMO_LIST)
        } else {
            if (activity != null) {
                showBannerWebViewOnAllPromoClickFromHomeIntent(BerandaUrl.PROMO_URL + BerandaUrl.FLAG_APP, getString(com.tokopedia.loyalty.R.string.title_activity_promo))
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
                    showToasterReviewSuccess()
                    getHomeViewModel().onRemoveSuggestedReview()
                }
            }
            REQUEST_CODE_PLAY_ROOM_PLAY_WIDGET -> if (data != null) {
                notifyPlayWidgetTotalView(data)
                notifyPlayWidgetReminder(data)
            }
            REQUEST_CODE_USER_LOGIN_PLAY_WIDGET_REMIND_ME -> if (resultCode == Activity.RESULT_OK) {
                val lastEvent = getHomeViewModel().playWidgetReminderEvent?.value
                if (lastEvent != null) getHomeViewModel().shouldUpdatePlayWidgetToggleReminder(lastEvent.first, lastEvent.second)
            }
            PRODUCT_CARD_OPTIONS_REQUEST_CODE -> {
                handleProductCardOptionsActivityResult(
                    requestCode,
                    resultCode,
                    data,
                    object : ProductCardOptionsWishlistCallback {
                        override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                            handleWishlistAction(productCardOptionsModel)
                        }
                    }
                )
            }
            REQUEST_CODE_LOGIN -> {
                activity?.let {
                    val intentNewUser = RouteManager.getIntent(context, ApplinkConst.DISCOVERY_NEW_USER)
                    val intentHome = RouteManager.getIntent(activity, ApplinkConst.HOME)
                    intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    if (resultCode == Activity.RESULT_OK &&
                        getUserSession().isLoggedIn &&
                        data?.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SMART_REGISTER, false) == false
                    ) {
                        it.startActivities(arrayOf(intentHome, intentNewUser))
                    } else {
                        it.startActivity(intentHome)
                    }
                    it.finish()
                }
            }
            REQUEST_CODE_LOGIN_STICKY_LOGIN,
            REQUEST_CODE_LOGIN_WIDGET_LOGIN -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val isSuccessRegister = data.getBooleanExtra(ApplinkConstInternalGlobal.PARAM_IS_SUCCESS_REGISTER, false)
                    if (isSuccessRegister && getUserSession().isLoggedIn) {
                        gotoNewUserZonePage()
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        bannerCarouselCallback?.resetImpression()
        resetFeedState()
        removeNetworkError()
        if (this::viewModel.isInitialized) {
            getHomeViewModel().getSearchHint(isFirstInstall())
            getHomeViewModel().refreshHomeData()
        } else {
            hideLoading()
        }
        if (activity is RefreshNotificationListener) {
            (activity as RefreshNotificationListener?)?.onRefreshNotification()
        }
        stickyLoginView?.loadContent()
        loadEggData(isPageRefresh)
    }

    override fun onChooseAddressUpdated() {
        context?.let { currentContext ->
            val localCacheModel = ChooseAddressUtils.getLocalizingAddressData(currentContext)
            getHomeViewModel().updateChooseAddressData(
                HomeChooseAddressData(isActive = true)
                    .setLocalCacheModel(localCacheModel)
            )
            chooseAddressWidgetInitialized = false
            if (getHomeViewModel().homeDataModel.list.isNotEmpty()) {
                getHomeViewModel().refreshWithThreeMinsRules(forceRefresh = true, isFirstInstall = isFirstInstall())
            }
        }
    }

    override fun initializeChooseAddressWidget(chooseAddressWidget: ChooseAddressWidget, needToShowChooseAddress: Boolean) {
        chooseAddressWidget.bindChooseAddress(ChooseAddressWidgetCallback(context, this, this))
        chooseAddressWidget.run {
            visibility = if (needToShowChooseAddress) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onChooseAddressServerDown() {
        getHomeViewModel().removeChooseAddressWidget()
    }

    override fun needToRotateTokopoints(): Boolean {
        return isNeedToRotateTokopoints
    }

    override fun setRotateTokopointsDone(boolean: Boolean) {
        this.isNeedToRotateTokopoints = false
    }

    private fun onNetworkRetry() {
        resetFeedState()
        removeNetworkError()
        homeRecyclerView?.isEnabled = false
        if (::viewModel.isInitialized) {
            getHomeViewModel().refreshHomeData()
        }
        if (activity is RefreshNotificationListener) {
            (activity as RefreshNotificationListener?)?.onRefreshNotification()
        }
        stickyLoginView?.loadContent()
        loadEggData()
    }

    private fun resetFeedState() {
        isFeedLoaded = false
    }

    private fun showLoading() {
        refreshLayout?.isRefreshing = true
        refreshLayoutOld?.isRefreshing = true
    }

    private fun hideLoading() {
        refreshLayout?.isRefreshing = false
        refreshLayoutOld?.isRefreshing = false
        homeRecyclerView?.isEnabled = true
    }

    private fun setOnRecyclerViewLayoutReady(isCache: Boolean) {
        isOnRecyclerViewLayoutAdded = true
        homeRecyclerView?.addOneTimeGlobalLayoutListener {
            homePerformanceMonitoringListener?.stopHomePerformanceMonitoring(isCache)
            homePerformanceMonitoringListener = null
            onPageLoadTimeEnd()
        }
    }

    private fun onPageLoadTimeEnd() {
        stickyLoginView?.loadContent()
        pageLoadTimeCallback?.invalidate()
        loadEggData(isPageRefresh)
    }

    private fun remoteConfigIsNewBalanceWidget(): Boolean {
        return remoteConfig.getBoolean(ConstantKey.RemoteConfigKey.HOME_SHOW_NEW_BALANCE_WIDGET, true)
    }

    private fun detectAndSendLocation() {
        activity?.let {
            Observable.just(true).map { aBoolean: Boolean? ->
                val locationDetectorHelper = LocationDetectorHelper(
                    permissionCheckerHelper.get(),
                    LocationServices.getFusedLocationProviderClient(it.applicationContext),
                    it.applicationContext
                )
                locationDetectorHelper.getLocation(
                    onGetLocation(),
                    it,
                    LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                    rationaleText = ""
                )
                true
            }.subscribeOn(Schedulers.io()).subscribe({ }) { }
        }
    }

    private fun onGetLocation(): Function1<DeviceLocation, Unit> {
        return { (latitude, longitude) ->
            saveLocation(activity, latitude, longitude)
        }
    }

    private fun saveLocation(context: Context?, latitude: Double, longitude: Double) {
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
                ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_SEARCH,
                Context.MODE_PRIVATE
            )
            sharedPrefs.edit().putLong(
                ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_TIME_SEARCH,
                0
            ).apply()
        }
    }

    private fun isFirstInstall(): Boolean {
        context?.let {
            if (!getUserSession().isLoggedIn &&
                isShowFirstInstallSearch
            ) {
                sharedPrefs = it.getSharedPreferences(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_SEARCH,
                    Context.MODE_PRIVATE
                )
                var firstInstallCacheValue = sharedPrefs.getLong(
                    ConstantKey.FirstInstallCache.KEY_FIRST_INSTALL_TIME_SEARCH,
                    0
                )

                if (firstInstallCacheValue == EMPTY_TIME_MILLIS) return false
                firstInstallCacheValue += (MONTH_DAY_COUNT * TIME_MILLIS_MINUTE).toLong()
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
        searchPlaceholder.data?.let { data ->
            navToolbar?.setupSearchbar(
                hints = listOf(
                    HintData(
                        data.placeholder ?: "",
                        data.keyword
                            ?: ""
                    )
                ),
                applink = if (data.keyword?.isEmpty() != false) {
                    ApplinkConstInternalDiscovery.AUTOCOMPLETE
                } else {
                    PARAM_APPLINK_AUTOCOMPLETE
                },
                searchbarClickCallback = {
                    val intent = RouteManager.getIntent(
                        context,
                        ApplinkConstInternalDiscovery.AUTOCOMPLETE + PARAM_APPLINK_AUTOCOMPLETE,
                        HOME_SOURCE,
                        data.keyword.safeEncodeUtf8(),
                        isFirstInstall().toString()
                    )

                    navToolbarMicroInteraction
                        ?.animate(intent, ::startActivity)
                        ?: startActivity(intent)
                },
                searchbarImpressionCallback = {},
                shouldShowTransition = false
            )
        }
    }

    private fun placeholderToHint(data: SearchPlaceholder.Data): ArrayList<HintData> {
        var hints = arrayListOf(HintData(data.placeholder ?: "", data.keyword ?: ""))
        data.placeholders?.let { placeholders ->
            if (placeholders.isNotEmpty()) {
                hints = arrayListOf()
                placeholders.forEach { placeholder ->
                    hints.add(HintData(placeholder.placeholder ?: "", placeholder.keyword ?: ""))
                }
            }
        }
        return hints
    }

    private fun addImpressionToTrackingQueue(visitables: List<Visitable<*>>) {
        val combinedTracking: MutableList<Any> = ArrayList()
        for (visitable in visitables) {
            if (visitable is HomeVisitable) {
                if (!visitable.isTrackingCombined && visitable.trackingData != null) {
                    HomePageTracking.eventEnhancedImpressionWidgetHomePage(getTrackingQueueObj(), visitable.trackingData)
                }
            }
        }
        if (combinedTracking.isNotEmpty()) {
            HomePageTracking.eventEnhanceImpressionLegoAndCuratedHomePage(getTrackingQueueObj(), combinedTracking)
        }
    }

    override fun showNetworkError(message: String) {
        if (isAdded && activity != null && adapter != null) {
            if (adapter?.itemCount ?: RV_EMPTY_TRESHOLD > RV_EMPTY_TRESHOLD) {
                showToaster(message, TYPE_ERROR)
            } else {
                NetworkErrorHelper.showEmptyState(activity, root, message) { onRefresh() }
            }
        }
    }

    override fun onDynamicChannelClicked(applink: String) {
        onActionLinkClicked(applink)
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
        if (activity != null &&
            RouteManager.isSupportApplink(activity, actionLink)
        ) {
            openApplink(actionLink, trackingAttribution)
        } else {
            openWebViewURL(actionLink, activity)
        }
    }

    override fun getTopAdsBannerNextPage(): String {
        return getHomeViewModel().currentTopAdsBannerPage
    }

    override fun getDynamicChannelData(visitable: Visitable<*>, channelModel: ChannelModel, channelPosition: Int) {
        getHomeViewModel().getDynamicChannelDataOnExpired(visitable, channelModel, channelPosition)
    }

    override fun getUserIdFromViewModel(): String {
        return getHomeViewModel().getUserId()
    }

    override fun recommendationListOnCloseBuyAgain(id: String, position: Int) {
        getHomeViewModel().onCloseBuyAgain(id, position)
    }

    override fun getOneClickCheckoutHomeComponent(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        getHomeViewModel().getOneClickCheckoutHomeComponent(channelModel, channelGrid, position)
    }

    override fun declineRechargeRecommendationItem(requestParams: Map<String, String>) {
        getHomeViewModel().declineRechargeRecommendationItem(requestParams)
    }

    override fun declineSalamItem(requestParams: Map<String, Int>) {
        getHomeViewModel().declineSalamItem(requestParams)
    }

    override fun getRechargeBUWidget(source: WidgetSource) {
        getHomeViewModel().getRechargeBUWidget(source)
    }

    override fun onDynamicChannelRetryClicked() {
        getHomeViewModel().refreshHomeData()
    }

    private fun openApplink(applink: String, trackingAttribution: String) {
        if (!TextUtils.isEmpty(applink)) {
            RouteManager.route(activity, appendTrackerAttributionIfNeeded(applink, trackingAttribution))
        }
    }

    private fun appendTrackerAttributionIfNeeded(applink: String, trackingAttribution: String): String {
        if (TextUtils.isEmpty(trackingAttribution)) {
            return applink
        }
        val newTrackingAttribution = try {
            URLEncoder.encode(trackingAttribution, UTF8_ENCODER)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            trackingAttribution.replace(" ".toRegex(), SPACE_CHAR)
        }
        return if (applink.contains(QUERY_CHAR_1) || applink.contains(QUERY_CHAR_2) || applink.contains(QUERY_CHAR_2)) {
            "$applink&tracker_attribution=$newTrackingAttribution"
        } else {
            "$applink?tracker_attribution=$newTrackingAttribution"
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

    override fun onRetryMembership(position: Int, headerTitle: String) {
        getHomeViewModel().onRefreshMembership(position, headerTitle)
    }

    override fun onRetryWalletApp(position: Int, headerTitle: String) {
        getHomeViewModel().onRefreshWalletApp(position, headerTitle)
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
                TopAdsUrlHitter(context).hitImpressionUrl(
                    className,
                    bannerSlidesModel.topadsViewUrl,
                    bannerSlidesModel.id.toString(),
                    bannerSlidesModel.title + " : " + bannerSlidesModel.creativeName,
                    bannerSlidesModel.imageUrl
                )
            }
            val dataLayer = getBannerImpression(bannerSlidesModel) as HashMap<String, Any>
            dataLayer[KEY_SESSION_IRIS] = getIrisSession().getSessionId()
            putEEToTrackingQueue(dataLayer)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (this::viewModel.isInitialized) {
            trackScreen(isVisibleToUser)
            if (isVisibleToUser) {
                conditionalViewModelRefresh()
            }
            playWidgetOnVisibilityChanged(
                isUserVisibleHint = isVisibleToUser
            )
            adapter?.onResumeSpecialRelease()
            manageCoachmarkOnFragmentVisible(isVisibleToUser)
        }
    }

    private fun manageCoachmarkOnFragmentVisible(isVisibleToUser: Boolean) {
        when (isVisibleToUser) {
            false -> {
                if (tokonowCoachmarkIsShowing) {
                    tokonowCoachmarkIsShowing = false
                    coachmarkTokonow?.hideCoachMark()
                } else if (subscriptionCoachmarkIsShowing) {
                    subscriptionCoachmarkIsShowing = false
                    coachmarkSubscription?.hideCoachMark()
                }
            }
            else -> {
                // no-op
            }
        }
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
        get() = homeRecyclerView?.recycledViewPool ?: RecyclerView.RecycledViewPool()

    override val isHomeFragment: Boolean
        get() {
            if (activity == null) {
                return false
            }
            val fragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container)
            return fragment is HomeRevampFragment
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
        getHomeViewModel().getPopularKeywordOnRefresh()
        PopularKeywordTracking.sendPopularKeywordClickReload(channel, getUserSession().userId)
    }

    override fun onPopularKeywordItemImpressed(channel: DynamicHomeChannel.Channels, position: Int, popularKeywordDataModel: PopularKeywordDataModel, positionInWidget: Int) {
        getTrackingQueueObj()?.putEETracking(PopularKeywordTracking.getPopularKeywordImpressionItem(channel, position, popularKeywordDataModel, positionInWidget, getUserSession().userId) as HashMap<String, Any>)
    }

    override fun onPopularKeywordItemClicked(applink: String, channel: DynamicHomeChannel.Channels, position: Int, popularKeywordDataModel: PopularKeywordDataModel, positionInWidget: Int) {
        RouteManager.route(context, applink)
        PopularKeywordTracking.sendPopularKeywordClickItem(channel, position, popularKeywordDataModel, positionInWidget, getUserSession().userId)
    }

    override fun onBestSellerClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        BestSellerWidgetTracker.sendClickTracker(recommendationItem, bestSellerDataModel, userId, widgetPosition)
        RouteManager.route(context, recommendationItem.appUrl)
    }

    override fun onBestSellerImpress(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        trackingQueue?.putEETracking(BestSellerWidgetTracker.getImpressionTracker(recommendationItem, bestSellerDataModel, userId, widgetPosition) as HashMap<String, Any>)
    }

    override fun onBestSellerThreeDotsClick(bestSellerDataModel: BestSellerDataModel, recommendationItem: RecommendationItem, widgetPosition: Int) {
        recommendationWishlistItem = recommendationItem
        showProductCardOptions(
            this,
            recommendationItem.createProductCardOptionsModel(widgetPosition)
        )
    }

    override fun onBestSellerFilterImpression(
        filter: RecommendationFilterChipsEntity.RecommendationFilterChip,
        bestSellerDataModel: BestSellerDataModel
    ) {
        val filterValue = UrlParamUtils.getParamMap(filter.value)
        val categoryId = filterValue[CATEGORY_ID].toString()

        trackingQueue?.putEETracking(
            BestSellerWidgetTracker.getFilterImpressionTracker(
                categoryId = categoryId,
                channelId = bestSellerDataModel.id,
                headerName = bestSellerDataModel.title,
                userId = userId,
                position = filter.position,
                ncpRank = filter.ncpRank,
                totalFilterCount = bestSellerDataModel.filterChip.size,
                chipsValue = filter.title
            ) as HashMap<String, Any>
        )
    }

    override fun onBestSellerFilterClick(
        filter: RecommendationFilterChipsEntity.RecommendationFilterChip,
        bestSellerDataModel: BestSellerDataModel,
        widgetPosition: Int,
        selectedChipsPosition: Int
    ) {
        val filterValue = UrlParamUtils.getParamMap(filter.value)
        val categoryId = filterValue[CATEGORY_ID].toString()

        BestSellerWidgetTracker.sendFilterClickTracker(
            categoryId = categoryId,
            channelId = bestSellerDataModel.id,
            headerName = bestSellerDataModel.title,
            position = filter.position,
            ncpRank = filter.ncpRank,
            chipsValue = filter.title
        )
        getHomeViewModel().getRecommendationWidget(filter, bestSellerDataModel, selectedChipsPosition = selectedChipsPosition)
    }

    override fun onBestSellerSeeMoreTextClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int) {
        BestSellerWidgetTracker.sendViewAllClickTracker(bestSellerDataModel.id, bestSellerDataModel.title, userId)
        RouteManager.route(context, appLink)
    }

    override fun onBestSellerSeeAllCardClick(bestSellerDataModel: BestSellerDataModel, appLink: String, widgetPosition: Int) {
        BestSellerWidgetTracker.sendViewAllCardClickTracker(bestSellerDataModel.id, bestSellerDataModel.title, userId)
        RouteManager.route(context, appLink)
    }

    override fun onRetryLoadFeeds() {
        getHomeViewModel().getFeedTabData()
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return

        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                recommendationWishlistItem?.isWishlist = !(
                    recommendationWishlistItem?.isWishlist
                        ?: false
                    )

                if (wishlistResult.isAddWishlist) {
                    showToasterSuccessWishlistV2(wishlistResult)
                } else {
                    context?.let { context ->
                        view?.let { v ->
                            AddRemoveWishlistV2Handler.showRemoveWishlistV2SuccessToaster(wishlistResult, context, v)
                        }
                    }
                }
            } else {
                var msg = if (wishlistResult.isAddWishlist) getString(com.tokopedia.wishlist_common.R.string.on_failed_add_to_wishlist_msg) else getString(com.tokopedia.wishlist_common.R.string.on_failed_remove_from_wishlist_msg)
                if (wishlistResult.messageV2.isNotEmpty()) msg = wishlistResult.messageV2

                context?.let { context ->
                    AddRemoveWishlistV2Handler.showWishlistV2ErrorToasterWithCta(
                        msg,
                        wishlistResult.ctaTextV2,
                        wishlistResult.ctaActionV2,
                        root,
                        context
                    )
                }
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    private fun showToasterSuccessWishlistV2(wishlistResult: ProductCardOptionsModel.WishlistResult) {
        context?.let { context ->
            view?.let { v ->
                if (activity?.isFinishing == false) {
                    AddRemoveWishlistV2Handler.showAddToWishlistV2SuccessToaster(
                        wishlistResult,
                        context,
                        v
                    )
                }
            }
        }
    }

    private val isUserLoggedIn: Boolean
        get() = getUserSession().isLoggedIn

    private val userShopId: String
        get() = getUserSession().shopId

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        userVisibleHint = !hidden
    }

    override fun hideEggOnScroll() {
        hideEggFragmentOnScrolling()
    }

    override fun onFeedContentScrolled(dy: Int, totalScrollY: Int) {}

    override fun onFeedContentScrollStateChanged(newState: Int) {}

    private fun goToProductDetail(productId: String) {
        activity?.startActivity(getProductIntent(productId))
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (activity != null) {
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    override fun onNotificationChanged(notificationCount: Int, inboxCount: Int, cartCount: Int) {
    }

    override val homeMainToolbarHeight: Int
        get() {
            var height = requireActivity().resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
            context?.let { context ->
                navToolbar?.let {
                    height = navToolbar?.height
                        ?: context.resources.getDimensionPixelSize(R.dimen.default_toolbar_status_height)
                    height += 8f.toDpInt()
                }
            }
            return height
        }

    private fun showFeedSectionViewHolderShadow(show: Boolean) {
        val feedViewHolder = homeRecyclerView?.findViewHolderForAdapterPosition(
            ((homeRecyclerView?.adapter?.itemCount ?: 0) - 1)
        )
        if (feedViewHolder is HomeRecommendationFeedViewHolder) {
            feedViewHolder.showFeedTabShadow(show)
        }
    }

    private fun initEggDragListener() {
        val floatingEggButtonFragment = floatingEggButtonFragment
        floatingEggButtonFragment?.setOnDragListener(object : FloatingEggButtonFragment.OnDragListener {
            override fun onDragStart() {
                refreshLayout?.setCanChildScrollUp(true)
                refreshLayoutOld?.setCanChildScrollUp(true)
            }

            override fun onDragEnd() {
                refreshLayout?.setCanChildScrollUp(false)
                refreshLayoutOld?.setCanChildScrollUp(false)
            }
        })
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

    override fun onReviewClick(position: Int, clickReviewAt: Int, delay: Long, applink: String) {
        Handler().postDelayed({
            val newAppLink = Uri.parse(applink)
                .buildUpon()
                .appendQueryParameter(REVIEW_CLICK_AT, clickReviewAt.toString())
                .appendQueryParameter(SOURCE, DEFAULT_SOURCE)
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
        if (stickyLoginView?.isShowing() == true) {
            stickyLoginView?.height?.let { params.setMargins(DEFAULT_MARGIN_VALUE, DEFAULT_MARGIN_VALUE, DEFAULT_MARGIN_VALUE, it) }
            val positionEgg = IntArray(POSITION_ARRAY_CONTAINER_SIZE)
            val eggHeight = floatingEggButtonFragment.egg.height
            val stickyTopLocation = stickyLoginView?.getLocation()?.get(POSITION_ARRAY_Y) ?: DEFAULT_MARGIN_VALUE
            floatingEggButtonFragment.egg.getLocationOnScreen(positionEgg)
            if (positionEgg[POSITION_ARRAY_Y] + eggHeight > stickyTopLocation) {
                floatingEggButtonFragment.moveEgg(stickyTopLocation - eggHeight)
            }
        } else {
            params.setMargins(DEFAULT_MARGIN_VALUE, DEFAULT_MARGIN_VALUE, DEFAULT_MARGIN_VALUE, DEFAULT_MARGIN_VALUE)
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

    override fun refreshHomeData(forceRefresh: Boolean) {
        onNetworkRetry()
    }

    override fun onTokopointCheckNowClicked(applink: String) {
        activity?.let {
            if (!TextUtils.isEmpty(applink)) {
                RouteManager.route(activity, applink)
            }
        }
    }

    private fun needToPerformanceMonitoring(data: List<Visitable<*>>): Boolean {
        return homePerformanceMonitoringListener != null && !isOnRecyclerViewLayoutAdded && data.size > 1
    }

    private fun showToaster(message: String, typeToaster: Int) {
        if (activity?.isFinishing == false) {
            showToasterWithAction(message, typeToaster, "", View.OnClickListener { v: View? -> })
        }
    }

    private fun showToasterWithAction(message: String, typeToaster: Int, actionText: String, clickListener: View.OnClickListener) {
        if (errorToaster != null || errorToaster?.isShown != true) {
            errorToaster?.dismiss()
            errorToaster = null
        }
        if (errorToaster == null || errorToaster?.isShown == false) {
            Toaster.toasterCustomBottomHeight = 56f.toDpInt()
            errorToaster = build(root, message, Snackbar.LENGTH_LONG, typeToaster, actionText, clickListener)
            if (activity?.isFinishing == false) {
                errorToaster?.show()
            }
        }
    }

    private fun getPageLoadTimeCallback(): PageLoadTimePerformanceInterface? {
        if (homePerformanceMonitoringListener != null && homePerformanceMonitoringListener?.pageLoadTimePerformanceInterface != null) {
            pageLoadTimeCallback = homePerformanceMonitoringListener?.pageLoadTimePerformanceInterface
        }
        return pageLoadTimeCallback
    }

    override fun onDetach() {
        if (this::viewModel.isInitialized) {
            this.viewModel.get().onCleared()
        }
        super.onDetach()
    }

    fun getHomeViewModel(): HomeRevampViewModel {
        if (!this::viewModel.isInitialized) {
            initInjectorHome()
        }
        val viewmodel = viewModel.get()
        if (getPageLoadTimeCallback() != null) {
            getPageLoadTimeCallback()?.stopPreparePagePerformanceMonitoring()
        }
        return viewmodel
    }

    private fun showToasterReviewSuccess() {
        if (activity?.isFinishing == false) {
            view?.let {
                build(
                    it,
                    getString(
                        R.string.review_create_success_toaster,
                        getHomeViewModel().getUserName()
                    ),
                    Snackbar.LENGTH_LONG,
                    TYPE_NORMAL,
                    getString(R.string.review_oke)
                ).show()
            }
        }
    }

    /**
     * Play Widget
     */
    private fun setupPlayWidgetCoordinator() {
        playWidgetCoordinator = PlayWidgetCoordinator(this, autoHandleLifecycleMethod = false).apply {
            setListener(this@HomeRevampFragment)
        }
    }

    override fun onWidgetOpenAppLink(view: View, appLink: String) {
        context?.let {
            startActivityForResult(RouteManager.getIntent(it, appLink), REQUEST_CODE_PLAY_ROOM_PLAY_WIDGET)
        }
    }

    override fun onToggleReminderClicked(view: PlayWidgetMediumView, channelId: String, reminderType: PlayWidgetReminderType, position: Int) {
        getHomeViewModel().shouldUpdatePlayWidgetToggleReminder(channelId, reminderType)
    }

    override fun onReminderClicked(
        view: PlayWidgetCarouselView,
        channelId: String,
        reminderType: PlayWidgetReminderType,
        position: Int
    ) {
        getHomeViewModel().shouldUpdatePlayWidgetToggleReminder(channelId, reminderType)
    }

    override fun onWidgetShouldRefresh(view: PlayWidgetView) {
        getHomeViewModel().getPlayWidgetWhenShouldRefresh()
    }

    private fun notifyPlayWidgetTotalView(data: Intent) {
        val channelId = data.getStringExtra(PlayWidgetConst.KEY_EXTRA_CHANNEL_ID)
        val totalView = data.getStringExtra(PlayWidgetConst.KEY_EXTRA_TOTAL_VIEW)

        if (channelId == null || totalView == null) return
        getHomeViewModel().updatePlayWidgetTotalView(channelId, totalView)
    }

    private fun notifyPlayWidgetReminder(data: Intent) {
        val channelId = data.getStringExtra(PlayWidgetConst.KEY_EXTRA_CHANNEL_ID)
        val isReminder = data.getBooleanExtra(PlayWidgetConst.KEY_EXTRA_IS_REMINDER, false)

        if (channelId == null) return
        getHomeViewModel().updatePlayWidgetReminder(channelId, isReminder)
    }

    private fun playWidgetOnVisibilityChanged(
        isViewResumed: Boolean = if (view == null) false else viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED),
        isUserVisibleHint: Boolean = userVisibleHint
    ) {
        if (::playWidgetCoordinator.isInitialized) {
            val isViewVisible = isViewResumed && isUserVisibleHint

            if (isViewVisible) {
                playWidgetCoordinator.onResume()
            } else {
                playWidgetCoordinator.onPause()
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun String?.safeEncodeUtf8(): String {
        return try {
            this?.encodeToUtf8() ?: ""
        } catch (throwable: Throwable) {
            ""
        }
    }

    private fun gotoNewUserZone() {
        context?.let {
            if (isRegisteredFromStickyLogin(it)) saveIsRegisteredFromStickyLogin(it, false)
            startActivity(RouteManager.getIntent(it, ApplinkConst.DISCOVERY_NEW_USER))
        }
    }

    private fun isSuccessReset(): Boolean {
        context?.let {
            val isResetPasswordSuccess = it.getSharedPreferences(KEY_MANAGE_PASSWORD, Context.MODE_PRIVATE).getBoolean(IS_SUCCESS_RESET, false)
            if (isResetPasswordSuccess) {
                saveStateReset(false)
                return true
            }
        }
        return false
    }

    private fun saveStateReset(state: Boolean) {
        context?.let {
            sharedPrefs = it.getSharedPreferences(KEY_MANAGE_PASSWORD, Context.MODE_PRIVATE)
            sharedPrefs.run {
                edit().putBoolean(IS_SUCCESS_RESET, state).apply()
            }
        }
    }

    private fun showSuccessResetPasswordDialog() {
        if (activity?.isFinishing == false) {
            Toaster.toasterCustomBottomHeight = 56f.toDpInt()
            Toaster.build(
                root,
                getString(R.string.text_dialog_success_reset_password),
                DELAY_TOASTER_RESET_PASSWORD,
                TYPE_NORMAL,
                getString(R.string.cta_dialog_success_reset_password),
                View.OnClickListener {
                    saveStateReset(false)
                    onGoToLogin()
                }
            ).addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    saveStateReset(false)
                }
            }).show()
        }
    }

    private fun RecommendationItem.createProductCardOptionsModel(position: Int): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = isWishlist
        productCardOptionsModel.productId = productId.toString()
        productCardOptionsModel.isTopAds = isTopAds
        productCardOptionsModel.topAdsWishlistUrl = wishlistUrl
        productCardOptionsModel.productPosition = position
        productCardOptionsModel.screenName = header
        return productCardOptionsModel
    }

    private fun getErrorString(e: Throwable): String {
        return context?.let {
            ErrorHandler.getErrorMessage(it, e)
        } ?: ""
    }

    private fun getErrorStringWithDefault(e: Throwable?): String =
        getErrorString(e ?: MessageErrorException(getString(R.string.home_error_connection)))

    private fun gotoNewUserZonePage() {
        activity?.let {
            val intentNewUser = RouteManager.getIntent(context, ApplinkConst.DISCOVERY_NEW_USER)
            val intentHome = RouteManager.getIntent(activity, ApplinkConst.HOME)
            intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            it.startActivities(arrayOf(intentHome, intentNewUser))
            it.finish()
        }
    }

    override fun onCMHomeWidgetDismissClick() {
        getHomeViewModel().deleteCMHomeWidget()
    }

    override fun onRemoveCMWidgetLocally() {
        getHomeViewModel().deleteCMHomeWidgetLocally()
    }

    override fun getCMHomeWidget() {
        getHomeViewModel().getCMHomeWidgetData()
    }

    override fun getPayLaterWidgetData() {
        getHomeViewModel().getPayLaterWidgetData()
    }

    override fun deletePayLaterWidget() {
        getHomeViewModel().deletePayLaterWidget()
    }

    override fun refreshBalanceWidget() {
        getHomeViewModel().getBalanceWidgetData()
    }

    override fun showHomeCoachmark(
        isShowBalanceWidgetCoachmark: Boolean,
        homeBalanceModel: HomeBalanceModel
    ) {
        if (isShowBalanceWidgetCoachmark) {
            showBalanceWidgetCoachMark(homeBalanceModel)
        } else {
            showTokonowCoachmark()
        }
    }

    private fun showBalanceWidgetCoachMark(homeBalanceModel: HomeBalanceModel) {
        val balanceSubscriptionCoachmark = homeBalanceModel.getSubscriptionBalanceCoachmark()
        if (balanceSubscriptionCoachmark == null && coachmarkSubscription?.isShowing == true) {
            coachmarkSubscription?.hideCoachMark()
        }
        showCoachMark(balanceSubscriptionCoachmark)
    }

    override fun pullRefreshIconCaptured(view: LayoutIconPullRefreshView?) {
        view?.let {
            refreshLayout?.setContentChildViewPullRefresh(it)
        }
    }
}
