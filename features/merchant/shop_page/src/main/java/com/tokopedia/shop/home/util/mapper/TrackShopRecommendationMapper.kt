package com.tokopedia.shop.home.util.mapper

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

object TrackShopRecommendationMapper {
    fun ShopHomeProductUiModel.asProductTrackModel(
        position: Int,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = id,
            position = position,
            moduleName = recommendationPageName,
            isAd = false,
            isUseCache = false,
            recSessionId = recSessionId,
            recParams = recParam,
            requestId = requestId,
            shopId = shopId,
            entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
            rate = rating.toFloat(),
            originalPrice = originalPrice.toFloatOrZero(),
            salesPrice = displayedPrice.toFloatOrZero(),
            volume = 0, //TODO
        )
    }
}
