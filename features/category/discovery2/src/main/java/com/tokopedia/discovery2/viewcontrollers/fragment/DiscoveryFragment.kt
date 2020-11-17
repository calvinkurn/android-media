package com.tokopedia.discovery2.viewcontrollers.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PHONE
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.analytics.DiscoveryAnalytics
import com.tokopedia.discovery2.data.*
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
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
import com.tokopedia.discovery2.viewcontrollers.customview.CustomTopChatView
import com.tokopedia.discovery2.viewcontrollers.customview.StickyHeadRecyclerView
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


private const val LOGIN_REQUEST_CODE = 35769
private const val MOBILE_VERIFICATION_REQUEST_CODE = 35770
const val PAGE_REFRESH_LOGIN = 35771
private const val SCROLL_TOP_DIRECTION = -1
private const val DEFAULT_SCROLL_POSITION = 0
private const val EXP_NAME = "Navigation Revamp"
private const val VARIANT_OLD = "Existing Navigation"
private const val VARIANT_REVAMP = "Navigation Revamp"

class DiscoveryFragment : BaseDaggerFragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, LihatSemuaViewHolder.OnLihatSemuaClickListener {

    private lateinit var discoveryViewModel: DiscoveryViewModel
    private lateinit var mDiscoveryFab: CustomTopChatView
    private lateinit var recyclerView: StickyHeadRecyclerView
    private lateinit var typographyHeader: Typography
    private lateinit var ivShare: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var ivToTop: ImageView
    private lateinit var globalError: GlobalError
    private lateinit var navToolbar: NavToolbar
    private lateinit var discoveryAdapter: DiscoveryRecycleAdapter
    private val analytics: DiscoveryAnalytics by lazy {
        DiscoveryAnalytics(trackingQueue = trackingQueue, pagePath = discoveryViewModel.pagePath, pageType = discoveryViewModel.pageType,
                pageIdentifier = discoveryViewModel.pageIdentifier, campaignCode = discoveryViewModel.campaignCode, sourceIdentifier = arguments?.getString(SOURCE, "")
                ?: "")
    }
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mProgressBar: LoaderUnify
    var pageEndPoint = ""
    private var componentPosition: Int? = null
    private var openScreenStatus = false
    private var pinnedAlreadyScrolled = false
    var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null
    private var showOldToolbar: Boolean = false

    @Inject
    lateinit var trackingQueue: TrackingQueue

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
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((context?.applicationContext
                        as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(view)
        initView(view)
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
            navToolbar.setOnBackButtonClickListener(::handleBackPress)
        }
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
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    ivToTop.hide()
                } else if (dy < 0) {
                    ivToTop.show()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_TOP_DIRECTION)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ivToTop.hide()
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
        discoveryViewModel.getDiscoveryResponseList().observe(viewLifecycleOwner, Observer {
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

        discoveryViewModel.getDiscoveryFabLiveData().observe(viewLifecycleOwner, Observer {
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

        discoveryViewModel.getDiscoveryPageInfo().observe(viewLifecycleOwner, Observer {
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
    }

    private fun setToolBarPageInfoOnSuccess(data: PageInfo?) {
        handleShare(data)
        if (showOldToolbar) {
            ivSearch.show()
            ivSearch.setOnClickListener {
                getDiscoveryAnalytics().trackSearchClick()
                RouteManager.route(context, handleSearchClick(data))
            }
            typographyHeader.text = data?.name ?: getString(R.string.tokopedia)
        } else {
            setupSearchBar(data)
        }
    }

    private fun handleShare(data: PageInfo?) {
        if (data?.share?.enabled == true) {
            if (showOldToolbar) {
                ivShare.show()
                ivShare.setOnClickListener {
                    handleShareClick(data.share)
                }
            } else {
                navToolbar.setIcon(
                        IconBuilder()
                                .addIcon(IconList.ID_SHARE) { handleShareClick(data.share) }
                                .addIcon(IconList.ID_CART) { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) }
                                .addIcon(IconList.ID_NAV_GLOBAL) { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) }
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

    private fun handleShareClick(share: Share) {
        if (showOldToolbar) {
            getDiscoveryAnalytics().trackShareClick()
        } else {
            handleGlobalNavClick(Constant.TOP_NAV_BUTTON.SHARE)
        }
        Utils.shareData(activity, share.description, share.url)
    }

    private fun setToolBarPageInfoOnFail() {
        if (showOldToolbar) {
            typographyHeader.text = getString(R.string.tokopedia)
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
                        .addIcon(IconList.ID_CART) { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.CART) }
                        .addIcon(IconList.ID_NAV_GLOBAL) { handleGlobalNavClick(Constant.TOP_NAV_BUTTON.GLOBAL_MENU) }
        )
    }

    private fun setupSearchBar(data: PageInfo?) {
        navToolbar.setupSearchbar(
                hints = listOf(HintData(placeholder = data?.searchTitle
                        ?: getString(R.string.default_search_title))),
                applink = handleSearchClick(data),
                searchbarClickCallback = {
                    handleGlobalNavClick(Constant.TOP_NAV_BUTTON.SEARCH_BAR)
                }
        )
    }

    private fun handleSearchClick(data: PageInfo?): String {
        return if (data?.searchApplink?.isNotEmpty() == true) {
            data.searchApplink
        } else {
            Utils.SEARCH_DEEPLINK
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
                Timber.w("P2#DISCOVERY_PAGE_ERROR#'${discoveryViewModel.pageIdentifier}';path='${discoveryViewModel.pagePath}';type='${discoveryViewModel.pageType}';err='${Log.getStackTraceString(it.throwable)}'")
            }
        }
        globalError.show()
        globalError.setActionClickListener {
            globalError.hide()
            showLoadingWithRefresh()
        }
    }

    private fun fetchDiscoveryPageData() {
        discoveryViewModel.getDiscoveryData(discoveryViewModel.getQueryParameterMapFromBundle(arguments))
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

    fun getDiscoveryAnalytics(): DiscoveryAnalytics {
        return analytics
    }

    private fun showLoadingWithRefresh() {
        mProgressBar.show()
        refreshPage()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
        getDiscoveryAnalytics().clearProductViewIds()
    }

    override fun onRefresh() {
        refreshPage()
    }

    private fun refreshPage() {
        trackingQueue.sendAll()
        getDiscoveryAnalytics().clearProductViewIds()
        discoveryViewModel.clearPageData()
        fetchDiscoveryPageData()
        getDiscoveryAnalytics().clearProductViewIds()
    }

    fun openLoginScreen(componentPosition: Int = -1) {
        this.componentPosition = componentPosition
        startActivityForResult(RouteManager.getIntent(activity, ApplinkConst.LOGIN), LOGIN_REQUEST_CODE)
    }

    fun openMobileVerificationWithBottomSheet(componentPosition: Int = -1) {
        this.componentPosition = componentPosition
        showVerificationBottomSheet()
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
        }
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
        discoveryViewModel.getDiscoveryPageInfo().observe(viewLifecycleOwner, Observer {
            if (!openScreenStatus) {
                when (it) {
                    is Success -> {
                        sendOpenScreenAnalytics(it.data.identifier, it.data.additionalInfo)
                    }
                    else -> sendOpenScreenAnalytics(discoveryViewModel.pageIdentifier)
                }
            }
        })
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

    override fun onProductCardHeaderClick(componentsItem: ComponentsItem) {
        getDiscoveryAnalytics().trackHeaderSeeAllClick(isUserLoggedIn(), componentsItem)
    }

    override fun onLihatSemuaClick(data: DataItem) {
        getDiscoveryAnalytics().trackLihatSemuaClick(data.name)
    }
}