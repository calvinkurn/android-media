package com.tokopedia.graphql.coroutines.data.repository

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.source.GraphqlCacheDataStore
import com.tokopedia.graphql.coroutines.data.source.GraphqlCloudDataStore
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.graphql.data.model.GraphqlError
import java.lang.reflect.Type
import kotlin.Exception

class GraphqlRepositoryImpl(private val graphqlCloudDataStore: GraphqlCloudDataStore,
                            private val graphqlCacheDataStore: GraphqlCacheDataStore) : GraphqlRepository {

    val mResults = mutableMapOf<Type, Any>()
    val mRefreshRequests = mutableListOf<GraphqlRequest>()
    private val mIsCachedData = mutableMapOf<Type, Boolean>()
    val gson = Gson()

    override suspend fun getReseponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy)
            : GraphqlResponse {
        return when (cacheStrategy.type) {
            CacheType.NONE, CacheType.ALWAYS_CLOUD -> {
                getCloudResponse(requests.toMutableList(), cacheStrategy)
            }
            CacheType.CACHE_ONLY -> graphqlCacheDataStore.getResponse(requests, cacheStrategy)
            else -> {
                try {
                    val responseCache = graphqlCacheDataStore.getResponse(requests, cacheStrategy)
                    val tempRequestCloud = ArrayList<GraphqlRequest>()
                    responseCache.indexOfEmptyCached.forEachIndexed { index, i ->
                        tempRequestCloud.add(requests.get(i))
                    }
                    var responseCloud: GraphqlResponseInternal? = null
                    if (!tempRequestCloud.isNullOrEmpty()) {
                        responseCloud = getCloudResponse(requests.toMutableList(), cacheStrategy)
                    }
                    responseCloud?.let {
                        responseCache.originalResponse.addAll(it.originalResponse)
                    }
                    GraphqlResponseInternal(responseCache.originalResponse, responseCache.indexOfEmptyCached)
                } catch (e: Exception) {
                    e.printStackTrace()
                    getCloudResponse(requests.toMutableList(), cacheStrategy)
                }
            }
        }.toGraphqlResponse(requests)
    }

    private fun List<GraphqlRequest>.regroup(indexOfEmptyCached: List<Int>?): MutableList<GraphqlRequest> {
        if (indexOfEmptyCached.isNullOrEmpty()) this.toMutableList()

        val tempGraphqlRequest: MutableList<GraphqlRequest> = this.toMutableList()
        indexOfEmptyCached?.sortedDescending()?.forEach {
            tempGraphqlRequest.removeAt(it)
        }
        indexOfEmptyCached?.forEach {
            tempGraphqlRequest.add(this.get(it))
        }
        return tempGraphqlRequest
    }

    fun GraphqlResponseInternal.toGraphqlResponse(requests: List<GraphqlRequest>): GraphqlResponse {
        val errors = mutableMapOf<Type, List<GraphqlError>>()
        val tempRequest = requests.regroup(indexOfEmptyCached)

        originalResponse?.forEachIndexed { index, jsonElement ->
            try {
                val typeOfT = tempRequest[index].typeOfT
                val data = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
                if (data != null && !data.isJsonNull) {
                    //Lookup for data
                    mResults.put(typeOfT, gson.fromJson(data, typeOfT))
                    mIsCachedData.put(typeOfT, false)
                }

                val error = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                if (error != null && !error.isJsonNull) {
                    errors.put(typeOfT, gson.fromJson(error, Array<GraphqlError>::class.java).toList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val graphqlResponse = GraphqlResponse(mResults, errors, mIsCachedData)

        if (mRefreshRequests.isEmpty()) {
            return graphqlResponse
        }

        //adding cached request.
        graphqlResponse.refreshRequests = mRefreshRequests

        return graphqlResponse
    }

    private suspend fun getCloudResponse(requests: MutableList<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        val counter = requests.size
        for (i in 0 until counter) {
            if (requests[i].isNoCache) {
                continue
            }

            val cachesResponse = graphqlCloudDataStore.cacheManager
                    .get(requests[i].cacheKey())

            if (cachesResponse == null || cachesResponse.isEmpty()) {
                continue
            }

            //Lookup for data
            mResults[requests[i].typeOfT] = gson.fromJson(cachesResponse, requests[i].typeOfT)
            mIsCachedData[requests[i].typeOfT] = true
            requests[i].isNoCache = true
            mRefreshRequests.add(requests[i])
            requests.remove(requests[i])
        }

        return graphqlCloudDataStore.getResponse(requests, cacheStrategy)
    }
}
