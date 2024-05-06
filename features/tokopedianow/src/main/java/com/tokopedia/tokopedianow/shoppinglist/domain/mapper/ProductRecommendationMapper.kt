package com.tokopedia.tokopedianow.shoppinglist.domain.mapper

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION

object ProductRecommendationMapper {
    fun mapRecommendedProducts(
        recommendationWidget: RecommendationWidget
    ): List<ShoppingListHorizontalProductCardItemUiModel> = recommendationWidget.recommendationItemList.map {
        ShoppingListHorizontalProductCardItemUiModel(
            id = it.productId.toString(),
            image = it.imageUrl,
            price = it.price,
            priceInt = it.priceInt.toDouble(),
            name = it.name,
            weight = it.labelGroupList.firstOrNull { label -> label.isWeightPosition() }?.title.orEmpty(),
            percentage = it.discountPercentage,
            slashPrice = it.slashedPrice,
            appLink = it.appUrl,
            minOrder = it.minOrder,
            shopId = it.shopId.toString(),
            warehouseId = it.warehouseId.toString(),
            productLayoutType = PRODUCT_RECOMMENDATION,
            state = TokoNowLayoutState.SHOW
        )
    }
}
