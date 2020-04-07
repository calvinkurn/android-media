package com.tokopedia.graphql.coroutines.data.source

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
import com.tokopedia.graphql.util.Const.AKAMAI_SENSOR_DATA_HEADER
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject
import okhttp3.internal.http2.ConnectionShutdownException

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
    private suspend fun getResponse(requests: List<GraphqlRequest>): JsonArray {
        CYFMonitor.setLogLevel(CYFMonitor.INFO)
        return if (isAkamai(requests.first().query)) {
            val header = mutableMapOf<String, String>()
            header[AKAMAI_SENSOR_DATA_HEADER] = CYFMonitor.getSensorData() ?: ""
            api.getResponseSuspend(requests.toMutableList(), header)
        } else {
            api.getResponseSuspend(requests.toMutableList(), mapOf())
        }
    }

    override suspend fun getResponse(
            requests: List<GraphqlRequest>,
            cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponseInternal {
        return withContext(Dispatchers.Default) {
            var result = JsonArray()
            try {
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

            val graphqlResponseInternal = GraphqlResponseInternal(result, false)

            launch(Dispatchers.IO) {
                when (cacheStrategy.type) {
                    CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD -> {
                        graphqlResponseInternal.originalResponse.forEachIndexed { index, jsonElement ->
                            if (!isError(jsonElement)) {
                                cacheManager.save(fingerprintManager.generateFingerPrint(requests[index].toString(),
                                        cacheStrategy.isSessionIncluded),
                                        jsonElement.toString(),
                                        cacheStrategy.expiryTime)
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
            graphqlResponseInternal
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