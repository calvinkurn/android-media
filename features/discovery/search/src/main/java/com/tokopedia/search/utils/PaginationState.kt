package com.tokopedia.search.utils

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_ROWS

data class PaginationState(
    val startFrom: Long = 0,
    val totalData: Long = 0,
) {
    val hasNextPage = startFrom < totalData

    val isFirstPage = startFrom == 0L

    val isLastPage = !hasNextPage

    fun incrementStart(): PaginationState =
        copy(startFrom = startFrom + DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt())

    fun decrementStart(): PaginationState =
        copy(startFrom = startFrom - DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt())
}
