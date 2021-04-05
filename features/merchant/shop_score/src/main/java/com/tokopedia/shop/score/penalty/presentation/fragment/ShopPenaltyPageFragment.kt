package com.tokopedia.shop.score.penalty.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemDetailPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyDateFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.model.FilterTypePenaltyUiModelWrapper
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.performance.presentation.viewmodel.ShopPerformanceViewModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPenaltyPageFragment: BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>(),
        FilterPenaltyListener, PenaltyDateFilterBottomSheet.CalenderListener,
        PenaltyFilterBottomSheet.PenaltyFilterFinishListener, ItemDetailPenaltyListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelShopPenalty by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPenaltyViewModel::class.java)
    }

    private val penaltyPageAdapterFactory by lazy { PenaltyPageAdapterFactory(this, this) }
    private val penaltyPageAdapter by lazy { PenaltyPageAdapter(penaltyPageAdapterFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_penalty_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        observePenaltyPage()
        observeUpdateSortFilter()
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
        viewModelShopPenalty.getDataDummyPenalty()
    }

    override fun onItemClicked(t: Visitable<*>?) {
        //no op
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory> {
        return penaltyPageAdapter
    }

    override fun loadData(page: Int) {

    }

    override fun onDateClick() {
        val bottomSheetDateFilter = PenaltyDateFilterBottomSheet.newInstance()
        bottomSheetDateFilter.setCalendarListener(this)
        bottomSheetDateFilter.show(childFragmentManager)
    }

    override fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem, position: Int) {
        viewModelShopPenalty.updateSortFilterSelected(sortFilterItem.title.toString(), sortFilterItem.type)
    }

    override fun onClickFilterApplied(penaltyFilterUiModelList: List<PenaltyFilterUiModel>) {
        val typePenaltyList = penaltyFilterUiModelList.find { it.title == ShopScoreConstant.TITLE_TYPE_PENALTY }?.chipsFilerList
        penaltyPageAdapter.updateSortFilterPenaltyFromBottomSheet(typePenaltyList)
    }

    override fun onParentSortFilterClick() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val itemPenaltyFilterData = penaltyPageAdapter.data.filterIsInstance<ItemDetailPenaltyFilterUiModel>().firstOrNull() ?: ItemDetailPenaltyFilterUiModel()
        val itemTypePenaltyData = itemPenaltyFilterData.itemSortFilterWrapperList.filterTypePenaltyTransform()
        cacheManager?.put(PenaltyFilterBottomSheet.KEY_FILTER_TYPE_PENALTY, FilterTypePenaltyUiModelWrapper(itemTypePenaltyData))

        val bottomSheetFilterPenalty = PenaltyFilterBottomSheet.newInstance(cacheManager?.id.orEmpty())
        bottomSheetFilterPenalty.setPenaltyFilterFinishListener(this)
        bottomSheetFilterPenalty.show(childFragmentManager)
    }

    override fun onSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>) {
        val date = if (startDate.second.isBlank() && endDate.second.isBlank()) {
            ""
        } else if (endDate.second.isBlank()) {
            startDate.second
        } else {
            "${startDate.second} - ${endDate.second}"
        }
        penaltyPageAdapter.updateFilterDatePenalty(date)
    }

    override fun onItemPenaltyClick(statusPenalty: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_PENALTY_DETAIL)
        intent.putExtra(ShopPenaltyDetailFragment.STATUS_PENALTY, statusPenalty)
        startActivity(intent)
    }

    override fun getAdapterTypeFactory(): PenaltyPageAdapterFactory {
        return penaltyPageAdapterFactory
    }

    override fun onDestroy() {
        removeObservers(viewModelShopPenalty.penaltyPageData)
        super.onDestroy()
    }

    private fun List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>.filterTypePenaltyTransform(): List<FilterTypePenaltyUiModelWrapper.ItemFilterTypePenalty> {
        val itemFilterTypePenaltyList = mutableListOf<FilterTypePenaltyUiModelWrapper.ItemFilterTypePenalty>()
        for (item in this) {
            itemFilterTypePenaltyList.add(FilterTypePenaltyUiModelWrapper.ItemFilterTypePenalty(
                    title = item.sortFilterItem?.title?.toString().orEmpty(),
                    isSelected = item.isSelected
            ))
        }
        return itemFilterTypePenaltyList
    }

    private fun observeUpdateSortFilter() {
        observe(viewModelShopPenalty.updateSortFilterSelected) {
            when (it) {
                is Success -> {
                    penaltyPageAdapter.updateItemSortFilterPenalty(it.data)
                }
            }
        }
    }

    private fun observePenaltyPage() {
        observe(viewModelShopPenalty.penaltyPageData) {
            hideLoading()
            when (it) {
                is Success -> {
                    penaltyPageAdapter.setPenaltyData(it.data)
                }
            }
        }
        viewModelShopPenalty.getDataDummyPenalty()
    }

    companion object {
        fun newInstance(): ShopPenaltyPageFragment {
            return ShopPenaltyPageFragment()
        }
    }
}