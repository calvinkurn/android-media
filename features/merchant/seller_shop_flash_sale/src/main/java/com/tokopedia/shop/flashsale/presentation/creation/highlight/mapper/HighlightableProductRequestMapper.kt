package com.tokopedia.shop.flashsale.presentation.creation.highlight.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct
import javax.inject.Inject

class HighlightableProductRequestMapper @Inject constructor() {

    fun map(products: List<HighlightableProduct>): List<DoSellerCampaignProductSubmissionRequest.ProductData> {
        return products.map { product ->
            DoSellerCampaignProductSubmissionRequest.ProductData(
                product.id,
                product.discountedPrice,
                product.customStock,
                product.toTeaser(),
                product.toWarehouses(),
                product.maxOrder
            )
        }
    }

    private fun HighlightableProduct.toTeaser(): DoSellerCampaignProductSubmissionRequest.ProductData.Teaser {
        val position = if (isSelected) this.position else 0
        return DoSellerCampaignProductSubmissionRequest.ProductData.Teaser(this.isSelected, position)
    }

    private fun HighlightableProduct.toWarehouses(): List<DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse> {
        val defaultWarehouses = listOf(
            DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(
                0,
                this.customStock
            )
        )

        val selectedWarehouse = this.warehouses.findSelectedWarehouse()
        val selectedWarehouseId = selectedWarehouse?.warehouseId.orZero()
        val selectedWarehouseStock = this.customStock.orZero()

        val selectedWarehouses =  listOf(
            DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(
                selectedWarehouseId,
                selectedWarehouseStock
            )
        )

        return if (this.warehouses.isEmpty()) {
            defaultWarehouses
        } else {
            selectedWarehouses
        }
    }


    private fun List<HighlightableProduct.Warehouse>.findSelectedWarehouse() : HighlightableProduct.Warehouse? {
        val selectedWarehouse = this.firstOrNull { warehouse -> warehouse.isSelected }
        return selectedWarehouse
    }

}