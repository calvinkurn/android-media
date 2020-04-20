package com.tokopedia.graphql.coroutines.data.source

import android.util.Log
import com.akamai.botman.CYFMonitor
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.tokopedia.akamai_bot_lib.isAkamai
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApiSuspend
import com.tokopedia.graphql.util.CacheHelper
import com.tokopedia.graphql.util.Const.AKAMAI_SENSOR_DATA_HEADER
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response

class GraphqlCloudDataStore @Inject constructor(
        private val api: GraphqlApiSuspend,
        private val cacheManager: GraphqlCacheManager,
        private val fingerprintManager: FingerprintManager
) : GraphqlDataStore {

    /*
   * akamai wrapper for generating a sensor data
   * the hash will be passing into header of
   * X-acf-sensor-data;
   * */
    private suspend fun getResponse(requests: List<GraphqlRequest>): Response<JsonArray> {
        CYFMonitor.setLogLevel(CYFMonitor.INFO)
        return if (isAkamai(requests.first().query)) {
            val header = mutableMapOf<String, String>()
            header[AKAMAI_SENSOR_DATA_HEADER] = CYFMonitor.getSensorData() ?: ""
            api.getResponseSuspend(requests.toMutableList(), header, FingerprintManager.getQueryDigest(requests))
        } else {
            api.getResponseSuspend(requests.toMutableList(), mapOf(), FingerprintManager.getQueryDigest(requests))
        }
    }

    override suspend fun getResponse(
            requests: List<GraphqlRequest>,
            cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponseInternal {
        return withContext(Dispatchers.Default) {
            var result: Response<JsonArray>? = null
            try {

                if (requests == null || requests.isEmpty()) {
                    Log.d("x-tkpd-clc", "No request available for network-call, hence stopping network communication.");
                    return@withContext GraphqlResponseInternal(null, false)
                }

                result = getResponse(requests.toMutableList())
            } catch (e: Throwable) {
                if (e !is UnknownHostException &&
                        e !is SocketException &&
                        e !is InterruptedIOException &&
                        e !is ConnectionShutdownException &&
                        e !is CancellationException) {
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