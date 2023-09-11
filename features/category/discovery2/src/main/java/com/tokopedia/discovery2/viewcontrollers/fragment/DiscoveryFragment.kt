package com.tokopedia.discovery2.viewcontrollers.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.ADD_PHONE
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery.common.manager.ProductCardOptionsResult
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Constant.DISCO_PAGE_SOURCE
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.dpToPx
import com.tokopedia.discovery2.Utils.Companion.toDecodedString
import com.tokopedia.discovery2.analytics.AFFILIATE
import com.tokopedia.discovery2.analytics.BaseDiscoveryAnalytics
import com.tokopedia.discovery2.analytics.CLICK_SCREENSHOT_SHARE_CHANNEL
import com.tokopedia.discovery2.analytics.CLICK_SHARE_CHANNEL
import com.tokopedia.discovery2.analytics.EMPTY_STRING
import com.tokopedia.discovery2.analytics.EVENT_CLICK_DISCOVERY
import com.tokopedia.discovery2.analytics.SHARE
import com.tokopedia.discovery2.analytics.UNIFY_CLICK_SHARE
import com.tokopedia.discovery2.analytics.UNIFY_CLOSE_SCREENSHOT_SHARE
import com.tokopedia.discovery2.analytics.UNIFY_CLOSE_SHARE
import com.tokopedia.discovery2.analytics.UTM_DISCOVERY
import com.tokopedia.discovery2.analytics.VIEW_DISCOVERY_IRIS
import com.tokopedia.discovery2.analytics.VIEW_SCREENSHOT_SHARE
import com.tokopedia.discovery2.analytics.VIEW_UNIFY_SHARE
import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.data.ParamsForOpenScreen
import com.tokopedia.discovery2.data.ScrollData
import com.tokopedia.discovery2.data.productcarditem.DiscoATCRequestParams
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.datamapper.discoveryPageData
import com.tokopedia.discovery2.datamapper.getSectionPositionMap
import com.tokopedia.discovery2.datamapper.setCartData
import com.tokopedia.discovery2.di.DiscoveryComponent
import com.tokopedia.discovery2.di.UIWidgetComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.AFFILIATE_UNIQUE_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CAMPAIGN_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CHANNEL
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.COMPONENT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.DYNAMIC_SUBTITLE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.FORCED_NAVIGATION
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.HIDE_NAV_FEATURES
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.QUERY_PARENT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.RECOM_PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SHOP_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SOURCE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_TITLE_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.VARIANT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher.DiscoMerchantVoucherViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs.TabsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.customview.CustomTopChatView
import com.tokopedia.discovery2.viewcontrollers.customview.StickyHeadRecyclerView
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.discovery2.viewmodel.livestate.GoToAgeRestriction
import com.tokopedia.discovery2.viewmodel.livestate.RouteToApplink
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.IntentManger
import com.tokopedia.mvcwidget.IntentManger.Keys.REGISTER_MEMBER_SUCCESS
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.play.widget.const.PlayWidgetConst
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconBuilderFlag
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.searchbar.navigation_component.listener.NavRecyclerViewScrollListener
import com.tokopedia.searchbar.navigation_component.util.StatusBarUtil
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.universal_sharing.view.model.Shop
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import com.tokopedia.unifyprinciples.R as RUnify

private const val LOGIN_REQUEST_CODE = 35769
private const val MOBILE_VERIFICATION_REQUEST_CODE = 35770
const val PAGE_REFRESH_LOGIN = 35771
private const val OPEN_PLAY_CHANNEL = 35772
private const val SCROLL_TOP_DIRECTION = -1
private const val DEFAULT_SCROLL_POSITION = 0
private const val ROTATION = 90f
const val CUSTOM_SHARE_SHEET = 1

