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
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.viewmodel.ShopPenaltyViewModel
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapter
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapterFactory
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyDateFilterBottomSheet
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPenaltyPageFragment: BaseListFragment<Visitable<*>, PenaltyPageAdapterFactory>(),
        FilterPenaltyListener, PenaltyDateFilterBottomSheet.CalenderListener {

    @Inject
    lateinit var viewModelShop: ShopPenaltyViewModel

    private val penaltyPageAdapterFactory by lazy { PenaltyPageAdapterFactory(this) }
    private val penaltyPageAdapter by lazy { PenaltyPageAdapter(penaltyPageAdapterFactory) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_penalty_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        observePenaltyPage()
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
        viewModelShop.getDataDummyPenalty()
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

    }

    override fun onParentSortFilterClick() {

    }

    override fun onSaveCalendarClicked(startDate: Pair<String, String>, endDate: Pair<String, String>) {

    }

    override fun getAdapterTypeFactory(): PenaltyPageAdapterFactory {
        return penaltyPageAdapterFactory
    }

    override fun onDestroy() {
        removeObservers(viewModelShop.penaltyPageData)
        super.onDestroy()
    }

    private fun observePenaltyPage() {
        observe(viewModelShop.penaltyPageData) {
            hideLoading()
            when (it) {
                is Success -> {
                    renderList(it.data)
                }
            }
        }
        viewModelShop.getDataDummyPenalty()
    }

    companion object {
        fun newInstance(): ShopPenaltyPageFragment {
            return ShopPenaltyPageFragment()
        }
    }
}