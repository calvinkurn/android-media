package com.tokopedia.flashsale.management.coroutineGraphql

import com.tokopedia.flashsale.management.util.AppExecutors
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class GraphqlCloudDataStoreKt: GraphqlDataStoreKt {
    private val mApiKt: GraphqlApiKt? = null /* GraphqlClient.getApiInterface() */ //should use deferred version
    private val mCacheManager = GraphqlCacheManager()
    private val mFingerprintManager = GraphqlClient.getFingerPrintManager()

    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(AppExecutors.networkContext) {
            val result = mApiKt!!.getResponse(requests).await()
                    .run{ GraphqlResponseInternal(this, false) }

            launch(AppExecutors.ioContext) {
                when (cacheStrategy.type) {
                    CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD -> {
                        mCacheManager.save(mFingerprintManager?.generateFingerPrint(requests.toString(), cacheStrategy.isSessionIncluded),
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