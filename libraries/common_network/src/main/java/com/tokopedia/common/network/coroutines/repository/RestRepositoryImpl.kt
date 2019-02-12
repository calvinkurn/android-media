package com.tokopedia.common.network.coroutines.repository

import android.content.Context
import com.tokopedia.common.network.coroutines.datasource.RestCacheDataStore
import com.tokopedia.common.network.coroutines.datasource.RestCloudDataStore
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.network.data.model.RestResponseIntermediate
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import okhttp3.Interceptor
import java.lang.reflect.Type
import javax.inject.Inject

class RestRepositoryImpl : RestRepository {

    private var mCloud: RestCloudDataStore
    private var mCache: RestCacheDataStore

    constructor() {
        this.mCloud = RestCloudDataStore()
        this.mCache = RestCacheDataStore()
    }

    constructor(interceptors: List<Interceptor>, context: Context) {
        this.mCloud = RestCloudDataStore(interceptors, context)
        this.mCache = RestCacheDataStore()
    }

    override suspend fun getResponse(request: RestRequest): RestResponse? {
        val cacheStrategy = request.cacheStrategy
        return if (cacheStrategy.type == CacheType.NONE
                || cacheStrategy.type == CacheType.ALWAYS_CLOUD) {
            getCloudResponse(request)
        } else if (cacheStrategy.type == CacheType.CACHE_ONLY) {
            getCachedResponse(request)
        } else { // CACHE FIRST
            try {
                val response = getCachedResponse(request)
                if (response == null) {
                    throw NullPointerException()
                } else {
                    return response
                }
            } catch (e: Exception) {
                getCloudResponse(request)
            }
        }
    }

    private suspend fun getCloudResponse(requests: RestRequest): RestResponse? {
        return mCloud.getResponse(requests).toRestResponse()
    }

    private suspend fun getCachedResponse(requests: RestRequest): RestResponse? {
        return mCache.getResponse(requests).toRestResponse()
    }

    private fun RestResponseIntermediate?.toRestResponse(): RestResponse? {
        if (this == null) {
            return null
        }
        return RestResponse(originalResponse, code, isCached).apply {
            this.isError = isError
            this.errorBody = errorBody
        }
    }

    /**
     * will parallel request to server/cache
     */
    override suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse?>? {
        return withContext(Dispatchers.IO) {
            val resultMap = mutableMapOf<Type, RestResponse?>()
            requests.map {
                async {
                    // it will do parallel even though the getResponse is await()
                    getResponse(it)
                }
            }.map { it.await() }
                    .mapNotNull { it }
                    .map { resultMap.put(it.type, it) }
            resultMap
        }
    }
}
