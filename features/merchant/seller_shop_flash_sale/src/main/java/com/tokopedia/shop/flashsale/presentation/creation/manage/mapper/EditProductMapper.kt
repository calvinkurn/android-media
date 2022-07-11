package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.EditProductInputModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel

object EditProductMapper {

    private const val TEASER_POS_DEFAULT_VALUE = 0
    private const val TEASER_ACTIVE_DEFAULT_VALUE = true

    fun map(
        productInput: EditProductInputModel?,
        warehouses: List<WarehouseUiModel>?
    ) = productInput?.let {
        val productId = it.productId.toLongOrZero()
        val warehouseID = warehouses.getSelected()
        listOf(it.mapToRequest(productId, warehouseID))
    } ?: emptyList()

    private fun EditProductInputModel.mapToRequest(
        productId: Long,
        warehouseID: Long
    ): DoSellerCampaignProductSubmissionRequest.ProductData {
        val finalPrice = price.orZero()
        val finalStock = stock.orDefaultStock(this)
        val finalMaxOrder = maxOrder.orDefaultMaxPrice(this)

        return DoSellerCampaignProductSubmissionRequest.ProductData(
            productId = productId,
            customStock = finalStock,
            finalPrice = finalPrice,
            teaser = DoSellerCampaignProductSubmissionRequest.ProductData.Teaser(
                active = TEASER_ACTIVE_DEFAULT_VALUE, position = TEASER_POS_DEFAULT_VALUE),
            warehouses = listOf(
                DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(warehouseID, finalStock)
            ),
            maxOrder = finalMaxOrder
        )
    }

    private fun List<WarehouseUiModel>?.getSelected() = this
        ?.firstOrNull{ it.isSelected }
        ?.id?.toLongOrNull()
        .orZero()

    private fun Long?.orDefaultStock(input: EditProductInputModel): Long {
        return this ?: input.productMapData.originalStock.toLong()
    }

    private fun Int?.orDefaultMaxPrice(input: EditProductInputModel): Int {
        return this ?: input.productMapData.originalStock
    }

    fun mapInputData(
        product: SellerCampaignProductList.Product,
        warehouseList: List<WarehouseUiModel>
    ): EditProductInputModel {
        val productMapData = product.productMapData
        return if (product.isInfoComplete) {
            EditProductInputModel(
                productId = product.productId,
                productMapData = productMapData,
                price = productMapData.discountedPrice,
                stock = productMapData.customStock,
                maxOrder = productMapData.maxOrder,
                warehouseId = warehouseList.getSelected().toString()
            )
        } else {
            EditProductInputModel(
                productId = product.productId,
                productMapData = productMapData
            )
        }
    }
}