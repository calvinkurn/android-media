package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam

sealed interface RecommendationWidgetSource {
    val eventCategory: String
    val appLogAdditionalParam: AppLogAdditionalParam
        get() = AppLogAdditionalParam.None

    class PDP(
        val anchorProductId: String = "",
        val trackingMap: Map<String, Any> = emptyMap(),
    ) : RecommendationWidgetSource {
        val xSourceValue: String
            get() = "pdp"
        override val eventCategory: String
            get() = "product detail page"
        override val appLogAdditionalParam: AppLogAdditionalParam
            get() = AppLogAdditionalParam.PDP()
    }

    class PDPAfterATC(
        val anchorProductId: String,
        val isUserLoggedIn: Boolean,
        val userId: String,
        val warehouseId: String,
        val offerId: String,
        val shopId: String
    ) : RecommendationWidgetSource {
        override val eventCategory: String
            get() = "product detail page"
    }
}
