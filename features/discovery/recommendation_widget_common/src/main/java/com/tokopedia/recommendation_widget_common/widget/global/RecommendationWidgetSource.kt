package com.tokopedia.recommendation_widget_common.widget.global

sealed interface RecommendationWidgetSource {
    val value: String

    object PDP : RecommendationWidgetSource {
        override val value: String
            get() = "pdp"
    }
}
