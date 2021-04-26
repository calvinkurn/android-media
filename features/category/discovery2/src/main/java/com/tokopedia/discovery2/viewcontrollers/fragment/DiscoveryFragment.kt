package com.tokopedia.discovery2.viewcontrollers.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PHONE
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.analytics.BaseDiscoveryAnalytics
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.datamapper.discoComponentQuery
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
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua.LihatSemuaViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemDecorator
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.playwidget.DiscoveryPlayWidgetViewModel
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
import com.tokopedia.media.loader.loadImage
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
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
private const val EXP_NAME = AbTestPlatform.NAVIGATION_EXP_TOP_NAV
private const val VARIANT_OLD = AbTestPlatform.NAVIGATION_VARIANT_OLD
private const val VARIANT_REVAMP = AbTestPlatform.NAVIGATION_VARIANT_REVAMP

class DiscoveryFragment :
        BaseDaggerFragment(),
        SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener,
        LihatSemuaViewHolder.OnLihatSemuaClickListener,
        TabLayout.OnTabSelectedListener,
        ChooseAddressWidget.ChooseAddressWidgetListener {

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
    private var shouldShowChooseAddressWidget:Boolean = true

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

    companion object {
        fun getInstance(endPoint: String?, queryParameterMap: Map<String, String?>?): DiscoveryFragment {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
    }

    private fun initChooseAddressWidget(view: View) {
        chooseAddressWidget = view.findViewById(R.id.choose_address_widget)
        chooseAddressWidgetDivider = view.findViewById(R.id.divider_view)
        chooseAddressWidget?.bindChooseAddress(this)
        context?.let {
            if (ChooseAddressUtils.isRollOutUser(it)) {
                fetchUserLatestAddressData()
            }
        }
    }

    private fun initToolbar(view: View) {
        showOldToolbar = !RemoteConfigInstance.getInstance().abTestPlatform.getString(EXP_NAME, VARIANT_OLD).equals(VARIANT_REVAMP, true)
        val oldToolbar: Toolbar = view.findViewById(R.id.oldToolbar)
        navToolbar = view.findViewById(R.id.navToolbar)
        if (showOldToolbar) {
            oldToolbar.visibility = View.VISIBLE
            navToolbar.visibility = View.GONE
        } else {
            navToolbar.visibility = View.VISIBLE
            oldToolbar.visibility = View.GONE
            navToolbar.setOnBackButtonClickListener(disableDefaultGtmTracker = true, backButtonClickListener = ::handleBackPress)
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
        mProgressBar.show()
        mSwipeRefreshLayout.setOnRefreshListener(this)
        ivToTop.setOnClickListener(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var scrollDist = 0
            val MINIMUM = 25.toPx()
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    ivToTop.hide()
                } else if (dy < 0) {
                    ivToTop.show()
                }
                scrollDist += dy
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_TOP_DIRECTION)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ivToTop.hide()
                }
                if (scrollDist > MINIMUM) {
                    chooseAddressWidget?.hide()
                    chooseAddressWidgetDivider?.hide()
                    shouldShowChooseAddressWidget = false
                    scrollDist = 0
                } else if (scrollDist < -MINIMUM) {
                    if (discoveryViewModel.getAddressVisibilityValue() && ChooseAddressUtils.isRollOutUser(context)) {
                        chooseAddressWidget?.show()
                        chooseAddressWidgetDivider?.show()
                        shouldShowChooseAddressWidget = true
                    }
                    scrollDist = 0
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        discoveryViewModel = (activity as DiscoveryActivity).getViewModel()
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
            setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
            discoveryAdapter = DiscoveryRecycleAdapter(this@DiscoveryFragment).also {
                setAdapter(it)
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
                            setPageErrorState(Fail(IllegalStateException()))
                        } else {
                            scrollToPinnedComponent(listComponent)
                        }
                    }
                    mProgressBar.hide()
                    stopDiscoveryPagePerformanceMonitoring()
                }
                is Fail -> {
                    mProgressBar.hide()
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
                    setToolBarPageInfoOnSuccess(it.data)
                }
                is Fail -> {
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

        discoveryViewModel.checkAddressVisibility().observe(viewLifecycleOwner, { widgetVisibilityStatus ->
            context?.let {
                if (widgetVisibilityStatus && ChooseAddressUtils.isRollOutUser(it)) {
                    if(shouldShowChooseAddressWidget) {
                        chooseAddressWidget?.show()
                        chooseAddressWidgetDivider?.show()
                    }
                    if(ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(it) == true){
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
                        tab.customView = LayoutInflater.from(this.context).inflate(R.layout.bottom_nav_item, bottomTabHolder, false).apply {
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
                                .addIcon(iconId = IconList.ID_SHARE, disableRouteManager = true, onClick = { handleShareClick(data) }, disableDefaultGtmTracker = true)
                                .addIcon(iconId = IconList.ID_CART, onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) }, disableDefaultGtmTracker = true)
                                .addIcon(iconId = IconList.ID_NAV_GLOBAL, onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) }, disableDefaultGtmTracker = true)
                )
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
        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0,
                linkerDataMapper(data), object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult) {
                if (linkerShareData.url != null) {
                    Utils.shareData(activity, data?.share?.description, linkerShareData.url)
                }
            }

            override fun onError(linkerError: LinkerError) {
                Utils.shareData(activity, data?.share?.description, data?.share?.url)
            }
        }))
    }

    private fun linkerDataMapper(data: PageInfo?): LinkerShareData {
        val linkerData = LinkerData()
        linkerData.id = data?.id?.toString() ?: ""
        linkerData.name = data?.name ?: ""
        linkerData.uri = Utils.getShareUrlQueryParamAppended(data?.share?.url
                ?: "", discoComponentQuery)
        linkerData.description = data?.share?.description ?: ""
        linkerData.isThrowOnError = true
        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        return linkerShareData
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
                        .addIcon(iconId = IconList.ID_CART, onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) }, disableDefaultGtmTracker = true)
                        .addIcon(iconId = IconList.ID_NAV_GLOBAL, onClick = { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) }, disableDefaultGtmTracker = true)
        )
    }

    private fun setupSearchBar(data: PageInfo?) {
        navToolbar.setupSearchbar(
                hints = listOf(HintData(placeholder = data?.searchTitle
                        ?: getString(R.string.discovery_default_search_title))),
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
                ServerLogger.log(Priority.P2, "DISCOVERY_PAGE_ERROR",
                        mapOf(
                                "identifier" to discoveryViewModel.pageIdentifier,
                                "path" to discoveryViewModel.pagePath,
                                "type" to discoveryViewModel.pageType,
                                "err" to Log.getStackTraceString(it.throwable)
                        ))
            }
        }
        globalError.show()
        globalError.setActionClickListener {
            globalError.hide()
            showLoadingWithRefresh()
        }
    }

    private fun fetchDiscoveryPageData() {
        discoveryViewModel.getDiscoveryData(discoveryViewModel.getQueryParameterMapFromBundle(arguments), userAddressData)
    }

    private fun scrollToPinnedComponent(listComponent: List<ComponentsItem>) {
        if (!pinnedAlreadyScrolled) {
            val pinnedComponentId = arguments?.getString(COMPONENT_ID, "")
            if (!pinnedComponentId.isNullOrEmpty()) {
                val position = discoveryViewModel.scrollToPinnedComponent(listComponent, pinnedComponentId)
                if (position >= 0) {
                    recyclerView.smoothScrollToPosition(position)
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
            setClick(data.applinks?.toEmptyStringIfNull().toString(), data.shopId?.toIntOrNull()
                    ?: 0)
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
        discoveryViewModel.clearPageData()
        fetchDiscoveryPageData()
        getDiscoveryAnalytics().clearProductViewIds(true)
    }

    fun openLoginScreen(componentPosition: Int = -1) {
        this.componentPosition = componentPosition
        startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.LOGIN), LOGIN_REQUEST_CODE)
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
                discoveryBaseViewModel?.loggedInCallback()
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
                    refreshPage()
                }
            }
            OPEN_PLAY_CHANNEL -> {
                if (data == null)
                    return
                val channelId = data.getStringExtra(PlayWidgetCardMediumChannelViewHolder.KEY_EXTRA_CHANNEL_ID).orEmpty()
                val totalView = data.getStringExtra(PlayWidgetCardMediumChannelViewHolder.KEY_EXTRA_TOTAL_VIEW).orEmpty()
                if (discoveryBaseViewModel is DiscoveryPlayWidgetViewModel)
                    (discoveryBaseViewModel as DiscoveryPlayWidgetViewModel).updatePlayWidgetTotalView(channelId, totalView)
            }
        }
        AdultManager.handleActivityResult(activity, requestCode, resultCode, data, object : AdultManager.Callback {
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
        val childView = View.inflate(context, R.layout.mobile_verification_bottom_sheet_layout, null)
        this.fragmentManager?.let {
            closeableBottomSheetDialog.apply {
                showCloseIcon = true
                setChild(childView)
                show(it, null)
            }
        }
        childView.findViewById<UnifyButton>(R.id.verify_btn).setOnClickListener {
            closeableBottomSheetDialog.dismiss()
            startActivityForResult(RouteManager.getIntent(activity, ADD_PHONE), MOBILE_VERIFICATION_REQUEST_CODE)
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
            if (ChooseAddressUtils.isRollOutUser(it) && discoveryViewModel.getAddressVisibilityValue()) {
                updateChooseAddressWidget()
                checkAddressUpdate()
            }
        }
    }

    private fun sendOpenScreenAnalytics(identifier: String?, additionalInfo: AdditionalInfo? = null) {
        if (identifier.isNullOrEmpty()) {
            getDiscoveryAnalytics().trackOpenScreen(discoveryViewModel.pageIdentifier, additionalInfo, isUserLoggedIn())
        } else {
            getDiscoveryAnalytics().trackOpenScreen(identifier, additionalInfo, isUserLoggedIn())
        }
        openScreenStatus = true
    }


    override fun onStop() {
        super.onStop()
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
        getDiscoveryAnalytics().trackLihatSemuaClick(data.name)
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
            Color.parseColor(textColor)
        } catch (exception: Exception) {
            ContextCompat.getColor(context, R.color.Green_G500)
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
        if (isRollOutUser && discoveryViewModel.getAddressVisibilityValue()) {
            chooseAddressWidget?.show()
            chooseAddressWidgetDivider?.show()
        } else {
            chooseAddressWidget?.hide()
            chooseAddressWidgetDivider?.hide()
        }
    }

    override fun getLocalizingAddressHostFragment(): Fragment {
        return this
    }

    override fun getLocalizingAddressHostSourceData(): String {
        return analytics.getHostSource()
    }

    override fun getLocalizingAddressHostSourceTrackingData(): String {
        return analytics.getHostTrackingSource()
    }

    override fun onLocalizingAddressLoginSuccess() {
    }

    override fun onLocalizingAddressUpdatedFromBackground() {

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
}