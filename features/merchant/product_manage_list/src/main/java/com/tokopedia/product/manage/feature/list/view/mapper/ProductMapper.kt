package com.tokopedia.product.manage.feature.list.view.mapper

import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.*
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

    fun mapToTabFilters(productList: List<ProductViewModel>?): List<FilterViewModel> {
        val productFilters = mutableListOf<FilterViewModel>(MoreFilter)

        val activeProductFilter = productList?.filter { it.isActive() }.orEmpty()
        val inActiveProductFilter = productList?.filter { it.isInactive()}.orEmpty()
        val violationProductFilter = productList?.filter { it.isViolation() }.orEmpty()

        if(activeProductFilter.isNotEmpty()) {
            val activeFilterCount = activeProductFilter.count()
            val activeFilter = Active(activeFilterCount)
            productFilters.add(activeFilter)
        }

        if(inActiveProductFilter.isNotEmpty()) {
            val inActiveFilterCount = inActiveProductFilter.count()
            val inActiveFilter = InActive(inActiveFilterCount)
            productFilters.add(inActiveFilter)
        }

        if(violationProductFilter.isNotEmpty()) {
            val violationFilterCount = violationProductFilter.count()
            val violationFilter = Violation(violationFilterCount)
            productFilters.add(violationFilter)
        }

        return productFilters
    }
}