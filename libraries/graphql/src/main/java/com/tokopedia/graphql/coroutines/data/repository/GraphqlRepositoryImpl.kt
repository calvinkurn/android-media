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
import com.tokopedia.graphql.util.RemoteConfigHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class GraphqlRepositoryImpl @Inject constructor(
    private val graphqlCloudDataStore: GraphqlCloudDataStore,
    private val graphqlCacheDataStore: GraphqlCacheDataStore
) : GraphqlRepository {

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val results = mutableMapOf<Type, Any>()
        val refreshRequests = mutableListOf<GraphqlRequest>()
        val isCachedData = mutableMapOf<Type, Boolean>()

        val originalRequests = requests.toMutableList()

        return withContext(Dispatchers.IO) {
            when (cacheStrategy.type) {
                CacheType.NONE, CacheType.ALWAYS_CLOUD -> {
                    getCloudResponse(
                        results,
                        refreshRequests,
                        isCachedData,
                        originalRequests.toMutableList(),
                        cacheStrategy
                    )
                }
                CacheType.CACHE_ONLY -> graphqlCacheDataStore.getResponse(
                    originalRequests,
                    cacheStrategy
                )
                else -> {
                    try {
                        val responseCache =
                            graphqlCacheDataStore.getResponse(originalRequests, cacheStrategy)
                        // fix java.lang.NullPointerException: responseCache.indexOfEmptyCached must not be null at
                        if (responseCache.indexOfEmptyCached == null) {
                            getCloudResponse(
                                results,
                                refreshRequests,
                                isCachedData,
                                originalRequests.toMutableList(),
                                cacheStrategy
                            )
                        } else {
                            val tempRequestCloud = ArrayList<GraphqlRequest>()
                            responseCache.indexOfEmptyCached.forEachIndexed { index, i ->
                                originalRequests.getOrNull(i)?.let { tempRequestCloud.add(it) }
                            }
                            var responseCloud: GraphqlResponseInternal? = null
                            if (tempRequestCloud.isNotEmpty()) {
                                responseCloud = getCloudResponse(
                                    results,
                                    refreshRequests,
                                    isCachedData,
                                    tempRequestCloud.toMutableList(),
                                    cacheStrategy
                                )
                            }
                            responseCloud?.let {
                                responseCache.originalResponse.addAll(it.originalResponse)
                            }
                            GraphqlResponseInternal(
                                responseCache.originalResponse,
                                responseCache.indexOfEmptyCached
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        getCloudResponse(
                            results,
                            refreshRequests,
                            isCachedData,
                            originalRequests.toMutableList(),
                            cacheStrategy
                        )
                    }
                }
            }.toGraphqlResponse(
                results,
                refreshRequests,
                isCachedData,
                originalRequests
            )
        }
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

    private fun GraphqlResponseInternal.toGraphqlResponse(
        results: MutableMap<Type, Any>,
        refreshRequests: MutableList<GraphqlRequest>,
        isCachedData: MutableMap<Type, Boolean>,
        requests: List<GraphqlRequest>
    ): GraphqlResponse {
        val errors = mutableMapOf<Type, List<GraphqlError>>()
        val tempRequest = requests.regroup(indexOfEmptyCached)
        originalResponse?.forEachIndexed { index, jsonElement ->

            val operationName = CommonUtils.getFullOperationName(requests.getOrNull(index))

            try {
                val typeOfT = tempRequest[index].typeOfT
                val data = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.DATA)
                if (data != null && !data.isJsonNull) {
                    //Lookup for data
                    results[typeOfT] = if(RemoteConfigHelper.isEnableGqlParseErrorLoggingImprovement()) {
                        CommonUtils.fromJson(data, typeOfT, this@GraphqlRepositoryImpl.javaClass)
                    } else {
                        CommonUtils.fromJson(data, typeOfT)
                    }
                    isCachedData[typeOfT] = false
                }

                val error = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
                if (error != null && !error.isJsonNull) {
                    errors[typeOfT] = if(RemoteConfigHelper.isEnableGqlParseErrorLoggingImprovement()) {
                        CommonUtils.fromJson(
                            error,
                            Array<GraphqlError>::class.java,
                            this@GraphqlRepositoryImpl.javaClass
                        ).toList()
                    } else {
                        CommonUtils.fromJson(error, Array<GraphqlError>::class.java).toList()
                    }
                }
                LoggingUtils.logGqlSuccessRateBasedOnStatusCode(operationName, httpStatusCode)
                LoggingUtils.logGqlParseSuccess("kt", requests.toString())
            } catch (jse: JsonSyntaxException) {
                LoggingUtils.logGqlSuccessRate(operationName, "0")
                LoggingUtils.logGqlParseError(
                    "json",
                    Log.getStackTraceString(jse),
                    requests.toString()
                )
                jse.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val graphqlResponse = GraphqlResponse(results, errors, isCachedData)
        // adding http status code
        graphqlResponse.httpStatusCode = httpStatusCode

        if (refreshRequests.isEmpty()) {
            return graphqlResponse
        }

        // adding cached request.
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
            copyRequests.addAll(requests)

            for (i in 0 until copyRequests.size) {
                operationName = CommonUtils.getFullOperationName(requests.getOrNull(i))

                if (copyRequests[i].isNoCache) {
                    continue
                }

                val cKey = copyRequests[i].cacheKey()
                val cachesResponse = graphqlCloudDataStore.cacheManager
                    .get(cKey)

                if (cachesResponse == null || cachesResponse.isEmpty()) {
                    continue
                }

                //Lookup for data
                results[copyRequests[i].typeOfT] = if(RemoteConfigHelper.isEnableGqlParseErrorLoggingImprovement()){
                    CommonUtils.fromJson(cachesResponse, copyRequests[i].typeOfT, this@GraphqlRepositoryImpl.javaClass)
                } else {
                    CommonUtils.fromJson(cachesResponse, copyRequests[i].typeOfT)
                }
                isCachedData[copyRequests[i].typeOfT] = true
                copyRequests[i].isNoCache = true
                refreshRequests.add(copyRequests[i])
                requests.remove(copyRequests[i])

                LoggingUtils.logGqlParseSuccess("kt", requests.toString())
                LoggingUtils.logGqlSuccessRate(operationName, "1")
            }
        } catch (jse: JsonSyntaxException) {
            LoggingUtils.logGqlSuccessRate(operationName, "0")
            LoggingUtils.logGqlParseError("json", Log.getStackTraceString(jse), requests.toString())
            jse.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return graphqlCloudDataStore.getResponse(requests, cacheStrategy)
    }
}
