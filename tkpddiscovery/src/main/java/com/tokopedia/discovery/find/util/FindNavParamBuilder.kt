package com.tokopedia.discovery.find.util

import com.tokopedia.discovery.categoryrevamp.data.filter.DAFilterQueryType
import com.tokopedia.discovery.categoryrevamp.utils.ParamMapToUrl
import com.tokopedia.usecase.RequestParams

class FindNavParamBuilder {

    companion object {
        private const val KEY_START = "start"
        private const val KEY_DEVICE = "device"
        private const val KEY_DEVICE_VALUE = "android"
        private const val KEY_SOURCE = "source"
        private const val KEY_SOURCE_VALUE = "search_product"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_ROWS = "rows"
        private const val KEY_SAFE_SEARCH = "safe_search"
        private const val KEY_QUERY = "q"
        private const val KEY_PARAMS = "params"
        private const val KEY_FILTER = "filter"
        private const val KEY_KEYWORD = "keyword"
    }

    enum class SourceType(val value: String) {
        DIRECTORY("directory"),
        QUICK_FILTER("quick_filter")
    }

    private fun prepareProductListParams(productKey: String, start: Int, rows: Int, uniqueId: String, sortParam: String, filterParam: String): String {
        var param: String
        param = "$KEY_SAFE_SEARCH=false&$KEY_SOURCE=$KEY_SOURCE_VALUE"
        if (filterParam.isNotEmpty()) {
            param = "$param&$filterParam"
        }
        if (sortParam.isNotEmpty()) {
            param = "$param&$sortParam"
        }
        param = "$param&$KEY_QUERY=$productKey&$KEY_DEVICE=$KEY_DEVICE_VALUE&$KEY_START=$start&$KEY_ROWS=$rows&$KEY_UNIQUE_ID=$uniqueId"

        return param
    }

    fun generateProductFilterParams(productId: String, start: Int, rows: Int, uniqueId: String,
                                    selectedSort: HashMap<String, String>, selectedFilter: HashMap<String, String>): RequestParams {
        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.putString(KEY_PARAMS, prepareProductListParams(productId, start, rows, uniqueId,
                createParametersForQuery(selectedSort), createParametersForQuery(selectedFilter)))
        return searchProductRequestParams
    }

    private fun createParametersForQuery(parameters: Map<String, Any>): String {
        return ParamMapToUrl.generateUrlParamString(parameters)
    }


    fun generateQuickFilterParams(productId: String): RequestParams {
        val quickFilterParam = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = productId
        quickFilterParam.putObject(KEY_FILTER, daFilterQueryType)
        quickFilterParam.putString(KEY_SOURCE, SourceType.QUICK_FILTER.value)
        return quickFilterParam
    }

    fun generateDynamicFilterParams(productId: String): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = productId
        paramMap.putObject(KEY_FILTER, daFilterQueryType)
        paramMap.putString(KEY_QUERY, "")
        paramMap.putString(KEY_SOURCE, SourceType.DIRECTORY.value)
        return paramMap
    }

    fun generateRelatedLinkParams(productId: String): RequestParams {
        val paramMap = RequestParams()
        paramMap.putObject(KEY_KEYWORD, productId)
        return paramMap
    }

}