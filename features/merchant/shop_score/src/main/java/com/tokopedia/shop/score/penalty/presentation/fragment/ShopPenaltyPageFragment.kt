package com.tokopedia.shop.score.penalty.presentation.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.analytics.ShopScorePenaltyTracking
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.common.setTypeGlobalError
import com.tokopedia.shop.score.common.toggle
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.*
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyViewHolder
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyDateFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.model.*
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.card_shop_score_total_penalty.*
import kotlinx.android.synthetic.main.fragment_penalty_page.*
import kotlinx.android.synthetic.main.item_detail_penalty_filter.*
import kotlinx.android.synthetic.main.item_shimmer_penalty.*
import kotlinx.android.synthetic.main.item_shop_penalty_error_state.*
import javax.inject.Inject

class ShopPenaltyPageFragment : BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>(),
        PenaltyDateFilterBottomSheet.CalenderListener,
        PenaltyFilterBottomSheet.PenaltyFilterFinishListener, ItemDetailPenaltyListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var shopScorePenaltyTracking: ShopScorePenaltyTracking

    private val viewModelShopPenalty by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopPenaltyViewModel::class.java)
    }

    private val penaltyPageAdapterFactory by lazy { PenaltyPageAdapterFactory(this) }
    private val penaltyPageAdapter by lazy { PenaltyPageAdapter(penaltyPageAdapterFactory) }

    private val impressHolderLearnMore = ImpressHolder()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_penalty_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        setupSwipeRefresh()
        observePenaltyPage()
        observeUpdateSortFilter()
        observeDetailPenaltyNextPage()
    }

    private fun setupSwipeRefresh() {
        appBarPenaltyPage?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            swipeToRefresh?.isEnabled = (verticalOffset == 0)
        })

        rvPenaltyPage?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView.childCount == 0) 0 else recyclerView.getChildAt(0).top
                swipeToRefresh?.isEnabled = topRowVerticalPosition >= 0
            }
        })
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
        hideAllViewWithLoading()
        viewModelShopPenalty.getDataPenalty()
    }

    override fun loadInitialData() {
        clearAllData()
        hideAllViewWithLoading()
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
        sortFilterDetailPenalty.setupSortFilter(typePenaltyList.chipsPenaltyMapToItemSortFilter())

        val typeId = typePenaltyList?.find { it.isSelected }?.value ?: 0
        val sortBy = penaltyFilterUiModelList.find { it.title == ShopScoreConstant.TITLE_SORT }?.chipsFilerList?.find { it.isSelected }?.value
                ?: 0
        viewModelShopPenalty.setSortTypeFilterData(Pair(sortBy, typeId))
        endlessRecyclerViewScrollListener.resetState()
        penaltyPageAdapter.hidePenaltyData()
        penaltyPageAdapter.removePenaltyNotFound()
        penaltyPageAdapter.setPenaltyLoading()
    }

    private fun List<PenaltyFilterUiModel.ChipsFilterPenaltyUiModel>?.chipsPenaltyMapToItemSortFilter(): List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper> {
        val itemSortFilterWrapperList = mutableListOf<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>()
        this?.map {
            itemSortFilterWrapperList.add(ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper(
                    title = it.title,
                    isSelected = it.isSelected
            ))
        }
        return itemSortFilterWrapperList
    }

    private fun updateItemChildSortFilterPenalty(sortFilterItemWrapperList: List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>?) {
        sortFilterDetailPenalty.setupSortFilter(sortFilterItemWrapperList)
        val typeId = sortFilterItemWrapperList?.find { it.isSelected }?.idFilter ?: 0
        viewModelShopPenalty.setTypeFilterData(typeId)
        endlessRecyclerViewScrollListener.resetState()
        penaltyPageAdapter.hidePenaltyData()
        penaltyPageAdapter.removePenaltyNotFound()
        penaltyPageAdapter.setPenaltyLoading()
    }

    override fun onSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>) {
        val date = if (startDate.second.isBlank() && endDate.second.isBlank()) {
            ""
        } else if (endDate.second.isBlank()) {
            startDate.second
        } else {
            "${startDate.second} - ${endDate.second}"
        }
        tvPeriodDetailPenalty?.text = getString(R.string.period_date_detail_penalty, date)
        penaltyPageAdapter.hidePenaltyData()
        penaltyPageAdapter.removePenaltyNotFound()
        viewModelShopPenalty.setDateFilterData(Pair(startDate.first, endDate.first))
        viewModelShopPenalty.getDataPenalty()
        hideAllViewWithLoading()
        endlessRecyclerViewScrollListener.resetState()
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

    private fun setupCardPenalty(element: ItemCardShopPenaltyUiModel?) {
        tvContentPenalty?.addOnImpressionListener(impressHolderLearnMore) {
            shopScorePenaltyTracking.impressLearnMorePenaltyPage()
        }
        tvContentPenalty?.setTextMakeHyperlink(getString(R.string.content_penalty_label)) {
            onMoreInfoHelpPenaltyClicked()
            shopScorePenaltyTracking.clickLearMorePenaltyPage()
        }
        bgTotalPenalty?.background = context?.let {
            ContextCompat.getDrawable(it, if (element?.hasPenalty == true) R.drawable.ic_has_penalty else R.drawable.ic_no_penalty)
        }
        val roundedRadius = 8F
        bgTotalPenalty?.shapeAppearanceModel = bgTotalPenalty.shapeAppearanceModel
                .toBuilder()
                .setAllCornerSizes(roundedRadius)
                .build()

        if (element?.hasPenalty == true) {
            tvCountTotalPenalty.text = element.totalPenalty.toString()
            context?.resources?.getDimension(R.dimen.scorePenaltyTextSize)?.let { tvCountTotalPenalty.setTextSize(TypedValue.COMPLEX_UNIT_PX, it) }
        } else {
            tvCountTotalPenalty?.text = getString(R.string.desc_no_penalty)
            tvCountTotalPenalty.setType(Typography.HEADING_3)
        }
        tvTotalPointDeductions?.text = if (element?.deductionPoints?.isLessThanZero() == true) element.deductionPoints.toString() else "-"
    }

    private fun setupDetailPenaltyFilter(element: ItemDetailPenaltyFilterUiModel?) {
        tvPeriodDetailPenalty?.text = getString(R.string.period_date_detail_penalty, element?.periodDetail.orEmpty())
        sortFilterDetailPenalty?.parentListener = {
            onParentSortFilterClick()
        }
        sortFilterDetailPenalty?.setupSortFilter(element?.itemSortFilterWrapperList)

        ic_detail_penalty_filter?.setOnClickListener {
            onDateClick()
        }
    }

    private fun SortFilter.setupSortFilter(updateSortFilterItemList: List<ItemDetailPenaltyFilterUiModel.ItemSortFilterWrapper>?) {
        sortFilterItems.removeAllViews()
        indicatorCounter = 0
        val sortFilterItemList = ArrayList<SortFilterItem>()

        updateSortFilterItemList?.map {
            sortFilterItemList.add(SortFilterItem(
                    title = it.title,
                    size = ChipsUnify.SIZE_SMALL,
                    type = if (it.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            ))
        }

        addItem(sortFilterItemList)

        sortFilterItemList.forEach {
            it.listener = {
                if (it.type != ChipsUnify.TYPE_DISABLE) {
                    onChildSortFilterItemClick(it)
                    it.toggle()
                }
            }
        }
    }

    private fun observeUpdateSortFilter() {
        observe(viewModelShopPenalty.updateSortFilterSelected) {
            when (it) {
                is Success -> {
                    updateItemChildSortFilterPenalty(it.data)
                }
            }
        }
    }

    private fun observePenaltyPage() {
        observe(viewModelShopPenalty.penaltyPageData) {
            when (it) {
                is Success -> {
                    showAllView()
                    showAllPenaltyData(it.data)
                }
                is Fail -> {
                    containerShimmerPenalty?.hide()
                    setupGlobalErrorState(it.throwable)
                }
            }
        }
    }

    private fun observeDetailPenaltyNextPage() {
        observe(viewModelShopPenalty.shopPenaltyDetailData) {
            when (it) {
                is Success -> {
                    onSuccessGetPenaltyListData(it.data)
                }
                is Fail -> {
                    containerShimmerPenalty?.hide()
                    setupGlobalErrorState(it.throwable)
                    penaltyPageAdapter.hidePenaltyData()
                    endlessRecyclerViewScrollListener.resetState()
                }
            }
        }
    }

    private fun onSuccessGetPenaltyListData(data: Triple<List<ItemPenaltyUiModel>, Boolean, Boolean>) {
        globalErrorPenalty?.hide()
        if (!data.third && data.first.isEmpty()) {
            penaltyPageAdapter.hideLoading()
            penaltyPageAdapter.setEmptyStatePenalty()
        } else {
            penaltyPageAdapter.removePenaltyNotFound()
            penaltyPageAdapter.hideLoading()
            penaltyPageAdapter.setPenaltyListDetailData(data.first)
        }
        updateScrollListenerState(data.second)
    }

    private fun setupGlobalErrorState(throwable: Throwable) {
        globalErrorPenalty?.show()
        globalErrorPenalty?.setTypeGlobalError(throwable)
        globalErrorPenalty?.errorAction?.setOnClickListener {
            onSwipeRefresh()
        }
    }

    private fun hideAllViewWithLoading() {
        containerShimmerPenalty?.show()
        globalErrorPenalty?.hide()
        appBarPenaltyPage?.hide()
        containerHeaderTotalPenalty?.hide()
        itemDetailPenaltyContainer?.hide()
        sortFilterDetailPenalty?.hide()
    }

    private fun showAllView() {
        containerShimmerPenalty?.hide()
        appBarPenaltyPage?.show()
        containerHeaderTotalPenalty?.show()
        itemDetailPenaltyContainer?.show()
        sortFilterDetailPenalty?.show()
    }

    private fun showAllPenaltyData(data: PenaltyDataWrapper) {
        val penaltyList = data.itemPenaltyUiModel
        val penaltyFilterData = data.itemDetailPenaltyFilterUiModel
        val cardPenaltyData = data.cardShopPenaltyUiModel
        setupCardPenalty(cardPenaltyData)
        setupDetailPenaltyFilter(penaltyFilterData)
        if(penaltyList.first.isNotEmpty()) {
            penaltyPageAdapter.setPenaltyData(penaltyList.first)
        } else {
            penaltyPageAdapter.setEmptyStatePenalty()
        }
    }

    private fun onDateClick() {
        val bottomSheetDateFilter = PenaltyDateFilterBottomSheet.newInstance(viewModelShopPenalty.getStartDate(), viewModelShopPenalty.getEndDate())
        bottomSheetDateFilter.setCalendarListener(this)
        bottomSheetDateFilter.show(childFragmentManager)
    }

    private fun onParentSortFilterClick() {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val filterPenalty = viewModelShopPenalty.getPenaltyFilterUiModelList()
        cacheManager?.put(PenaltyFilterBottomSheet.KEY_FILTER_TYPE_PENALTY, FilterTypePenaltyUiModelWrapper(
                filterPenalty
        ))

        val bottomSheetFilterPenalty = PenaltyFilterBottomSheet.newInstance(cacheManager?.id.orEmpty())
        bottomSheetFilterPenalty.setPenaltyFilterFinishListener(this)
        bottomSheetFilterPenalty.show(childFragmentManager)
    }

    private fun onMoreInfoHelpPenaltyClicked() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, ShopScoreConstant.SYSTEM_PENALTY_HELP_URL)
    }

    private fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem) {
        viewModelShopPenalty.updateSortFilterSelected(sortFilterItem.title.toString(), sortFilterItem.type)
    }

    companion object {
        fun newInstance(): ShopPenaltyPageFragment {
            return ShopPenaltyPageFragment()
        }
    }
}