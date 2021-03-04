package com.tokopedia.tokopoints.view.couponlisting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.DaggerTokopointBundleComponent
import com.tokopedia.tokopoints.di.TokopointsQueryModule
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingActivity
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CouponliststackPlt.Companion.COUPONLISTSTACK_TOKOPOINT_PLT
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CouponliststackPlt.Companion.COUPONLISTSTACK_TOKOPOINT_PLT_NETWORK_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CouponliststackPlt.Companion.COUPONLISTSTACK_TOKOPOINT_PLT_PREPARE_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant.CouponliststackPlt.Companion.COUPONLISTSTACK_TOKOPOINT_PLT_RENDER_METRICS
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.tp_fragment_stacked_coupon_listing.*
import kotlinx.android.synthetic.main.tp_fragment_stacked_coupon_listing.view.*
import javax.inject.Inject

class CouponListingStackedFragment : BaseDaggerFragment(), CouponListingStackedContract.View, View.OnClickListener, AdapterCallback, TokopointPerformanceMonitoringListener {

    private var mItemDecoration: SpacesItemDecoration? = null

    @Inject
    lateinit var factory: ViewModelFactory

    val presenter: CouponLisitingStackedViewModel by lazy { ViewModelProviders.of(this, factory)[CouponLisitingStackedViewModel::class.java] }

    private val mAdapter: CouponListStackedBaseAdapter by lazy { CouponListStackedBaseAdapter(presenter, this) }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var redeemMessage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        redeemMessage = arguments?.getString(CommonConstant.CATALOG_CLAIM_MESSAGE, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tp_fragment_stacked_coupon_listing, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        ToasterHelper.showCouponClaimToast(redeemMessage,view)
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTrackerUtil.sendScreenEvent(activity, screenName)
    }

    override fun getAppContext(): Context? {
        return context
    }

    override fun showLoader() {
        container?.displayedChild = CONTAINER_LOADER
        swipe_refresh_layout?.isRefreshing = false
    }

    override fun hideLoader() {
        container?.displayedChild = CONTAINER_DATA
        swipe_refresh_layout?.isRefreshing = false
    }

