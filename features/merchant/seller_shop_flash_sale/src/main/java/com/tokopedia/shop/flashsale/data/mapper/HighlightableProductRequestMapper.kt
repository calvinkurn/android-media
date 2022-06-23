package com.tokopedia.shop.flashsale.data.mapper

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
        return if (this.warehouses.isEmpty()) {
            return listOf(
                DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(
                    0,
                    this.customStock
                )
            )
        } else {
            this.warehouses.map {
                DoSellerCampaignProductSubmissionRequest.ProductData.Warehouse(
                    it.warehouseId,
                    it.customStock
                )
            }
        }
    }
}