package com.tokopedia.recommendation_widget_common.widget.global

sealed interface RecommendationWidgetSource {
    val xSourceValue: String
    val eventCategory: String

    class PDP(
        val anchorProductId: String = "",
        val trackingMap: Map<String, Any> = emptyMap()
    ) : RecommendationWidgetSource {
        override val xSourceValue: String
            get() = "pdp"
        override val eventCategory: String
            get() = "product detail page"
    }

    class PDPAfterATC(
        val anchorProductId: String,
        val isUserLoggedIn: Boolean,
        val userId: String,
        val warehouseId: String,
        val offerId: String,
        val shopId: String
    ) : RecommendationWidgetSource {
        override val xSourceValue: String
            get() = "pdp"
        override val eventCategory: String
            get() = "product detail page"
    }
}
