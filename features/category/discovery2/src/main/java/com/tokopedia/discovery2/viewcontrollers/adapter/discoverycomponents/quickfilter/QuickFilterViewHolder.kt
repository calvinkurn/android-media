package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.di.getSubComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

class QuickFilterViewHolder(itemView: View, private val fragment: Fragment) :
    AbstractViewHolder(itemView, fragment.viewLifecycleOwner), SortFilterBottomSheet.Callback,
    FilterGeneralDetailBottomSheet.Callback {
    private val delay: Long = 1000L
    private lateinit var quickFilterViewModel: QuickFilterViewModel
    private val quickSortFilter: SortFilter = itemView.findViewById(R.id.quick_sort_filter)
    private var sortFilterBottomSheet: SortFilterBottomSheet = SortFilterBottomSheet()
    private val filterGeneralBottomSheet: FilterGeneralDetailBottomSheet by lazy {
        FilterGeneralDetailBottomSheet()
    }
    private var dynamicFilterModel: DynamicFilterModel? = null
    private var componentName: String? = null
    private var runnable = Runnable {
        (fragment as DiscoveryFragment).mSwipeRefreshLayout?.isEnabled = true
    }

    init {
        quickSortFilter.dismissListener = {
            quickFilterViewModel.clearQuickFilters()
        }
    }

    private fun setScrollListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            quickSortFilter.sortFilterHorizontalScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                try {
                    if ((fragment as DiscoveryFragment).mSwipeRefreshLayout?.isEnabled == true)
                        fragment.mSwipeRefreshLayout?.isEnabled = false
                    quickSortFilter.removeCallbacks(runnable)
                    runnable = Runnable {
                        fragment.mSwipeRefreshLayout?.isEnabled = true
                    }
                    quickSortFilter.postDelayed(runnable, delay)
                } catch (e: Exception) {
                    Utils.logException(e)
                }
            }
        }
    }

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
                quickFilterViewModel.getSyncPageLiveData().observe(it) { item ->
                    if (item) {
                        (fragment as DiscoveryFragment).reSync()
                        quickFilterViewModel.fetchQuickFilters()
                    }
                }
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

    private fun setQuickFilters(filters: ArrayList<Filter>) {
        if (quickFilterViewModel.components.data?.isEmpty() == true) return
        val sortFilterItems: ArrayList<SortFilterItem> = ArrayList()
        componentName = quickFilterViewModel.getTargetComponent()?.name
        val prop = quickFilterViewModel.components.properties
        if (prop?.fullFilterType == Constant.FullFilterType.CATEGORY) {
                sortFilterItems.add(createSemuaKategoriFilterItem())
        }
        for (filter in filters) {
            if(filter.options.size == 1)
                sortFilterItems.add(createSortFilterItem(filter.options.first()))
            else{
                sortFilterItems.add(createDropDownSortFilterItem(filter))
            }
        }
        quickSortFilter.let {
            it.filterType = prop?.let { properties ->
                if ((properties.fullFilterType != Constant.FullFilterType.CATEGORY) && (properties.filter || properties.sort))
                    SortFilter.TYPE_ADVANCED else SortFilter.TYPE_QUICK
            } ?: SortFilter.TYPE_ADVANCED
            if (prop?.chipSize == Constant.ChipSize.LARGE) {
                it.filterSize = SortFilter.SIZE_LARGE
                setScrollListener()
                it.sortFilterHorizontalScrollView.scrollX = 0
            } else
                it.filterSize = SortFilter.SIZE_DEFAULT
            it.prefixText =
                when {
                    prop?.fullFilterType == Constant.FullFilterType.CATEGORY -> fragment.getString(R.string.semua_kategori)
                    prop?.chipSize == Constant.ChipSize.LARGE -> ""
                    else -> fragment.getString(com.tokopedia.filter.R.string.filter)
                }
            it.sortFilterItems.removeAllViews()
            it.addItem(sortFilterItems)
            it.parentListener = {
                if (prop?.fullFilterType == Constant.FullFilterType.CATEGORY) {
                    dynamicFilterModel?.data?.filter?.firstOrNull()?.let { filter ->
                        openGeneralFilterForCategory(filter)
                    }
                } else {
                    openBottomSheetFilterRevamp()
                }
            }
        }
        refreshQuickFilter(filters)
    }

    private fun createSortFilterItem(option: Option): SortFilterItem {
        var icon : Drawable? = null
        val item =
            if (quickFilterViewModel.components.isFromCategory) {
                when (option.name) {
                    "Official Store" -> {
                        icon = getIconUnifyDrawable(itemView.context, IconUnify.BADGE_OS_FILLED)
                    }
                    "4 Keatas" -> {
                        icon = getIconUnifyDrawable(
                            itemView.context,
                            IconUnify.STAR_FILLED,
                            MethodChecker.getColor(itemView.context, R.color.discovery2_dms_5_star)
                        )
                    }
                }
                SortFilterItem(option.name, icon)
            } else {
                SortFilterItem(option.name, iconUrl = option.iconUrl)
            }
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

    private fun createDropDownSortFilterItem(filter: Filter):SortFilterItem {
        val item = SortFilterItem(filter.title)
        item.listener = {
            onClickDropDownItem(filter)
        }
        item.chevronListener = {
            onClickDropDownItem(filter)
        }
        for(option in filter.options) {
            if (quickFilterViewModel.isQuickFilterSelected(option)) {
                item.type = ChipsUnify.TYPE_SELECTED
                item.typeUpdated = false
            }
        }
        return item
    }

    private fun createSemuaKategoriFilterItem(): SortFilterItem {
        val item = SortFilterItem(fragment.getString(R.string.semua_kategori))
        item.listener = {
            dynamicFilterModel?.data?.filter?.firstOrNull()?.let { filter ->
                openGeneralFilterForCategory(filter)
            }
        }
        return item
    }

    private fun onClickDropDownItem(filter: Filter) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickQuickFilter(
            filter.title,
            componentName,
            "",
            false
        )
        filterGeneralBottomSheet.show(
            fragment.childFragmentManager,
            filter,
            this,
        )
    }

    private fun refreshQuickFilter(filters: ArrayList<Filter>) {
        val options: List<Filter> = filters
        setSortFilterItemState(options)
    }

    private fun setSortFilterItemState(filters: List<Filter>) {
        quickSortFilter.chipItems?.let {
            if (filters.size != it.size) return
        }
        filters.forEachIndexed { filterIndex, filter ->
            var isOptionSelected  = false
            for (option in filter.options) {
                if(quickFilterViewModel.isQuickFilterSelected(option)){
                    isOptionSelected = true
                    option.inputState = true.toString()
                }else{
                    option.inputState = ""
                }
            }
            if (isOptionSelected) {
                setQuickFilterChipsSelected(filterIndex)
            } else
                setQuickFilterChipsNormal(filterIndex)
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

    private fun openGeneralFilterForCategory(filter: Filter) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()
            ?.trackClickDetailedFilter(componentName)
        for (option in filter.options) {
            if (quickFilterViewModel.isQuickFilterSelected(option)) {
                option.inputState = true.toString()
            } else {
                option.inputState = ""
            }
        }
        filterGeneralBottomSheet.show(
            fragment.childFragmentManager,
            filter,
            this,
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

    override fun onApplyButtonClicked(optionList: List<Option>?) {
        if (!optionList.isNullOrEmpty())
            quickFilterViewModel.onDropDownFilterSelected(optionList)
    }

}
