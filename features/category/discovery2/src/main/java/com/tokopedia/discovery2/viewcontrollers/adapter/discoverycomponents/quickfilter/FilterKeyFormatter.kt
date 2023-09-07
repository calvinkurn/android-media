package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickfilter

internal object FilterKeyFormatter {

    fun format(
        filterParameter: Map<String, String>,
        originalKeyParameter: Set<String>
    ): MutableMap<String, String> {
        val filtersQueryParameterMap = mutableMapOf<String, String>()
        filterParameter
            .filter { it.value.isNotEmpty() }
            .forEach { (key, value) ->
                var formattedKey = key

                if (!originalKeyParameter.contains(key)) {
                    formattedKey = RPC_FILTER_KEY + key
                }

                filtersQueryParameterMap[formattedKey] = value
            }

        return filtersQueryParameterMap
    }

    private const val RPC_FILTER_KEY = "rpc_"
}
