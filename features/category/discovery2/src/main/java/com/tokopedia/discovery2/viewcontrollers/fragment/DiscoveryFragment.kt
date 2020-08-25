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
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
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
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.analytics.DiscoveryAnalytics
import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.PageInfo
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.SOURCE_QUERY
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.customview.CustomTopChatView
import com.tokopedia.discovery2.viewcontrollers.customview.StickyHeadRecyclerView
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.BottomSheetUnify
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
private const val SCROLL_TOP_DIRECTION = -1

class DiscoveryFragment : BaseDaggerFragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private lateinit var discoveryViewModel: DiscoveryViewModel
    private lateinit var mDiscoveryFab: CustomTopChatView
    private lateinit var recyclerView: StickyHeadRecyclerView
    private lateinit var typographyHeader: Typography
    private lateinit var ivShare: ImageView
    private lateinit var ivSearch: ImageView
    private lateinit var ivToTop: ImageView
    private lateinit var globalError: GlobalError
    private lateinit var discoveryAdapter: DiscoveryRecycleAdapter
    private val analytics: DiscoveryAnalytics by lazy {
        DiscoveryAnalytics(trackingQueue = trackingQueue, pagePath = discoveryViewModel.pagePath, pageType = discoveryViewModel.pageType,
                pageIdentifier = discoveryViewModel.pageIdentifier, campaignCode = discoveryViewModel.campaignCode, sourceIdentifier = arguments?.getString(SOURCE_QUERY, "") ?: "")
    }
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mProgressBar: ProgressBar
    var pageEndPoint = ""
    private var componentPosition: Int? = null
    private var openScreenStatus = false

    @JvmField
    @Inject
    var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var trackingQueue: TrackingQueue

    companion object {
        fun getInstance(endPoint: String?, queryParameter: String?): Fragment {
            val bundle = Bundle()
            val fragment = DiscoveryFragment()
            if (!endPoint.isNullOrEmpty()) {
                bundle.putString(END_POINT, endPoint)
            }
            bundle.putString(SOURCE_QUERY, queryParameter)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discovery, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
//        mDiscoveryFab = view.findViewById(R.id.fab)
        typographyHeader = view.findViewById(R.id.typography_header)
        ivShare = view.findViewById(R.id.iv_share)
        ivSearch = view.findViewById(R.id.iv_search)
        view.findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            getDiscoveryAnalytics().trackBackClick()
            activity?.onBackPressed()
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
                if (!recyclerView.canScrollVertically(SCROLL_TOP_DIRECTION) && newState == RecyclerView.SCROLL_STATE_IDLE) {
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
        discoveryViewModel.getDiscoveryData()
        setUpObserver()
    }

    fun setAdapter() {
        recyclerView.apply {
            setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
            discoveryAdapter = DiscoveryRecycleAdapter(this@DiscoveryFragment).also {
                setAdapter(it)
            }
        }
    }

    fun reSync() {
        discoveryViewModel.getDiscoveryData()
    }

    private fun setUpObserver() {
        discoveryViewModel.getDiscoveryResponseList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    it.data.let { listComponent ->
                        if (mSwipeRefreshLayout.isRefreshing) setAdapter()
                        discoveryAdapter.addDataList(listComponent)
                    }
                    mProgressBar.hide()
                    stopDiscoveryPagePerformanceMonitoring()
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
                    ivSearch.show()
                    setPageInfo(it.data)
                }

                is Fail -> {
                    typographyHeader.text = ""
                    ivSearch.hide()
                    ivShare.hide()
                    mProgressBar.hide()
                    mSwipeRefreshLayout.isRefreshing = false

                    if (it.throwable is UnknownHostException
                            || it.throwable is SocketTimeoutException) {
                        globalError.setType(GlobalError.NO_CONNECTION)
                    } else {
                        globalError.setType(GlobalError.SERVER_ERROR)
                        Timber.w("P2#DISCOVERY_PAGE_ERROR#'${discoveryViewModel.pageIdentifier}';path='${discoveryViewModel.pagePath}';type='${discoveryViewModel.pageType}';err='${Log.getStackTraceString(it.throwable)}'")
                    }
                    globalError.show()
                    globalError.setOnClickListener {
                        globalError.hide()
                        discoveryViewModel.getDiscoveryData()
                    }
                }
            }
        })
    }

    private fun setPageInfo(data: PageInfo?) {
        typographyHeader.text = data?.name
        ivSearch.setOnClickListener {
            getDiscoveryAnalytics().trackSearchClick()
            if (data?.searchApplink?.isNotEmpty() == true) {
                RouteManager.route(context, data.searchApplink)
            } else {
                RouteManager.route(context, Utils.SEARCH_DEEPLINK)
            }
        }
        if (data?.share?.enabled == true) {
            ivShare.show()
            ivShare.setOnClickListener {
                getDiscoveryAnalytics().trackShareClick()
                Utils.shareData(activity, data.share.description, data.share.url)
            }
        } else {
            ivShare.hide()
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

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
        getDiscoveryAnalytics().clearProductViewIds()
    }

    override fun onRefresh() {
        trackingQueue.sendAll()
        getDiscoveryAnalytics().clearProductViewIds()
        discoveryViewModel.clearPageData()
        discoveryViewModel.getDiscoveryData()
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
                recyclerView.smoothScrollToTop()
                ivToTop.hide()
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return UserSession(activity).isLoggedIn
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
}