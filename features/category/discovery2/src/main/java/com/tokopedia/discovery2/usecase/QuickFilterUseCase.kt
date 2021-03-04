package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.ComponentsItem
import java.util.*
import javax.inject.Inject

class QuickFilterUseCase @Inject constructor() {

    fun onFilterApplied(component: ComponentsItem?, selectedFilter: HashMap<String, String>?, selectedSort: HashMap<String, String>?): Boolean {
        component?.apply {
            setComponentsItem(null, component.tabName)
            noOfPagesLoaded = 0
            this.selectedSort = selectedSort
            this.selectedFilters = selectedFilter
            return true
        }
        return false
    }
}