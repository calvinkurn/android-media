package com.tokopedia.shop.score.penalty.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.ShopScorePrefManager
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.common.plt.ShopPenaltyMonitoringContract
import com.tokopedia.shop.score.common.plt.ShopPenaltyPerformanceMonitoringListener
import com.tokopedia.shop.score.databinding.FragmentPenaltyPageBinding
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.presentation.activity.ShopPenaltyNotYetDeductedActivity
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemDetailPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemHeaderCardPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyErrorListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyInfoNotificationListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyPointCardListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltySubsectionListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyTickerListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPeriodDateFilterListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemSortFilterPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.BaseFilterPenaltyPage
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyCalculationBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyDateFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyStatusBottomSheet
import com.tokopedia.shop.score.penalty.presentation.model.ChipsFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.FilterTypePenaltyUiModelWrapper
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyErrorUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class ShopPenaltyPageFragment: BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>(),
    PenaltyDateFilterBottomSheet.CalenderListener,
    PenaltyFilterBottomSheet.PenaltyFilterFinishListener,
    ItemDetailPenaltyListener,
    ItemHeaderCardPenaltyListener,
    ItemPeriodDateFilterListener,
    ItemPenaltyErrorListener,
    ItemSortFilterPenaltyListener,
    ItemPenaltySubsectionListener,
    ItemPenaltyPointCardListener,
    ItemPenaltyInfoNotificationListener,
    ItemPenaltyTickerListener,
    ShopPenaltyMonitoringContract {

    @Inject
    lateinit var shopScorePenaltyTracking: ShopScorePenaltyTracking

    @Inject
    lateinit var viewModelShopPenalty: ShopPenaltyViewModel

    @Inject
    lateinit var shopScoreSharedPrefManager: ShopScorePrefManager

    private val penaltyPageAdapterFactory by lazy {
        PenaltyPageAdapterFactory(
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this,
            this
        )
    }

    private val penaltyPageAdapter by lazy {
        PenaltyPageAdapter(penaltyPageAdapterFactory)
    }

    private val pageType by lazy {
        arguments?.getString(PAGE_TYPE_KEY).orEmpty()
    }

    private val binding: FragmentPenaltyPageBinding? by viewBinding()

    private var shopPenaltyPerformanceMonitoringListener:
        ShopPenaltyPerformanceMonitoringListener? = null

    private var initialStartDate = String.EMPTY
    private var initialEndDate = String.EMPTY

    override fun onAttach(context: Context) {
        super.onAttach(context)
        shopPenaltyPerformanceMonitoringListener =
            castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onItemClicked(t: Visitable<*>?) { }

    override fun getScreenName(): String = String.EMPTY

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        viewModelShopPenalty.getPenaltyDetailListNext(page)
    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        return binding?.rvPenaltyPage
    }

    override fun getAdapterTypeFactory(): PenaltyPageAdapterFactory {
        return penaltyPageAdapterFactory
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return binding?.penaltySwipeRefresh
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory> {
        return penaltyPageAdapter
    }

    override fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = false
        clearAllPenaltyData()
        showLoading()
        viewModelShopPenalty.getDataPenalty(pageType)
    }

    override fun loadInitialData() {
        clearAllPenaltyData()
        showLoading()
        viewModelShopPenalty.getDataPenalty(pageType)
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
        observePenaltyPage()
        observeUpdateSortFilter()
        observeDetailPenaltyNextPage()
    }

    override fun onDateClick() {
        val bottomSheetDateFilter = PenaltyDateFilterBottomSheet.newInstance(
            viewModelShopPenalty.getStartDate(),
            viewModelShopPenalty.getEndDate(),
            viewModelShopPenalty.getMaxStartDate(),
            viewModelShopPenalty.getMaxEndDate()
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
            PenaltyFilterBottomSheet.KEY_FILTER_TYPE_PENALTY,
            FilterTypePenaltyUiModelWrapper(
                filterPenalty.filterIsInstance<PenaltyFilterUiModel>(),
                filterPenalty.filterIsInstance<PenaltyFilterDateUiModel>()
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

    override fun onClickFilterApplied(
        penaltyFilterUiModelList: List<BaseFilterPenaltyPage>
    ) {
        val typePenaltyList =
            penaltyFilterUiModelList.filterIsInstance<PenaltyFilterUiModel>()
                .find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.chipsFilterList
        val datePenaltyFilter =
            penaltyFilterUiModelList.filterIsInstance<PenaltyFilterDateUiModel>()?.firstOrNull()

        val chipsPenaltyMap = typePenaltyList.chipsPenaltyMapToItemSortFilter()

        viewModelShopPenalty.setItemSortFilterWrapperList(
            penaltyFilterUiModelList,
            chipsPenaltyMap
        )

        val typeIds = typePenaltyList?.filter { it.isSelected }?.map { it.value }.orEmpty()
        val sortBy =
            penaltyFilterUiModelList.filterIsInstance<PenaltyFilterUiModel>()
                .find { it.title == ShopScoreConstant.TITLE_SORT }?.chipsFilterList?.find { it.isSelected }?.value
                ?: ZERO_NUMBER
        endlessRecyclerViewScrollListener.resetState()

        var startDate = String.EMPTY
        var endDate = String.EMPTY
        datePenaltyFilter?.let {
            viewModelShopPenalty.setDateFilterData(it.startDate, it.endDate, it.completeDate)
            startDate = it.startDate
            endDate = it.endDate
        }
        viewModelShopPenalty.setSortTypeFilterData(Pair(sortBy, typeIds))

        binding?.rvPenaltyPage?.post {
            penaltyPageAdapter.run {
                updateChipsSelected(chipsPenaltyMap, getIsDateFilterApplied(startDate, endDate))
                removePenaltyListData()
                refreshSticky()
                removeNotFoundPenalty()
                removeErrorStatePenalty()
                showLoading()
                datePenaltyFilter?.let {
                    updateDateFilterText(it.completeDate)
                }
            }
        }
    }

    override fun onSaveCalendarClicked(
        startDate: Pair<String, String>,
        endDate: Pair<String, String>
    ) {
        // No-op
    }

    override fun onPenaltySubsectionIconClicked() {
        showStatusPenaltyBottomSheet()
    }

    override fun onPenaltyPointsButtonClicked(uiModel: ItemPenaltyPointCardUiModel) {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(
            PenaltyCalculationBottomSheet.KEY_PENALTY_CALCULATION,
            uiModel
        )

        PenaltyCalculationBottomSheet.createInstance(cacheManager?.id.orEmpty()).show(childFragmentManager)
    }

    override fun onNotYetPenaltyCardClicked(latestOngoingPenaltyId: String?) {
        latestOngoingPenaltyId?.let {
            shopScoreSharedPrefManager.setLatestOngoingPenaltyId(it)
        }
        penaltyPageAdapter.removeRedDots()
        startActivity(
            Intent(activity, ShopPenaltyNotYetDeductedActivity::class.java)
        )
    }

    override fun onDescriptionClicked(linkUrl: String) {
        context?.let { it ->
            RouteManager.route(
                it,
                ApplinkConstInternalGlobal.WEBVIEW,
                linkUrl.takeIf { link ->
                    link.isNotBlank()
                } ?: ShopScoreConstant.PRODUCT_PENALTY_CALCULATION_URL
            )
        }
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

    private fun updateItemChildSortFilterPenalty(sortFilterItemPeriodWrapperList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>?) {
        val typeIds = sortFilterItemPeriodWrapperList?.filter { it.isSelected }?.map {
            it.idFilter
        }.orEmpty()
        sortFilterItemPeriodWrapperList?.let { penaltyPageAdapter.updateChipsSelected(it) }
        penaltyPageAdapter.run {
            removePenaltyListData()
            refreshSticky()
            removeNotFoundPenalty()
            removeErrorStatePenalty()
            showLoading()
        }
        endlessRecyclerViewScrollListener.resetState()
        viewModelShopPenalty.setTypeFilterData(typeIds)
    }


    private fun observeUpdateSortFilter() {
        observe(viewModelShopPenalty.updateSortSelectedPeriod) {
            when (it) {
                is Success -> {
                    updateItemChildSortFilterPenalty(it.data)
                }
                else -> {
                    // no op
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

                    initialStartDate = viewModelShopPenalty.getStartDate()
                    initialEndDate = viewModelShopPenalty.getEndDate()
                }
                is Fail -> {
                    penaltyPageAdapter.setErrorStatePenalty(ItemPenaltyErrorUiModel(it.throwable))
                    ShopScoreReputationErrorLogger.logToCrashlytic(
                        ShopScoreReputationErrorLogger.SHOP_PENALTY_ERROR,
                        it.throwable
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
                        ShopScoreReputationErrorLogger.SHOP_PENALTY_DETAIL_NEXT_ERROR,
                        it.throwable
                    )
                }
            }
        }
    }

    private fun onSuccessGetPenaltyListData(data: List<ItemPenaltyUiModel>, hasNext: Boolean) {
        val penaltyList = penaltyPageAdapter.list.filterIsInstance<ItemPenaltyUiModel>()
        if (penaltyList.isEmpty() && data.isEmpty()) {
            penaltyPageAdapter.setEmptyStatePenalty(pageType)
        } else {
            penaltyPageAdapter.updatePenaltyListData(data)
        }
        updateScrollListenerState(hasNext)
    }

    private fun clearAllPenaltyData() {
        endlessRecyclerViewScrollListener?.resetState()
        penaltyPageAdapter.run {
            removeShopPenaltyLoading()
            removeErrorStatePenalty()
            removeNotFoundPenalty()
            removeShopPenaltyAllData()
            refreshSticky()
        }
    }

    private fun showStatusPenaltyBottomSheet() {
        val bottomSheet = PenaltyStatusBottomSheet.newInstance(pageType)
        bottomSheet.show(childFragmentManager)
    }

    private fun List<ChipsFilterPenaltyUiModel>?.chipsPenaltyMapToItemSortFilter(): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        val itemSortFilterWrapperList =
            mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()
        this?.forEachIndexed { index, chipsFilterPenaltyUiModel ->
            if (index < PenaltyMapper.MAX_SHOWN_FILTER_CHIPS || chipsFilterPenaltyUiModel.isSelected) {
                itemSortFilterWrapperList.add(
                    ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
                        title = chipsFilterPenaltyUiModel.title,
                        isSelected = chipsFilterPenaltyUiModel.isSelected,
                        idFilter = chipsFilterPenaltyUiModel.value
                    )
                )
            }
        }
        return itemSortFilterWrapperList
    }

    private fun getIsDateFilterApplied(startDate: String, endDate: String): Boolean {
        return startDate != initialStartDate || endDate != initialEndDate
    }

    companion object {

        private const val PAGE_TYPE_KEY = "page_type"

        @JvmStatic
        fun createInstance(@ShopPenaltyPageType pageType: String): ShopPenaltyPageFragment {
            val bundle = Bundle().apply {
                putString(PAGE_TYPE_KEY, pageType)
            }
            return ShopPenaltyPageFragment().apply {
                arguments = bundle
            }
        }

    }

}
