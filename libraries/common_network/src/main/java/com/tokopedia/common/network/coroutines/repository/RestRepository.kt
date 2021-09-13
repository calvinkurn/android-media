package com.tokopedia.common.network.coroutines.repository

import android.content.Context
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import okhttp3.Interceptor
import java.lang.reflect.Type

interface RestRepository {
    suspend fun getResponse(request: RestRequest): RestResponse

    suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse>

    fun updateInterceptors(interceptors: List<Interceptor>, context: Context)
}
