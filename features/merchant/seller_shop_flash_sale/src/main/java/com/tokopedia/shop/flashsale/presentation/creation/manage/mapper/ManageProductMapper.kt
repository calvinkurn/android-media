package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList

object ManageProductMapper {

    fun mapToProductData(product: SellerCampaignProductList.Product) =
        DoSellerCampaignProductSubmissionRequest.ProductData(
            productId = product.productId.toLongOrNull().orZero(),
            customStock = product.warehouseList.firstOrNull()?.customStock.orZero().toLong(),
            finalPrice = product.price.toLong(),
            teaser = DoSellerCampaignProductSubmissionRequest.ProductData.Teaser(
                active = false,
                position = 0
            ),
            warehouses = product.warehouseList.map {
                DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(
                    product.warehouseList.firstOrNull()?.warehouseId.toLongOrZero(),
                    product.warehouseList.firstOrNull()?.customStock?.toLong().orZero()
                )
            },
            maxOrder = product.productMapData.maxOrder
        )

    fun mapToProductDataList(reserveProductList: List<SellerCampaignProductList.Product>) =
        reserveProductList
            .map { mapToProductData(it) }

}
