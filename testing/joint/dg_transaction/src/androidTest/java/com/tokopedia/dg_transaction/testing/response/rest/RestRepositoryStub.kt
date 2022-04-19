package com.tokopedia.dg_transaction.testing.response.rest

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.network.data.model.response.DataResponse
import kotlinx.coroutines.delay
import java.lang.reflect.Type
import javax.inject.Inject

class RestRepositoryStub @Inject constructor(): RestRepository {
    lateinit var responses: Map<Type, RestResponse>
    var isError = false
    var delayMs = 0L

    override suspend fun getResponse(request: RestRequest): RestResponse {
        return RestResponse(DataResponse<ResponseCartData>(), 200, false)
    }

    override suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse> {
        if (delayMs != 0L) {
            delay(delayMs)
        }
        if (isError) {
            throw IllegalStateException()
        }
        return responses
    }
}