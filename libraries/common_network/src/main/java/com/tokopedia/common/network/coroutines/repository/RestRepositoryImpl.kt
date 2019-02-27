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

class RestRepositoryImpl(private val mCloud: RestCloudDataStore,
                         private val mCache: RestCacheDataStore) : RestRepository {

    fun updateInterceptors(interceptors: List<Interceptor>, context: Context){
        mCloud.updateInterceptor(interceptors, context)
    }

    override suspend fun getResponse(request: RestRequest): RestResponse {
        val cacheStrategy = request.cacheStrategy
        return if (cacheStrategy.type == CacheType.NONE
                || cacheStrategy.type == CacheType.ALWAYS_CLOUD) {
            getCloudResponse(request)
        } else if (cacheStrategy.type == CacheType.CACHE_ONLY) {
            getCachedResponse(request)
        } else { // CACHE FIRST
            try {
                getCachedResponse(request)
            } catch (e: Exception) {
                getCloudResponse(request)
            }
        }
    }

    private suspend fun getCloudResponse(requests: RestRequest): RestResponse {
        return mCloud.getResponse(requests)
    }

    private suspend fun getCachedResponse(requests: RestRequest): RestResponse {
        return mCache.getResponse(requests)
    }

    /**
     * will parallel request to server/cache
     */
    override suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse> {
        return withContext(Dispatchers.IO) {
            requests.map {
                async {
                    // it will do parallel even though the getResponse is await()
                    getResponse(it)
                }
            }.map { it.await() }.groupBy { it.type }.mapValues { it.value.first() }
        }
    }
}
