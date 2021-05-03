package com.tokopedia.shop.score.penalty.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.*
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyDateFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.model.*
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPenaltyPageFragment : BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>(),
        PenaltyDateFilterBottomSheet.CalenderListener,
        PenaltyFilterBottomSheet.PenaltyFilterFinishListener,
        ItemDetailPenaltyListener, ItemHeaderCardPenaltyListener,
        ItemPeriodDateFilterListener, ItemPenaltyErrorListener,
        ItemSortFilterPenaltyListener {


    @Inject
    lateinit var shopScorePenaltyTracking: ShopScorePenaltyTracking

    @Inject
    lateinit var viewModelShopPenalty: ShopPenaltyViewModel

    private val penaltyPageAdapterFactory by lazy {
        PenaltyPageAdapterFactory(this,
                this, this, this, this)
    }
    private val penaltyPageAdapter by lazy { PenaltyPageAdapter(penaltyPageAdapterFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_penalty_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
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
        clearAllData()
        penaltyPageAdapter.showLoading()
        viewModelShopPenalty.getDataPenalty()
    }

    override fun loadInitialData() {
        clearAllData()
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
        val typePenaltyList = penaltyFilterUiModelList.find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.chipsFilerList
        viewModelShopPenalty.setItemSortFilterWrapperList(penaltyFilterUiModelList, typePenaltyList.chipsPenaltyMapToItemSortFilter())

        penaltyPageAdapter.updateChipsSelected(typePenaltyList.chipsPenaltyMapToItemSortFilter())

        val typeId = typePenaltyList?.find { it.isSelected }?.value ?: 0
        val sortBy = penaltyFilterUiModelList.find { it.title == ShopScoreConstant.TITLE_SORT }?.chipsFilerList?.find { it.isSelected }?.value
                ?: 0
        viewModelShopPenalty.setSortTypeFilterData(Pair(sortBy, typeId))
        penaltyPageAdapter.apply {
            removePenaltyListData()
            refreshSticky()
            removeNotFoundPenalty()
            removeErrorStatePenalty()
            showLoading()
        }
        endlessRecyclerViewScrollListener.resetState()
    }

    private fun List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>?.chipsPenaltyMapToItemSortFilter(): List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper> {
        val itemSortFilterWrapperList = mutableListOf<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>()
        this?.map {
            itemSortFilterWrapperList.add(ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper(
                    title = it.title,
                    isSelected = it.isSelected
            ))
        }
        return itemSortFilterWrapperList
    }

    private fun updateItemChildSortFilterPenalty(sortFilterItemPeriodWrapperList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>?) {
        sortFilterItemPeriodWrapperList?.let { penaltyPageAdapter.updateChipsSelected(it) }
        val typeId = sortFilterItemPeriodWrapperList?.find { it.isSelected }?.idFilter ?: 0
        viewModelShopPenalty.setTypeFilterData(typeId)
        penaltyPageAdapter.apply {
            removePenaltyListData()
            refreshSticky()
            removeNotFoundPenalty()
            removeErrorStatePenalty()
            showLoading()
        }
        endlessRecyclerViewScrollListener.resetState()
    }

    override fun onSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>) {
        val date = if (startDate.second.isBlank() && endDate.second.isBlank()) {
            ""
        } else if (endDate.second.isBlank()) {
            startDate.second
        } else {
            "${startDate.second} - ${endDate.second}"
        }
        clearAllData()
        viewModelShopPenalty.setDateFilterData(Pair(startDate.first, endDate.first))
        viewModelShopPenalty.getDataPenalty()
        penaltyPageAdapter.showLoading()
        penaltyPageAdapter.updateDateFilterText(date)
    }

    override fun onItemPenaltyClick(itemPenaltyUiModel: ItemPenaltyUiModel) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL)
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(ShopPenaltyDetailFragment.KEY_ITEM_PENALTY_DETAIL, itemPenaltyUiModel)
        intent.putExtra(ShopPenaltyDetailFragment.KEY_CACHE_MANAGE_ID, cacheManager?.id)
        startActivity(intent)
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
            hideLoading()
            when (it) {
                is Success -> {
                    val basePenaltyData = it.data.penaltyVisitableList.first.filterNot { visitable -> visitable is ItemPenaltyUiModel }
                    val penaltyFilterDetailData = it.data.penaltyVisitableList.first.filterIsInstance<ItemPenaltyUiModel>()
                    penaltyPageAdapter.setPenaltyData(basePenaltyData)
                    onSuccessGetPenaltyListData(penaltyFilterDetailData, it.data.penaltyVisitableList.second, it.data.penaltyVisitableList.third)
                    penaltyPageAdapter.refreshSticky()
                }
                is Fail -> {
                    penaltyPageAdapter.setErrorStatePenalty(ItemPenaltyErrorUiModel(it.throwable))
                }
            }
        }
    }

    private fun observeDetailPenaltyNextPage() {
        observe(viewModelShopPenalty.shopPenaltyDetailData) {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetPenaltyListData(it.data.first, it.data.second, it.data.third)
                }
                is Fail -> {
                    penaltyPageAdapter.setErrorStatePenalty(ItemPenaltyErrorUiModel(it.throwable))
                }
            }
        }
    }

    private fun onSuccessGetPenaltyListData(data: List<ItemPenaltyUiModel>, hasPrev: Boolean, hasNext: Boolean) {
        if (!hasPrev && data.isEmpty()) {
            penaltyPageAdapter.setEmptyStatePenalty()
        } else {
            penaltyPageAdapter.updatePenaltyListData(data)
        }
        updateScrollListenerState(hasNext)
    }


    override fun onDateClick() {
        val bottomSheetDateFilter = PenaltyDateFilterBottomSheet.newInstance(viewModelShopPenalty.getStartDate(),
                viewModelShopPenalty.getEndDate())
        bottomSheetDateFilter.setCalendarListener(this)
        bottomSheetDateFilter.show(childFragmentManager)
    }

    override fun impressLearnMorePenaltyPage() {
        shopScorePenaltyTracking.impressLearnMorePenaltyPage()
    }

    override fun onMoreInfoHelpPenaltyClicked() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, ShopScoreConstant.SYSTEM_PENALTY_HELP_URL)
        shopScorePenaltyTracking.clickLearMorePenaltyPage()
    }

    override fun onRetryRefreshError() {
        onSwipeRefresh()
    }

    override fun onParentSortFilterClicked() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val filterPenalty = viewModelShopPenalty.getPenaltyFilterUiModelList()
        cacheManager?.put(PenaltyFilterBottomSheet.KEY_FILTER_TYPE_PENALTY, FilterTypePenaltyUiModelWrapper(
                filterPenalty
        ))

        val bottomSheetFilterPenalty = PenaltyFilterBottomSheet.newInstance(cacheManager?.id.orEmpty())
        bottomSheetFilterPenalty.setPenaltyFilterFinishListener(this)
        bottomSheetFilterPenalty.show(childFragmentManager)
    }

    override fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem) {
        viewModelShopPenalty.updateSortFilterSelected(sortFilterItem.title.toString(), sortFilterItem.type)
    }

    companion object {
        fun newInstance(): ShopPenaltyPageFragment {
            return ShopPenaltyPageFragment()
        }
    }
}