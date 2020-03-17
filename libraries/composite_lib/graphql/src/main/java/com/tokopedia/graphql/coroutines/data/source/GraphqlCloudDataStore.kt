package com.tokopedia.graphql.coroutines.data.source

import com.google.gson.JsonElement
import com.google.gson.JsonArray
import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.*
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApi
import com.tokopedia.graphql.data.source.cloud.api.GraphqlApiSuspend
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class GraphqlCloudDataStore @Inject constructor(private val api: GraphqlApiSuspend,
                                                private val cacheManager: GraphqlCacheManager,
                                                private val fingerprintManager: FingerprintManager) : GraphqlDataStore {

    override suspend fun getResponse(requests: List<GraphqlRequest>, cacheStrategy: GraphqlCacheStrategy): GraphqlResponseInternal {
        return withContext(Dispatchers.Default) {
            var result = JsonArray()
            try {
                result = api.getResponseSuspend(requests.toMutableList())
            } catch (e: Throwable) {
                if (e !is UnknownHostException && e!is SocketTimeoutException && e !is CancellationException) {
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