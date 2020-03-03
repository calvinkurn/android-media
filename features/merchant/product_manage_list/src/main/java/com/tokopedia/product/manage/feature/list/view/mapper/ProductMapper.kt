package com.tokopedia.product.manage.feature.list.view.mapper

import com.tokopedia.product.manage.common.extension.getCurrencyFormatted
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel
import com.tokopedia.product.manage.feature.list.view.model.FilterViewModel.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductList
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.*

object ProductMapper {
    fun mapToViewModels(productListResponse: ProductList?): List<ProductViewModel> {
        val productList = productListResponse?.data

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
                isFeatured = it.featured == 1,
                url = it.url,
                cashBack = 100
            )
        } ?: emptyList()
    }

    fun mapToProductFilters(productListResponse: ProductList?): List<FilterViewModel> {
        val productList = productListResponse?.data
        val productFilters = mutableListOf<FilterViewModel>(Default)

        val availableFilters = productList
            ?.distinctBy { it.status }
            ?.map { product ->
                val productStatus = product.status
                val productCount = productList.filter {
                    it.status == productStatus
                }.count()

                when (productStatus) {
                    ACTIVE -> Active(productCount)
                    INACTIVE -> InActive(productCount)
                    BANNED -> Banned(productCount)
                    else -> Default
                }
            } ?: emptyList()

        productFilters.addAll(availableFilters)
        return productFilters
    }
}