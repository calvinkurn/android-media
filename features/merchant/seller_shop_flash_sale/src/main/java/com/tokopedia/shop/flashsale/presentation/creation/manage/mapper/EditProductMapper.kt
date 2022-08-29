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

    private fun EditProductInputModel.mapToRequest(
        productId: Long,
        warehouseId: Long
    ): DoSellerCampaignProductSubmissionRequest.ProductData {
        val finalPrice = price.orZero()
        val finalStock = stock.orDefaultStock(this)
        val finalMaxOrder = maxOrder.orDefaultMaxOrder(finalStock)

        return DoSellerCampaignProductSubmissionRequest.ProductData(
            productId = productId,
            customStock = finalStock,
            finalPrice = finalPrice,
            teaser = DoSellerCampaignProductSubmissionRequest.ProductData.Teaser(
                active = TEASER_ACTIVE_DEFAULT_VALUE, position = TEASER_POS_DEFAULT_VALUE),
            warehouses = listOf(
                DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(warehouseId, finalStock)
            ),
            maxOrder = finalMaxOrder
        )
    }

    private fun List<WarehouseUiModel>?.getSelected() = this
        ?.firstOrNull{ it.isSelected }

    private fun Long?.orDefaultStock(input: EditProductInputModel): Long {
        return this ?: input.originalStock
    }

    private fun Int?.orDefaultMaxOrder(finalStock: Long): Int {
        return this ?: finalStock.toInt()
    }

    fun map(productInput: EditProductInputModel?) = productInput?.let {
        val productId = it.productId.toLongOrZero()
        val warehouseId = it.warehouseId.toLongOrZero()
        listOf(it.mapToRequest(productId, warehouseId))
    } ?: emptyList()

    fun mapInputData(
        product: SellerCampaignProductList.Product,
        warehouseList: List<WarehouseUiModel>
    ): EditProductInputModel {
        val productMapData = product.productMapData
        val warehouseId = warehouseList.getSelected()?.id.orEmpty()
        val warehouseStock = warehouseList.getSelected()?.stock
        val originalStock = warehouseStock ?: productMapData.originalStock.toLong()

        return if (product.isInfoComplete) {
            EditProductInputModel(
                productId = product.productId,
                productMapData = productMapData,
                price = productMapData.discountedPrice,
                stock = productMapData.customStock,
                maxOrder = productMapData.maxOrder,
                warehouseId = warehouseId,
                originalStock = originalStock
            )
        } else {
            EditProductInputModel(
                productId = product.productId,
                warehouseId = warehouseId,
                originalStock = originalStock,
                productMapData = productMapData
            )
        }
    }
}