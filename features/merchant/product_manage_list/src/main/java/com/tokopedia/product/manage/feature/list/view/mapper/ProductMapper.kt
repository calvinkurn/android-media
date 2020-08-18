package com.tokopedia.product.manage.feature.list.view.mapper

import androidx.lifecycle.LiveData
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.filter.data.model.Tab
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.ShowFilterTab
import com.tokopedia.product.manage.feature.list.view.model.GetFilterTabResult.UpdateFilterTab
import com.tokopedia.product.manage.feature.list.view.model.PriceUiModel
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.*
import com.tokopedia.usecase.coroutines.Result

object ProductMapper {

    fun mapToViewModels(productList: List<Product>?, multiSelectActive: Boolean): List<ProductViewModel> {
        return productList?.map {
            val minPrice = it.price?.min
            val maxPrice = it.price?.max
            val picture = it.pictures?.firstOrNull()

            ProductViewModel(
                id = it.id,
                title = it.name,
                imageUrl = picture?.urlThumbnail,
                minPrice = PriceUiModel(
                        price = minPrice.toString(),
                        priceFormatted = minPrice?.getCurrencyFormatted()
                ),
                maxPrice = PriceUiModel(
                        price = maxPrice.toString(),
                        priceFormatted = maxPrice?.getCurrencyFormatted()
                ),
                status = mapProductStatus(it),
                stock = it.stock,
                isVariant = it.isVariant,
                isFeatured = it.featured > 0,
                url = it.url,
                cashBack = it.cashback,
                multiSelectActive = multiSelectActive,
                isChecked = false,
                hasStockReserved = it.hasStockReserved
            )
        } ?: emptyList()
    }

    private fun mapProductStatus(product: Product): ProductStatus? {
        return when(product.status) {
            PENDING, MODERATED -> VIOLATION
            else -> product.status
        }
    }

    fun LiveData<Result<GetFilterTabResult>>?.mapToFilterTabResult(filterTabs: List<Tab>): GetFilterTabResult {
        var totalProductCount = 0
        val productFilters = mutableListOf<FilterTabViewModel>()

        val activeProductFilter = filterTabs.firstOrNull { it.id == FilterId.ACTIVE.name }
        val inActiveProductFilter = filterTabs.firstOrNull { it.id == FilterId.INACTIVE.name }
        val violationProductFilter = filterTabs.firstOrNull { it.id == FilterId.VIOLATION.name }

        val activeFilterCount = activeProductFilter?.value.toIntOrZero()
        val inActiveFilterCount = inActiveProductFilter?.value.toIntOrZero()
        val violationFilterCount = violationProductFilter?.value.toIntOrZero()

        if(activeFilterCount > 0) {
            val activeFilter = Active(activeFilterCount)
            productFilters.add(activeFilter)
            totalProductCount += activeFilterCount
        }

        if(inActiveFilterCount > 0) {
            val inActiveFilter = InActive(inActiveFilterCount)
            productFilters.add(inActiveFilter)
            totalProductCount += inActiveFilterCount
        }

        if(violationFilterCount > 0) {
            val violationFilter = Violation(violationFilterCount)
            productFilters.add(violationFilter)
            totalProductCount += violationFilterCount
        }

        return if(this?.value == null) {
            ShowFilterTab(productFilters, totalProductCount)
        } else {
            UpdateFilterTab(productFilters, totalProductCount)
        }
    }
}