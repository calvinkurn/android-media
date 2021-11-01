package com.tokopedia.review.feature.reading.presentation.uimodel

sealed class FilterType(open val param: String, open var value: String) {
    companion object {
        const val PARAM_TOPIC = "topic"
        const val PARAM_WITH_IMAGE = "image"
        const val PARAM_RATING = "rating"
    }

    data class FilterWithImage(override var value: String = true.toString()) : FilterType(PARAM_WITH_IMAGE, value)
    data class FilterTopic(override var value: String = "") : FilterType(PARAM_TOPIC, value)
    data class FilterRating(override var value: String = "") : FilterType(PARAM_RATING, value)
}