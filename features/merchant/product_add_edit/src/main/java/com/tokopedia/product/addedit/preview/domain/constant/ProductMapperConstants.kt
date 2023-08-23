package com.tokopedia.product.addedit.preview.domain.constant

object ProductMapperConstants {
    const val IS_ACTIVE = 1
    const val IS_INACTIVE = 0
    const val IS_ACTIVE_STRING = "ACTIVE"
    const val IS_INACTIVE_STRING = "INACTIVE"
    const val UNIT_DAY = 0
    const val UNIT_WEEK = 1
    const val UNIT_MONTH = 2
    const val UNIT_GRAM = 0
    const val UNIT_KILOGRAM = 1
    const val UNIT_GRAM_TO_KILOGRAM_MULTIPLIER = 1000
    const val UNIT_DAY_STRING = "DAY"
    const val UNIT_WEEK_STRING = "WEEK"
    const val UNIT_MONTH_STRING = "MONTH"
    const val UNIT_GRAM_STRING = "GR"
    const val UNIT_KILOGRAM_STRING = "KG"
    const val YOUTUBE_URL_DELIMITER = "/watch?v="
    const val YOUTUBE_URL_DELIMITER_SHORT = "/"
    const val YOUTUBE_URL = "youtube.com"
    const val YOUTUBE_URL_SHORTEN = "youtu.be"
    const val YOUTUBE_SOURCE = "youtube"
    const val PRICE_CURRENCY = "IDR"

    fun getActiveStatus(status: Int) = when (status) {
            IS_INACTIVE -> IS_INACTIVE_STRING
            IS_ACTIVE -> IS_ACTIVE_STRING
            else -> IS_ACTIVE_STRING
        }

    fun getActiveStatus(type: String) = when (type) {
            IS_INACTIVE_STRING -> IS_INACTIVE
            IS_ACTIVE_STRING -> IS_ACTIVE
            else -> IS_INACTIVE
        }

    fun getTimeUnitString(timeUnit: Int) = when (timeUnit) {
        UNIT_DAY -> UNIT_DAY_STRING
        UNIT_WEEK -> UNIT_WEEK_STRING
        else -> UNIT_MONTH_STRING
    }

    fun getWeightUnitString(weightUnit: Int) = when (weightUnit) {
        UNIT_GRAM -> UNIT_GRAM_STRING
        UNIT_KILOGRAM -> UNIT_KILOGRAM_STRING
        else -> UNIT_GRAM_STRING
    }

    fun getWeightUnitConstant(weightUnit: String) = when (weightUnit) {
        UNIT_GRAM_STRING -> UNIT_GRAM
        UNIT_KILOGRAM_STRING -> UNIT_KILOGRAM
        else -> UNIT_GRAM
    }

    fun getYoutubeDelimiter(source: String) =
        if (source.contains(YOUTUBE_URL)) YOUTUBE_URL_DELIMITER
        else YOUTUBE_URL_DELIMITER_SHORT

    fun getYoutubeHost(source: String) =
        if (source == YOUTUBE_SOURCE) YOUTUBE_URL_SHORTEN
        else source
}
