package com.tokopedia.topads.view

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import java.lang.reflect.Type


object RequestHelper {

    fun getGraphQlRequest(query: String?, typeOfT: Type, parameters: HashMap<String, Any>?): GraphqlRequest {
        return GraphqlRequest(query, typeOfT, parameters)
    }

    fun getCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
    }
}