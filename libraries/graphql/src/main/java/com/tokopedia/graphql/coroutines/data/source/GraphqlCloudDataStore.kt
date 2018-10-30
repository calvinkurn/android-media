package com.tokopedia.graphql.coroutines.data.source

import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
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
                            private val fingerprintManager: FingerprintManager): GraphqlDataStore {


    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(Dispatchers.Default) {
            val result = api.getResponseDeferred(requests).await()
                    .run{ GraphqlResponseInternal(this, false) }

            launch(Dispatchers.IO) {
                when (cacheStrategy.type) {
                    CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD -> {
                        cacheManager.save(fingerprintManager.generateFingerPrint(requests.toString(),
                                cacheStrategy.isSessionIncluded),
                                result.originalResponse.toString(),
                                cacheStrategy.expiryTime)
                    }
                    else -> {
                    }
                }
            }
            result
        }
    }
}