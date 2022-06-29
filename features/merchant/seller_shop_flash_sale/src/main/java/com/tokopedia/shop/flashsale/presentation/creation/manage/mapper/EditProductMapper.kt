package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel

object EditProductMapper {

    private const val TEASER_POS_DEFAULT_VALUE = 0
    private const val TEASER_ACTIVE_DEFAULT_VALUE = true

    fun map(
        product: SellerCampaignProductList.Product?,
        productMap: SellerCampaignProductList.ProductMapData?,
        warehouse: List<WarehouseUiModel>?
    ) = productMap?.let {
        val productId = product?.productId?.toLongOrNull().orZero()
        val warehouseID = warehouse?.firstOrNull()?.id?.toLongOrNull().orZero()
        listOf(it.mapToRequest(productId, warehouseID))
    } ?: emptyList()

    private fun SellerCampaignProductList.ProductMapData.mapToRequest(
        productId: Long,
        warehouseID: Long
    ) = DoSellerCampaignProductSubmissionRequest.ProductData(
        productId = productId,
        customStock = customStock,
        finalPrice = discountedPrice,
        teaser = DoSellerCampaignProductSubmissionRequest.ProductData.Teaser(
            active = TEASER_ACTIVE_DEFAULT_VALUE, position = TEASER_POS_DEFAULT_VALUE),
        warehouses = listOf(
            DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(warehouseID, customStock)
        ),
        maxOrder = maxOrder
    )
}