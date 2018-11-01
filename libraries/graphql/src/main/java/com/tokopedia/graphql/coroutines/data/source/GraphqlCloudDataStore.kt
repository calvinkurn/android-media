package com.tokopedia.graphql.coroutines.data.source

import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi
import com.tokopedia.kotlin.extensions.coroutines.AppExecutors
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class GraphqlCloudDataStore(private val api: GraphqlApi,
                            private val cacheManager: GraphqlCacheManager,
                            private val fingerprintManager: FingerprintManager): GraphqlDataStore {


    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(AppExecutors.bgContext) {
            val result = api.getResponseDeferred(requests).await()
            val graphqlResponseInternal = GraphqlResponseInternal(result, false)

            launch(AppExecutors.ioContext) {
                when (cacheStrategy.type) {
                    CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD -> {
                        cacheManager.save(fingerprintManager.generateFingerPrint(requests.toString(),
                                cacheStrategy.isSessionIncluded),
                                graphqlResponseInternal.originalResponse.toString(),
                                cacheStrategy.expiryTime)
                    }
                    else -> { }
                }
            }
            graphqlResponseInternal
        }
    }
}