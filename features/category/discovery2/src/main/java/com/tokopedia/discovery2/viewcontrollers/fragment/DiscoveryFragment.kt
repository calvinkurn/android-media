package com.tokopedia.discovery2.viewcontrollers.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PHONE
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.R
import com.tokopedia.unifyprinciples.R as RUnify
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.analytics.*
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.datamapper.discoComponentQuery
import com.tokopedia.discovery2.datamapper.getSectionPositionMap
import com.tokopedia.discovery2.datamapper.setCartData
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.ACTIVE_TAB
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.CATEGORY_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.COMPONENT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.EMBED_CATEGORY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PIN_PRODUCT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.PRODUCT_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SOURCE
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.TARGET_COMP_ID
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs.AnchorTabsViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher.DiscoMerchantVoucherViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import com.tokopedia.discovery2.viewcontrollers.customview.CustomTopChatView
import com.tokopedia.discovery2.viewcontrollers.customview.StickyHeadRecyclerView
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.discovery2.viewmodel.livestate.GoToAgeRestriction
import com.tokopedia.discovery2.viewmodel.livestate.RouteToApplink
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.IntentManger
import com.tokopedia.mvcwidget.IntentManger.Keys.REGISTER_MEMBER_SUCCESS
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import java.net.SocketTimeoutException
import java.net.UnknownHostException


private const val LOGIN_REQUEST_CODE = 35769
private const val MOBILE_VERIFICATION_REQUEST_CODE = 35770
const val PAGE_REFRESH_LOGIN = 35771
private const val OPEN_PLAY_CHANNEL = 35772
private const val SCROLL_TOP_DIRECTION = -1
private const val DEFAULT_SCROLL_POSITION = 0
private const val ROTATION = 90f
const val CUSTOM_SHARE_SHEET = 1

