package com.tokopedia.discovery2.datamapper

import com.tokopedia.discovery.common.model.SearchParameter

internal object SearchParameterFactory {
    fun construct(query: String?, path: String): SearchParameter? {
        if (query == null) return null

        val deepLinkUri = "tokopedia://$path?$query"
        return SearchParameter(deepLinkUri)
    }
}
