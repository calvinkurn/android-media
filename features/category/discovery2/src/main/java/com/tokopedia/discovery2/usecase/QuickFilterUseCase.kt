package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
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
            if (component.name == ComponentNames.ProductCardSprintSaleCarousel.componentName ||
                component.name == ComponentNames.ProductCardCarousel.componentName ||
                component.name == ComponentNames.MerchantVoucherCarousel.componentName ||
                component.name == ComponentNames.ShopCardView.componentName
            ) {
                shouldRefreshComponent = true
            }
            return true
        }
        return false
    }
}
