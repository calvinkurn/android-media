package com.tokopedia.common.network.coroutines.repository

import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponseIntermediate

interface RestRepository {
    suspend fun getResponse(request: RestRequest): RestResponseIntermediate?

    suspend fun getResponses(requests: List<RestRequest>): List<RestResponseIntermediate?>?
}
