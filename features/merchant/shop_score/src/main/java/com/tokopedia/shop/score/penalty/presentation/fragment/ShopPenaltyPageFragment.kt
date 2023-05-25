package com.tokopedia.shop.score.penalty.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.FragmentPenaltyPageBinding
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemDetailPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemHeaderCardPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyErrorListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltySubsectionListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPeriodDateFilterListener
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemSortFilterPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyCalculationBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyDateFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.utils.view.binding.viewBinding

class ShopPenaltyPageFragment: BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>(),
    PenaltyDateFilterBottomSheet.CalenderListener,
    PenaltyFilterBottomSheet.PenaltyFilterFinishListener,
    ItemDetailPenaltyListener,
    ItemHeaderCardPenaltyListener,
    ItemPeriodDateFilterListener,
    ItemPenaltyErrorListener,
    ItemSortFilterPenaltyListener,
    ItemPenaltySubsectionListener {

    private val penaltyPageAdapterFactory by lazy {
        PenaltyPageAdapterFactory(
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

    private val binding: FragmentPenaltyPageBinding? by viewBinding()

    override fun onItemClicked(t: Visitable<*>?) { }

    override fun getScreenName(): String = String.EMPTY

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {

    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        return binding?.rvPenaltyPage
    }

    override fun getAdapterTypeFactory(): PenaltyPageAdapterFactory {
        return penaltyPageAdapterFactory
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory> {
        return penaltyPageAdapter
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
        super.onViewCreated(view, savedInstanceState)
        penaltyPageAdapter.setPenaltyData(getDummyData())
    }

    private fun getDummyData(): List<BasePenaltyPage> {
        return listOf(
            ItemPenaltyTickerUiModel,
            ItemPenaltyInfoNotificationUiModel(5, true),
            ItemPenaltySubsectionUiModel("Sedang Berlangsung", "10-10-2020"),
            ItemPenaltyPointCardUiModel(-5, "10 - 10 -2020")
        )
    }

    override fun impressLearnMorePenaltyPage() {
//        TODO("Not yet implemented")
    }

    override fun onMoreInfoHelpPenaltyClicked() {
//        TODO("Not yet implemented")
    }

    override fun onDateClick() {
//        TODO("Not yet implemented")
    }

    override fun onRetryRefreshError() {
//        TODO("Not yet implemented")
    }

    override fun onParentSortFilterClicked() {
//        TODO("Not yet implemented")
    }

    override fun onChildSortFilterItemClick(sortFilterItem: SortFilterItem) {
//        TODO("Not yet implemented")
    }

    override fun onClickFilterApplied(penaltyFilterUiModelList: List<PenaltyFilterUiModel>) {
//        TODO("Not yet implemented")
    }

    override fun onSaveCalendarClicked(
        startDate: Pair<String, String>,
        endDate: Pair<String, String>
    ) {
//        TODO("Not yet implemented")
    }

    override fun onIconClicked() {
        PenaltyCalculationBottomSheet.createInstance().show(childFragmentManager)
    }


}
