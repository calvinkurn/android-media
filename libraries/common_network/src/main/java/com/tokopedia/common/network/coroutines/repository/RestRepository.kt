package com.tokopedia.common.network.coroutines.repository

import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import java.lang.reflect.Type

interface RestRepository {
    suspend fun getResponse(request: RestRequest): RestResponse?

    suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse?>?
}
