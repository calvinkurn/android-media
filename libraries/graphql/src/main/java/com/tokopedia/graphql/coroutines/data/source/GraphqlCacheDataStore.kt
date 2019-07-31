package com.tokopedia.graphql.coroutines.data.source

import com.google.gson.JsonParser
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GraphqlCacheDataStore(private val mCacheManager: GraphqlCacheManager,
                            private val mFingerprintManager: FingerprintManager): GraphqlDataStore {

    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(Dispatchers.IO){
            if(cacheStrategy.type == CacheType.CACHE_FIRST) {
                val indexOfEmptyCached = ArrayList<Int>()
                val listOfCached = ArrayList<String>()
                requests.forEachIndexed { index, graphqlRequest ->
                    val rawJson = mCacheManager.get(mFingerprintManager.generateFingerPrint(graphqlRequest.toString(),
                            cacheStrategy.isSessionIncluded))
                    if (rawJson.isNullOrEmpty()) {
                        indexOfEmptyCached.add(index)
                    } else {
                        listOfCached.add(rawJson)
                    }
                }
                GraphqlResponseInternal(JsonParser().parse(listOfCached.toString()).asJsonArray, true, indexOfEmptyCached)
            }else{
                val rawJson = mCacheManager.get(mFingerprintManager.generateFingerPrint(requests.toString(),
                        cacheStrategy.isSessionIncluded))
                GraphqlResponseInternal(JsonParser().parse(rawJson).asJsonArray, true)
            }
        }
    }
}