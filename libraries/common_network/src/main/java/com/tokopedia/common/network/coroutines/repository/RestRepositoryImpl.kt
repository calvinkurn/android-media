package com.tokopedia.common.network.coroutines.repository

import android.content.Context
import com.tokopedia.common.network.coroutines.datasource.RestCacheDataStore
import com.tokopedia.common.network.coroutines.datasource.RestCloudDataStore
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponseIntermediate
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import okhttp3.Interceptor
import javax.inject.Inject

class RestRepositoryImpl : RestRepository {

    private var mCloud: RestCloudDataStore
    private var mCache: RestCacheDataStore

    @Inject
    constructor() {
        this.mCloud = RestCloudDataStore()
        this.mCache = RestCacheDataStore()
    }

    constructor(interceptors: List<Interceptor>, context: Context) {
        this.mCloud = RestCloudDataStore(interceptors, context)
        this.mCache = RestCacheDataStore()
    }

    override suspend fun getResponse(request: RestRequest): RestResponseIntermediate? {
        val cacheStrategy = request.cacheStrategy
        return if (cacheStrategy == null
                || cacheStrategy.type == CacheType.NONE
                || cacheStrategy.type == CacheType.ALWAYS_CLOUD) {
            getCloudResponse(request)
        } else if (cacheStrategy.type == CacheType.CACHE_ONLY) {
            getCachedResponse(request)
        } else {
            try {
                mCache.getResponse(request)
            } catch (e: Exception){
                mCloud.getResponse(request)
            }
        }
    }

    private suspend fun getCloudResponse(requests: RestRequest): RestResponseIntermediate? {
        return mCloud.getResponse(requests)
    }

    private suspend fun getCachedResponse(requests: RestRequest): RestResponseIntermediate? {
        return mCache.getResponse(requests)
    }

    /**
     * will parallel request to server/cache
     */
    override suspend fun getResponses(requests: List<RestRequest>): List<RestResponseIntermediate?>? {
        return withContext(Dispatchers.IO) {
            val result = mutableListOf<RestResponseIntermediate?>()
            requests.map {
                async {
                    // it will do parallel even though the getResponse is await()
                    getResponse(it)
                }
            }.map { result.add(it.await()) }
            result
        }
    }
}
