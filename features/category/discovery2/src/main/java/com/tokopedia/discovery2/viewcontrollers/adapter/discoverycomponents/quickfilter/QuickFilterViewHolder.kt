package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

class QuickFilterViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView, fragment.viewLifecycleOwner), SortFilterBottomSheet.Callback {
    private lateinit var quickFilterViewModel: QuickFilterViewModel
    private val quickSortFilter: SortFilter = itemView.findViewById(R.id.quick_sort_filter)
    private var sortFilterBottomSheet: SortFilterBottomSheet = SortFilterBottomSheet()
    private var dynamicFilterModel: DynamicFilterModel? = null
    private var componentName: String? = null

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        quickFilterViewModel = discoveryBaseViewModel as QuickFilterViewModel
        setQuickFilters()
        refreshQuickFilter()
        quickFilterViewModel.fetchDynamicFilterModel()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            quickFilterViewModel.getDynamicFilterModelLiveData().observe(it, Observer { filterModel ->
                if (filterModel != null) {
                    dynamicFilterModel = filterModel
                }
            })
            quickFilterViewModel.getSyncPageLiveData().observe(it, Observer { item ->
                if (item) {
                    (fragment as DiscoveryFragment).reSync()
                    refreshQuickFilter()
                }
            })
        }
    }


    private fun setQuickFilters() {
        quickSortFilter.let {
            it.textView.text = fragment.getString(R.string.filter)
            it.parentListener = { openBottomSheetFilterRevamp() }
            it.sortFilterItems.removeAllViews()
        }
        val sortFilterItems: ArrayList<SortFilterItem> = ArrayList()
        if (quickFilterViewModel.components.data?.isEmpty() == true) return
        componentName = quickFilterViewModel.getTargetComponent()?.name

        for (option in quickFilterViewModel.getQuickFiltersList()) {
            sortFilterItems.add(createSortFilterItem(option))
        }
        quickSortFilter.addItem(sortFilterItems)
    }

    private fun createSortFilterItem(option: Option): SortFilterItem {
        val item = SortFilterItem(option.name)
        item.listener = {
            quickFilterViewModel.onQuickFilterSelected(option)
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickQuickFilter(option.name, componentName)
        }
        if (quickFilterViewModel.isQuickFilterSelected(option)) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        }
        return item
    }

    private fun refreshQuickFilter() {
        val options: List<Option> = quickFilterViewModel.getQuickFiltersList()
        setSortFilterItemState(options)
    }

    private fun setSortFilterItemState(options: List<Option>) {
        if (quickFilterViewModel.getQuickFiltersList().size != quickSortFilter.chipItems.size) return
        var selectedCount = 0
        for (i in options.indices) {
            if (quickFilterViewModel.isQuickFilterSelected(options[i])) {
                setQuickFilterChipsSelected(i)
                selectedCount ++
            } else
                setQuickFilterChipsNormal(i)
        }
        quickSortFilter.indicatorCounter = selectedCount
    }

    private fun setQuickFilterChipsSelected(position: Int) {
        quickSortFilter.chipItems[position].let {
            it.type = ChipsUnify.TYPE_SELECTED
            it.refChipUnify.chipType = ChipsUnify.TYPE_SELECTED
            it.typeUpdated = false
        }
    }

    private fun setQuickFilterChipsNormal(position: Int) {
        quickSortFilter.chipItems[position].let {
            it.type = ChipsUnify.TYPE_NORMAL
            it.refChipUnify.chipType = ChipsUnify.TYPE_NORMAL
            it.typeUpdated = false
        }
    }

    private fun openBottomSheetFilterRevamp() {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickDetailedFilter(componentName)
        sortFilterBottomSheet.show(
                fragment.childFragmentManager,
                quickFilterViewModel.getSearchParameterHashMap(),
                dynamicFilterModel,
                this
        )
    }

    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        quickFilterViewModel.onApplySortFilter(applySortFilterModel)
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickApplyFilter(applySortFilterModel.selectedFilterMapParameter)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        sortFilterBottomSheet.setResultCountText(fragment.getString(R.string.bottom_sheet_filter_finish_button_text))
    }

}

