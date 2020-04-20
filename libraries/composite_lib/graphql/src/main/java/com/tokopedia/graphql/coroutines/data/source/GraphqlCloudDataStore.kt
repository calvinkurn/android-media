package com.tokopedia.graphql.coroutines.data.source

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApiSuspend
import com.tokopedia.graphql.util.CacheHelper
import kotlinx.coroutines.*
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GraphqlCloudDataStore(private val api: GraphqlApiSuspend,
                            val cacheManager: GraphqlCacheManager,
                            private val fingerprintManager: FingerprintManager) : GraphqlDataStore {

    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(Dispatchers.Default) {
            var result: Response<JsonArray>? = null
            try {

                if (requests == null || requests.isEmpty()) {
                    Log.d("x-tkpd-clc", "No request available for network-call, hence stopping network communication.");
                    return@withContext GraphqlResponseInternal(null, false)
                }

                result = api.getResponseSuspend(requests.toMutableList(), FingerprintManager.getQueryDigest(requests))
            } catch (e: Throwable) {
                if (e !is UnknownHostException && e !is SocketTimeoutException && e !is CancellationException) {
                    Timber.e(e, "P1#REQUEST_ERROR_GQL#$requests")
                }
                if (e !is CancellationException) {
                    throw e
                }
            }

            yield()

            val gJsonArray = if (result?.body() == null) JsonArray() else result.body()
            val cacheHeaders = if (result?.headers()?.get(GraphqlConstant.GqlApiKeys.CACHE) == null) ""
            else result.headers().get(GraphqlConstant.GqlApiKeys.CACHE);
            val gResponse = GraphqlResponseInternal(gJsonArray, false, cacheHeaders)
            result?.let {

                launch(Dispatchers.IO) {
                    //Handling backend cache
                    val caches = CacheHelper.parseCacheHeaders(gResponse.beCache)

                    caches?.let {
                        if (!caches.isEmpty()) {
                            requests.forEachIndexed { index, request ->
                                //Do nothing
                                if (request == null || request.isNoCache || it[request.md5] == null) {
                                    return@forEachIndexed
                                }

                                //TODO need to save response of individual query
                                val cache = caches[request.md5]
                                val objectData = gResponse.originalResponse[index].asJsonObject[GraphqlConstant.GqlApiKeys.DATA]
                                if (objectData != null && cache != null) {
                                    cacheManager.save(request.cacheKey(), objectData.toString(), cache.maxAge * 1000.toLong())
                                    Log.e("x-tkpd-clc","responce saved {${objectData.toString()}}, key {${request.cacheKey()}}");

                                }
                            }
                        }
                    }

                    when (cacheStrategy.type) {
                        CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD -> {
                            gResponse.originalResponse.forEachIndexed { index, jsonElement ->
                                if (!isError(jsonElement)) {
                                    cacheManager.save(fingerprintManager.generateFingerPrint(requests[index].toString(),
                                            cacheStrategy.isSessionIncluded),
                                            jsonElement.toString(),
                                            cacheStrategy.expiryTime)
                                }
                            }
                        }
                    }
                }
            }

            gResponse
        }
    }

    fun isError(jsonElement: JsonElement): Boolean {
        try {
            val error = jsonElement.asJsonObject.get(GraphqlConstant.GqlApiKeys.ERROR)
            if (error != null && !error.isJsonNull) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}