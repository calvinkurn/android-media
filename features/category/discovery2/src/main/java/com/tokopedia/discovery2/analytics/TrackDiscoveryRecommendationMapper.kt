package com.tokopedia.discovery2.analytics

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationType
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero

object TrackDiscoveryRecommendationMapper {
    fun DataItem.asProductTrackModel(
        position: Int,
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = productId.orEmpty(),
            position = position,
            tabName = tabName.orEmpty(),
            tabPosition = tabIndex?.firstOrNull().orZero(),
            moduleName = componentPromoName.orEmpty(),
            isAd = isTopads.orFalse(),
            isUseCache = false,
            recSessionId = "", // TODO waiting for BE
            requestId = "", // TODO waiting for BE
            recParams = "", // TODO waiting for BE
            shopId = shopId.orEmpty(),
            entranceForm = getEntranceForm(),
            type = getType(),
            originalPrice = price.toFloatOrZero(),
            salesPrice = discountedPrice.toFloatOrZero(),
        )
    }

    private fun DataItem.getType(): AppLogRecommendationType {
        return AppLogRecommendationType.PRODUCT_CAROUSEL
    }

    private fun DataItem.getEntranceForm(): EntranceForm {
        return EntranceForm.HORIZONTAL_GOODS_CARD
    }
}
