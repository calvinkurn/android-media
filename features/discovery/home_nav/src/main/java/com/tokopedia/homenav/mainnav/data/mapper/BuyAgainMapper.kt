package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.homenav.mainnav.view.widget.BuyAgainModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

object BuyAgainMapper {

    private const val MAX_PRODUCT_LIST_LIMIT = 5

    fun map(items: List<RecommendationItem>): List<BuyAgainModel> {
        return items
            .map { map(it) }
            .take(MAX_PRODUCT_LIST_LIMIT)
    }

    fun map(item: RecommendationItem): BuyAgainModel {
        return BuyAgainModel(
            productId = item.productId.toString(),
            shopId = item.shopId.toString(),
            hasVariant = item.isUseQuantityEditor(),
            bannerUrl = item.imageUrl,
            price = item.price,
            slashPrice = item.slashedPrice,
            discount = item.discountPercentage
        )
    }
}
