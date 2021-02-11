package com.tokopedia.discovery2.usecase.emptystateusecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import javax.inject.Inject

class EmptyStateUseCase @Inject constructor() {
    private val quickFilterOptionList: ArrayList<Option> = ArrayList()

    fun resetChildComponents(components: ComponentsItem): Boolean{
        getComponent(components.parentComponentId, components.pageEndPoint)?.let {
            it.setComponentsItem(null, components.tabName)
            it.selectedFilters = null
            it.selectedSort = null
            it.noOfPagesLoaded = 0
            getComponent(it.parentComponentId, it.pageEndPoint)?.let { item ->
                item.getComponentsItem()?.find { childItem ->
                    childItem.name == ComponentNames.QuickFilter.componentName
                }.apply {
                    this?.searchParameter?.getSearchParameterHashMap()?.clear()
                    val optionLists = addFilterOptions(this?.data?.firstOrNull()?.filter
                            ?: arrayListOf())
                    this?.filterController?.resetAllFilters()
                    if (optionLists.isNotEmpty()) {
                        this?.filters?.clear()
                        this?.selectedFilters?.clear()
                        this?.selectedSort?.clear()
                        for (option in optionLists) {
                            this?.filterController?.setFilter(option, isFilterApplied = false,
                                    isCleanUpExistingFilterWithSameKey = false)
                        }
                    }
                }
            }
            return true
        }
        return false
    }

    private fun addFilterOptions(filters: ArrayList<Filter>): ArrayList<Option> {
        quickFilterOptionList.clear()
        for (item in filters) {
            if (!item.options.isNullOrEmpty()) {
                quickFilterOptionList.addAll(item.options)
            }
        }
        return quickFilterOptionList
    }
}
