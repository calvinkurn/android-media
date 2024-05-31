package com.tokopedia.product.detail.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.smoothSnapToPosition
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.pdplayout.CacheState
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLoadingDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTabletLeftSectionDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductTabletRightSectionDataModel
import com.tokopedia.product.detail.data.model.datamodel.TabletPosition
import com.tokopedia.product.detail.data.util.CenterLayoutManager
import com.tokopedia.product.detail.data.util.CenterLayoutManagerTablet
import com.tokopedia.product.detail.data.util.ProductDetailConstant.TABLET_LEFT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.TABLET_RIGHT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.TABLET_TYPE
import com.tokopedia.product.detail.databinding.ProductDetailFragmentBinding
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.adapter.dynamicadapter.ProductDetailAdapter
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationAdapter
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by Yehezkiel on 05/01/21
 */
abstract class BaseProductDetailFragment<T : Visitable<*>, F : AdapterTypeFactory> :
    BaseDaggerFragment() {

    companion object {
        private const val INSTANT_SMOOTH_SCROLL_MILLISECONDS_PER_INCH = 0.1f
        private const val DEFAULT_BLOCK_LIMIT = 5
    }

    var productAdapter: ProductDetailAdapter? = null
    var concatAdapter: ConcatAdapter? = null
    var productDaggerComponent: ProductDetailComponent? = null

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private var rvPdp: RecyclerView? = null
    private var swipeToRefresh: SwipeRefreshLayout? = null

    protected abstract fun createAdapterInstance(): ProductDetailAdapter

    protected abstract fun loadData(forceRefresh: Boolean = false)

    protected abstract fun observeData()

    protected var binding by autoClearedNullable<ProductDetailFragmentBinding>()

    open fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = true
        loadData(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                androidx.core.content.ContextCompat.getColor(
                    it,
                    unifyprinciplesR.color.Unify_Background
                )
            )
        }
        setHasOptionsMenu(true)
        productAdapter = createAdapterInstance()
        concatAdapter = ConcatAdapter(productAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductDetailFragmentBinding.inflate(inflater, container, false)
        getProductDetailActivity()?.getBlocksPerformanceMonitoring()
            ?.addViewPerformanceBlocks(binding?.pdpNavtoolbar)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeLayout(view)
        setupRecyclerView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    fun submitInitialList(visitables: List<DynamicPdpDataModel>, cacheState: CacheState?) {
        hideSwipeLoading()

        val finalData = if (isTabletMode()) {
            manipulateDataTabletMode(visitables)
        } else {
            recordPerformanceTrace(visitables, true, cacheState)
            visitables
        }

        rvPdp?.post {
            productAdapter?.submitList(finalData)
        }
    }

    fun submitList(visitables: List<DynamicPdpDataModel>) {
        rvPdp?.post {
            recordPerformanceTrace(visitables, false)
            productAdapter?.submitList(visitables)
        }
    }

    internal fun isTabletMode(): Boolean {
        val isEnableTabletMode =
            remoteConfig.getBoolean(
                RemoteConfigKey.ANDROID_PDP_ENABLE_TABLET_MODE,
                false
            )
        return DeviceScreenInfo.isTablet(requireContext()) && isEnableTabletMode
    }

    internal fun manipulateDataTabletMode(newData: List<DynamicPdpDataModel>)
        : List<DynamicPdpDataModel> {
        val leftSide = mutableListOf<DynamicPdpDataModel>()
        val rightSide = mutableListOf<DynamicPdpDataModel>()
        val bottomSide = mutableListOf<DynamicPdpDataModel>()

        newData.forEach { dynamicPdpDataModel ->
            if (dynamicPdpDataModel.tabletSectionPosition == TabletPosition.LEFT) {
                leftSide.add(dynamicPdpDataModel)
                return@forEach
            }

            if (dynamicPdpDataModel.tabletSectionPosition == TabletPosition.RIGHT) {
                rightSide.add(dynamicPdpDataModel)
                return@forEach
            }

            if (dynamicPdpDataModel.tabletSectionPosition == TabletPosition.BOTTOM) {
                bottomSide.add(dynamicPdpDataModel)
                return@forEach
            }
        }
        val leftTab = ProductTabletLeftSectionDataModel(TABLET_LEFT, TABLET_TYPE, leftSide)
        val rightTab = ProductTabletRightSectionDataModel(TABLET_RIGHT, TABLET_TYPE, rightSide)
        return listOf(leftTab, rightTab) + bottomSide
    }

    private fun recordPerformanceTrace(
        visitables: List<DynamicPdpDataModel>,
        intialList: Boolean,
        cacheState: CacheState? = null
    ) {
        var position = 0
        (getRecyclerView()?.layoutManager as? CenterLayoutManager)?.let { layoutManager ->
            val lastVisibleItemPosition = IntArray(layoutManager.getSpanCount())
            layoutManager.findLastVisibleItemPositions(lastVisibleItemPosition)
            var lastVisibleItemPositionSpan1 = 0
            if (lastVisibleItemPosition.size >= 1) {
                lastVisibleItemPositionSpan1 = lastVisibleItemPosition[1]
            }
            position = lastVisibleItemPositionSpan1
        }
        var visitablesForPerf = visitables
        if (!intialList && position >= 0) {
            visitablesForPerf = visitablesForPerf.take(
                position
            )
        }
        val blockIdentifier = if (cacheState?.isFromCache == true) {
            ProductDetailActivity.P1_CACHE_KEY
        } else if (cacheState?.isPrefetch == true) {
            ProductDetailActivity.P1_PREFETCH_KEY
        } else {
            ProductDetailActivity.P1_NETWORK_KEY
        }

        getProductDetailActivity()?.getBlocksPerformanceMonitoring()?.setBlock(
            visitablesForPerf,
            blockIdentifier,
            DEFAULT_BLOCK_LIMIT
        )
    }

    fun showLoading() {
        productAdapter?.showLoading()
    }

    fun renderPageError(errorModel: PageErrorDataModel) {
        context?.let { _ ->
            productAdapter?.showError(errorModel)
            swipeToRefresh?.let {
                it.isEnabled = false
            }
            binding?.partialLayoutButtonAction?.baseBtnAction?.gone()
        }
    }

    fun getViewHolderByPosition(position: Int): RecyclerView.ViewHolder? {
        if (position == -1) {
            return null
        }
        return rvPdp?.findViewHolderForAdapterPosition(position)
    }

    fun <T : DynamicPdpDataModel> getComponentPosition(data: T?): Int {
        return if (data != null) {
            productAdapter?.currentList?.indexOf(data) ?: RecyclerView.NO_POSITION
        } else {
            RecyclerView.NO_POSITION
        }
    }

    fun <T : DynamicPdpDataModel> getComponentPositionBeforeUpdate(data: T?): Int {
        return if (data != null) {
            productAdapter?.currentList?.indexOfFirst {
                it.name() == data.name()
            } ?: RecyclerView.NO_POSITION
        } else {
            RecyclerView.NO_POSITION
        }
    }

    fun scrollToPosition(position: Int) {
        if (position >= 0) {
            getRecyclerView()?.post {
                try {
                    getRecyclerView()?.smoothScrollToPosition(position)
                } catch (_: Throwable) {
                }
            }
        }
    }

    /**
     * "Instantly" scroll and snap to the specified RecyclerView item position. This function triggers
     * a smooth scroll position with the speed of 0.1 ms per inch so that it looks like an instant scroll
     * (the default smooth scroll speed is 25 ms per inch).
     *
     * @param position The position of the item we want to scroll into
     * @param snapMode The snap mode preference must be any of the option provided on [LinearSmoothScroller]
     */
    fun snapScrollToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
        if (position > RecyclerView.NO_POSITION) {
            getRecyclerView()?.post {
                try {
                    val offsetY = binding
                        ?.pdpNavigation
                        ?.getNavigationTabHeight()
                        ?.plus(binding?.pdpNavtoolbar?.height.orZero())
                        .orZero()
                    getRecyclerView()?.smoothSnapToPosition(
                        position = position,
                        snapMode = snapMode,
                        topOffset = offsetY,
                        millisecondsPerInch = INSTANT_SMOOTH_SCROLL_MILLISECONDS_PER_INCH
                    )
                } catch (_: Throwable) {
                }
            }
        }
    }

    fun getRecyclerView(): RecyclerView? {
        return rvPdp
    }

    private fun setupSwipeLayout(view: View) {
        swipeToRefresh = view.findViewById<View>(R.id.swipeRefreshPdp) as SwipeRefreshLayout
        swipeToRefresh?.let {
            it.setOnRefreshListener { onSwipeRefresh() }
        }
    }

    private fun hideSwipeLoading() {
        swipeToRefresh?.let {
            it.isEnabled = true
            it.isRefreshing = false
        }
    }

    private fun setupRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.rv_pdp) ?: return

        rv.apply {
            isNestedScrollingEnabled = false
            itemAnimator = null
            layoutManager = if (isTabletMode()) {
                initGridLayoutManager(view.context)
            } else {
                initStaggeredLayoutManager(view.context)
            }
            adapter = concatAdapter
        }
        rvPdp = rv
    }

    private fun initStaggeredLayoutManager(context: Context): StaggeredGridLayoutManager {
        return CenterLayoutManager(context).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        }
    }

    private fun initGridLayoutManager(context: Context): GridLayoutManager {
        val manager = CenterLayoutManagerTablet(context)
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return decideSpanSize(position)
            }
        }
        return manager
    }

    private fun decideSpanSize(position: Int): Int {
        val isPageError = productAdapter?.currentList.orEmpty().any {
            it is PageErrorDataModel || it is ProductLoadingDataModel
        }

        /**
         * Infinite recom needs to treat differently when deciding span,
         * because the default implementation is only for StaggeredGridLayoutManager
         *
         * It needs to be manually decide the span here as GridLayoutManager
         */
        val infiniteAdapter = concatAdapter?.adapters?.last() as? InfiniteRecommendationAdapter
        if (infiniteAdapter != null &&
            position > productAdapter?.currentList?.size?.dec().orZero()
        ) {
            val infinitePosition = position - productAdapter?.currentList?.size.orZero()
            val data =
                infiniteAdapter.currentList
                    .getOrNull(infinitePosition) as InfiniteRecommendationUiModel

            return if (data.isFullSpan) {
                2
            } else {
                1
            }
        }

        return if (position > 1 || isPageError) {
            2
        } else {
            1
        }
    }

    protected fun getProductDetailActivity() = activity as? ProductDetailActivity
}
