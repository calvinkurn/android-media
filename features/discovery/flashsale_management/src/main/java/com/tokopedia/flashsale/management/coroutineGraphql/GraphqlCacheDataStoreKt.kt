package com.tokopedia.flashsale.management.coroutineGraphql

import com.google.gson.JsonParser
import com.tokopedia.flashsale.management.util.AppExecutors
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import kotlinx.coroutines.experimental.withContext

class GraphqlCacheDataStoreKt: GraphqlDataStoreKt {
    private val mCacheManager = GraphqlCacheManager()
    private val mFingerprintManager = GraphqlClient.getFingerPrintManager()

    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(AppExecutors.ioContext){
            val rawJson = mCacheManager.get(mFingerprintManager?.generateFingerPrint(requests.toString(),
                    cacheStrategy.isSessionIncluded))
            GraphqlResponseInternal(JsonParser().parse(rawJson).asJsonArray, true)
        }
    }
}