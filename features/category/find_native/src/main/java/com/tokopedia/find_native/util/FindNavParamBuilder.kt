package com.tokopedia.find_native.util

import com.tokopedia.common_category.model.filter.DAFilterQueryType
import com.tokopedia.common_category.util.ParamMapToUrl
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

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
private const val KEY_DIRECTORY = "directory"
private const val KEY_QUICK_FILTER = "quick_filter"
private const val VALUE_SAFE_SEARCH = "false"

class FindNavParamBuilder @Inject constructor() {

    private fun prepareProductListParams(productKey: String, start: Int, rows: Int, uniqueId: String, sortParam: HashMap<String, String>, filterParam: HashMap<String, String>): Map<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap[KEY_SAFE_SEARCH] = VALUE_SAFE_SEARCH
        queryMap[KEY_SOURCE] = KEY_SOURCE_VALUE
        if (filterParam.isNotEmpty()) {
            queryMap.putAll(filterParam)
        }
        if (sortParam.isNotEmpty()) {
            queryMap.putAll(sortParam)
        }
        queryMap[KEY_QUERY] = productKey
        queryMap[KEY_DEVICE] = KEY_DEVICE_VALUE
        queryMap[KEY_START] = start * rows
        queryMap[KEY_ROWS] = rows
        queryMap[KEY_UNIQUE_ID] = uniqueId
        return queryMap
    }

    fun generateProductFilterParams(productId: String, start: Int, rows: Int, uniqueId: String,
                                    selectedSort: HashMap<String, String>, selectedFilter: HashMap<String, String>): RequestParams {
        val searchProductRequestParams = RequestParams.create()
        searchProductRequestParams.putString(KEY_PARAMS, createParametersForQuery(prepareProductListParams(productId, start, rows, uniqueId,
                selectedSort, selectedFilter)))
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
        quickFilterParam.putString(KEY_SOURCE, KEY_QUICK_FILTER)
        return quickFilterParam
    }

    fun generateDynamicFilterParams(productId: String): RequestParams {
        val paramMap = RequestParams()
        val daFilterQueryType = DAFilterQueryType()
        daFilterQueryType.sc = productId
        paramMap.putObject(KEY_FILTER, daFilterQueryType)
        paramMap.putString(KEY_QUERY, "")
        paramMap.putString(KEY_SOURCE, KEY_DIRECTORY)
        return paramMap
    }

    fun generateRelatedLinkParams(productId: String): RequestParams {
        val paramMap = RequestParams()
        paramMap.putObject(KEY_KEYWORD, productId)
        return paramMap
    }

}