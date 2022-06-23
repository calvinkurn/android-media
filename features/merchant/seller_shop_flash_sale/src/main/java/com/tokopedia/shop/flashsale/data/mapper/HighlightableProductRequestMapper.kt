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
                emptyList(),
                product.maxOrder
            )
        }
    }
}