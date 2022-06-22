package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignHighlightProductsResponse
import com.tokopedia.shop.flashsale.domain.entity.HighlightedProduct
import javax.inject.Inject

class HighlightedProductMapper @Inject constructor() {

    fun map(data: GetSellerCampaignHighlightProductsResponse): List<HighlightedProduct> {
        return data.getSellerCampaignHighlightProducts.highlightProductDatas.map { product ->
            HighlightedProduct(
                product.campaignStatus,
                product.discountedPercentage,
                product.discountedPrice,
                product.endDate,
                product.id,
                product.imageURL,
                product.name,
                product.originalPrice,
                product.price,
                product.startDate,
                product.url
            )
        }
    }
}