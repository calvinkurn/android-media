package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.model.ProductShopDecorationTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopHomeCarouselProductWidgetClickCtaTrackerModel

object ShopPageHomeTrackingMapper {
    fun mapToProductShopDecorationTrackerDataModel(
        shopId: String,
        userId: String,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        verticalPosition: Int,
        horizontalPosition: Int,
        widgetName: String,
        widgetOption: Int,
        widgetMasterId: String,
        isFestivity: Boolean
    ): ProductShopDecorationTrackerDataModel {
        return ProductShopDecorationTrackerDataModel(
            shopId = shopId,
            userId = userId,
            productName = productName,
            productId = productId,
            productDisplayedPrice = productDisplayedPrice,
            verticalPosition = verticalPosition,
            horizontalPosition = horizontalPosition,
            widgetName = widgetName,
            widgetOption = widgetOption,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity
        )
    }

    fun mapToShopHomeCarouselProductWidgetClickCtaTrackerModel(
        shopId: String,
        userId: String,
        widgetMasterId: String,
        isFestivity: Boolean
    ): ShopHomeCarouselProductWidgetClickCtaTrackerModel {
        return ShopHomeCarouselProductWidgetClickCtaTrackerModel(
            shopId = shopId,
            userId = userId,
            widgetMasterId = widgetMasterId,
            isFestivity = isFestivity
        )
    }
}
