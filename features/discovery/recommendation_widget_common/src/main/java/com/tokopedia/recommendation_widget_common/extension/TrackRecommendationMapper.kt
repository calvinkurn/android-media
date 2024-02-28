package com.tokopedia.recommendation_widget_common.extension

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

fun RecommendationItem.asProductTrackModel(
    isCache: Boolean = false,
    entranceForm: EntranceForm,
    enterMethod: String = "",
    tabName: String = "",
    tabPosition: Int = 0,
): AppLogRecommendationProductModel {
    return AppLogRecommendationProductModel.create(
        productId = productId.toString(),
        position = position,
        moduleName = pageName,
        isAd = isTopAds,
        isUseCache = isCache,
        recParams = appLog.recParam,
        requestId = appLog.requestId,
        recSessionId = appLog.sessionId,
        shopId = shopId.toString(),
        entranceForm = entranceForm,
        tabName = tabName,
        tabPosition = tabPosition,
        rate = ratingAverage.toFloatOrZero(),
        enterMethod = enterMethod,
        volume = null, //TODO
        originalPrice = (if(slashedPriceInt > 0) slashedPriceInt else priceInt).toFloat(),
        salesPrice = priceInt.toFloat(),
    )
}
