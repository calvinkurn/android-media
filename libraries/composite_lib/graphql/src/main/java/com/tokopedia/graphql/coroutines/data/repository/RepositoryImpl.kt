package com.tokopedia.graphql.coroutines.data.repository

import com.google.gson.Gson
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.source.GraphqlCacheDataStore
import com.tokopedia.graphql.coroutines.data.source.GraphqlCloudDataStore
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.*
import java.lang.reflect.Type
import javax.inject.Inject

open class RepositoryImpl @Inject constructor(private val graphqlCloudDataStore: GraphqlCloudDataStore,
                                              private val graphqlCacheDataStore: GraphqlCacheDataStore) : GraphqlRepository {


    override suspend fun getReseponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy)
            : GraphqlResponse {
        return when (cacheStrategy.type) {
            CacheType.NONE, CacheType.ALWAYS_CLOUD -> {
                graphqlCloudDataStore.getResponse(requests, cacheStrategy)
            }
            CacheType.CACHE_ONLY -> graphqlCacheDataStore.getResponse(requests, cacheStrategy)
            else -> {
                try {
                    val responseCache = graphqlCacheDataStore.getResponse(requests, cacheStrategy)
                    val tempRequestCloud =  ArrayList<GraphqlRequest>()
                    responseCache.indexOfEmptyCached.forEachIndexed { index, i ->
                        tempRequestCloud.add(requests.get(i))
                    }
                    var responseCloud : GraphqlResponseInternal? = null
                    if(!tempRequestCloud.isNullOrEmpty()){
                        responseCloud = graphqlCloudDataStore.getResponse(tempRequestCloud, cacheStrategy);
                    }
                    responseCloud?.let {
                        responseCache.originalResponse.addAll(it.originalResponse)
                    }
                    GraphqlResponseInternal(responseCache.originalResponse, responseCache.indexOfEmptyCached)
                } catch (e: Exception) {
                    e.printStackTrace()
                    graphqlCloudDataStore.getResponse(requests, cacheStrategy)
                }
            }
        }.toGraphqlResponse(requests)
    }

    private fun List<GraphqlRequest>.regroup(indexOfEmptyCached: List<Int>?) : MutableList<GraphqlRequest>{
        if(indexOfEmptyCached.isNullOrEmpty()) this.toMutableList()

        val tempGraphqlRequest : MutableList<GraphqlRequest> = this.toMutableList()
        indexOfEmptyCached?.sortedDescending()?.forEach{
            tempGraphqlRequest.removeAt(it)
        }
        indexOfEmptyCached?.forEach {
            tempGraphqlRequest.add(this.get(it))
        }
        return tempGraphqlRequest
    }

    fun GraphqlResponseInternal.toGraphqlResponse(requests: List<GraphqlRequest>): GraphqlResponse {
        val gson = Gson()
        val results = mutableMapOf<Type, Any>()
        val errors = mutableMapOf<Type, List<GraphqlError>>()
        val tempRequest = requests.regroup(indexOfEmptyCached)

        originalResponse.forEachIndexed { index, jsonElement ->
            try {
                val typeOfT = tempRequest[index].typeOfT
                val data = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
                if (data != null && !data.isJsonNull) {
                    results.put(typeOfT, gson.fromJson(data, typeOfT))
                }

                val error = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                if (error != null && !error.isJsonNull) {
                    errors.put(typeOfT, gson.fromJson(error, Array<GraphqlError>::class.java).toList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return GraphqlResponse(results, errors, isCached)
    }

}