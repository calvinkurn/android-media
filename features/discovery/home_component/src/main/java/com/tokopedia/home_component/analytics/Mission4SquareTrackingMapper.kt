package com.tokopedia.home_component.analytics

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationCardModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.CardName
import com.tokopedia.home_component.visitable.Mission4SquareUiModel

object Mission4SquareTrackingMapper {

    fun Mission4SquareUiModel.asProductModel(
        isCache: Boolean = false,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = data.productID,
            parentProductId = data.parentProductID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = data.shopId,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
            position = cardPosition,
            cardName = CardName.MISSION_PRODUCT_CARD.format(data.title),
        )
    }

    fun Mission4SquareUiModel.asCardModel(
        isCache: Boolean = false,
    ): AppLogRecommendationCardModel {
        return AppLogRecommendationCardModel.create(
            cardId = data.id.toString(),
            cardName = CardName.MISSION_PAGE_CARD.format(data.title),
            productId = data.productID,
            parentProductId = data.parentProductID,
            moduleName = data.pageName,
            isAd = data.isTopads,
            isUseCache = isCache,
            recParams = appLog.recParam,
            requestId = appLog.requestId,
            recSessionId = appLog.sessionId,
            shopId = data.shopId,
            entranceForm = EntranceForm.MISSION_HORIZONTAL_GOODS_CARD,
            position = cardPosition,
        )
    }
}
