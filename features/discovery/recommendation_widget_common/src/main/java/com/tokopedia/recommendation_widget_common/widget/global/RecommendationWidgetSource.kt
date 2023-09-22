package com.tokopedia.recommendation_widget_common.widget.global

sealed interface RecommendationWidgetSource {
    val xSourceValue: String
    val trackingValue: String

    object PDP : RecommendationWidgetSource {
        override val xSourceValue: String
            get() = "pdp"
        override val trackingValue: String
            get() = "product detail page"
    }

    class PDPAfterATC(
        val anchorProductId: String,
        val isUserLoggedIn: Boolean,
        val userId: String,
    ): RecommendationWidgetSource {
        override val xSourceValue: String
            get() = "pdp"
        override val trackingValue: String
            get() = "product detail page"
    }
}
