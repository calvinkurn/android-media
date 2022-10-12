package com.tokopedia.search.result.product.postprocessing

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_ROWS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.START
import com.tokopedia.filter.common.helper.isPostProcessingFilter
import com.tokopedia.kotlin.extensions.view.toIntOrZero

class PostProcessingFilter {
    private var consecutiveEmptyProductCount = 0

    fun resetCount() {
        consecutiveEmptyProductCount = 0
    }

    fun checkPostProcessingFilter(
        isPostProcessing: Boolean,
        searchParameter: Map<String, Any>,
        totalData: Int,
        callbackLoadData: (Map<String, Any>) -> Unit,
        handleEmptyState: () -> Unit,
    ) {
        if (isPostProcessing || isPostProcessingFilter(searchParameter))
            handlePostProcessingFilter(
                searchParameter,
                totalData,
                callbackLoadData,
                handleEmptyState,
            )
        else
            handleEmptyState()
    }

    private fun handlePostProcessingFilter(
        searchParameter: Map<String, Any>,
        totalData: Int,
        callbackLoad: (Map<String, Any>) -> Unit,
        handleEmptyState: () -> Unit,
    ) {
        consecutiveEmptyProductCount++

        val isNotEmpty = totalData > 0
        val hasNextPage = getNextStartParam(searchParameter) < totalData
        val isBelowThreshold = consecutiveEmptyProductCount < LOAD_EMPTY_PRODUCT_THRESHOLD
        val willLoadNextPage = isNotEmpty && hasNextPage && isBelowThreshold

        if (willLoadNextPage)
            loadNextPage(searchParameter, callbackLoad)
        else
            handleEmptyState()
    }

    private fun loadNextPage(
        searchParameter: Map<String, Any>,
        callbackLoad: (Map<String, Any>) -> Unit,
    ) {
        val nextStart = getNextStartParam(searchParameter)

        val searchParameterNextPage = searchParameter.toMutableMap()
        searchParameterNextPage[START] = nextStart.toString()

        callbackLoad(searchParameterNextPage)
    }

    private fun getNextStartParam(searchParameter: Map<String, Any>): Int {
        val currentStart = searchParameter[START].toString().toIntOrZero()
        val rows = DEFAULT_VALUE_OF_PARAMETER_ROWS.toIntOrZero()

        return currentStart + rows
    }

    companion object {
        const val LOAD_EMPTY_PRODUCT_THRESHOLD = 3
    }
}