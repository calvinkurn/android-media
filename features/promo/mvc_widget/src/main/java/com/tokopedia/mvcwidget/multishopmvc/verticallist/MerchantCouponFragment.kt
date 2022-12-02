package com.tokopedia.mvcwidget.multishopmvc.verticallist

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.library.baseadapter.AdapterCallback
import com.tokopedia.mvcwidget.*
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
        binding = MvcLayoutMultishopMerchatCouponListBinding.inflate(inflater, container, false)

        (activity as BaseSimpleActivity).setSupportActionBar(binding?.merchantCouponRewardToolBar)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
        binding?.merchantCouponErrorView?.errorSecondaryAction?.setOnClickListener {
            showLoader()
            mCouponAdapter.loadData(1)
        }
        binding?.merchantCouponRewardToolBar?.setTitle(R.string.mvc_kupon_toko)

        mViewModel.couponData.value = LiveDataResult.loading()
    }

    private fun initObserver() {
        addListObserver()
    }

    private fun initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding?.merchantCouponAppBar?.outlineProvider = null
        }
        binding?.merchantCouponSwipeRefresh?.setOnRefreshListener(this)
        val linearLayoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,
                false)
        binding?.rvMerchantCouponList?.layoutManager = linearLayoutManager
        binding?.rvMerchantCouponList?.addItemDecoration(MerchantCouponItemDecoration(dpToPx(8).toInt()))
        mCouponAdapter.adImpression = arguments?.getSerializable(MVC_ADINFO) as HashSet<String?>
        mCouponAdapter.source = arguments?.getInt(MVC_SOURCE_KEY) ?: DEFAULT
        binding?.rvMerchantCouponList?.adapter = mCouponAdapter
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
        binding?.rvMerchantCouponList?.viewTreeObserver
                ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (pageLoadTimePerformanceMonitoring != null) {
                            stopRenderPerformanceMonitoring()
                            stopPerformanceMonitoring()
                        }
                        pageLoadTimePerformanceMonitoring = null
                        binding?.rvMerchantCouponList?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    }
                })
    }

    override fun onRefresh() {
        mViewModel.couponData.value = LiveDataResult.loading()
    }

    fun showLoader() {
        binding?.merchantCouponContainer?.displayedChild = CONTAINER_LOADER
        binding?.merchantCouponSwipeRefresh?.isRefreshing = false
    }

    fun hideLoader() {
        binding?.merchantCouponContainer?.displayedChild = CONTAINER_DATA
        binding?.merchantCouponSwipeRefresh?.isRefreshing = false
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
        binding?.merchantCouponSwipeRefresh?.isRefreshing = false
    }

    override fun onStartPageLoad(pageNumber: Int) {}

    override fun onStartFirstPageLoad() {
        showLoader()
    }

    override fun onError(pageNumber: Int) {
        if (pageNumber == 1) {
            binding?.merchantCouponContainer?.displayedChild = CONTAINER_ERROR
        }
        binding?.merchantCouponSwipeRefresh?.isRefreshing = false
    }

    private fun showEmptyView() {
        binding?.merchantCouponContainer?.displayedChild = CONTAINER_EMPTY
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
