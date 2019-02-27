package com.tokopedia.common.network.coroutines.datasource

import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse

interface RestDataStore {
    suspend fun getResponse(request: RestRequest): RestResponse
}
