package com.tokopedia.home_recom.model.datamodel

/**
 * Created by yfsx on 02/09/21.
 */
data class RecomErrorResponse(
        val pageNumber: Int = 0,
        val errorThrowable: Throwable = Throwable(),
        val isEmptyFirstPage: Boolean = false,
        val isErrorFirstPage: Boolean = false,
        val isEmptyNextPage: Boolean = false,
        val isForceRefreshAndError: Boolean = false
)