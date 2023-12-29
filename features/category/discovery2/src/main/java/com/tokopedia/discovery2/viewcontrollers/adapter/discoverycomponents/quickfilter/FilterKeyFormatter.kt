package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

import com.tokopedia.discovery.common.constants.SearchApiConst

internal object FilterKeyFormatter {

    fun format(
        selectedParameter: Map<String, String>,
        filterKey: Set<String>
    ): MutableMap<String, String> {
        val filtersQueryParameterMap = mutableMapOf<String, String>()

        val needToAppendPrefixKeys = filterKey + DEFAULT_KEY

        selectedParameter
            .filter { it.value.isNotEmpty() }
            .forEach { (key, value) ->
                var formattedKey = key

                if (needToAppendPrefixKeys.contains(key)) {
                    formattedKey = RPC_FILTER_KEY + key
                }

                filtersQueryParameterMap[formattedKey] = value
            }

        return filtersQueryParameterMap
    }

    private const val RPC_FILTER_KEY = "rpc_"

    private val DEFAULT_KEY = setOf(SearchApiConst.ORIGIN_FILTER, SORT_FILTER_KEY)
}
