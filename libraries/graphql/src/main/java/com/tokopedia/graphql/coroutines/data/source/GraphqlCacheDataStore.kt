package com.tokopedia.graphql.coroutines.data.source

import com.google.gson.JsonParser
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.kotlin.extensions.coroutines.AppExecutors
import kotlinx.coroutines.experimental.withContext

class GraphqlCacheDataStore(private val mCacheManager: GraphqlCacheManager,
                            private val mFingerprintManager: FingerprintManager): GraphqlDataStore {

    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(AppExecutors.ioContext){
            val rawJson = mCacheManager.get(mFingerprintManager.generateFingerPrint(requests.toString(),
                    cacheStrategy.isSessionIncluded))
            GraphqlResponseInternal(JsonParser().parse(rawJson).asJsonArray, true)
        }
    }
}