package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.graphics.drawable.Drawable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
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
        getSubComponent().inject(quickFilterViewModel)
        quickFilterViewModel.fetchDynamicFilterModel()
    }

    override fun setUpObservers(lifecycleOwner: LifecycleOwner?) {
        lifecycleOwner?.let {
            quickFilterViewModel.getDynamicFilterModelLiveData().observe(it, { filterModel ->
                if (filterModel != null) {
                    dynamicFilterModel = filterModel
                }
            })
            if(!quickFilterViewModel.getSyncPageLiveData().hasObservers()) {
                quickFilterViewModel.getSyncPageLiveData().observe(it, { item ->
                    if (item) {
                        (fragment as DiscoveryFragment).reSync()
                        quickFilterViewModel.fetchQuickFilters()
                    }
                })
            }

            quickFilterViewModel.productCountLiveData.observe(it, { count ->
                if (!count.isNullOrEmpty()) {
                    sortFilterBottomSheet.setResultCountText(count)
                } else {
                    sortFilterBottomSheet.setResultCountText(fragment.getString(R.string.discovery_bottom_sheet_filter_finish_button_text))
                }
            })
            quickFilterViewModel.getQuickFilterLiveData().observe(fragment.viewLifecycleOwner, { filters ->
                setQuickFilters(filters)
            })

            quickFilterViewModel.filterCountLiveData.observe(fragment.viewLifecycleOwner,{ filterCount ->
                quickSortFilter.indicatorCounter = filterCount
            })
        }
    }

    override fun removeObservers(lifecycleOwner: LifecycleOwner?) {
        super.removeObservers(lifecycleOwner)
        lifecycleOwner?.let {
            quickFilterViewModel.getDynamicFilterModelLiveData().removeObservers(it)
            quickFilterViewModel.getSyncPageLiveData().removeObservers(it)
            quickFilterViewModel.productCountLiveData.removeObservers(it)
            quickFilterViewModel.getQuickFilterLiveData().removeObservers(it)
            quickFilterViewModel.filterCountLiveData.removeObservers(it)
        }
    }

    private fun setQuickFilters(filters: ArrayList<Option>) {
        if (quickFilterViewModel.components.data?.isEmpty() == true) return
        val sortFilterItems: ArrayList<SortFilterItem> = ArrayList()
        componentName = quickFilterViewModel.getTargetComponent()?.name
        for (option in filters) {
            sortFilterItems.add(createSortFilterItem(option))
        }
        quickSortFilter.let {
            it.sortFilterItems.removeAllViews()
            it.addItem(sortFilterItems)
            it.textView?.text = fragment.getString(R.string.filter)
            it.parentListener = { openBottomSheetFilterRevamp() }
        }
        refreshQuickFilter(filters)
    }

    private fun createSortFilterItem(option: Option): SortFilterItem {
        var icon : Drawable? = null
        when (option.name) {
            "Official Store" -> {
                icon = getIconUnifyDrawable(itemView.context, IconUnify.BADGE_OS_FILLED)
            }
            "4 Keatas" -> {
                icon = getIconUnifyDrawable(itemView.context, IconUnify.STAR_FILLED, MethodChecker.getColor(itemView.context, R.color.discovery2_5_star))
            }
        }
        val item = SortFilterItem(option.name, icon)
        item.listener = {
            quickFilterViewModel.onQuickFilterSelected(option)
            (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickQuickFilter(option.name, componentName, option.value, quickFilterViewModel.isQuickFilterSelected(option))
        }
        if (quickFilterViewModel.isQuickFilterSelected(option)) {
            item.type = ChipsUnify.TYPE_SELECTED
            item.typeUpdated = false
        }
        return item
    }

    private fun refreshQuickFilter(filters: ArrayList<Option>) {
        val options: List<Option> = filters
        setSortFilterItemState(options)
    }

    private fun setSortFilterItemState(options: List<Option>) {
        quickSortFilter.chipItems?.let {
            if (options.size != it.size) return
        }
        for (i in options.indices) {
            if (quickFilterViewModel.isQuickFilterSelected(options[i])) {
                setQuickFilterChipsSelected(i)
            } else
                setQuickFilterChipsNormal(i)
        }
        quickFilterViewModel.getSelectedFilterCount()
    }

    private fun setQuickFilterChipsSelected(position: Int) {
        quickSortFilter.chipItems?.get(position)?.apply {
            this.type = ChipsUnify.TYPE_SELECTED
            this.refChipUnify.chipType = ChipsUnify.TYPE_SELECTED
            this.typeUpdated = false
        }
    }

    private fun setQuickFilterChipsNormal(position: Int) {
        quickSortFilter.chipItems?.get(position)?.apply {
            this.type = ChipsUnify.TYPE_NORMAL
            this.refChipUnify.chipType = ChipsUnify.TYPE_NORMAL
            this.typeUpdated = false
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
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickApplyFilter(applySortFilterModel.mapParameter)
        quickFilterViewModel.getSelectedFilterCount()
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        if(quickFilterViewModel.components.showFilterCount)
            quickFilterViewModel.filterProductsCount(mapParameter)
        else
            sortFilterBottomSheet.setResultCountText(fragment.getString(R.string.discovery_bottom_sheet_filter_finish_button_text))
    }

}
