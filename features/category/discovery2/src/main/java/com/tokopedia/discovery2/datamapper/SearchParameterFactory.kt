package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery.common.model.SearchParameter

internal object SearchParameterFactory {

    private var existingParameter: Map<String, Any>? = null

    fun construct(query: String?, path: String): SearchParameter? {
        if (query == null) return null

        val deepLinkUri = "tokopedia://$path?$query"

        val searchParameter = SearchParameter(deepLinkUri)

        existingParameter?.forEach { filter ->
            searchParameter.set(filter.key, filter.value.toString())
        }

        return searchParameter
    }

    fun withExistingParameter(params: Map<String, Any>): SearchParameterFactory {
        this.existingParameter = params

        return this
    }
}
