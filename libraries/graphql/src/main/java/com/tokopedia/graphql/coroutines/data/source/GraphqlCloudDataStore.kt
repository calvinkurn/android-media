package com.tokopedia.graphql.coroutines.data.source

import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class GraphqlCloudDataStore(private val api: GraphqlApi,
                            private val cacheManager: GraphqlCacheManager,
                            private val fingerprintManager: FingerprintManager) : GraphqlDataStore {


    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(Dispatchers.Default) {
            val result = api.getResponseDeferred(requests).await()
            val graphqlResponseInternal = GraphqlResponseInternal(result, false)

            launch(Dispatchers.IO) {
                when (cacheStrategy.type) {
                    CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD -> {
                        if (!isError(graphqlResponseInternal)) {
                            cacheManager.save(fingerprintManager.generateFingerPrint(requests.toString(),
                                    cacheStrategy.isSessionIncluded),
                                    graphqlResponseInternal.originalResponse.toString(),
                                    cacheStrategy.expiryTime)
                        }
                    }
                    else -> {
                    }
                }
            }
            graphqlResponseInternal
        }
    }

    fun isError(graphqlResponseInternal: GraphqlResponseInternal): Boolean {
        var index = 0
        for (item in graphqlResponseInternal.originalResponse) {
            try {
                val error = item.asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                if (error != null && !error.isJsonNull) {
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            index++
        }
        return false
    }
}