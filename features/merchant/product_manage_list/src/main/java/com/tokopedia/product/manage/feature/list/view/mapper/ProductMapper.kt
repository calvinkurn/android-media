package com.tokopedia.product.manage.feature.list.view.mapper

import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaResponse
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterTabViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.*

object ProductMapper {

    fun mapToViewModels(productList: List<Product>?, multiSelectActive: Boolean): List<ProductViewModel> {
        return productList?.map {
            val price = it.price?.min
            val picture = it.pictures?.firstOrNull()

            ProductViewModel(
                id = it.id,
                title = it.name,
                imageUrl = picture?.urlThumbnail,
                price = price.toString(),
                priceFormatted = price?.getCurrencyFormatted(),
                status = mapProductStatus(it),
                stock = it.stock,
                isVariant = it.isVariant,
                isFeatured = it.featured > 0,
                url = it.url,
                cashBack = it.cashback,
                multiSelectActive = multiSelectActive
            )
        } ?: emptyList()
    }

    private fun mapProductStatus(product: Product): ProductStatus? {
        return when(product.status) {
            PENDING, MODERATED -> VIOLATION
            else -> product.status
        }
    }

    fun mapToTabFilters(response: ProductListMetaResponse, filterCount: Int = 0): List<FilterTabViewModel> {
        val filterTabs = response.productListMetaWrapper.productListMetaData.tabs
        val productFilters = mutableListOf<FilterTabViewModel>(MoreFilter(filterCount))

        val activeProductFilter = filterTabs.firstOrNull { it.id == FilterId.ACTIVE.name }
        val inActiveProductFilter = filterTabs.firstOrNull { it.id == FilterId.INACTIVE.name }
        val violationProductFilter = filterTabs.firstOrNull { it.id == FilterId.VIOLATION.name }

        val activeFilterCount = activeProductFilter?.value.toIntOrZero()
        val inActiveFilterCount = inActiveProductFilter?.value.toIntOrZero()
        val violationFilterCount = violationProductFilter?.value.toIntOrZero()

        if(activeFilterCount > 0) {
            val activeFilter = Active(activeFilterCount)
            productFilters.add(activeFilter)
        }

        if(inActiveFilterCount > 0) {
            val inActiveFilter = InActive(inActiveFilterCount)
            productFilters.add(inActiveFilter)
        }

        if(violationFilterCount > 0) {
            val violationFilter = Violation(violationFilterCount)
            productFilters.add(violationFilter)
        }

        return productFilters
    }
}