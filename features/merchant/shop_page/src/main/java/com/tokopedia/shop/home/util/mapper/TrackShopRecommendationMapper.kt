package com.tokopedia.shop.home.util.mapper

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationType
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

object TrackShopRecommendationMapper {
    fun ShopHomeProductUiModel.asProductTrackModel(
        position: Int,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = id,
            position = position,
            moduleName = widgetName,
            isAd = false,
            isUseCache = false,
            recSessionId = "", // TODO pageName from BE
            recParams = "", // TODO pageName from BE
            requestId = "", // TODO pageName from BE
            shopId = shopId,
            entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
            rate = rating.toFloat(),
            type = AppLogRecommendationType.PRODUCT_CAROUSEL,
            originalPrice = originalPrice.toFloatOrZero(),
            salesPrice = displayedPrice.toFloatOrZero(),
            volume = stock
        )
    }
}
