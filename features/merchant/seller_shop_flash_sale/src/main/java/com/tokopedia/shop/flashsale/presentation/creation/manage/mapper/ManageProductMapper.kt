package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

object ManageProductMapper {

    fun mapToProductData(product: SellerCampaignProductList.Product) =
        DoSellerCampaignProductSubmissionRequest.ProductData(
            productId = product.productId.toLongOrNull().orZero(),
            customStock = product.warehouseList.firstOrNull()?.customStock.orZero().toLong(),
            finalPrice = product.price.toLong(),
            // TODO: move to constant
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

    fun mapToProductDataList(productList: List<SellerCampaignProductList.Product>) =
        productList.map { mapToProductData(it) }

    fun filterInfoNotCompleted(
        productListResult: Result<SellerCampaignProductList>
    ): List<SellerCampaignProductList.Product> {
        val resultData = (productListResult as? Success)?.data
        val productList = resultData?.productList.orEmpty()

        return productList.filterNot {
            it.isInfoComplete
        }
    }
}
