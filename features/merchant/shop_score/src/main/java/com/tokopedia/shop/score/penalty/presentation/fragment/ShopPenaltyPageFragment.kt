package com.tokopedia.shop.score.penalty.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.gm.common.constant.ZERO_NUMBER
import com.tokopedia.gm.common.utils.ShopScoreReputationErrorLogger
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.common.plt.ShopPenaltyMonitoringContract
import com.tokopedia.shop.score.common.plt.ShopPenaltyPerformanceMonitoringListener
import com.tokopedia.shop.score.common.plt.ShopScorePerformanceMonitoringListener
import com.tokopedia.shop.score.databinding.FragmentPenaltyPageBinding
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.*
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyDateFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.model.*
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class ShopPenaltyPageFragment : BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>(),
    PenaltyDateFilterBottomSheet.CalenderListener,
    PenaltyFilterBottomSheet.PenaltyFilterFinishListener,
    ItemDetailPenaltyListener, ItemHeaderCardPenaltyListener,
    ItemPeriodDateFilterListener, ItemPenaltyErrorListener,
    ItemSortFilterPenaltyListener, ShopPenaltyMonitoringContract {

    @Inject
    lateinit var shopScorePenaltyTracking: ShopScorePenaltyTracking

    @Inject
    lateinit var viewModelShopPenalty: ShopPenaltyViewModel

    private val penaltyPageAdapterFactory by lazy {
        PenaltyPageAdapterFactory(
            this,
            this, this, this, this
        )
    }

    open val penaltyPageAdapter by lazy { PenaltyPageAdapter(penaltyPageAdapterFactory) }

    private val binding: FragmentPenaltyPageBinding? by viewBinding()

    private var shopPenaltyPerformanceMonitoringListener:
            ShopPenaltyPerformanceMonitoringListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        shopPenaltyPerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_penalty_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startNetworkRequestPerformanceMonitoring()
        stopPreparePerformancePageMonitoring()
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(
                ContextCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
        setupActionBar()
        observePenaltyPage()
        observeUpdateSortFilter()
        observeDetailPenaltyNextPage()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java).inject(this)
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvPenaltyPage)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return view.findViewById(R.id.penaltySwipeRefresh)
    }

    override fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = false
        clearAllPenaltyData()
        showLoading()
        viewModelShopPenalty.getDataPenalty()
    }


    override fun loadInitialData() {
        clearAllPenaltyData()
        showLoading()
        viewModelShopPenalty.getDataPenalty()
    }

    override fun onItemClicked(t: Visitable<*>?) {
        //no op
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory> {
        return penaltyPageAdapter
    }

    override fun loadData(page: Int) {
        viewModelShopPenalty.getPenaltyDetailListNext(page)
    }

    override fun onClickFilterApplied(penaltyFilterUiModelList: List<PenaltyFilterUiModel>) {
        val typePenaltyList =
            penaltyFilterUiModelList.find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.chipsFilterList
        val chipsPenaltyMap = typePenaltyList.chipsPenaltyMapToItemSortFilter()

        viewModelShopPenalty.setItemSortFilterWrapperList(
            penaltyFilterUiModelList,
            chipsPenaltyMap
        )

        penaltyPageAdapter.updateChipsSelected(chipsPenaltyMap)

        val typeId = typePenaltyList?.find { it.isSelected }?.value ?: ZERO_NUMBER
        val sortBy =
            penaltyFilterUiModelList.find { it.title == ShopScoreConstant.TITLE_SORT }?.chipsFilterList?.find { it.isSelected }?.value
                ?: ZERO_NUMBER
        penaltyPageAdapter.run {
            removePenaltyListData()
            refreshSticky()
            removeNotFoundPenalty()
            removeErrorStatePenalty()
            showLoading()
        }
        endlessRecyclerViewScrollListener.resetState()
        viewModelShopPenalty.setSortTypeFilterData(Pair(sortBy, typeId))
    }


    private fun updateItemChildSortFilterPenalty(sortFilterItemPeriodWrapperList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>?) {
        val typeId = sortFilterItemPeriodWrapperList?.find { it.isSelected }?.idFilter ?: ZERO_NUMBER
        sortFilterItemPeriodWrapperList?.let { penaltyPageAdapter.updateChipsSelected(it) }
        penaltyPageAdapter.run {
            removePenaltyListData()
            refreshSticky()
            removeNotFoundPenalty()
            removeErrorStatePenalty()
            showLoading()
        }
        endlessRecyclerViewScrollListener.resetState()
        viewModelShopPenalty.setTypeFilterData(typeId)
    }

    private fun clearAllPenaltyData() {
        penaltyPageAdapter.run {
            removeShopPenaltyLoading()
            removeErrorStatePenalty()
            removeNotFoundPenalty()
            removeShopPenaltyAllData()
            refreshSticky()
        }
    }

    private fun List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>?.chipsPenaltyMapToItemSortFilter(): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        val itemSortFilterWrapperList =
            mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()
        this?.map {
            itemSortFilterWrapperList.add(
                ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
                    title = it.title,
                    isSelected = it.isSelected,
                    idFilter = it.value
                )
            )
        }
        return itemSortFilterWrapperList
    }

    override fun onSaveCalendarClicked(
        startDate: Pair<String, String>,
        endDate: Pair<String, String>
    ) {
        val date = if (startDate.second.isBlank() && endDate.second.isBlank()) {
            ""
        } else if (endDate.second.isBlank()) {
            startDate.second
        } else {
            "${startDate.second} - ${endDate.second}"
        }
        viewModelShopPenalty.setDateFilterData(Pair(startDate.first, endDate.first))
        penaltyPageAdapter.run {
            removePenaltyListData()
            refreshSticky()
            removeNotFoundPenalty()
            removeErrorStatePenalty()
            showLoading()
        }
        endlessRecyclerViewScrollListener.resetState()
        penaltyPageAdapter.updateDateFilterText(date)
    }

    override fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel) {
        context?.let {
            val intent =
                RouteManager.getIntent(it, ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL)
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(ShopPenaltyDetailFragment.KEY_ITEM_PENALTY_DETAIL, itemPenaltyUiModel)
            intent.putExtra(ShopPenaltyDetailFragment.KEY_CACHE_MANAGE_ID, cacheManager.id)
            startActivity(intent)
        }
    }

    override fun getAdapterTypeFactory(): PenaltyPageAdapterFactory {
        return penaltyPageAdapterFactory
    }

    override fun onDestroy() {
        removeObservers(viewModelShopPenalty.penaltyPageData)
        super.onDestroy()
    }

    private fun observeUpdateSortFilter() {
        observe(viewModelShopPenalty.updateSortSelectedPeriod) {
            when (it) {
                is Success -> {
                    updateItemChildSortFilterPenalty(it.data)
                }
            }
        }
    }

    private fun observePenaltyPage() {
        observe(viewModelShopPenalty.penaltyPageData) {
            penaltyPageAdapter.removeShopPenaltyLoading()
            when (it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    val basePenaltyData =
                        it.data.penaltyVisitableList.first.filterNot { visitable -> visitable is ItemPenaltyUiModel }
                    val penaltyFilterDetailData =
                        it.data.penaltyVisitableList.first.filterIsInstance<ItemPenaltyUiModel>()
                    penaltyPageAdapter.setPenaltyData(basePenaltyData)
                    onSuccessGetPenaltyListData(
                        penaltyFilterDetailData,
                        it.data.penaltyVisitableList.third
                    )
                    penaltyPageAdapter.refreshSticky()
                }
                is Fail -> {
                    penaltyPageAdapter.setErrorStatePenalty(ItemPenaltyErrorUiModel(it.throwable))
                    ShopScoreReputationErrorLogger.logToCrashlytic(
                        ShopScoreReputationErrorLogger.SHOP_PENALTY_ERROR, it.throwable
                    )
                }
            }
        }
    }

    private fun observeDetailPenaltyNextPage() {
        observe(viewModelShopPenalty.shopPenaltyDetailData) {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetPenaltyListData(it.data.first, it.data.third)
                }
                is Fail -> {
                    penaltyPageAdapter.setErrorStatePenalty(ItemPenaltyErrorUiModel(it.throwable))
                    ShopScoreReputationErrorLogger.logToCrashlytic(
                        ShopScoreReputationErrorLogger.SHOP_PENALTY_DETAIL_NEXT_ERROR, it.throwable
                    )
                }
            }
        }
    }

    protected open fun onSuccessGetPenaltyListData(data: List<ItemPenaltyUiModel>, hasNext: Boolean) {
        val penaltyList = penaltyPageAdapter.list.filterIsInstance<ItemPenaltyUiModel>()
        if (penaltyList.isEmpty() && data.isEmpty()) {
            penaltyPageAdapter.setEmptyStatePenalty()
        } else {
            penaltyPageAdapter.updatePenaltyListData(data)
        }
        updateScrollListenerState(hasNext)
    }

    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            binding?.penaltyPageToolbar?.run {
                title = getString(R.string.title_penalty_shop_score)
                setNavigationOnClickListener {
                    onBackPressed()
                }
                isShowBackButton = true
            }
        }
    }

    override fun onDateClick() {
        val bottomSheetDateFilter = PenaltyDateFilterBottomSheet.newInstance(
            viewModelShopPenalty.getStartDate(),
            viewModelShopPenalty.getEndDate()
        )
        bottomSheetDateFilter.setCalendarListener(this)
        bottomSheetDateFilter.show(childFragmentManager)
    }

    override fun impressLearnMorePenaltyPage() {
        shopScorePenaltyTracking.impressLearnMorePenaltyPage()
    }

    override fun onMoreInfoHelpPenaltyClicked() {
        context?.let {
            RouteManager.route(
                it,
                ApplinkConstInternalGlobal.WEBVIEW,
                ShopScoreConstant.SYSTEM_PENALTY_HELP_URL
            )
        }
        shopScorePenaltyTracking.clickLearMorePenaltyPage()
    }

    override fun onRetryRefreshError() {
        onSwipeRefresh()
    }

    override fun onParentSortFilterClicked() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val filterPenalty = viewModelShopPenalty.getPenaltyFilterUiModelList()
        cacheManager?.put(
            PenaltyFilterBottomSheet.KEY_FILTER_TYPE_PENALTY, FilterTypePenaltyUiModelWrapper(
                filterPenalty
            )
        )

        val bottomSheetFilterPenalty =
            PenaltyFilterBottomSheet.newInstance(cacheManager?.id.orEmpty())
        bottomSheetFilterPenalty.setPenaltyFilterFinishListener(this)
        bottomSheetFilterPenalty.show(childFragmentManager)
    }

    override fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem) {
        viewModelShopPenalty.updateSortFilterSelected(
            sortFilterItem.title.toString(),
            sortFilterItem.type
        )
    }

    override fun stopPreparePerformancePageMonitoring() {
        shopPenaltyPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        shopPenaltyPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        shopPenaltyPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        shopPenaltyPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        binding?.rvPenaltyPage?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                shopPenaltyPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                shopPenaltyPerformanceMonitoringListener?.stopPerformanceMonitoring()
                binding?.rvPenaltyPage?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ShopPenaltyPerformanceMonitoringListener? {
        return if (context is ShopPenaltyPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    companion object {
        fun newInstance(): ShopPenaltyPageFragment {
            return ShopPenaltyPageFragment()
        }
    }
}