class DiscoveryFragment :
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

    private lateinit var discoveryViewModel: DiscoveryViewModel
    private lateinit var mDiscoveryFab: CustomTopChatView
    private lateinit var recyclerView: StickyHeadRecyclerView
    private lateinit var typographyHeader: Typography
    private lateinit var ivShare: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var ivToTop: ImageView
    private lateinit var globalError: GlobalError
    private lateinit var navToolbar: NavToolbar
    private var bottomNav: TabsUnify? = null
    private lateinit var discoveryAdapter: DiscoveryRecycleAdapter
    private var chooseAddressWidget: ChooseAddressWidget? = null
    private var chooseAddressWidgetDivider: View? = null
    private var shouldShowChooseAddressWidget: Boolean = true
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var parentLayout: FrameLayout
    private var pageInfoHolder: PageInfo? = null
    private var miniCartWidget: MiniCartWidget? = null
    private var miniCartData: MiniCartSimplifiedData? = null
    private var miniCartInitialized: Boolean = false

    private val analytics: BaseDiscoveryAnalytics by lazy {
        (context as DiscoveryActivity).getAnalytics()
    }
    private val trackingQueue: TrackingQueue by lazy {
        (context as DiscoveryActivity).trackingQueue
    }
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mProgressBar: LoaderUnify
    var pageEndPoint = ""
    private var componentPosition: Int? = null
    private var openScreenStatus = false
    private var pinnedAlreadyScrolled = false
    var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null
    private var showOldToolbar: Boolean = false
    private var userAddressData: LocalCacheModel? = null
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var lastVisibleComponent: ComponentsItem? = null
    private var screenScrollPercentage = 0
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenshotDetector: ScreenshotDetector? = null
    private var shareType: Int = 1

    private var isManualScroll = true

    companion object {
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
                bundle.putString(TARGET_COMP_ID, queryParameterMap[TARGET_COMP_ID])
                bundle.putString(PRODUCT_ID, queryParameterMap[PRODUCT_ID])
                bundle.putString(PIN_PRODUCT, queryParameterMap[PIN_PRODUCT])
                bundle.putString(CATEGORY_ID, queryParameterMap[CATEGORY_ID])
                bundle.putString(EMBED_CATEGORY, queryParameterMap[EMBED_CATEGORY])
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
            screenshotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
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

    private fun initChooseAddressWidget(view: View) {
        chooseAddressWidget = view.findViewById(R.id.choose_address_widget)
        chooseAddressWidgetDivider = view.findViewById(R.id.divider_view)
        chooseAddressWidget?.bindChooseAddress(this)
    }

    private fun initToolbar(view: View) {
        showOldToolbar = false
        val oldToolbar: Toolbar = view.findViewById(R.id.oldToolbar)
        navToolbar = view.findViewById(R.id.navToolbar)
        viewLifecycleOwner.lifecycle.addObserver(navToolbar)
        if (showOldToolbar) {
            oldToolbar.visibility = View.VISIBLE
            navToolbar.visibility = View.GONE
        } else {
            navToolbar.visibility = View.VISIBLE
            oldToolbar.visibility = View.GONE
            navToolbar.setOnBackButtonClickListener(
                disableDefaultGtmTracker = true,
                backButtonClickListener = ::handleBackPress
            )
        }

        bottomNav = view.findViewById(R.id.bottomNav)
        bottomNav?.tabLayout?.addOnTabSelectedListener(this)
    }

    private fun initView(view: View) {
        typographyHeader = view.findViewById(R.id.typography_header)
        ivShare = view.findViewById(R.id.iv_share)
        ivSearch = view.findViewById(R.id.iv_search)
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            handleBackPress()
        }
        globalError = view.findViewById(R.id.global_error)
        recyclerView = view.findViewById(R.id.discovery_recyclerView)
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        mProgressBar = view.findViewById(R.id.progressBar)
        ivToTop = view.findViewById(R.id.toTopImg)
        coordinatorLayout = view.findViewById(R.id.parent_coordinator)
        parentLayout = view.findViewById(R.id.parent_frame)
        miniCartWidget = view.findViewById(R.id.miniCartWidget)

        mProgressBar.show()
        mSwipeRefreshLayout.setOnRefreshListener(this)
        ivToTop.setOnClickListener(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var dy = 0
            var dx = 0
            var scrollDist = 0
            val MINIMUM = 25.toPx()
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
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_TOP_DIRECTION)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    ivToTop.hide()
                }
                discoveryViewModel.updateScroll(dx, dy, newState)
                if (scrollDist > MINIMUM) {
                    chooseAddressWidget?.hide()
                    chooseAddressWidgetDivider?.hide()
                    shouldShowChooseAddressWidget = false
                    scrollDist = 0
                } else if (scrollDist < -MINIMUM) {
                    if (discoveryViewModel.getAddressVisibilityValue()) {
                        chooseAddressWidget?.show()
                        chooseAddressWidgetDivider?.show()
                        shouldShowChooseAddressWidget = true
                    }
                    scrollDist = 0
                }
            }
        })
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
        if (lastVisibleComponent != null && (lastVisibleComponent?.name ==
                    ComponentsList.ProductCardRevamp.componentName || lastVisibleComponent?.name ==
                    ComponentsList.ProductCardSprintSale.componentName)
        ) {
            return
        }
        staggeredGridLayoutManager?.findLastVisibleItemPositions(null)?.let { positionArray ->
            if (positionArray.isNotEmpty() && positionArray.first() >= 0) {
                if (discoveryAdapter.currentList.size <= positionArray.first()) return
                lastVisibleComponent = discoveryAdapter.currentList[positionArray.first()]

                if (lastVisibleComponent != null && (lastVisibleComponent?.name ==
                            ComponentsList.ProductCardRevampItem.componentName || lastVisibleComponent?.name ==
                            ComponentsList.ProductCardSprintSaleItem.componentName ||
                            lastVisibleComponent?.name == ComponentsList.ShimmerProductCard.componentName)
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
        discoveryViewModel = (activity as DiscoveryActivity).getViewModel()
        discoveryViewModel.sendCouponInjectDataForLoggedInUsers()
        /** Future Improvement : Please don't remove any commented code from this file. Need to work on this **/
//        mDiscoveryViewModel = ViewModelProviders.of(requireActivity()).get((activity as BaseViewModelActivity<DiscoveryViewModel>).getViewModelType())
        setAdapter()
        discoveryViewModel.pageIdentifier = arguments?.getString(END_POINT, "") ?: ""
        pageEndPoint = discoveryViewModel.pageIdentifier
        fetchDiscoveryPageData()
        setUpObserver()
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
                    e.printStackTrace()
                }
            }
        }
    }

    fun reSync() {
        fetchDiscoveryPageData()
    }

    private fun setUpObserver() {
        discoveryViewModel.getDiscoveryResponseList().observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    it.data.let { listComponent ->
                        if (mSwipeRefreshLayout.isRefreshing) setAdapter()
                        discoveryAdapter.addDataList(listComponent)
                        if (listComponent.isNullOrEmpty()) {
                            discoveryAdapter.addDataList(ArrayList())
                            setPageErrorState(Fail(IllegalStateException()))
                        } else {
                            scrollToPinnedComponent(listComponent)
                        }
                    }
                    hideGlobalError()
                    mProgressBar.hide()
                    stopDiscoveryPagePerformanceMonitoring()
                }
                is Fail -> {
                    mProgressBar.hide()
                    setPageErrorState(it)
                }
            }
            mSwipeRefreshLayout.isRefreshing = false
        })

        discoveryViewModel.getDiscoveryFabLiveData().observe(viewLifecycleOwner, {
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
        })

        discoveryViewModel.getDiscoveryPageInfo().observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    pageInfoHolder = it.data
                    setToolBarPageInfoOnSuccess(it.data)
                    addMiniCartToPageFirstTime()
                }
                is Fail -> {
                    discoveryAdapter.addDataList(ArrayList())
                    setToolBarPageInfoOnFail()
                    setPageErrorState(it)
                }
            }
        })

        discoveryViewModel.getDiscoveryLiveStateData().observe(viewLifecycleOwner, {
            when (it) {
                is RouteToApplink -> {
                    RouteManager.route(context, it.applink)
                    activity?.finish()
                }
                is GoToAgeRestriction -> {
                    AdultManager.showAdultPopUp(this, it.origin, it.departmentId)
                }
            }
        })

        discoveryViewModel.getDiscoveryBottomNavLiveData().observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    setBottomNavigationComp(it)
                }
                is Fail -> {
                    bottomNav?.hide()
                }
            }
        })

        discoveryViewModel.checkAddressVisibility()
            .observe(viewLifecycleOwner, { widgetVisibilityStatus ->
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
            })

        discoveryViewModel.miniCart.observe(viewLifecycleOwner, {
            if (it is Success) {
                setupMiniCart(it.data)
            }
        })

        discoveryViewModel.miniCartAdd.observe(viewLifecycleOwner, {
            if (it is Success) {
                getMiniCart()
                showToaster(
                    message = it.data.errorMessage.joinToString(separator = ", "),
                    type = Toaster.TYPE_NORMAL
                )
            } else if (it is Fail) {
                if (it.throwable is ResponseErrorException)
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
            }
        })

        discoveryViewModel.miniCartUpdate.observe(viewLifecycleOwner, {
            if (it is Success) {
                getMiniCart()
            } else if (it is Fail) {
                if (it.throwable is ResponseErrorException)
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
            }
        })

        discoveryViewModel.miniCartRemove.observe(viewLifecycleOwner, {
            if (it is Success) {
                getMiniCart()
                showToaster(
                    message = it.data.second,
                    type = Toaster.TYPE_NORMAL
                )
            } else if (it is Fail) {
                if (it.throwable is ResponseErrorException)
                    showToaster(
                        message = it.throwable.message.orEmpty(),
                        type = Toaster.TYPE_ERROR
                    )
            }
        })

        discoveryViewModel.miniCartOperationFailed.observe(viewLifecycleOwner,
            { (parentPosition, position) ->
                if (parentPosition >= 0) {
                    discoveryAdapter.getViewModelAtPosition(parentPosition)
                        ?.let { discoveryBaseViewModel ->
                            if (discoveryBaseViewModel is ProductCardCarouselViewModel)
                                discoveryBaseViewModel.handleAtcFailed(position)
                        }
                } else if (position >= 0) {
                    discoveryAdapter.getViewModelAtPosition(position)
                        ?.let { discoveryBaseViewModel ->
                            if (discoveryBaseViewModel is MasterProductCardItemViewModel)
                                discoveryBaseViewModel.handleATCFailed()
                        }
                }
            })
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

    private fun setBottomNavigationComp(it: Success<ComponentsItem>) {
        if (bottomNav != null && !it.data.data.isNullOrEmpty()) {
            bottomNav?.let { bottomTabHolder ->
                bottomTabHolder.tabLayout.apply {
                    tabMode = TabLayout.MODE_FIXED
                    removeAllTabs()
                    setBackgroundResource(0)
                }
                bottomTabHolder.getUnifyTabLayout().setSelectedTabIndicator(null)
                it.data.data!!.forEach { item ->
                    if (item.image.isNotEmpty()) {
                        val tab = bottomTabHolder.tabLayout.newTab()
                        tab.customView = LayoutInflater.from(this.context)
                            .inflate(R.layout.bottom_nav_item, bottomTabHolder, false).apply {
                            findViewById<ImageUnify>(R.id.tab_image).loadImage(item.image)
                            findViewById<Typography>(R.id.tab_text).apply {
                                text = item.name
                                setTextColor(getTabTextColor(this.context, item.fontColor))
                            }
                        }
                        bottomTabHolder.tabLayout.addTab(tab, false)
                    }
                }
                bottomTabHolder.show()
            }
        }
    }

    private fun setToolBarPageInfoOnSuccess(data: PageInfo?) {
        handleShare(data)
        if (showOldToolbar) {
            ivSearch.show()
            ivSearch.setOnClickListener {
                getDiscoveryAnalytics().trackSearchClick()
                handleSearchClick(data)
            }
            typographyHeader.text = data?.name ?: getString(R.string.discovery_tokopedia)
        } else {
            setupSearchBar(data)
        }
    }

    private fun handleShare(data: PageInfo?) {
        if (data?.share?.enabled == true) {
            if (showOldToolbar) {
                ivShare.show()
                ivShare.setOnClickListener {
                    handleShareClick(data)
                }
            } else {
                navToolbar.setIcon(
                    IconBuilder()
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
            }
        } else {
            if (showOldToolbar) {
                ivShare.hide()
            } else {
                setCartAndNavIcon()
            }
        }
    }


    private fun handleShareClick(data: PageInfo?) {
        if (showOldToolbar) {
            getDiscoveryAnalytics().trackShareClick()
        } else {
            handleGlobalNavClick(Constant.TOP_NAV_BUTTON.SHARE)
        }

        if (UniversalShareBottomSheet.isCustomSharingEnabled(context)) {
            sendUnifyShareGTM()
            showUniversalShareBottomSheet(data)
        } else {
            discoDefaultShare(data)
        }
    }

    private fun discoDefaultShare(data: PageInfo?) {
        data?.let {
            LinkerManager.getInstance().executeShareRequest(
                LinkerUtils.createShareRequest(0,
                    linkerDataMapper(it), object : ShareCallback {
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
                    })
            )
        }
    }

    private fun linkerDataMapper(data: PageInfo?): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = data?.id?.toString() ?: ""
        linkerData.name = data?.name ?: ""
        linkerData.uri = Utils.getShareUrlQueryParamAppended(
            data?.share?.url
                ?: "", discoComponentQuery
        )
        linkerData.description = data?.share?.description ?: ""
        linkerData.ogTitle = data?.share?.title ?: ""
        linkerData.ogDescription = data?.share?.description ?: ""
        linkerData.isThrowOnError = true
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
    }

    private fun showUniversalShareBottomSheet(data: PageInfo?) {
        data?.let { pageInfo ->
            universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
                init(this@DiscoveryFragment)
                setUtmCampaignData(
                    this@DiscoveryFragment.context?.resources?.getString(R.string.discovery)
                        ?: UTM_DISCOVERY,
                    if (UserSession(this@DiscoveryFragment.context).userId.isNullOrEmpty()) "0"
                    else UserSession(this@DiscoveryFragment.context).userId,
                    discoveryViewModel.getShareUTM(pageInfo),
                    this@DiscoveryFragment.context?.resources?.getString(R.string.share) ?: SHARE
                )
                setMetaData(
                    pageInfo.share?.title ?: "",
                    pageInfo.share?.image ?: ""
                )
                setOgImageUrl(pageInfo.share?.image ?: "")
            }
            universalShareBottomSheet?.show(
                fragmentManager,
                this@DiscoveryFragment,
                screenshotDetector
            )
            shareType = UniversalShareBottomSheet.getShareBottomSheetType()
            getDiscoveryAnalytics().trackUnifyShare(
                VIEW_DISCOVERY_IRIS,
                if (shareType == CUSTOM_SHARE_SHEET) VIEW_UNIFY_SHARE else VIEW_SCREENSHOT_SHARE,
                getUserID()
            )
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val linkerShareData = linkerDataMapper(pageInfoHolder)
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
            getUserID(), shareModel.channel ?: ""
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
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
            })
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

    override fun screenShotTaken() {
        showUniversalShareBottomSheet(pageInfoHolder)
    }

    private fun setToolBarPageInfoOnFail() {
        if (showOldToolbar) {
            typographyHeader.text = getString(R.string.discovery_tokopedia)
            ivSearch.hide()
            ivShare.hide()
        } else {
            setCartAndNavIcon()
            setupSearchBar(null)
        }
        mProgressBar.hide()
        mSwipeRefreshLayout.isRefreshing = false
    }

    private fun setCartAndNavIcon() {
        navToolbar.setIcon(
            IconBuilder()
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
        if (showOldToolbar) {
            getDiscoveryAnalytics().trackBackClick()
        } else {
            handleGlobalNavClick(Constant.TOP_NAV_BUTTON.BACK_BUTTON)
        }
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
                globalError.setType(GlobalError.PAGE_FULL)
            }
            else -> {
                globalError.setType(GlobalError.SERVER_ERROR)
                ServerLogger.log(
                    Priority.P2, "DISCOVERY_PAGE_ERROR",
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
            ), userAddressData
        )
    }

    private fun scrollToPinnedComponent(listComponent: List<ComponentsItem>) {
        if (!pinnedAlreadyScrolled) {
            val pinnedComponentId = arguments?.getString(COMPONENT_ID, "")
            if (!pinnedComponentId.isNullOrEmpty()) {
                val position =
                    discoveryViewModel.scrollToPinnedComponent(listComponent, pinnedComponentId)
                if (position >= 0) {
                    recyclerView.smoothScrollToPosition(position)
                    isManualScroll = false
                }
            }
            pinnedAlreadyScrolled = true
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
                data.applinks?.toEmptyStringIfNull().toString(), data.shopId?.toIntOrNull()
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
        discoveryViewModel.clearPageData()
        fetchDiscoveryPageData()
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
                    hashCode = hashCodeForMVC
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
                if (data == null)
                    return
                val channelId =
                    data.getStringExtra(PlayWidgetCardMediumChannelViewHolder.KEY_EXTRA_CHANNEL_ID)
                        .orEmpty()
                val totalView =
                    data.getStringExtra(PlayWidgetCardMediumChannelViewHolder.KEY_EXTRA_TOTAL_VIEW)
                        .orEmpty()
                val isReminder = data.getBooleanExtra(
                    PlayWidgetCardMediumChannelViewHolder.KEY_EXTRA_IS_REMINDER,
                    false
                )
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

            })
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
        discoveryViewModel.getDiscoveryPageInfo().observe(viewLifecycleOwner, {
            if (!openScreenStatus) {
                when (it) {
                    is Success -> {
                        sendOpenScreenAnalytics(it.data.identifier, it.data.additionalInfo)
                    }
                    else -> sendOpenScreenAnalytics(discoveryViewModel.pageIdentifier)
                }
            }
        })
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
        additionalInfo: AdditionalInfo? = null
    ) {
        if (identifier.isNullOrEmpty()) {
            getDiscoveryAnalytics().trackOpenScreen(
                discoveryViewModel.pageIdentifier,
                additionalInfo,
                isUserLoggedIn()
            )
        } else {
            getDiscoveryAnalytics().trackOpenScreen(identifier, additionalInfo, isUserLoggedIn())
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
        return if ((context as DiscoveryActivity).isFromCategory())
            Constant.ChooseAddressGTMSSource.CATEGORY_HOST_SOURCE
        else
            Constant.ChooseAddressGTMSSource.HOST_SOURCE
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return if ((context as DiscoveryActivity).isFromCategory())
            Constant.ChooseAddressGTMSSource.CATEGORY_HOST_TRACKING_SOURCE
        else
            Constant.ChooseAddressGTMSSource.HOST_TRACKING_SOURCE
    }

    override fun onLocalizingAddressLoginSuccess() {
    }

    override fun onLocalizingAddressUpdatedFromBackground() {

    }

    override fun getEventLabelHostPage(): String {
        return if ((context as DiscoveryActivity).isFromCategory())
            (context as DiscoveryActivity).getPageIdentifier()
        else
            EMPTY_STRING
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
                    com.tokopedia.unifyprinciples.R.color.Unify_G900
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
                    com.tokopedia.unifyprinciples.R.color.Unify_N0
                )
            )
        }
        coordinatorLayout.show()
        if (parentLayout.childCount > 1) {
            parentLayout.removeViewAt(1)
        }
    }

    private fun addMiniCartToPageFirstTime() {
        if (miniCartData == null)
            addMiniCartToPage()
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

    fun addOrUpdateItemCart(parentPosition: Int, position: Int, productId: String, quantity: Int) {
        discoveryViewModel.addProductToCart(
            parentPosition,
            position,
            productId,
            quantity,
            userAddressData?.shop_id ?: ""
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
                    pageName = MiniCartAnalytics.Page.DISCOVERY_PAGE
                )
                miniCartWidget?.show()
            } else {
                miniCartWidget?.updateData(data)
            }
            bottomNav?.hide()
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
        val map = HashMap<String, MiniCartItem>()
        data.miniCartItems.associateByTo(map, { it.productId })
        val variantMap = data.miniCartItems.groupBy { it.productParentId }
        for ((parentProductId, list) in variantMap) {
            if (parentProductId.isNotEmpty() && parentProductId != "0") {
                val quantity = list.sumOf { it.quantity }
                map[parentProductId] =
                    MiniCartItem(productParentId = parentProductId, quantity = quantity)
            }
        }
        setCartData(map, pageEndPoint)
        miniCartData = data
        reSync()
    }

    private fun hideSystemUi() {
        activity?.window?.apply {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
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
                AtcVariantHelper.DISCOVERY_PAGESOURCE,
                true,
                userAddressData?.shop_id ?: "",
                startActivitResult = { intent, reqCode ->
                    startActivityForResult(intent, reqCode)
                }
            )
        }
    }

    fun scrollToSection(sectionID: String, parentPosition: Int) {
        getSectionPositionMap(pageEndPoint)?.let {
            it[sectionID]?.let { position ->
                if (position >= 0) {
                    discoveryAdapter.getViewModelAtPosition(parentPosition)?.let { vm ->
                        (vm as? AnchorTabsViewModel)?.updateSelectedSection(sectionID)
                    }
//                    Todo:: Scroll with offset equal to size of anchor tabs.
                    recyclerView.smoothScrollToPosition(position)
                }
            }
        }
    }

    fun updateSelectedSection(sectionID: String) {
        getSectionPositionMap(pageEndPoint)?.let {
            it[sectionID]?.let { position ->
                val anchorPos = it[ComponentNames.AnchorTabs.componentName] ?: -1
                if (position >= 0 && anchorPos >= 0) {
                    discoveryAdapter.getViewModelAtPosition(anchorPos)?.let { vm ->
                        (vm as? AnchorTabsViewModel)?.updateSelectedSection(sectionID)
                    }
                }
            }
        }
    }

    fun handleHideSection(sectionID: String) {
        if (sectionID.isNotEmpty()) {
            val anchorPos =
                getSectionPositionMap(pageEndPoint)?.get(ComponentNames.AnchorTabs.componentName)
                    ?: -1
            if (anchorPos >= 0) {
                discoveryAdapter.getViewModelAtPosition(anchorPos)?.let { vm ->
                    (vm as? AnchorTabsViewModel)?.deleteSectionTab(sectionID)
                }
            }
        }
    }

    fun getScrollLiveData(): LiveData<ScrollData> {
        return discoveryViewModel.scrollState
    }

}