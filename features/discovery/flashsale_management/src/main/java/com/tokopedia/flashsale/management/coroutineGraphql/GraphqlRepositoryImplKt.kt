package com.tokopedia.flashsale.management.coroutineGraphql

import com.google.gson.Gson
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.graphql.data.model.GraphqlError
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.Exception

class GraphqlRepositoryImplKt @Inject constructor(): GraphqlRepositoryKt {
    private val mGraphqlCloudDataStore: GraphqlCloudDataStoreKt
    private val mGraphqlCache: GraphqlCacheDataStoreKt

    init {
        mGraphqlCloudDataStore = GraphqlCloudDataStoreKt()
        mGraphqlCache = GraphqlCacheDataStoreKt()
    }

    override suspend fun getReseponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy)
                :GraphqlResponse {
        return when(cacheStrategy.type){
            CacheType.NONE, CacheType.ALWAYS_CLOUD -> {
                mGraphqlCloudDataStore.getResponse(requests, cacheStrategy)
            }
            CacheType.CACHE_ONLY -> mGraphqlCache.getResponse(requests, cacheStrategy)
            else -> {
                try {
                    mGraphqlCache.getResponse(requests, cacheStrategy)
                } catch (e: Exception){
                    e.printStackTrace()
                    mGraphqlCloudDataStore.getResponse(requests, cacheStrategy)
                }
            }
        }.toGraphqlResponse(requests)
    }

    fun GraphqlResponseInternal.toGraphqlResponse(requests: List<GraphqlRequest>): GraphqlResponse {
        val gson = Gson()
        val results = mutableMapOf<Type, Any>()
        val errors = mutableMapOf<Type, List<GraphqlError>>()

        originalResponse.forEachIndexed { index, jsonElement ->
            try {
                val typeOfT = requests[index].typeOfT
                val data = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
                if (!data.isJsonNull){
                    results.put(typeOfT, gson.fromJson(data, typeOfT))
                }

                val error = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                if (!error.isJsonNull){
                    errors.put(typeOfT, gson.fromJson(error, Array<GraphqlError>::class.java).toList())
                }
            } catch (e: Exception){ e.printStackTrace()}
        }

        return GraphqlResponse(results, errors, isCached)
    }

}