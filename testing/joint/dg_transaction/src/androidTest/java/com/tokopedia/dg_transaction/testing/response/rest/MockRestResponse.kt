package com.tokopedia.dg_transaction.testing.response.rest

import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.network.data.model.response.DataResponse
import java.lang.reflect.Type

abstract class MockRestResponse<T> {

    fun getMockResponse(): Map<Type, RestResponse> {
        val response = DataResponse<T>().apply {
            data = getResponse()
        }
        val restResponse = RestResponse(response, 200, false)
        return mapOf(getToken() to restResponse)
    }

    abstract fun getToken(): Type
    abstract fun getResponse(): T
}