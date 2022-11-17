package com.tokopedia.graphql.coroutines.data.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.source.GraphqlCacheDataStore
import com.tokopedia.graphql.coroutines.data.source.GraphqlCloudDataStore
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.*
import com.tokopedia.graphql.util.LoggingUtils
import java.lang.reflect.Type
import javax.inject.Inject

@Deprecated("Dont use it, will be deleted after Interactor not being used")
open class RepositoryImpl @Inject constructor(private val graphqlCloudDataStore: GraphqlCloudDataStore,
                                              private val graphqlCacheDataStore: GraphqlCacheDataStore) : GraphqlRepository {


    override suspend fun response(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy)
            : GraphqlResponse {
        val results = mutableMapOf<Type, Any>()
        val refreshRequests = mutableListOf<GraphqlRequest>()
        val isCachedData = mutableMapOf<Type, Boolean>()

        val originalRequests = requests.toMutableList();

        return when (cacheStrategy.type) {
            CacheType.NONE, CacheType.ALWAYS_CLOUD -> {
                getCloudResponse(results, refreshRequests, isCachedData,
                        originalRequests, cacheStrategy)
            }
            CacheType.CACHE_ONLY -> graphqlCacheDataStore.getResponse(originalRequests, cacheStrategy)
            else -> {
                try {
                    val responseCache = graphqlCacheDataStore.getResponse(originalRequests, cacheStrategy)
                    val tempRequestCloud = ArrayList<GraphqlRequest>()
                    responseCache.indexOfEmptyCached.forEachIndexed { index, i ->
                        tempRequestCloud.add(originalRequests?.get(i))
                    }
                    var responseCloud: GraphqlResponseInternal? = null
                    if (!tempRequestCloud.isNullOrEmpty()) {
                        responseCloud = getCloudResponse(results, refreshRequests, isCachedData, tempRequestCloud.toMutableList(), cacheStrategy)
                    }
                    responseCloud?.let {
                        responseCache.originalResponse.addAll(it.originalResponse)
                    }
                    GraphqlResponseInternal(responseCache.originalResponse, responseCache.indexOfEmptyCached)
                } catch (e: Exception) {
                    e.printStackTrace()
                    getCloudResponse(results, refreshRequests, isCachedData,
                            originalRequests.toMutableList(), cacheStrategy)
                }
            }
        }.toGraphqlResponse(results, refreshRequests, isCachedData,
                originalRequests)
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

    fun GraphqlResponseInternal.toGraphqlResponse(
        results: MutableMap<Type, Any>,
        refreshRequests: MutableList<GraphqlRequest>,
        isCachedData: MutableMap<Type, Boolean>,
        requests: List<GraphqlRequest>
    ): GraphqlResponse {
        val errors = mutableMapOf<Type, List<GraphqlError>>()
        val tempRequest = requests.regroup(indexOfEmptyCached)

        originalResponse?.forEachIndexed { index, jsonElement ->
            val operationName = CommonUtils.getFullOperationName(tempRequest.getOrNull(index))
            try {
                val typeOfT = tempRequest[index].typeOfT
                val data = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
                try {
                    if (data != null && !data.isJsonNull) {
                        //Lookup for data03-19 00:06:47.537 32115-32488/com.tokopedia.tkpd D/OkHttp: x-tkpd-clc: AddToken-291ac79f54b52aa73eb4413dbe00703a,
                        results[typeOfT] = CommonUtils.fromJson(data, typeOfT)
                        isCachedData.put(typeOfT, false)
                    }

                    val error = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                    if (error != null && !error.isJsonNull) {
                        errors[typeOfT] =
                            CommonUtils.fromJson(error, Array<GraphqlError>::class.java).toList()
                    }
                } catch (jse: JsonSyntaxException) {
                    LoggingUtils.logGqlSuccessRate(operationName, "0")
                    LoggingUtils.logGqlParseError(
                        "json",
                        "${jse.message.orEmpty()} at ${jse.stackTrace.firstOrNull()?.toString().orEmpty()}",
                        requests[index],
                        jsonElement.toString()
                    )
                    jse.printStackTrace()
                    return@forEachIndexed
                }
                LoggingUtils.logGqlParseSuccess("kt", requests.toString())
                LoggingUtils.logGqlSuccessRateBasedOnStatusCode(operationName, httpStatusCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val graphqlResponse = GraphqlResponse(results, errors, isCachedData)

        if (refreshRequests.isEmpty()) {
            return graphqlResponse
        }

        //adding cached request.
        graphqlResponse.refreshRequests = refreshRequests

        return graphqlResponse
    }

    /**
     * Helper method to merge the partial caches response with lived response of network
     */
    private suspend fun getCloudResponse(
            results: MutableMap<Type, Any>,
            refreshRequests: MutableList<GraphqlRequest>,
            isCachedData: MutableMap<Type, Boolean>,
            requests: MutableList<GraphqlRequest>,
            cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponseInternal {
        var operationName = ""
        try {
            val copyRequests = mutableListOf<GraphqlRequest>()
            copyRequests.addAll(requests);

            for (i in 0 until copyRequests.size) {
                operationName = CommonUtils.getFullOperationName(copyRequests.getOrNull(i))

                if (copyRequests[i].isNoCache) {
                    continue
                }

                val cKey = copyRequests[i].cacheKey()
                val cachesResponse = graphqlCloudDataStore.cacheManager
                        .get(cKey)

                if (cachesResponse == null || cachesResponse.isEmpty()) {
                    continue
                }

                try {
                    //Lookup for data
                    results[copyRequests[i].typeOfT] = CommonUtils.fromJson(cachesResponse, copyRequests[i].typeOfT)
                } catch (jse: JsonSyntaxException) {
                    LoggingUtils.logGqlSuccessRate(operationName, "0")
                    LoggingUtils.logGqlParseError(
                        "json",
                        "${jse.message.orEmpty()} at ${jse.stackTrace.firstOrNull()?.toString().orEmpty()}",
                        copyRequests[i],
                        cachesResponse
                    )
                    jse.printStackTrace()
                    continue
                }
                isCachedData[copyRequests[i].typeOfT] = true
                copyRequests[i].isNoCache = true
                refreshRequests.add(copyRequests[i])
                requests.remove(copyRequests[i])

                LoggingUtils.logGqlParseSuccess("kt", requests.toString())
                LoggingUtils.logGqlSuccessRate(operationName, "1")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return graphqlCloudDataStore.getResponse(requests, cacheStrategy)
    }
}
