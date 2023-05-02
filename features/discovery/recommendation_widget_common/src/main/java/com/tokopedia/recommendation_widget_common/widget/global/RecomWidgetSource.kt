package com.tokopedia.recommendation_widget_common.widget.global

sealed interface RecomWidgetSource {
    val value: String

    object PDP : RecomWidgetSource {
        override val value: String
            get() = "pdp"
    }
}
