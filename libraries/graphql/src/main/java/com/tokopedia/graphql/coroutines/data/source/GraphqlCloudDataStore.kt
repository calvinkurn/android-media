package com.tokopedia.graphql.coroutines.data.source

import android.util.Log
import com.akamai.botman.CYFMonitor
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.tokopedia.akamai_bot_lib.isAkamai
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponseInternal
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApiSuspend
import com.tokopedia.graphql.util.CacheHelper
import com.tokopedia.graphql.util.Const
import com.tokopedia.graphql.util.Const.AKAMAI_SENSOR_DATA_HEADER
import com.tokopedia.graphql.util.LoggingUtils
import kotlinx.coroutines.*
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

class GraphqlCloudDataStore @Inject constructor(
        private val api: GraphqlApiSuspend,
        val cacheManager: GraphqlCacheManager,
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
            header[AKAMAI_SENSOR_DATA_HEADER] = GraphqlClient.getFunction().getAkamaiValue()
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
                    return@withContext GraphqlResponseInternal(null, false)
                }
                result = getResponse(requests.toMutableList())
            } catch (e: Throwable) {
                if (e !is UnknownHostException &&
                        e !is SocketException &&
                        e !is InterruptedIOException &&
                        e !is ConnectionShutdownException &&
                        e !is CancellationException) {
                    LoggingUtils.logGqlError("kt", requests.toString(), e)
                }
                if (e !is CancellationException) {
                    throw e
                }
            }

            yield()

            val gJsonArray = if (result?.body() == null) JsonArray() else result.body()

            //Checking response CLC headers.
            val cacheHeaders = if (result?.headers()?.get(GraphqlConstant.GqlApiKeys.CACHE) == null) ""
            else result.headers().get(GraphqlConstant.GqlApiKeys.CACHE);
            val gResponse = GraphqlResponseInternal(gJsonArray, false, cacheHeaders)

            try {
                result?.let {

                    launch(Dispatchers.IO) {
                        if (result.code() != Const.GQL_RESPONSE_HTTP_OK) {
                            LoggingUtils.logGqlResponseCode(result.code(), requests.toString(), gResponse.originalResponse.toString())
                        }
                        LoggingUtils.logGqlSize("kt", requests.toString(), gResponse.originalResponse.toString())
                        //Handling backend cache
                        val caches = CacheHelper.parseCacheHeaders(gResponse.beCache)

                        caches?.let {
                            if (!caches.isEmpty()) {
                                requests.forEachIndexed { index, request ->
                                    if (request.isNoCache || it[request.md5] == null) {
                                        return@forEachIndexed  //Do nothing
                                    }

                                    //Saving response for indivisual query.
                                    val cache = caches[request.md5]
                                    val objectData = gResponse.originalResponse[index].asJsonObject[GraphqlConstant.GqlApiKeys.DATA]
                                    if (objectData != null && cache != null) {
                                        cacheManager.save(request.cacheKey(), objectData.toString(), cache.maxAge * 1000.toLong())
                                        Timber.d("Android CLC - Request saved to cache " + CacheHelper.getQueryName(request.query) + " KEY: " + request.cacheKey())
                                    }
                                }
                            }
                        }

                        //Proceed for local cache as usual
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
            } catch (e: Exception) {
                e.printStackTrace()
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