open class DiscoveryFragment :
    BaseDaggerFragment(),
    SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener,
    LihatSemuaViewHolder.OnLihatSemuaClickListener,
    TabLayout.OnTabSelectedListener,
    ChooseAddressWidget.ChooseAddressWidgetListener,
    ShareBottomsheetListener,
    ScreenShotListener,
    PermissionListener,
    MiniCartWidgetListener {

    private var recyclerViewPaddingResetNeeded: Boolean = false
    private var thematicHeaderColor: String = ""
    private var navScrollListener: NavRecyclerViewScrollListener? = null
    private var autoScrollSectionID: String? = null
    private var anchorViewHolder: AnchorTabsViewHolder? = null
    lateinit var discoveryViewModel: DiscoveryViewModel
    lateinit var discoveryComponent: DiscoveryComponent
    private lateinit var mDiscoveryFab: CustomTopChatView
    private lateinit var mAnchorHeaderView: FrameLayout
    private lateinit var recyclerView: StickyHeadRecyclerView
    private lateinit var ivToTop: ImageView
    private lateinit var globalError: GlobalError
    private lateinit var navToolbar: NavToolbar
    private lateinit var discoveryAdapter: DiscoveryRecycleAdapter
    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var chooseAddressWidgetDivider: View? = null
    private var shouldShowChooseAddressWidget: Boolean = true
    private var hideShowChangeAvailable: Boolean = true
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var parentLayout: FrameLayout
    private lateinit var appBarLayout: AppBarLayout
    private var pageInfoHolder: PageInfo? = null
    private var miniCartWidget: MiniCartWidget? = null
    private var miniCartData: MiniCartSimplifiedData? = null
    private var miniCartInitialized: Boolean = false
    private var userPressed: Boolean = false
    private var isTabPresentToDoubleScroll: Boolean = false

    private val isFromCategory: Boolean by lazy {
        (context as? DiscoveryActivity)?.isFromCategory() ?: false
    }

    private val analytics: BaseDiscoveryAnalytics by lazy {
        provideAnalytics()
    }

    open fun provideAnalytics(): BaseDiscoveryAnalytics {
        return (context as DiscoveryActivity).getAnalytics()
    }

    val trackingQueue: TrackingQueue by lazy {
        provideTrackingQueue()
    }
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    open fun provideTrackingQueue(): TrackingQueue {
        return (context as DiscoveryActivity).trackingQueue
    }

    private lateinit var mProgressBar: LoaderUnify
    var pageEndPoint = ""
    private var componentPosition: Int? = null
    private var openScreenStatus = false
    private var pinnedAlreadyScrolled = false
    var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null
    private var userAddressData: LocalCacheModel? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var lastVisibleComponent: ComponentsItem? = null
    private var screenScrollPercentage = 0
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector: ScreenshotDetector? = null
    private var shareType: Int = 1
    var currentTabPosition: Int? = null
    var isAffiliateInitialized = false
        private set
    private var isFromForcedNavigation = false

    private var isManualScroll = true
    private var stickyHeaderShowing = false
    private var hasColouredHeader: Boolean = false
    private var isLightThemeStatusBar: Boolean? = null

    companion object {
        private const val FIRST_POSITION = 0

        fun getInstance(
            endPoint: String?,
            queryParameterMap: Map<String, String?>?
        ): DiscoveryFragment {
            val bundle = Bundle()
            val fragment = DiscoveryFragment()
            if (!endPoint.isNullOrEmpty()) {
                bundle.putString(END_POINT, endPoint)
            }
            getQueryParams(bundle, queryParameterMap)
            fragment.arguments = bundle
            return fragment
        }

        private fun getQueryParams(bundle: Bundle, queryParameterMap: Map<String, String?>?) {
            queryParameterMap?.let {
                bundle.putString(SOURCE, queryParameterMap[SOURCE])
                bundle.putString(COMPONENT_ID, queryParameterMap[COMPONENT_ID])
                bundle.putString(ACTIVE_TAB, queryParameterMap[ACTIVE_TAB])
                bundle.putString(HIDE_NAV_FEATURES, queryParameterMap[HIDE_NAV_FEATURES])
                bundle.putString(TARGET_COMP_ID, queryParameterMap[TARGET_COMP_ID])
                bundle.putString(PRODUCT_ID, queryParameterMap[PRODUCT_ID])
                bundle.putString(PIN_PRODUCT, queryParameterMap[PIN_PRODUCT])
                bundle.putString(CATEGORY_ID, queryParameterMap[CATEGORY_ID])
                bundle.putString(EMBED_CATEGORY, queryParameterMap[EMBED_CATEGORY])
                bundle.putString(RECOM_PRODUCT_ID, queryParameterMap[RECOM_PRODUCT_ID])
                bundle.putString(DYNAMIC_SUBTITLE, queryParameterMap[DYNAMIC_SUBTITLE])
                bundle.putString(TARGET_TITLE_ID, queryParameterMap[TARGET_TITLE_ID])
                bundle.putString(CAMPAIGN_ID, queryParameterMap[CAMPAIGN_ID])
                bundle.putString(VARIANT_ID, queryParameterMap[VARIANT_ID])
                bundle.putString(SHOP_ID, queryParameterMap[SHOP_ID])
                bundle.putString(QUERY_PARENT, queryParameterMap[QUERY_PARENT])
                bundle.putString(AFFILIATE_UNIQUE_ID, queryParameterMap[AFFILIATE_UNIQUE_ID])
                bundle.putString(CHANNEL, queryParameterMap[CHANNEL])
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discovery, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        with(context) {
            if (this is DiscoveryActivity) {
                this.discoveryComponent
                    .inject(this@DiscoveryFragment)
                this@DiscoveryFragment.discoveryComponent = this.discoveryComponent
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDiscoveryFab = view.findViewById(R.id.fab)
        initToolbar(view)
        initChooseAddressWidget(view)
        initView(view)
        context?.let {
            screenshotDetector = SharingUtil.createAndStartScreenShotDetector(
                it,
                this,
                this,
                addFragmentLifecycleObserver = true,
                permissionListener = this
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenshotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    // need to surpress this one, since there are no pii related data defined on this class
    @SuppressLint("PII Data Exposure")
    private fun initChooseAddressWidget(view: View) {
        chooseAddressWidget = view.findViewById(R.id.choose_address_widget)
        chooseAddressWidgetDivider = view.findViewById(R.id.divider_view)
        chooseAddressWidget?.bindChooseAddress(this)
    }

    private fun initToolbar(view: View) {
        navToolbar = view.findViewById(R.id.navToolbar)
        viewLifecycleOwner.lifecycle.addObserver(navToolbar)
        navToolbar.setOnBackButtonClickListener(
            disableDefaultGtmTracker = true,
            backButtonClickListener = ::handleBackPress
        )
        if (arguments?.getString(DISCO_PAGE_SOURCE) == Constant.DiscoveryPageSource.HOME) {
            navToolbar.setBackButtonType(NavToolbar.Companion.BackType.BACK_TYPE_NONE)
            navToolbar.setIcon(
                IconBuilder(IconBuilderFlag(NavSource.SOS))
                    .addIcon(
                        IconList.ID_MESSAGE,
                        onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.INBOX) },
                        disableDefaultGtmTracker = false
                    )
                    .addIcon(
                        IconList.ID_NOTIFICATION,
                        onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.NOTIF) },
                        disableDefaultGtmTracker = false
                    )
                    .addIcon(
                        iconId = IconList.ID_CART,
                        onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) },
                        disableDefaultGtmTracker = true
                    )
                    .addIcon(
                        iconId = IconList.ID_NAV_GLOBAL,
                        onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) },
                        disableDefaultGtmTracker = true
                    )
            )
        }
    }

    private fun initView(view: View) {
        mAnchorHeaderView = view.findViewById(R.id.header_comp_holder)
        globalError = view.findViewById(R.id.global_error)
        recyclerView = view.findViewById(R.id.discovery_recyclerView)
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        mProgressBar = view.findViewById(R.id.progressBar)
        ivToTop = view.findViewById(R.id.toTopImg)
        coordinatorLayout = view.findViewById(R.id.parent_coordinator)
        parentLayout = view.findViewById(R.id.parent_frame)
        appBarLayout = view.findViewById(R.id.appbarLayout)
        miniCartWidget = view.findViewById(R.id.miniCartWidget)

        mProgressBar.show()
        mSwipeRefreshLayout?.setOnRefreshListener(this)
        ivToTop.setOnClickListener(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var dy = 0
            var dx = 0
            var scrollDist = 0
            val MINIMUM = 25.toPx()

            @SuppressLint("PII Data Exposure")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this.dy = dy
                this.dx = dx
                if (dy >= 0) {
                    ivToTop.hide()
                    calculateScrollDepth(recyclerView)
                } else if (dy < 0) {
                    ivToTop.show()
                }
                scrollDist += dy
                if (recyclerView.canScrollVertically(SCROLL_TOP_DIRECTION)) {
                    if (dy > 0 && shouldShowChooseAddressWidget && scrollDist > MINIMUM) {
                        shouldShowChooseAddressWidget = false
                        hideShowChangeAvailable = true
                        recyclerView.postDelayed(100) {
                            hideShowChooseAddressOnScroll()
                        }
                    }
                } else {
                    if (dy <= 0 && !shouldShowChooseAddressWidget && discoveryViewModel.getAddressVisibilityValue()) {
                        shouldShowChooseAddressWidget = true
                        hideShowChangeAvailable = true
                        recyclerView.postDelayed(100) {
                            hideShowChooseAddressOnScroll()
                        }
                    }
                }
                enableRefreshWhenFirstItemCompletelyVisible()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_TOP_DIRECTION) && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                    ivToTop.hide()
                }
                if (scrollDist > MINIMUM) {
                    scrollDist = 0
                    discoveryViewModel.updateScroll(dx, dy, newState, userPressed)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        scrollToLastSection()
                    }
                } else if (scrollDist < -MINIMUM) {
                    scrollDist = 0
                    discoveryViewModel.updateScroll(dx, dy, newState, userPressed)
                    if (mAnchorHeaderView.childCount == 0) {
                        setupObserveAndShowAnchor()
                    }
                    if (userPressed && recyclerViewPaddingResetNeeded) {
                        recyclerViewPaddingResetNeeded = false
                        recyclerView.setPadding(0, 0, 0, 0)
                    }
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        scrollToLastSection()
                    }
                }
            }
        })
        recyclerView.setOnTouchListenerRecyclerView { v, event ->
            userPressed = true
            isTabPresentToDoubleScroll = false
            if (event.actionMasked == MotionEvent.ACTION_UP) {
                v.performClick()
            }
            false
        }
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                userPressed = true
                isTabPresentToDoubleScroll = false
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                userPressed = true
                isTabPresentToDoubleScroll = false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                userPressed = true
                isTabPresentToDoubleScroll = false
            }
        })

        val searchBarTransitionRange = context?.resources?.getDimensionPixelSize(R.dimen.dp_16) ?: 0
        navScrollListener = NavRecyclerViewScrollListener(
            navToolbar = navToolbar,
            startTransitionPixel = homeMainToolbarHeight,
            toolbarTransitionRangePixel = searchBarTransitionRange,
            navScrollCallback = object : NavRecyclerViewScrollListener.NavScrollCallback {
                override fun onAlphaChanged(offsetAlpha: Float) {
                }

                override fun onSwitchToDarkToolbar() {
                    if (hasColouredHeader) {
                        if (isLightThemeStatusBar != true) {
                            requestStatusBarLight()
                            navToolbar.hideShadow()
                            if (discoveryViewModel.getAddressVisibilityValue()) {
                                setupHexBackgroundColor(thematicHeaderColor)
                            }
                        }
                    }
                }

                override fun onSwitchToLightToolbar() {
                    if (hasColouredHeader) {
                        if (isLightThemeStatusBar != false) {
                            requestStatusBarDark()
                            // Don't uncomment - It will show a black line between toolbar and choose address in dark mode
                            //           navToolbar.setShowShadowEnabled(true)
                            //           navToolbar.showShadow(true)
                        }
                    }
                }

                override fun onYposChanged(yOffset: Int) {
                }
            }
        )
        if (arguments?.getString(DISCO_PAGE_SOURCE) == Constant.DiscoveryPageSource.HOME) {
            activity?.let {
                navToolbar.setupToolbarWithStatusBar(it)
            }
        }
    }

    private fun enableRefreshWhenFirstItemCompletelyVisible() {
        if (mSwipeRefreshLayout?.isRefreshing == false) {
            val firstPosition: Int = staggeredGridLayoutManager?.findFirstCompletelyVisibleItemPositions(null)?.getOrNull(FIRST_POSITION).orZero()
            mSwipeRefreshLayout?.isEnabled = firstPosition == FIRST_POSITION
        }
    }

    private fun hideShowChooseAddressOnScroll() {
        if (!hideShowChangeAvailable) {
            return
        }
        hideShowChangeAvailable = false
        if (!shouldShowChooseAddressWidget) {
            chooseAddressWidget?.hide()
            chooseAddressWidgetDivider?.hide()
        } else {
            chooseAddressWidget?.show()
            if (isLightThemeStatusBar != true) {
                chooseAddressWidgetDivider?.show()
            } else {
                chooseAddressWidgetDivider?.hide()
            }
        }
    }

    private fun scrollToLastSection() {
        if (!userPressed && !autoScrollSectionID.isNullOrEmpty()) {
            scrollToSection(autoScrollSectionID!!)
        } else if (!userPressed && isTabPresentToDoubleScroll) {
//            isTabPresentToDoubleScroll = false
            pinnedAlreadyScrolled = false
            discoveryAdapter.currentList.let {
                if (it.isNotEmpty()) {
                    scrollToPinnedComponent(it)
                }
            }
        }
    }

    private fun calculateScrollDepth(recyclerView: RecyclerView) {
        if (::discoveryViewModel.isInitialized) {
            val offset =
                recyclerView.computeVerticalScrollOffset() // area of view not visible on screen
            val extent =
                recyclerView.computeVerticalScrollExtent() // area of view visible on screen
            val range = recyclerView.computeVerticalScrollRange() // max scroll height

            val currentScrollDepth = discoveryViewModel.getScrollDepth(offset, extent, range)
            if (screenScrollPercentage == 0 || currentScrollDepth > screenScrollPercentage) {
                screenScrollPercentage = currentScrollDepth
            }
            updateLastVisibleComponent()
        }
    }

    private fun updateLastVisibleComponent() {
        if (lastVisibleComponent != null && (
            lastVisibleComponent?.name ==
                ComponentsList.ProductCardRevamp.componentName || lastVisibleComponent?.name ==
                ComponentsList.ProductCardSprintSale.componentName
            )
        ) {
            return
        }
        staggeredGridLayoutManager?.findLastVisibleItemPositions(null)?.let { positionArray ->
            if (positionArray.isNotEmpty() && positionArray.first() >= 0) {
                if (discoveryAdapter.currentList.size <= positionArray.first()) return
                lastVisibleComponent = discoveryAdapter.currentList[positionArray.first()]

                if (lastVisibleComponent != null && (
                    lastVisibleComponent?.name ==
                        ComponentsList.ProductCardRevampItem.componentName || lastVisibleComponent?.name ==
                        ComponentsList.ProductCardSprintSaleItem.componentName ||
                        lastVisibleComponent?.name == ComponentsList.ShimmerProductCard.componentName
                    )
                ) {
                    lastVisibleComponent = com.tokopedia.discovery2.datamapper
                        .getComponent(
                            lastVisibleComponent!!.parentComponentId,
                            lastVisibleComponent!!.pageEndPoint
                        )
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDiscoveryViewModel()
        discoveryViewModel.sendCouponInjectDataForLoggedInUsers()
        /** Future Improvement : Please don't remove any commented code from this file. Need to work on this **/
//        mDiscoveryViewModel = ViewModelProviders.of(requireActivity()).get((activity as BaseViewModelActivity<DiscoveryViewModel>).getViewModelType())
        setAdapter()
        discoveryViewModel.pageIdentifier = arguments?.getString(END_POINT, "") ?: ""
        pageEndPoint = discoveryViewModel.pageIdentifier
        checkForSamePageOpened()
        fetchDiscoveryPageData()
        setUpObserver()
    }

    open fun injectDiscoveryViewModel() {
        discoveryViewModel = (activity as DiscoveryActivity).getViewModel()
    }

    private fun checkForSamePageOpened() {
        discoveryViewModel.checkForSamePageOpened(
            discoveryViewModel.getQueryParameterMapFromBundle(
                arguments
            )
        )
    }

    private fun setAdapter() {
        recyclerView.apply {
            addDecorator(MasterProductCardItemDecorator())
            staggeredGridLayoutManager = getLayoutManager()
            setLayoutManager(staggeredGridLayoutManager!!)
            discoveryAdapter = DiscoveryRecycleAdapter(this@DiscoveryFragment).also {
                setAdapter(it)
            }
        }
    }

    private fun getLayoutManager(): StaggeredGridLayoutManager {
        return object : StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
            override fun supportsPredictiveItemAnimations(): Boolean {
                return false
            }

            override fun onLayoutChildren(
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: Exception) {
                    Utils.logException(e)
                }
            }

            override fun onItemsUpdated(
                recyclerView: RecyclerView,
                positionStart: Int,
                itemCount: Int,
                payload: Any?
            ) {
                try {
                    super.onItemsUpdated(recyclerView, positionStart, itemCount, payload)
                } catch (e: Exception) {
                    Utils.logException(e)
                }
            }

            override fun onItemsAdded(
                recyclerView: RecyclerView,
                positionStart: Int,
                itemCount: Int
            ) {
                try {
                    super.onItemsAdded(recyclerView, positionStart, itemCount)
                } catch (e: Exception) {
                    Utils.logException(e)
                }
            }

            override fun onItemsRemoved(
                recyclerView: RecyclerView,
                positionStart: Int,
                itemCount: Int
            ) {
                try {
                    super.onItemsRemoved(recyclerView, positionStart, itemCount)
                } catch (e: Exception) {
                    Utils.logException(e)
                }
            }

            override fun onItemsMoved(
                recyclerView: RecyclerView,
                from: Int,
                to: Int,
                itemCount: Int
            ) {
                try {
                    super.onItemsMoved(recyclerView, from, to, itemCount)
                } catch (e: Exception) {
                    Utils.logException(e)
                }
            }
        }
    }

    fun reSync() {
        fetchDiscoveryPageData()
    }

    private fun setUpObserver() {
        discoveryViewModel.getDiscoveryResponseList().observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.let { listComponent ->
                        if (mSwipeRefreshLayout?.isRefreshing == true) setAdapter()
                        discoveryAdapter.addDataList(listComponent)
                        if (listComponent.isEmpty()) {
                            discoveryAdapter.addDataList(ArrayList())
                            setPageErrorState(Fail(IllegalStateException()))
                        } else {
                            hideGlobalError()
                            scrollToPinnedComponent(listComponent)
                            removePaddingIfComponent()
                        }
                    }
                    mProgressBar.hide()
                    stopDiscoveryPagePerformanceMonitoring()
                    recyclerView.post {
                        scrollToLastSection()
                        addMarginInRuntime(it.data)
                    }
                }

                is Fail -> {
                    mProgressBar.hide()
                    setPageErrorState(it)
                }
            }
            mSwipeRefreshLayout?.isEnabled = true
            mSwipeRefreshLayout?.isRefreshing = false
        }

        discoveryViewModel.getDiscoveryFabLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.data?.get(0)?.let { data ->
                        setFloatingActionButton(data)
                        setAnimationOnScroll()
                    }
                }

                is Fail -> {
                    mDiscoveryFab.hide()
                }
            }
        }

        discoveryViewModel.getDiscoveryPageInfo().observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    pageInfoHolder = it.data
                    setToolBarPageInfoOnSuccess(it.data)
                    setupBackgroundForHeader(it.data)
                    addMiniCartToPageFirstTime()
                    setupAffiliate()
                }

                is Fail -> {
                    discoveryAdapter.addDataList(ArrayList())
                    setToolBarPageInfoOnFail()
                    setPageErrorState(it)
                }
            }
        }

        discoveryViewModel.getDiscoveryLiveStateData().observe(viewLifecycleOwner) {
            when (it) {
                is RouteToApplink -> {
                    RouteManager.route(context, it.applink)
                    activity?.finish()
                }

                is GoToAgeRestriction -> {
                    AdultManager.showAdultPopUp(this, it.origin, it.departmentId)
                }
            }
        }

        discoveryViewModel.getDiscoveryAnchorTabLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    setupAnchorTabComponent(it)
                }

                is Fail -> {
                    resetAnchorTabs()
                }
            }
        }

        discoveryViewModel.checkAddressVisibility()
            .observe(viewLifecycleOwner) { widgetVisibilityStatus ->
                context?.let {
                    if (widgetVisibilityStatus) {
                        if (shouldShowChooseAddressWidget) {
                            chooseAddressWidget?.show()
                            chooseAddressWidgetDivider?.show()
                        }
                        if (ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(it) == true) {
                            ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(it)
                            showLocalizingAddressCoachMark()
                        }
                        fetchUserLatestAddressData()
                    } else {
                        chooseAddressWidget?.hide()
                        chooseAddressWidgetDivider?.hide()
                    }
                }
            }

        discoveryViewModel.miniCart.observe(viewLifecycleOwner) {
            if (it is Success) {
                setupMiniCart(it.data)
            }
        }

        discoveryViewModel.miniCartAdd.observe(viewLifecycleOwner) {
            if (it is Success) {
                if (it.data.requestParams.isGeneralCartATC) {
                    showToasterWithAction(
                        message = it.data.addToCartDataModel.errorMessage.joinToString(separator = ", "),
                        Toaster.LENGTH_LONG,
                        type = Toaster.TYPE_NORMAL,
                        actionText = getString(R.string.disco_lihat),
                        clickListener = {
                            context?.let { context ->
                                RouteManager.route(context, ApplinkConst.CART)
                            }
                        }
                    )
                    analytics.trackEventProductATC(
                        it.data.requestParams.requestingComponent,
                        it.data.addToCartDataModel.data.cartId
                    )
                } else {
                    analytics.trackEventProductATCTokonow(
                        it.data.requestParams.requestingComponent,
                        it.data.addToCartDataModel.data.cartId
                    )
                    getMiniCart()
                    showToaster(
                        message = it.data.addToCartDataModel.errorMessage.joinToString(separator = ", "),
                        type = Toaster.TYPE_NORMAL
                    )
                }
            } else if (it is Fail) {
                if (it.throwable is ResponseErrorException) {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        discoveryViewModel.miniCartUpdate.observe(viewLifecycleOwner) {
            if (it is Success) {
                analytics.trackEventProductATCTokonow(
                    it.data.requestParams.requestingComponent,
                    it.data.cartId
                )
                getMiniCart()
            } else if (it is Fail) {
                if (it.throwable is ResponseErrorException) {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        discoveryViewModel.miniCartRemove.observe(viewLifecycleOwner) {
            if (it is Success) {
                analytics.trackEventProductATCTokonow(
                    it.data.requestParams.requestingComponent,
                    it.data.cartId
                )
                getMiniCart()
                showToaster(
                    message = it.data.message,
                    type = Toaster.TYPE_NORMAL
                )
            } else if (it is Fail) {
                if (it.throwable is ResponseErrorException) {
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        discoveryViewModel.miniCartOperationFailed.observe(viewLifecycleOwner) { (parentPosition, position) ->
            if (parentPosition >= 0) {
                discoveryAdapter.getViewModelAtPosition(parentPosition)
                    ?.let { discoveryBaseViewModel ->
                        if (discoveryBaseViewModel is ProductCardCarouselViewModel) {
                            discoveryBaseViewModel.handleAtcFailed(position)
                        }
                    }
            } else if (position >= 0) {
                discoveryAdapter.getViewModelAtPosition(position)?.let { discoveryBaseViewModel ->
                    if (discoveryBaseViewModel is MasterProductCardItemViewModel) {
                        discoveryBaseViewModel.handleATCFailed()
                    }
                }
            }
        }
    }

    private fun addMarginInRuntime(data: List<ComponentsItem>) {
        val componentsToExclude = mutableSetOf<String>(
            ComponentsList.CLPFeatureProducts.componentName,
            ComponentsList.MerchantVoucherCarousel.componentName,
            ComponentsList.ProductCardRevamp.componentName,
            ComponentsList.LihatSemua.componentName
        )
        data.let { data ->
            data.forEachIndexed { index, item ->
                if (item.name == ComponentNames.Tabs.componentName) {
                    val tabsViewModel = discoveryAdapter.getTabItem() as? TabsViewModel
                    if (componentsToExclude.contains(data.getOrNull(index + 1)?.name ?: "")) {
                        tabsViewModel?.shouldAddSpace(false)
                    } else if (data.getOrNull(index + 1)?.name == ComponentsList.Section.componentName) {
                        val latestComponent = data.getOrNull(index + 1)?.getComponentsItem()?.getOrNull(0)?.name
                            ?: return@let
                        if (latestComponent == ComponentsList.LihatSemua.componentName || componentsToExclude.contains(latestComponent)) {
                            tabsViewModel?.shouldAddSpace(false)
                        } else {
                            tabsViewModel?.shouldAddSpace(true)
                        }
                    } else {
                        tabsViewModel?.shouldAddSpace(true)
                    }
                    return@let
                }
            }
        }
    }

    private fun setupBackgroundForHeader(data: PageInfo?) {
        if (!data?.thematicHeader?.color.isNullOrEmpty()) {
            hasColouredHeader = true
            activity?.let { navToolbar.setupToolbarWithStatusBar(it) }
            context?.let {
                navToolbar.setIconCustomColor(getDarkIconColor(it), getLightIconColor(it))
            }
            if (isLightThemeStatusBar == true) {
                navToolbar.hideShadow()
            } else {
                // Don't uncomment - It will show a black line between toolbar and choose address in dark mode
                //         navToolbar.setShowShadowEnabled(true)
                //          navToolbar.showShadow(true)
                navToolbar.hideShadow()
            }
            appBarLayout.elevation = 0f
            setupHexBackgroundColor(data?.thematicHeader?.color ?: "")
            setupNavScrollListener()
        } else {
            hasColouredHeader = false
        }
    }

    private fun setupHexBackgroundColor(color: String) {
        thematicHeaderColor = color
        try {
            val colorResource = Color.parseColor(color)
            if (discoveryViewModel.getAddressVisibilityValue() && isLightThemeStatusBar != false) {
                chooseAddressWidget?.background = ColorDrawable(colorResource)
                chooseAddressWidget?.updateWidget()
                chooseAddressWidgetDivider?.hide()
            }
            appBarLayout.setBackgroundColor(colorResource)
        } catch (e: Exception) {
            e
        }
    }

    private fun setupNavScrollListener() {
        navScrollListener?.let {
            recyclerView.removeOnScrollListener(it)
            recyclerView.addOnScrollListener(it)
        }
    }

    private fun setupAffiliate() {
        if (pageInfoHolder?.isAffiliate == true && !discoveryViewModel.isAffiliateInitialized) {
            isAffiliateInitialized = true
            discoveryViewModel.initAffiliateSDK()
        }
    }

    private fun setupAnchorTabComponent(it: Success<ComponentsItem>) {
        if (anchorViewHolder == null) {
            val view = layoutInflater.inflate(ComponentsList.AnchorTabs.id, null, false)
            anchorViewHolder = AnchorTabsViewHolder(view, this).apply {
                uiWidgetComponent = provideSubComponent()
            }
            val viewModel =
                AnchorTabsViewModel(context?.applicationContext as Application, it.data, 0)
            anchorViewHolder?.bindView(viewModel)
            viewModel.onAttachToViewHolder()
            anchorViewHolder?.onViewAttachedToWindow()
        }
        setupObserveAndShowAnchor()
    }

    private fun setupObserveAndShowAnchor() {
        if (!stickyHeaderShowing) {
            anchorViewHolder?.let {
                it.viewModel?.let { anchorTabsViewModel ->
                    if (!anchorTabsViewModel.getCarouselItemsListData().hasActiveObservers()) {
                        anchorViewHolder?.setUpObservers(viewLifecycleOwner)
                    }
                    if (mAnchorHeaderView.findViewById<RecyclerView>(R.id.anchor_rv) == null) {
                        mAnchorHeaderView.removeAllViews()
                        (anchorViewHolder?.itemView?.parent as? FrameLayout)?.removeView(
                            anchorViewHolder?.itemView
                        )
                        mAnchorHeaderView.addView(it.itemView)
                    }
                }
            }
        }
    }

    private fun showToaster(message: String, duration: Int = Toaster.LENGTH_SHORT, type: Int) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type
                ).show()
            }
        }
    }

    private fun showToasterWithAction(
        message: String,
        duration: Int = Toaster.LENGTH_SHORT,
        type: Int,
        actionText: String,
        clickListener: View.OnClickListener = View.OnClickListener {}
    ) {
        view?.let { view ->
            if (message.isNotBlank()) {
                Toaster.build(
                    view = view,
                    text = message,
                    duration = duration,
                    type = type,
                    actionText = actionText,
                    clickListener = clickListener
                ).show()
            }
        }
    }

    private fun showLocalizingAddressCoachMark() {
        chooseAddressWidget?.let {
            val coachMarkItem = ArrayList<CoachMark2Item>()
            val coachMark = CoachMark2(requireContext())
            coachMark.isOutsideTouchable = true
            coachMarkItem.add(
                CoachMark2Item(
                    it,
                    getString(R.string.choose_address_title),
                    getString(R.string.choose_address_description)
                )
            )
            coachMark.showCoachMark(coachMarkItem)
        }
    }

    // Setting NavToolbar on Success Response
    private fun setToolBarPageInfoOnSuccess(data: PageInfo?) {
        settingUpNavBar(data)
        setSearchBar(data)
    }

    // Setting NavToolbar on Fail Response
    private fun setToolBarPageInfoOnFail() {
        setCartAndNavIcon()
        setSearchBar(null)
        mProgressBar.hide()
        mSwipeRefreshLayout?.isRefreshing = false
    }

    private fun settingUpNavBar(data: PageInfo?) {
        if (arguments?.getString(DISCO_PAGE_SOURCE) == Constant.DiscoveryPageSource.HOME) {
            navToolbar.updateNotification()
            return
        }
        handleHideNavbarIcons(data)
    }

    private fun handleHideNavbarIcons(data: PageInfo?) {
        val hideNaveFeaturesValue = arguments?.getString(HIDE_NAV_FEATURES, "")
        if (hideNaveFeaturesValue.isNullOrEmpty()) {
            navWithShareData(data)
        } else {
            val navIcons = Utils.navIcons(
                getNavIconBuilderFlag(),
                onClick = { handleShareClick(data) },
                handleGlobalNavCartClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) },
                handleGlobalMenuCartClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) }
            )
            navIcons[requireArguments().getString(HIDE_NAV_FEATURES)?.toIntOrZero()]?.let {
                navToolbar.setIcon(
                    it
                )
            }
            navToolbar.updateNotification()
        }
    }

    private fun setSearchBar(data: PageInfo?) {
        if (arguments?.getString(HIDE_NAV_FEATURES)?.toIntOrZero() !in listOf(3, 13, 23, 123)) {
            navToolbar.setupSearchbar(
                hints = listOf(
                    HintData(
                        placeholder = data?.searchTitle
                            ?: getString(R.string.discovery_default_search_title)
                    )
                ),
                searchbarClickCallback = {
                    handleGlobalNavClick(Constant.TOP_NAV_BUTTON.SEARCH_BAR)
                    handleSearchClick(data)
                },
                disableDefaultGtmTracker = true
            )
        } else {
            navToolbar.setSearchBarAlpha(0F)
        }
    }

    private fun navWithShareData(data: PageInfo?) {
        if (data?.share?.enabled == true) {
            navToolbar.setIcon(
                IconBuilder(getNavIconBuilderFlag())
                    .addIcon(
                        iconId = IconList.ID_SHARE,
                        disableRouteManager = true,
                        onClick = { handleShareClick(data) },
                        disableDefaultGtmTracker = true
                    )
                    .addIcon(
                        iconId = IconList.ID_CART,
                        onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) },
                        disableDefaultGtmTracker = true
                    )
                    .addIcon(
                        iconId = IconList.ID_NAV_GLOBAL,
                        onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) },
                        disableDefaultGtmTracker = true
                    )
            )
            navToolbar.updateNotification()
        } else {
            setCartAndNavIcon()
        }
    }

    private fun setCartAndNavIcon() {
        navToolbar.setIcon(
            IconBuilder(getNavIconBuilderFlag())
                .addIcon(
                    iconId = IconList.ID_CART,
                    onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) },
                    disableDefaultGtmTracker = true
                )
                .addIcon(
                    iconId = IconList.ID_NAV_GLOBAL,
                    onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) },
                    disableDefaultGtmTracker = true
                )
        )
        navToolbar.updateNotification()
    }

    private fun handleShareClick(data: PageInfo?) {
        handleGlobalNavClick(Constant.TOP_NAV_BUTTON.SHARE)
        if (SharingUtil.isCustomSharingEnabled(context)) {
            sendUnifyShareGTM()
            showUniversalShareBottomSheet(data)
        } else {
            discoDefaultShare(data)
        }
    }

    private fun discoDefaultShare(data: PageInfo?) {
        data?.let {
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(
                    0,
                    linkerDataMapper(it),
                    object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult) {
                            if (linkerShareData.url != null) {
                                Utils.shareData(
                                    activity,
                                    it.share?.description,
                                    linkerShareData.url
                                )
                            }
                        }

                        override fun onError(linkerError: LinkerError) {
                            Utils.shareData(activity, it.share?.description, it.share?.url)
                        }
                    }
                )
            )
        }
    }

    private fun linkerDataMapper(data: PageInfo?, isAffiliate: Boolean = false): LinkerShareData {
        val linkerData = LinkerData()
        if (isAffiliate) {
            linkerData.id = data?.name ?: ""
            linkerData.linkAffiliateType = AffiliateLinkType.CAMPAIGN.value
            linkerData.isAffiliate = true
        } else {
            linkerData.id = data?.id?.toString() ?: ""
        }
        linkerData.name = data?.name ?: ""
        linkerData.uri = Utils.getShareUrlQueryParamAppended(
            data?.share?.url
                ?: "",
            discoComponentQuery
        )
        linkerData.description = data?.share?.description ?: ""
        linkerData.ogTitle = data?.share?.title ?: ""
        linkerData.ogDescription = data?.share?.description ?: ""
        linkerData.isThrowOnError = true
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun showUniversalShareBottomSheet(data: PageInfo?, screenshotPath: String? = null) {
        data?.let { pageInfo ->
            universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
                screenshotPath?.let { path ->
                    setImageOnlySharingOption(true)
                    setScreenShotImagePath(path)
                }
                setFeatureFlagRemoteConfigKey()
                init(this@DiscoveryFragment)
                setUtmCampaignData(
                    this@DiscoveryFragment.context?.resources?.getString(R.string.discovery)
                        ?: UTM_DISCOVERY,
                    if (UserSession(this@DiscoveryFragment.context).userId.isNullOrEmpty()) {
                        "0"
                    } else {
                        UserSession(this@DiscoveryFragment.context).userId
                    },
                    discoveryViewModel.getShareUTM(pageInfo),
                    this@DiscoveryFragment.context?.resources?.getString(R.string.share) ?: SHARE
                )
                setMetaData(
                    pageInfo.share?.title ?: "",
                    pageInfo.share?.image ?: ""
                )
                setOgImageUrl(pageInfo.share?.image ?: "")

                if (this@DiscoveryFragment.isAffiliateInitialized) {
                    val inputShare = AffiliateInput().apply {
                        pageDetail = PageDetail(
                            pageId = "0",
                            pageType = "campaign",
                            siteId = "1",
                            verticalId = "1",
                            pageName = pageEndPoint
                        )
                        pageType = PageType.CAMPAIGN.value
                        product = Product()
                        shop = Shop(shopID = "0", shopStatus = 0, isOS = false, isPM = false)
                    }
                    enableAffiliateCommission(inputShare)
                }
            }

            universalShareBottomSheet?.show(
                fragmentManager,
                this@DiscoveryFragment,
                screenshotDetector
            )
            shareType = universalShareBottomSheet?.getShareBottomSheetType() ?: 0
            getDiscoveryAnalytics().trackUnifyShare(
                VIEW_DISCOVERY_IRIS,
                if (shareType == CUSTOM_SHARE_SHEET) VIEW_UNIFY_SHARE else VIEW_SCREENSHOT_SHARE,
                getUserID()
            )
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val linkerShareData = linkerDataMapper(pageInfoHolder, shareModel.isAffiliate)
        linkerShareData.linkerData.apply {
            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign
            isThrowOnError = false
            if (shareModel.ogImgUrl != null && shareModel.ogImgUrl!!.isNotEmpty()) {
                ogImageUrl = shareModel.ogImgUrl
            }
        }
        getDiscoveryAnalytics().trackUnifyShare(
            EVENT_CLICK_DISCOVERY,
            if (shareType == CUSTOM_SHARE_SHEET) CLICK_SHARE_CHANNEL else CLICK_SCREENSHOT_SHARE_CHANNEL,
            getUserID(),
            shareModel.channel ?: ""
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        val shareString =
                            "${pageInfoHolder?.share?.description} ${linkerShareData?.url}"
                        shareModel.subjectName = pageInfoHolder?.share?.title ?: ""
                        SharingUtil.executeShareIntent(
                            shareModel,
                            linkerShareData,
                            activity,
                            view,
                            shareString
                        )
                        universalShareBottomSheet?.dismiss()
                    }

                    override fun onError(linkerError: LinkerError?) {
                        universalShareBottomSheet?.dismiss()
                        discoDefaultShare(pageInfoHolder)
                    }
                }
            )
        )
    }

    override fun onCloseOptionClicked() {
        getDiscoveryAnalytics().trackUnifyShare(
            EVENT_CLICK_DISCOVERY,
            if (shareType == CUSTOM_SHARE_SHEET) UNIFY_CLOSE_SHARE else UNIFY_CLOSE_SCREENSHOT_SHARE,
            getUserID()
        )
        universalShareBottomSheet?.dismiss()
    }

    private fun sendUnifyShareGTM() {
        getDiscoveryAnalytics().trackUnifyShare(
            EVENT_CLICK_DISCOVERY,
            UNIFY_CLICK_SHARE,
            getUserID()
        )
    }

    override fun screenShotTaken(path: String) {
        showUniversalShareBottomSheet(pageInfoHolder, path)
    }

    private fun setupSearchBar(data: PageInfo?) {
        navToolbar.setupSearchbar(
            hints = listOf(
                HintData(
                    placeholder = data?.searchTitle
                        ?: getString(R.string.discovery_default_search_title)
                )
            ),
            searchbarClickCallback = {
                handleGlobalNavClick(Constant.TOP_NAV_BUTTON.SEARCH_BAR)
                handleSearchClick(data)
            },
            disableDefaultGtmTracker = true
        )
    }

    private fun handleSearchClick(data: PageInfo?) {
        if (data?.searchApplink?.isNotEmpty() == true) {
            RouteManager.route(context, data.searchApplink)
        } else {
            RouteManager.route(context, Utils.SEARCH_DEEPLINK)
        }
    }

    private fun handleBackPress() {
        handleGlobalNavClick(Constant.TOP_NAV_BUTTON.BACK_BUTTON)
        activity?.onBackPressed()
    }

    private fun handleGlobalNavClick(navBarIcon: String) {
        getDiscoveryAnalytics().trackGlobalNavBarClick(navBarIcon, getUserID())
    }

    private fun setPageErrorState(it: Fail) {
        when (it.throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                globalError.setType(GlobalError.NO_CONNECTION)
            }

            is IllegalStateException -> {
                globalError.setType(GlobalError.PAGE_NOT_FOUND)
            }

            else -> {
                globalError.setType(GlobalError.SERVER_ERROR)
                ServerLogger.log(
                    Priority.P2,
                    "DISCOVERY_PAGE_ERROR",
                    mapOf(
                        "identifier" to discoveryViewModel.pageIdentifier,
                        "path" to discoveryViewModel.pagePath,
                        "type" to discoveryViewModel.pageType,
                        "err" to Log.getStackTraceString(it.throwable)
                    )
                )
            }
        }
        globalError.show()
        globalError.setActionClickListener {
            globalError.hide()
            showLoadingWithRefresh()
        }
    }

    private fun hideGlobalError() {
        globalError.hide()
    }

    private fun fetchDiscoveryPageData() {
        discoveryViewModel.getDiscoveryData(
            discoveryViewModel.getQueryParameterMapFromBundle(
                arguments
            ),
            userAddressData
        )
    }

    private fun scrollToPinnedComponent(
        listComponent: List<ComponentsItem>,
        pinnedId: String? = null
    ) {
        if (!pinnedAlreadyScrolled) {
            val pinnedComponentId = pinnedId ?: arguments?.getString(COMPONENT_ID, "")
            if (!pinnedComponentId.isNullOrEmpty()) {
                val (position, isTabPresent) = discoveryViewModel.scrollToPinnedComponent(
                    listComponent,
                    pinnedComponentId
                )
                isTabPresentToDoubleScroll = isTabPresent
                if (position >= 0) {
                    userPressed = false
                    if (position > 0 && isTabPresent) {
                        handleAutoScrollUI()
                        if (isFromForcedNavigation) {
                            var pos = -1
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(2000)
                                discoveryAdapter.currentList.forEachIndexed { index, componentsItem ->
                                    if (componentsItem.id == pinnedComponentId) {
                                        pos = index
                                    }
                                }
                                recyclerView.smoothScrollToPosition(pos)
                            }
                            isFromForcedNavigation = false
                        }
                    }
                    if (this.isResumed) {
                        recyclerView.smoothScrollToPosition(position)
                    }
                    isManualScroll = false
                }
            }
            pinnedAlreadyScrolled = true
        }
    }

    private fun handleAutoScrollUI() {
        recyclerViewPaddingResetNeeded = true
        shouldShowChooseAddressWidget = false
        chooseAddressWidget?.hide()
        chooseAddressWidgetDivider?.hide()
        recyclerView.setPaddingToInnerRV(0, recyclerView.dpToPx(55).toInt(), 0, 0)
    }

    private fun removePaddingIfComponent() {
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    var pos = Int.MIN_VALUE
                    discoveryAdapter.currentList.forEachIndexed { index, componentsItem ->
                        if (componentsItem.name == ComponentsList.Tabs.componentName) {
                            pos = index
                        }
                        if (index == pos + 1) {
                            val i = pos + 1
                            val firstVisibleItemPositions =
                                staggeredGridLayoutManager?.findFirstVisibleItemPositions(null)
                            val lastVisibleItemPositions =
                                staggeredGridLayoutManager?.findLastVisibleItemPositions(null)

                            if (firstVisibleItemPositions != null && lastVisibleItemPositions != null) {
                                val firstVisibleItemPosition =
                                    firstVisibleItemPositions.minOrNull() ?: -1
                                val lastVisibleItemPosition = lastVisibleItemPositions.maxOrNull() ?: -1

                                if (firstVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition != RecyclerView.NO_POSITION) {
                                    if (i in firstVisibleItemPosition..lastVisibleItemPosition) {
                                        recyclerView.setPaddingToInnerRV(
                                            0,
                                            recyclerView.dpToPx(0).toInt(),
                                            0,
                                            0
                                        )
                                    }
                                }
                            }
                        }
                    }
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    fun scrollToComponentWithID(componentID: String) {
        val position = discoveryViewModel.scrollToPinnedComponent(
            discoveryAdapter.currentList,
            componentID
        ).first
        if (position >= 0) {
            userPressed = false
            smoothScrollToComponentWithPosition(position)
        }
    }

    private fun setAnimationOnScroll() {
        recyclerView.addOnScrollListener(mDiscoveryFab.getScrollListener())
    }

    private fun setFloatingActionButton(data: DataItem) {
        mDiscoveryFab.apply {
            show()
            showTextAnimation(data)
            data.thumbnailUrlMobile?.let { showImageOnFab(context, it) }
            setClick(
                data.applinks?.toEmptyStringIfNull().toString(),
                data.shopId?.toIntOrNull()
                    ?: 0
            )
        }
    }

    private fun setClick(appLinks: String, shopId: Int) {
        mDiscoveryFab.getFabButton().setOnClickListener {
            getDiscoveryAnalytics().trackClickCustomTopChat()
            if (appLinks.isNotEmpty() && shopId != 0) {
                activity?.let { it1 -> discoveryViewModel.openCustomTopChat(it1, appLinks, shopId) }
            }
        }
    }

    fun getDiscoveryAnalytics(): BaseDiscoveryAnalytics {
        return analytics
    }

    private fun showLoadingWithRefresh() {
        mProgressBar.show()
        refreshPage()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
        getDiscoveryAnalytics().clearProductViewIds(false)
    }

    override fun onRefresh() {
        refreshPage()
    }

    private fun refreshPage() {
        trackingQueue.sendAll()
        getDiscoveryAnalytics().clearProductViewIds(true)
        miniCartData = null
        resetAnchorTabs()
        checkTabPositionBeforeRefresh()
        discoveryViewModel.resetScroll()
        discoveryViewModel.clearPageData()
        fetchDiscoveryPageData()
    }

    private fun checkTabPositionBeforeRefresh() {
        if (!isFromCategory && currentTabPosition != null) {
            this.arguments?.putString(ACTIVE_TAB, (currentTabPosition).toString())
        }
    }

    private fun resetAnchorTabs() {
        anchorViewHolder?.removeObservers(viewLifecycleOwner)
        anchorViewHolder = null
        mAnchorHeaderView.removeAllViews()
    }

    fun openLoginScreen(componentPosition: Int = -1) {
        this.componentPosition = componentPosition
        startActivityForResult(
            RouteManager.getIntent(activity, ApplinkConst.LOGIN),
            LOGIN_REQUEST_CODE
        )
    }

    fun openMobileVerificationWithBottomSheet(componentPosition: Int = -1) {
        this.componentPosition = componentPosition
        showVerificationBottomSheet()
    }

    fun openPlay(componentPosition: Int = -1, appLink: String) {
        this.componentPosition = componentPosition
        val intent = RouteManager.getIntent(requireContext(), appLink)
        startActivityForResult(intent, OPEN_PLAY_CHANNEL)
    }

    fun startMVCTransparentActivity(
        componentPosition: Int = -1,
        shopId: String,
        productId: String,
        hashCodeForMVC: Int
    ) {
        this.componentPosition = componentPosition
        context?.let {
            startActivityForResult(
                TransParentActivity.getIntent(
                    it,
                    shopId,
                    MvcSource.DISCO,
                    ApplinkConst.SHOP.replace("{shop_id}", shopId),
                    hashCode = hashCodeForMVC,
                    productId = productId
                ),
                MvcView.REQUEST_CODE
            )
        }
    }

    fun refreshCarouselData(componentPosition: Int = -1) {
        if (componentPosition >= 0) {
            discoveryAdapter.getViewModelAtPosition(componentPosition)
                ?.refreshProductCarouselError()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var discoveryBaseViewModel: DiscoveryBaseViewModel? = null
        this.componentPosition?.let { position ->
            if (position >= 0) {
                discoveryBaseViewModel = discoveryAdapter.getViewModelAtPosition(position)
            }
        }
        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (this.componentPosition != null && this.componentPosition!! >= 0) {
                        discoveryViewModel.sendCouponInjectDataForLoggedInUsers()
                        discoveryBaseViewModel?.loggedInCallback()
                    } else {
                        discoveryViewModel.sendCouponInjectDataForLoggedInUsers()
                    }
                }
            }

            MOBILE_VERIFICATION_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    discoveryBaseViewModel?.isPhoneVerificationSuccess(true)
                } else {
                    discoveryBaseViewModel?.isPhoneVerificationSuccess(false)
                }
            }

            PAGE_REFRESH_LOGIN -> {
                if (resultCode == Activity.RESULT_OK) {
                    discoveryViewModel.sendCouponInjectDataForLoggedInUsers()
                    refreshPage()
                }
            }

            OPEN_PLAY_CHANNEL -> {
                if (data == null) {
                    return
                }
                val channelId = data.getStringExtra(PlayWidgetConst.KEY_EXTRA_CHANNEL_ID).orEmpty()
                val totalView = data.getStringExtra(PlayWidgetConst.KEY_EXTRA_TOTAL_VIEW).orEmpty()
                val isReminder = data.getBooleanExtra(PlayWidgetConst.KEY_EXTRA_IS_REMINDER, false)
                if (discoveryBaseViewModel is DiscoveryPlayWidgetViewModel) {
                    val discoveryPlayWidgetViewModel =
                        (discoveryBaseViewModel as DiscoveryPlayWidgetViewModel)
                    discoveryPlayWidgetViewModel.updatePlayWidgetTotalView(channelId, totalView)
                    discoveryPlayWidgetViewModel.updatePlayWidgetReminder(channelId, isReminder)
                }
            }

            MvcView.REQUEST_CODE -> {
                if (resultCode == MvcView.RESULT_CODE_OK) {
                    data?.let {
                        val bundle = data.getBundleExtra(REGISTER_MEMBER_SUCCESS)
                        bundle?.let {
                            val listInfo =
                                bundle.getParcelableArrayList<AnimatedInfos>(IntentManger.Keys.ANIMATED_INFO)
                                    ?: ArrayList()
                            val isShown = bundle.getBoolean(IntentManger.Keys.IS_SHOWN, true)
                            val shopID = bundle.getString(IntentManger.Keys.SHOP_ID, "")
                            (discoveryBaseViewModel as? DiscoMerchantVoucherViewModel)?.updateData(
                                shopID,
                                isShown,
                                listInfo
                            )
                        }
                    }
                }
            }
        }
        AdultManager.handleActivityResult(
            activity,
            requestCode,
            resultCode,
            data,
            object : AdultManager.Callback {
                override fun onFail() {
                    activity?.finish()
                }

                override fun onVerificationSuccess(message: String?) {
                }

                override fun onLoginPreverified() {
                }
            }
        )

        handleProductCardOptionsActivityResult(
            requestCode,
            resultCode,
            data,
            object : ProductCardOptionsWishlistCallback {
                override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleWishlistAction(productCardOptionsModel)
                }
            },
            visitShopCallback = object : ProductCardOptionsResult {
                override fun onReceiveResult(productCardOptionsModel: ProductCardOptionsModel) {
                    handleClickVisitShopCallback(productCardOptionsModel)
                }
            },
            shareProductCallback = object : ProductCardOptionsResult {
                override fun onReceiveResult(productCardOptionsModel: ProductCardOptionsModel) {
                    analytics.track3DotsOptionsClickedShareProduct()
                }
            }
        )
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel) {
        activity?.let { activity ->
            if (productCardOptionsModel.wishlistResult.isUserLoggedIn) {
                if (productCardOptionsModel.wishlistResult.isAddWishlist) {
                    trackAddToWishlist(productCardOptionsModel)
                    if (productCardOptionsModel.wishlistResult.isSuccess) {
                        if (isFromCategory) {
                            NetworkErrorHelper.showSnackbar(
                                activity,
                                getString(R.string.discovery_msg_success_add_wishlist)
                            )
                        } else {
                            showToasterForWishlistAddSuccess()
                        }
                        this.discoveryViewModel.updateWishlist(productCardOptionsModel)
                    } else {
                        NetworkErrorHelper.showSnackbar(
                            activity,
                            getString(R.string.discovery_msg_error_add_wishlist)
                        )
                    }
                } else {
                    if (productCardOptionsModel.wishlistResult.isSuccess) {
                        this.discoveryViewModel.updateWishlist(productCardOptionsModel)
                        NetworkErrorHelper.showSnackbar(
                            activity,
                            getString(R.string.discovery_msg_success_remove_wishlist)
                        )
                    } else {
                        NetworkErrorHelper.showSnackbar(
                            activity,
                            getString(R.string.discovery_msg_error_remove_wishlist)
                        )
                    }
                }
            } else {
                openLoginScreen()
            }
        }
    }

    private fun trackAddToWishlist(productCardOptionsModel: ProductCardOptionsModel) {
        analytics.track3DotsOptionsClickedWishlist(productCardOptionsModel)
    }

    fun handleClickVisitShopCallback(productCardOptionsModel: ProductCardOptionsModel) {
        if (productCardOptionsModel.shopId.isNotEmpty()) {
            context?.let {
                analytics.track3DotsOptionsClickedLihatToko()
                RouteManager.route(
                    it,
                    (
                        ApplinkConst.SHOP.replace(
                            "{shop_id}",
                            productCardOptionsModel.shopId
                        )
                        )
                )
            }
        }
    }

    private fun showToasterForWishlistAddSuccess() {
        showToasterWithAction(
            getString(R.string.discovery_msg_success_add_wishlist),
            Snackbar.LENGTH_LONG,
            Toaster.TYPE_NORMAL,
            actionText = getString(R.string.discovery_msg_success_add_wishlist_CTA)
        ) { goToWishlistPage() }
    }

    private fun goToWishlistPage() {
        this.context?.let {
            RouteManager.route(it, ApplinkConst.NEW_WISHLIST)
        }
    }

    private fun showVerificationBottomSheet() {
        val closeableBottomSheetDialog = BottomSheetUnify()
        val childView =
            View.inflate(context, R.layout.mobile_verification_bottom_sheet_layout, null)
        this.fragmentManager?.let {
            closeableBottomSheetDialog.apply {
                showCloseIcon = true
                setChild(childView)
                show(it, null)
            }
        }
        childView.findViewById<UnifyButton>(R.id.verify_btn).setOnClickListener {
            closeableBottomSheetDialog.dismiss()
            startActivityForResult(
                RouteManager.getIntent(activity, ADD_PHONE),
                MOBILE_VERIFICATION_REQUEST_CODE
            )
            getDiscoveryAnalytics().trackQuickCouponPhoneVerified()
        }
        closeableBottomSheetDialog.setCloseClickListener {
            closeableBottomSheetDialog.dismiss()
            getDiscoveryAnalytics().trackQuickCouponPhoneVerifyCancel()
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            ivToTop -> {
                userPressed = true
                isTabPresentToDoubleScroll = false
                recyclerView.smoothScrollToPosition(DEFAULT_SCROLL_POSITION)
                ivToTop.hide()
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return UserSession(activity).isLoggedIn
    }

    private fun getUserID(): String? {
        return UserSession(activity).userId
    }

    private fun stopDiscoveryPagePerformanceMonitoring() {
        recyclerView.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    pageLoadTimePerformanceInterface?.stopRenderPerformanceMonitoring()
                    pageLoadTimePerformanceInterface?.stopMonitoring()
                    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
    }

    override fun onResume() {
        super.onResume()
        discoveryViewModel.getDiscoveryPageInfo().observe(viewLifecycleOwner) {
            if (!openScreenStatus) {
                when (it) {
                    is Success -> {
                        sendOpenScreenAnalytics(
                            it.data.identifier,
                            it.data.additionalInfo,
                            it.data.isAffiliate
                        )
                    }

                    else -> sendOpenScreenAnalytics(discoveryViewModel.pageIdentifier)
                }
            }
        }
        context?.let {
            if (discoveryViewModel.getAddressVisibilityValue()) {
                updateChooseAddressWidget()
                checkAddressUpdate()
            }
        }
        addMiniCartToPage()
    }

    private fun sendOpenScreenAnalytics(
        identifier: String?,
        additionalInfo: AdditionalInfo? = null,
        isAffiliate: Boolean = false
    ) {
        val campaignId = arguments?.getString(CAMPAIGN_ID, "") ?: ""
        val variantId = arguments?.getString(VARIANT_ID, "") ?: ""
        val shopId = arguments?.getString(SHOP_ID, "") ?: ""
        val affiliateUID = arguments?.getString(AFFILIATE_UNIQUE_ID, "")?.toDecodedString() ?: ""
        val affiliateChannelID = if (isAffiliate && affiliateUID.isNotEmpty()) {
            val trackerID = discoveryViewModel.getTrackerIDForAffiliate()
            "$affiliateUID - $trackerID - $AFFILIATE"
        } else {
            ""
        }
        if (identifier.isNullOrEmpty()) {
            getDiscoveryAnalytics().trackOpenScreen(
                discoveryViewModel.pageIdentifier,
                additionalInfo,
                isUserLoggedIn(),
                ParamsForOpenScreen(campaignId, variantId, shopId, affiliateChannelID)
            )
        } else {
            getDiscoveryAnalytics().trackOpenScreen(
                identifier,
                additionalInfo,
                isUserLoggedIn(),
                ParamsForOpenScreen(campaignId, variantId, shopId, affiliateChannelID)
            )
        }
        openScreenStatus = true
    }

    override fun onStop() {
        super.onStop()
        if (lastVisibleComponent == null) {
            updateLastVisibleComponent()
        }
        getDiscoveryAnalytics().trackScrollDepth(
            screenScrollPercentage,
            lastVisibleComponent,
            isManualScroll
        )
        openScreenStatus = false
    }

    override fun onDestroy() {
        super.onDestroy()
        discoComponentQuery = null
    }

    override fun onProductCardHeaderClick(componentsItem: ComponentsItem) {
        getDiscoveryAnalytics().trackHeaderSeeAllClick(isUserLoggedIn(), componentsItem)
    }

    override fun onLihatSemuaClick(data: DataItem) {
        getDiscoveryAnalytics().trackLihatSemuaClick(data)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        tabItemRedirection(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tabItemRedirection(tab)
    }

    private fun tabItemRedirection(tab: TabLayout.Tab?) {
        tab?.let {
            discoveryViewModel.getTabItem(it.position)?.let { dataItem ->
                getDiscoveryAnalytics().trackBottomNavBarClick(dataItem.name ?: "", getUserID())
                RouteManager.route(this.context, dataItem.applinks)
                activity?.finish()
            }
        }
    }

    private fun getTabTextColor(context: Context, textColor: String?): Int {
        return try {
            if (textColor.isNullOrEmpty()) {
                ContextCompat.getColor(context, RUnify.color.Unify_G500)
            } else {
                Color.parseColor(textColor)
            }
        } catch (exception: Exception) {
            ContextCompat.getColor(context, RUnify.color.Unify_G500)
        }
    }

    override fun onLocalizingAddressUpdatedFromWidget() {
        updateChooseAddressWidget()
        checkAddressUpdate()
    }

    override fun onLocalizingAddressServerDown() {
        chooseAddressWidget?.hide()
        chooseAddressWidgetDivider?.hide()
    }

    override fun onLocalizingAddressRollOutUser(isRollOutUser: Boolean) {
    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return this
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return if (isFromCategory) {
            Constant.ChooseAddressGTMSSource.CATEGORY_HOST_SOURCE
        } else {
            Constant.ChooseAddressGTMSSource.HOST_SOURCE
        }
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return if (isFromCategory) {
            Constant.ChooseAddressGTMSSource.CATEGORY_HOST_TRACKING_SOURCE
        } else {
            Constant.ChooseAddressGTMSSource.HOST_TRACKING_SOURCE
        }
    }

    override fun onLocalizingAddressLoginSuccess() {
    }

    override fun onLocalizingAddressUpdatedFromBackground() {
    }

    override fun getEventLabelHostPage(): String {
        return if (isFromCategory) {
            (context as DiscoveryActivity).getPageIdentifier()
        } else {
            EMPTY_STRING
        }
    }

    override fun onChangeTextColor(): Int {
        return if (hasColouredHeader && isLightThemeStatusBar != false) {
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        } else {
            com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
        }
    }

    private fun fetchUserLatestAddressData() {
        context?.let {
            userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
        }
    }

    private fun checkAddressUpdate() {
        context?.let {
            if (userAddressData != null) {
                if (ChooseAddressUtils.isLocalizingAddressHasUpdated(it, userAddressData!!)) {
                    userAddressData = ChooseAddressUtils.getLocalizingAddressData(it)
                    showLoadingWithRefresh()
                }
            }
        }
    }

    private fun updateChooseAddressWidget() {
        chooseAddressWidget?.updateWidget()
    }

    fun showCustomContent(view: View) {
        hideSystemUi()
        coordinatorLayout.hide()
        view.rotation = ROTATION
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = activity?.let {
            val metrics = DisplayMetrics()
            activity?.windowManager?.defaultDisplay?.getRealMetrics(metrics)
            metrics.heightPixels
        } ?: Resources.getSystem().displayMetrics.heightPixels
        val offset = width - height
        view.translationX = offset.toFloat() / 2
        view.translationY = -offset.toFloat() / 2

        val layoutParams = FrameLayout.LayoutParams(height, width)
        view.layoutParams = layoutParams

        context?.let {
            parentLayout.setBackgroundColor(
                MethodChecker.getColor(
                    it,
                    RUnify.color.Unify_GN950
                )
            )
        }
        parentLayout.addView(view)
        parentLayout.requestFocus()
    }

    fun hideCustomContent() {
        showSystemUi()
        context?.let {
            parentLayout.setBackgroundColor(
                MethodChecker.getColor(
                    it,
                    RUnify.color.Unify_NN0
                )
            )
        }
        coordinatorLayout.show()
        if (parentLayout.childCount > 1) {
            parentLayout.removeViewAt(1)
        }
    }

    private fun addMiniCartToPageFirstTime() {
        if (miniCartData == null) {
            addMiniCartToPage()
        }
    }

    private fun addMiniCartToPage() {
        if (pageInfoHolder?.tokonowMiniCartActive == true) {
            getMiniCart()
        }
    }

    private fun getMiniCart() {
        val shopId = listOf(userAddressData?.shop_id.orEmpty())
        val warehouseId = userAddressData?.warehouse_id
        discoveryViewModel.getMiniCart(shopId, warehouseId)
    }

    fun addOrUpdateItemCart(discoATCRequestParams: DiscoATCRequestParams) {
        if (discoATCRequestParams.shopId.isNullOrEmpty()) {
            discoATCRequestParams.shopId = (userAddressData?.shop_id ?: "")
        }
        discoveryViewModel.addProductToCart(
            discoATCRequestParams
        )
    }

    private fun setupMiniCart(data: MiniCartSimplifiedData) {
        if (data.isShowMiniCartWidget) {
            val shopIds = listOf(userAddressData?.shop_id.orEmpty())
            if (!miniCartInitialized) {
                miniCartWidget?.initialize(
                    shopIds,
                    this,
                    this,
                    pageName = MiniCartAnalytics.Page.DISCOVERY_PAGE,
                    source = MiniCartSource.TokonowDiscoveryPage
                )
                miniCartWidget?.show()
            } else {
                miniCartWidget?.updateData(data)
            }
        } else {
            miniCartWidget?.hide()
        }
        syncWithCart(data)
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (!miniCartSimplifiedData.isShowMiniCartWidget) {
            miniCartWidget?.hide()
        }
        discoveryViewModel.miniCartSimplifiedData = miniCartSimplifiedData
        syncWithCart(miniCartSimplifiedData)
    }

    private fun syncWithCart(data: MiniCartSimplifiedData) {
        setCartData(data.miniCartItems, pageEndPoint)
        miniCartData = data
        reSync()
    }

    private fun hideSystemUi() {
        activity?.window?.apply {
            decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val params: WindowManager.LayoutParams = attributes
                params.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                attributes = params
            }
        }
    }

    private fun showSystemUi() {
        activity?.window?.decorView?.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    // ScreenShot Access Dialogue Permission
    override fun permissionAction(action: String, label: String) {
        getDiscoveryAnalytics().trackScreenshotAccess(action, label, getUserID())
    }

    fun openVariantBottomSheet(productId: String) {
        context?.let {
            AtcVariantHelper.goToAtcVariant(
                it,
                productId,
                VariantPageSource.DISCOVERY_PAGESOURCE,
                true,
                userAddressData?.shop_id ?: "",
                startActivitResult = { intent, reqCode ->
                    startActivityForResult(intent, reqCode)
                }
            )
        }
    }

    fun scrollToSection(sectionID: String) {
        autoScrollSectionID = sectionID
        getSectionPositionMap(pageEndPoint)?.let {
            it[sectionID]?.let { position ->
                if (position >= 0) {
                    userPressed = false
                    anchorViewHolder?.viewModel?.updateSelectedSection(sectionID, true)
                    smoothScrollToComponentWithPosition(position)
                }
            }
        }
    }

    private fun smoothScrollToComponentWithPosition(position: Int) {
        try {
            val smoothScroller: RecyclerView.SmoothScroller =
                object : LinearSmoothScroller(context) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }

                    override fun calculateDyToMakeVisible(view: View?, snapPreference: Int): Int {
                        return super.calculateDyToMakeVisible(
                            view,
                            snapPreference
                        ) + dpToPx(55).toInt()
                    }
                }
            smoothScroller.targetPosition = position
            if (this.isResumed) {
                staggeredGridLayoutManager?.startSmoothScroll(smoothScroller)
            }
        } catch (e: Exception) {
        }
    }

    fun updateSelectedSection(sectionID: String) {
        getSectionPositionMap(pageEndPoint)?.let {
            it[sectionID]?.let { position ->
                if (position >= 0) {
                    anchorViewHolder?.viewModel?.updateSelectedSection(sectionID, false)
                }
            }
        }
    }

    fun handleHideSection(sectionID: String) {
        if (sectionID.isNotEmpty()) {
            anchorViewHolder?.viewModel?.deleteSectionTab(sectionID)
        }
    }

    fun getScrollLiveData(): LiveData<ScrollData> {
        return discoveryViewModel.scrollState
    }

    fun stickyHeaderIsHidden() {
        stickyHeaderShowing = false
    }

    fun showingStickyHeader() {
        if (!stickyHeaderShowing) {
            anchorViewHolder?.removeObservers(viewLifecycleOwner)
            mAnchorHeaderView.removeAllViews()
        }
        stickyHeaderShowing = true
    }

    private val homeMainToolbarHeight: Int
        get() {
            var height = 0
            navToolbar.let {
                height = navToolbar.height
                height += 8f.toDpInt()
            }
            return height
        }

    private fun requestStatusBarDark() {
        isLightThemeStatusBar = false
        activity?.let {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                requestStatusBarLight()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    it.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    StatusBarUtil.setWindowFlag(
                        it,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        false
                    )
                    it.window.statusBarColor = Color.TRANSPARENT
                }
            }
        }
    }

    private fun requestStatusBarLight() {
        isLightThemeStatusBar = true
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                StatusBarUtil.setWindowFlag(
                    it,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    false
                )
                it.window.statusBarColor = Color.TRANSPARENT
            }
        }
    }

    private fun getLightIconColor(context: Context): Int {
        return if (context.isDarkMode()) {
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        } else {
            ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        }
    }

    private fun getDarkIconColor(context: Context): Int {
        return if (context.isDarkMode()) {
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        } else {
            ContextCompat.getColor(
                context,
                com.tokopedia.searchbar.R.color.searchbar_dms_state_light_icon
            )
        }
    }

    fun createAffiliateLink(applink: String): String {
        return discoveryViewModel.createAffiliateLink(applink)
    }

    fun getItemCount(): Int {
        return discoveryAdapter.itemCount
    }

    fun provideSubComponent(): UIWidgetComponent {
        return if (::discoveryComponent.isInitialized) {
            discoveryComponent.provideSubComponent()
        } else {
            initInjector()
            discoveryComponent.provideSubComponent()
        }
    }

    fun scrollToNextComponent(currentCompPosition: Int?) {
        if (currentCompPosition != null && currentCompPosition + 1 < discoveryAdapter.itemCount) {
            smoothScrollToComponentWithPosition(currentCompPosition + 1)
        }
    }

    private fun getNavIconBuilderFlag(): IconBuilderFlag {
        val pageSource = if (isFromCategory) NavSource.CLP else NavSource.DISCOVERY
        return IconBuilderFlag(pageSource, pageEndPoint)
    }

    fun redirectToOtherTab(queryParams: String?) {
        val queryParameterMapWithRpc = mutableMapOf<String, String>()
        val queryParameterMapWithoutRpc = mutableMapOf<String, String>()

        Utils.setParameterMapUtil(
            queryParams,
            queryParameterMapWithRpc,
            queryParameterMapWithoutRpc
        )

        val activeTab = queryParameterMapWithoutRpc[ACTIVE_TAB]?.toIntOrNull()
        val componentId = queryParameterMapWithoutRpc[COMPONENT_ID]?.toIntOrNull()
        discoveryPageData[discoveryViewModel.pageIdentifier]?.let {
            it.queryParamMapWithRpc.putAll(queryParameterMapWithRpc)
            it.queryParamMapWithoutRpc.putAll(queryParameterMapWithoutRpc)
        }
        pinnedAlreadyScrolled = false
        if (activeTab != null) {
            this.arguments?.putString(FORCED_NAVIGATION, "true")
            if (componentId != null) {
                this.arguments?.putString(COMPONENT_ID, componentId.toString())
                isFromForcedNavigation = true
            }
            discoveryViewModel.getDiscoveryData(
                discoveryViewModel.getQueryParameterMapFromBundle(
                    arguments
                ),
                userAddressData,
                true
            )
        } else if (componentId != null) {
            scrollToPinnedComponent(discoveryAdapter.currentList, componentId.toString())
        }
    }
}
