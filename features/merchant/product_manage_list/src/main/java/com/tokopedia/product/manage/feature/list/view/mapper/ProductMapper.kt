package com.tokopedia.product.manage.feature.list.view.mapper

import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.*

object ProductMapper {

    private val filterType = listOf(
        ACTIVE,
        INACTIVE,
        BANNED
    )

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
                status = it.status,
                stock = it.stock,
                isVariant = it.isVariant,
                isFeatured = it.featured > 0,
                url = it.url,
                cashBack = 0 /*waiting confirmation from backend team, new query doesn't return cashback response*/,
                multiSelectActive = multiSelectActive
            )
        } ?: emptyList()
    }

    fun mapToTabFilters(productList: List<ProductViewModel>?): List<FilterViewModel> {
        val productFilters = mutableListOf<FilterViewModel>(MoreFilter)

        val availableFilters = productList
            ?.filter { filterType.contains(it.status) }
            ?.distinctBy { it.status }
            ?.sortedBy { it.status }
            ?.map { product ->
                val productStatus = product.status
                val productCount = productList.filter {
                    it.status == productStatus
                }.count()

                when (productStatus) {
                    ACTIVE -> Active(productCount)
                    INACTIVE -> InActive(productCount)
                    BANNED -> Banned(productCount)
                    else -> MoreFilter
                }
            } ?: emptyList()

        productFilters.addAll(availableFilters)
        return productFilters
    }
}