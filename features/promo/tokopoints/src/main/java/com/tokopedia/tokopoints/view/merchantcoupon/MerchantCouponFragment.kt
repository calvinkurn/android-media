package com.tokopedia.tokopoints.view.merchantcoupon

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.di.TokopointBundleComponent
import com.tokopedia.tokopoints.view.adapter.MerchantCouponItemDecoration
import com.tokopedia.tokopoints.view.customview.MerchantRewardToolbar
import com.tokopedia.tokopoints.view.customview.ServerErrorView
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceConstant
import com.tokopedia.tokopoints.view.firebaseAnalytics.TokopointPerformanceMonitoringListener
import com.tokopedia.tokopoints.view.util.*
import kotlinx.android.synthetic.main.tp_layout_merchat_coupon_list.*
import javax.inject.Inject

class MerchantCouponFragment : BaseDaggerFragment(), TokopointPerformanceMonitoringListener, SwipeRefreshLayout.OnRefreshListener, AdapterCallback, View.OnClickListener {

    @Inject
    lateinit var factory: ViewModelFactory

    private val mViewModel: MerchantCouponViewModel by lazy { ViewModelProvider(this, factory)[MerchantCouponViewModel::class.java] }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private val mCouponAdapter: MerchantCouponListAdapter by lazy { MerchantCouponListAdapter(mViewModel, this) }

    private var merchantRewardToolbar: MerchantRewardToolbar? = null
    private lateinit var exploreCouponRv: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var appBarLayout: View
    private var statusBarBgView: View? = null
    private var categoryId: String = ""
    private var serverErrorView: ServerErrorView? = null

    override fun getScreenName(): String {
        return AnalyticsTrackerUtil.ScreenKeys.MERCHANT_COUPONLIST_SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(TokopointBundleComponent::class.java)
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        val view = inflater.inflate(R.layout.tp_layout_merchat_coupon_list, container, false)
        exploreCouponRv = view.findViewById(R.id.rv_merchant_couponlist)
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout)
        appBarLayout = view.findViewById(R.id.app_bar_layout)
        merchantRewardToolbar = view.findViewById(R.id.toolbar_merchant)
        statusBarBgView = view.findViewById(R.id.status_bar_bg)
        (activity as BaseSimpleActivity).setSupportActionBar(merchantRewardToolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initVar()
        initObserver()
        view?.findViewById<View>(R.id.text_failed_action).setOnClickListener(this)
        merchantRewardToolbar?.setTitle(R.string.tp_kupon_toko)

        mViewModel.couponData.value = Loading()
    }

    private fun initVar() {
        if (arguments != null) {
            categoryId = (requireArguments().getString(
                    PARAM_CATEGORY_ID,
                    DEFAULT_CATEGORY)
                    )
        }
    }

    private fun initObserver() {
        addListObserver()
    }

    override fun onClick(source: View) {
        if (source.id == R.id.text_failed_action) {
            showLoader()
            mCouponAdapter.loadData(1)
        }
    }

    private fun initViews(view: View) {

        serverErrorView = view.findViewById(R.id.server_error_view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.outlineProvider = null
        }
        swipeToRefresh.setOnRefreshListener(this)
        val linearLayoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,
                false)
        exploreCouponRv.layoutManager = linearLayoutManager
        exploreCouponRv.addItemDecoration(MerchantCouponItemDecoration(convertDpToPixel(8, exploreCouponRv.context)))
        exploreCouponRv.adapter = mCouponAdapter
    }

    private fun addListObserver() = mViewModel.couponData.observe(viewLifecycleOwner, Observer {
        it?.let {
            when (it) {
                is Loading -> {
                    mCouponAdapter.resetAdapter()
                    mCouponAdapter.notifyDataSetChanged()
                    mCouponAdapter.startDataLoading()
                }
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    setOnRecyclerViewLayoutReady()
                    it.data.merchantCouponResponse.productlist?.let { it1 -> mCouponAdapter.onSuccess(it1) }
                }
                is ErrorMessage -> {
                    mCouponAdapter.onError()
                }
                else -> {
                }
            }
        }
    })


    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                TokopointPerformanceConstant.CouponliststackPlt.COUPONLISTSTACK_TOKOPOINT_PLT_PREPARE_METRICS,
                TokopointPerformanceConstant.CouponliststackPlt.COUPONLISTSTACK_TOKOPOINT_PLT_NETWORK_METRICS,
                TokopointPerformanceConstant.CouponliststackPlt.COUPONLISTSTACK_TOKOPOINT_PLT_RENDER_METRICS,
                0,
                0,
                0,
                0,
                null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(TokopointPerformanceConstant.CouponliststackPlt.COUPONLISTSTACK_TOKOPOINT_PLT)
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
        exploreCouponRv?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (pageLoadTimePerformanceMonitoring != null) {
                            stopRenderPerformanceMonitoring()
                            stopPerformanceMonitoring()
                        }
                        pageLoadTimePerformanceMonitoring = null
                        exploreCouponRv?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                })
    }

    override fun onRefresh() {
        val id = mViewModel.category
        id?.let { mViewModel.setCategoryRootId(id) }
    }

    fun showLoader() {
        container?.displayedChild = CONTAINER_LOADER
        swipe_refresh_layout?.isRefreshing = false
    }

    fun hideLoader() {
        container?.displayedChild = CONTAINER_DATA
        swipe_refresh_layout?.isRefreshing = false
    }

    fun getStatusBarHeight(context: Context?): Int {
        var height = 0
        val resId = requireContext().resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0 && context != null) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }

    companion object {

        const val PARAM_CATEGORY_ID = "categoryRootID"
        private const val DEFAULT_CATEGORY = "0"
        const val CONTAINER_LOADER = 0
        const val CONTAINER_DATA = 1
        const val CONTAINER_ERROR = 2

        @JvmStatic
        fun newInstance(bundle: Bundle?): MerchantCouponFragment {
            val fragment = MerchantCouponFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {}

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
        swipe_refresh_layout.isRefreshing = false
    }

    override fun onStartPageLoad(pageNumber: Int) {}

    override fun onStartFirstPageLoad() {
        showLoader()
    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            container.displayedChild = CONTAINER_ERROR
            server_error_view?.showErrorUi(NetworkDetector.isConnectedToInternet(context?.applicationContext))
        }
        swipe_refresh_layout.isRefreshing = false
    }

    override fun onEmptyList(rawObject: Any?) {
        hideLoader()
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        view?.postDelayed({ hideLoader() }, CommonConstant.UI_SETTLING_DELAY_MS.toLong())
    }
}