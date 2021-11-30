package com.tokopedia.inbox.fake.domain.usecase.notifcenter.topads

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.topads.sdk.domain.model.TopAdsmageViewResponse
import java.lang.reflect.Type

class FakeTopAdsRestRepository : RestRepository {

    var isError = false
    var response = TopAdsmageViewResponse(null, null, null)

    override suspend fun getResponse(request: RestRequest): RestResponse {
        if (isError) {
            throw IllegalStateException("Error Get TDN")
        }
        return RestResponse(response, 200, false)
    }

    // TODO: create fake impl
    override suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse> {
        return HashMap()
    }
}