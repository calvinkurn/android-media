package com.tokopedia.graphql.coroutines.data.source

import android.text.TextUtils
import com.akamai.botman.CYFMonitor
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.tokopedia.akamai_bot_lib.getAkamaiQuery
import com.tokopedia.graphql.CommonUtils
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
import com.tokopedia.graphql.util.Const.QUERY_HASHING_HEADER
import com.tokopedia.graphql.util.Const.TKPD_AKAMAI
import com.tokopedia.graphql.util.LoggingUtils
import com.tokopedia.graphql.util.LoggingUtils.logGqlErrorSsl
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.*
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response
import java.io.InterruptedIOException
import java.lang.NullPointerException
import java.net.SocketException
import java.net.UnknownHostException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLHandshakeException




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
        val header = mutableMapOf<String, String>()

        putAkamaiHeader(header, requests)

        if (requests[0].isDoQueryHash) {
            val queryHashingHeaderValue = StringBuilder()
            for (graphqlRequest in requests) {
                val queryHashValue: String = cacheManager.getQueryHashValue(graphqlRequest.md5)
                if (TextUtils.isEmpty(queryHashValue)) {
                    queryHashingHeaderValue.append("")
                } else {
                    if (queryHashingHeaderValue.length <= 0) {
                        queryHashingHeaderValue.append(queryHashValue)
                    } else {
                        queryHashingHeaderValue.append(",")
                            .append(queryHashValue)
                    }
                    graphqlRequest.query = ""
                }
            }
            if (TextUtils.isEmpty(queryHashingHeaderValue.toString())) {
                queryHashingHeaderValue.append(";true")
            } else {
                queryHashingHeaderValue.append(";false")
            }
            header[QUERY_HASHING_HEADER] = queryHashingHeaderValue.toString()
        }
        var url = requests[0].url
        if(url.isNullOrEmpty()){
            var opName: String? = requests[0].operationName
            if (TextUtils.isEmpty(opName)) {
                opName = CacheHelper.getQueryName(requests[0].query)
            }
            url = "graphql/$opName"
        }
        return api.getResponseSuspendWithPath(url, requests.toMutableList(), header, FingerprintManager.getQueryDigest(requests), FingerprintManager.getQueryDigest(requests))
    }

    private fun putAkamaiHeader(header: MutableMap<String, String>, requests: List<GraphqlRequest>) {
        //akamai query Logic
        var akamaiQuery = ""
        requests.forEach { req ->
            val queryNamelist = req.queryNameList
            akamaiQuery = if (queryNamelist?.isNotEmpty() == true) {
                getAkamaiQuery(queryNamelist) ?: ""
            } else {
                getAkamaiQuery(req.query) ?: ""
            }
            if (akamaiQuery.isNotEmpty()) {
                return@forEach
            }
        }
        if (akamaiQuery.isNotEmpty()) {
            header[AKAMAI_SENSOR_DATA_HEADER] = GraphqlClient.getFunction().akamaiValue
            header[TKPD_AKAMAI] = akamaiQuery
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
                if (e is SSLHandshakeException) {
                    var tls: String
                    var cipherSuites: String = ""
                    try {
                        tls =
                            Arrays.toString(SSLContext.getDefault().defaultSSLParameters.protocols)
                        cipherSuites =
                            Arrays.toString(SSLContext.getDefault().defaultSSLParameters.cipherSuites)
                    } catch (e: NoSuchAlgorithmException) {
                        tls = "Failed to get ssl"
                    } catch (e: NullPointerException) {
                        tls = "Got null on ssl"
                    }
                    logGqlErrorSsl("kt", requests.toString(), e, tls, cipherSuites)
                } else if (e !is UnknownHostException &&
                    e !is SocketException &&
                    e !is InterruptedIOException &&
                    e !is ConnectionShutdownException &&
                    e !is CancellationException
                ) {
                    LoggingUtils.logGqlError("kt", requests.toString(), e)
                } else {
                    LoggingUtils.logGqlErrorNetwork("kt", requests.toString(), e)
                }
                if (e !is CancellationException) {
                    throw e
                }
            }

            yield()

            val gJsonArray = CommonUtils.getOriginalResponse(result)

            //Checking response CLC headers.
            val cacheHeaders =
                if (result?.headers()?.get(GraphqlConstant.GqlApiKeys.CACHE) == null) ""
                else result.headers().get(GraphqlConstant.GqlApiKeys.CACHE);

            val queryHashHeaders =
                if (result?.headers()?.get(GraphqlConstant.GqlApiKeys.QUERYHASH) == null) ""
                else result.headers().get(GraphqlConstant.GqlApiKeys.QUERYHASH)

            val gResponse =
                GraphqlResponseInternal(gJsonArray, false, cacheHeaders, queryHashHeaders)

            try {
                result?.let {

                    launch(Dispatchers.IO) {
                        gResponse.httpStatusCode = result.code()
                        if (result.code() == Const.GQL_QUERY_HASHING_ERROR) {
                            val queryHashValues = StringBuilder()
                            //Reset request bodies
                            if (requests.size > 0) {
                                for (graphqlRequest in requests) {
                                    graphqlRequest.query = graphqlRequest.queryCopy
                                    queryHashValues.append(
                                        cacheManager.getQueryHashValue(
                                            graphqlRequest.md5
                                        )
                                    ).append(",")
                                    cacheManager.deleteQueryHashValue(graphqlRequest.md5)
                                }
                            }
                            val header: MutableMap<String, String> = HashMap()
                            if (requests.get(0).queryHashRetryCount > 0) {
                                header[QUERY_HASHING_HEADER] = ";true"
                                requests.get(0).queryHashRetryCount =
                                    requests.get(0).queryHashRetryCount - 1
                            } else {
                                header[QUERY_HASHING_HEADER] = ""
                            }
                            val opName = requests[0]./Name
                            ServerLogger.log(
                                Priority.P1, "GQL_HASHING",
                                mapOf(
                                    "type" to "error",
                                    "name" to if (opName?.isNotEmpty() == true) {
                                        opName
                                    } else {
                                        CacheHelper.getQueryName(requests[0].query)
                                    },
                                    "key" to requests[0].md5,
                                    "hash" to queryHashValues.toString()
                                )
                            )
                            var url = requests[0].url
                            if(url.isNullOrEmpty()){
                                var opName: String? = requests[0].operationName
                                if (TextUtils.isEmpty(opName)) {
                                    opName = CacheHelper.getQueryName(requests[0].query)
                                }
                                url = "graphql/$opName"
                            }
                            api.getResponseSuspendWithPath(
                                    url,
                                    requests.toMutableList(),
                                    header,
                                    FingerprintManager.getQueryDigest(requests),
                                    FingerprintManager.getQueryDigest(requests)
                            )
                        }
                        if (result.code() != Const.GQL_RESPONSE_HTTP_OK) {
                            LoggingUtils.logGqlResponseCode(
                                result.code(),
                                requests.toString(),
                                gResponse.originalResponse.toString()
                            )
                        }
                        LoggingUtils.logGqlSize(
                            "kt",
                            requests,
                            gResponse.originalResponse.toString()
                        )
                        //Handling backend cache
                        val caches = CacheHelper.parseCacheHeaders(gResponse.beCache)
                        //handling query hash
                        val qhValues = CacheHelper.parseQueryHashHeader(gResponse.queryHash)

                        var executeCacheFlow = false
                        var executeQueryHashFlow = false

                        if (qhValues.size > 0) {
                            executeQueryHashFlow = true
                        }
                        if (caches != null && !caches.isEmpty()) {
                            executeCacheFlow = true
                        }

                        requests.forEachIndexed { index, request ->
                            if (executeQueryHashFlow) {
                                cacheManager.saveQueryHash(request.md5, qhValues.get(index))
                                val opName = requests[0].operationName
                                ServerLogger.log(
                                    Priority.P1, "GQL_HASHING",
                                    mapOf(
                                        "type" to "success",
                                        "name" to if (opName?.isNotEmpty() == true) {
                                            opName
                                        } else {
                                            CacheHelper.getQueryName(requests[0].query)
                                        },
                                        "key" to request.md5,
                                        "hash" to qhValues[index]
                                    )
                                )
                            }
                            if (request.isNoCache || (executeCacheFlow && caches[request.md5] == null)) {
                                return@forEachIndexed  //Do nothing
                            }
                            if (executeCacheFlow) {
                                //Saving response for indivisual query.
                                val cache = caches[request.md5]
                                val objectData =
                                    gResponse.originalResponse[index].asJsonObject[GraphqlConstant.GqlApiKeys.DATA]
                                if (objectData != null && cache != null) {
                                    cacheManager.save(
                                        request.cacheKey(),
                                        objectData.toString(),
                                        cache.maxAge * 1000.toLong()
                                    )
                                }
                            }
                        }

                        //Proceed for local cache as usual
                        when (cacheStrategy.type) {
                            CacheType.CACHE_FIRST, CacheType.ALWAYS_CLOUD -> {
                                gResponse.originalResponse.forEachIndexed { index, jsonElement ->
                                    if (!isError(jsonElement)) {
                                        cacheManager.save(
                                            fingerprintManager.generateFingerPrint(
                                                requests[index].toString(),
                                                cacheStrategy.isSessionIncluded
                                            ),
                                            jsonElement.toString(),
                                            cacheStrategy.expiryTime
                                        )
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
