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
            productName = item.name,
            productId = item.productId.toString(),
            cartId = item.cartId,
            shopId = item.shopId.toString(),
            shopName = item.shopName,
            shopType = item.shopType,
            hasVariant = item.isProductHasParentID(),
            bannerUrl = item.imageUrl,
            price = item.price,
            priceInt = item.priceInt.toString(),
            slashPrice = item.slashedPrice,
            discount = item.discountPercentage,
            categoryChildId = item.departmentId,
            category = item.categoryBreadcrumbs,
        )
    }
}