    override fun getActivityContext(): Context? {
        return activity
    }

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.MY_COUPON_LISTING_SCREEN_NAME
    }

    override fun initInjector() {
        DaggerTokopointBundleComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .tokopointsQueryModule(TokopointsQueryModule(requireActivity()))
                .build().inject(this)
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_failed_action) {
            showLoader()
            mAdapter.loadData(mAdapter.currentPageIndex)
        }
    }

    private fun initViews(view: View) {
        mItemDecoration = SpacesItemDecoration(0,
                activityContext!!.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_0),
                activityContext!!.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_0))
        if (view.recycler_view_coupons.itemDecorationCount > 0) {
            view.recycler_view_coupons.removeItemDecoration(mItemDecoration!!)
        }
        view.recycler_view_coupons.addItemDecoration(mItemDecoration!!)
        view.recycler_view_coupons.adapter = mAdapter

    }

    private fun initListener() {
        if (view == null) {
            return
        }

        view!!.findViewById<View>(R.id.text_failed_action).setOnClickListener(this)
        view!!.findViewById<View>(R.id.button_continue).setOnClickListener { view12 ->
            val bundle = Bundle()
            bundle.putInt(CommonConstant.EXTRA_COUPON_COUNT, 0)
            startActivity(CatalogListingActivity.getCallingIntent(activityContext, bundle))
        }
        view!!.findViewById<View>(R.id.text_empty_action).setOnClickListener { v -> RouteManager.route(activityContext, ApplinkConstInternalGlobal.WEBVIEW, CommonConstant.WebLink.INFO) }

        stopPreparePagePerformanceMonitoring()
        startNetworkRequestPerformanceMonitoring()
        swipe_refresh_layout.setOnRefreshListener {
            val id = presenter.category
            id?.let { presenter.getCoupons(id) }
        }

        addListObserver()
        addInStackedObserverList()
    }

    private fun addInStackedObserverList() = presenter.inStackedAdapter.observe(viewLifecycleOwner, Observer {
        it?.let {
            showCouponInStackBottomSheet(it)
        }
    })

    private fun addListObserver() = presenter.startAdapter.observe(this, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                    mAdapter.resetAdapter()
                    mAdapter.notifyDataSetChanged()
                    mAdapter.startDataLoading()
                }
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    setOnRecyclerViewLayoutReady()
                    mAdapter.onSuccess(it.data)
                }
                is ErrorMessage -> {
                    mAdapter.onError()
                }
            }
        }
    })

    override fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    override fun emptyCoupons(errors: Map<String, String>?) {
        if (view == null || errors == null) {
            return
        }
        view!!.findViewById<View>(R.id.button_continue).visibility = View.VISIBLE

        container.displayedChild = CONTAINER_EMPTY
    }

    override fun onRetryPageLoad(pageNumber: Int) {}

    override fun onEmptyList(rawObject: Any) {
        hideLoader()
        emptyCoupons((rawObject as TokoPointPromosEntity).coupon.emptyMessage)
    }

    override fun onStartFirstPageLoad() {
        showLoader()
    }

    override fun onFinishFirstPageLoad(count: Int, rawObject: Any?) {
        view!!.postDelayed({ hideLoader() }, CommonConstant.UI_SETTLING_DELAY_MS.toLong())
    }

    override fun onStartPageLoad(pageNumber: Int) {}

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
        swipe_refresh_layout.isRefreshing = false
    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            container.displayedChild = CONTAINER_ERROR
            server_error_view?.showErrorUi(NetworkDetector.isConnectedToInternet(appContext))
        }
        swipe_refresh_layout.isRefreshing = false
    }

    fun showCouponInStackBottomSheet(data: TokoPointPromosEntity) {
        val closeableBottomSheetDialog = BottomSheetUnify()

        closeableBottomSheetDialog.setShowListener {
            val sideMargin = 8.toPx()
            closeableBottomSheetDialog.bottomSheetWrapper.setPadding(sideMargin, sideMargin, sideMargin, 0)
            (closeableBottomSheetDialog.bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(sideMargin, sideMargin, sideMargin, 0)
        }

        val view = layoutInflater.inflate(R.layout.tp_bottosheet_coupon_in_stack, null, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_coupon_in_stack)
        if (mItemDecoration != null) {
            recyclerView.addItemDecoration(mItemDecoration!!)
        }

        val mStackedInadapter = CouponInStackBaseAdapter(object : AdapterCallback {
            override fun onRetryPageLoad(pageNumber: Int) {

            }

            override fun onEmptyList(rawObject: Any) {
                closeableBottomSheetDialog.dismiss()
            }

            override fun onStartFirstPageLoad() {

            }

            override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
                closeableBottomSheetDialog.show(childFragmentManager, "")
            }

            override fun onStartPageLoad(pageNumber: Int) {

            }

            override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {

            }

            override fun onError(pageNumber: Int) {
                closeableBottomSheetDialog.dismiss()
            }
        }, data)

        recyclerView.adapter = mStackedInadapter
        closeableBottomSheetDialog.apply {
            setChild(view)
            isDragable = true
            isHideable = true
            showCloseIcon = false
            showHeader = false
            isFullpage = false
            showKnob = true
            this@CouponListingStackedFragment.view?.height?.div(2)?.let { height ->
                customPeekHeight = height
            }
        }
        closeableBottomSheetDialog.show(childFragmentManager, "")
        mStackedInadapter.startDataLoading()

    }

    override fun onDestroyView() {
        mAdapter.onDestroyView()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val code = data!!.getStringExtra(CommonConstant.EXTRA_COUPON_CODE)
        if (requestCode == REQUEST_CODE_STACKED_ADAPTER) {
            mAdapter.couponCodeVisible(code, false)
        } else if (requestCode == REQUEST_CODE_STACKED_IN_ADAPTER) {
            mAdapter.couponStackedVisible()
        }
    }

    companion object {
        private val CONTAINER_LOADER = 0
        private val CONTAINER_DATA = 1
        private val CONTAINER_ERROR = 2
        private val CONTAINER_EMPTY = 3
        val REQUEST_CODE_STACKED_IN_ADAPTER = 4
        val REQUEST_CODE_STACKED_ADAPTER = 5

        fun newInstance(): CouponListingStackedFragment {
            return CouponListingStackedFragment()
        }
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                COUPONLISTSTACK_TOKOPOINT_PLT_PREPARE_METRICS,
                COUPONLISTSTACK_TOKOPOINT_PLT_NETWORK_METRICS,
                COUPONLISTSTACK_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(COUPONLISTSTACK_TOKOPOINT_PLT)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    private fun setOnRecyclerViewLayoutReady() {
        recycler_view_coupons?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (pageLoadTimePerformanceMonitoring != null) {
                            stopRenderPerformanceMonitoring()
                            stopPerformanceMonitoring()
                        }
                        pageLoadTimePerformanceMonitoring = null
                        recycler_view_coupons?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                })
    }
}
