package com.tokopedia.mvcwidget.multishopmvc.verticallist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ViewFlipper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.mvcwidget.*
import com.tokopedia.mvcwidget.customview.MerchantRewardToolbar
import com.tokopedia.mvcwidget.di.components.MvcComponent
import com.tokopedia.mvcwidget.multishopmvc.MvcPerformanceConstant
import com.tokopedia.mvcwidget.multishopmvc.MvcPerformanceMonitoringListener
import com.tokopedia.mvcwidget.trackers.MvcSource.Companion.DEFAULT
import com.tokopedia.mvcwidget.trackers.Tracker.Constants.MERCHANT_COUPONLIST_SCREEN_NAME
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.mvcwidget.databinding.MvcLayoutMultishopMerchatCouponListBinding
import javax.inject.Inject

class MerchantCouponFragment : BaseDaggerFragment(), MvcPerformanceMonitoringListener,
    SwipeRefreshLayout.OnRefreshListener, AdapterCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel: MerchantCouponViewModel by lazy { ViewModelProvider(this, viewModelFactory)[MerchantCouponViewModel::class.java] }
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private val mCouponAdapter: MerchantCouponListAdapter by lazy { MerchantCouponListAdapter(mViewModel, this, HashSet() , DEFAULT) }

    private var merchantRewardToolbar: MerchantRewardToolbar? = null
    private lateinit var exploreCouponRv: RecyclerView
    private lateinit var swipeToRefresh: SwipeToRefresh
    private lateinit var appBarLayout: View
    private var statusBarBgView: View? = null
    private var serverErrorView: GlobalError? = null
    private var viewContainer: ViewFlipper? = null

    private var binding by autoClearedNullable<MvcLayoutMultishopMerchatCouponListBinding>()

    override fun getScreenName(): String {
        return MERCHANT_COUPONLIST_SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(MvcComponent::class.java)
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initInjector()
        val view = inflater.inflate(R.layout.mvc_layout_multishop_merchat_coupon_list, container, false)

        exploreCouponRv = view.findViewById(R.id.rv_merchant_couponlist)
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout)
        appBarLayout = view.findViewById(R.id.app_bar_layout)
        merchantRewardToolbar = view.findViewById(R.id.toolbar_merchant)
        statusBarBgView = view.findViewById(R.id.status_bar_bg)
        viewContainer = view.findViewById(R.id.container)
        (activity as BaseSimpleActivity).setSupportActionBar(merchantRewardToolbar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObserver()
        serverErrorView?.errorSecondaryAction?.setOnClickListener {
            showLoader()
            mCouponAdapter.loadData(1)
        }
        merchantRewardToolbar?.setTitle(R.string.mvc_kupon_toko)

        mViewModel.couponData.value = LiveDataResult.loading()
    }

    private fun initObserver() {
        addListObserver()
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
        exploreCouponRv.addItemDecoration(MerchantCouponItemDecoration(dpToPx(8).toInt()))
        mCouponAdapter.adImpression = arguments?.getSerializable(MVC_ADINFO) as HashSet<String?>
        mCouponAdapter.source = arguments?.getInt(MVC_SOURCE_KEY) ?: DEFAULT
        exploreCouponRv.adapter = mCouponAdapter
    }

    private fun addListObserver() = mViewModel.couponData.observe(viewLifecycleOwner, Observer {
        it?.let {
            when (it.status) {
                 LiveDataResult.STATUS.LOADING -> {
                    mCouponAdapter.resetAdapter()
                    mCouponAdapter.notifyDataSetChanged()
                    mCouponAdapter.startDataLoading()
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    setOnRecyclerViewLayoutReady()
                    it.data?.merchantCouponResponse?.productlist?.let { it1 -> mCouponAdapter.onSuccess(it1) }

                }
                LiveDataResult.STATUS.ERROR -> {
                    mCouponAdapter.onError()
                }
            }
        }
    })

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
            MvcPerformanceConstant.MVCCouponListPlt.MVCLISTSTACK_MVC_PLT_PREPARE_METRICS,
            MvcPerformanceConstant.MVCCouponListPlt.MVCLISTSTACK_MVC_PLT_NETWORK_METRICS,
            MvcPerformanceConstant.MVCCouponListPlt.MVCLISTSTACK_MVC_PLT_RENDER_METRICS,
            0,
            0,
            0,
            0,
            null
        )

        pageLoadTimePerformanceMonitoring?.startMonitoring(MvcPerformanceConstant.MVCCouponListPlt.MVCLISTSTACK_MVC_PLT)
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
        mViewModel.couponData.value = LiveDataResult.loading()
    }

    fun showLoader() {
        viewContainer?.displayedChild = CONTAINER_LOADER
        binding?.swipeRefreshLayout?.isRefreshing = false
    }

    fun hideLoader() {
        binding?.container?.displayedChild = CONTAINER_DATA
        binding?.swipeRefreshLayout?.isRefreshing = false
    }

    companion object {
        const val CONTAINER_LOADER = 0
        const val CONTAINER_DATA = 1
        const val CONTAINER_ERROR = 2
        const val CONTAINER_EMPTY = 3

        @JvmStatic
        fun newInstance(bundle: Bundle?): MerchantCouponFragment {
            val fragment = MerchantCouponFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onRetryPageLoad(pageNumber: Int) {}

    override fun onFinishPageLoad(itemCount: Int, pageNumber: Int, rawObject: Any?) {
        binding?.swipeRefreshLayout?.isRefreshing = false
    }

    override fun onStartPageLoad(pageNumber: Int) {}

    override fun onStartFirstPageLoad() {
        showLoader()
    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            binding?.container?.displayedChild = CONTAINER_ERROR
        }
        binding?.swipeRefreshLayout?.isRefreshing = false
    }

    private fun showEmptyView() {
        binding?.container?.displayedChild = CONTAINER_EMPTY
        binding?.errorLayout?.btnError?.setOnClickListener {
            onFragmentBackPressed()
        }
    }

    override fun onEmptyList(rawObject: Any?) {
        hideLoader()
        showEmptyView()
    }

    override fun onFinishFirstPageLoad(itemCount: Int, rawObject: Any?) {
        view?.postDelayed({ hideLoader() }, UI_SETTLING_DELAY_MS.toLong())
    }
}
