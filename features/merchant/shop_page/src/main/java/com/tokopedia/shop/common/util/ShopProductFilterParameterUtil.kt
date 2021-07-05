package com.tokopedia.shop.common.util

import com.tokopedia.shop.common.constant.*

private val LIST_FILTER_PARAMETER_KEY = setOf(
        PMIN_PARAM_KEY,
        PMAX_PARAM_KEY,
        RATING_PARAM_KEY,
        SORT_PARAM_KEY
)

fun getIndicatorCount(mapData: Map<String, Any>?): Int {
    return mapData?.let {
        var indicatorCount = 0
        for (entryData in it) {
            if (entryData.isValidFilter()) {
                ++indicatorCount
            }
        }
        if (mapData.containsKey(PMIN_PARAM_KEY) && mapData.containsKey(PMAX_PARAM_KEY))
            --indicatorCount
        if (mapData[SORT_PARAM_KEY].toString() == DEFAULT_SORT_ID)
            --indicatorCount
        indicatorCount
    } ?: 0
}

private fun Map.Entry<String, Any>.isValidFilter(): Boolean {
    return LIST_FILTER_PARAMETER_KEY.contains(key) && isValidPrice() && isSortIdValid()
}

private fun Map.Entry<String, Any>.isValidPrice(): Boolean {
    return if (key == PMIN_PARAM_KEY || key == PMAX_PARAM_KEY)
        value.toString() != "0"
    else
        true
}

private fun Map.Entry<String, Any>.isSortIdValid(): Boolean {
    return if (key == SORT_PARAM_KEY)
        value.toString().isNotEmpty()
    else
        true
}