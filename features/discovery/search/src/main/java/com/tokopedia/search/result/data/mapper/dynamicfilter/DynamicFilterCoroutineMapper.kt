package com.tokopedia.search.result.data.mapper.dynamicfilter

import com.google.gson.Gson
import com.tokopedia.discovery.common.Mapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.constant.ResponseStatus
import retrofit2.Response

class DynamicFilterCoroutineMapper(
        private val gson: Gson
): Mapper<Response<String>, DynamicFilterModel> {

    override fun convert(source: Response<String>): DynamicFilterModel {
        if (source.isSuccessful) {
            return gson.fromJson(source.body(), DynamicFilterModel::class.java)
                    ?: throw RuntimeException(source.errorBody().toString())
        } else {
            throw RuntimeException(getRuntimeErrorExceptionMessage(source.code()))
        }
    }

    private fun getRuntimeErrorExceptionMessage(errorCode: Int): String {
        return when (errorCode) {
            ResponseStatus.SC_INTERNAL_SERVER_ERROR -> ErrorNetMessage.MESSAGE_ERROR_SERVER
            ResponseStatus.SC_FORBIDDEN -> ErrorNetMessage.MESSAGE_ERROR_FORBIDDEN
            ResponseStatus.SC_REQUEST_TIMEOUT, ResponseStatus.SC_GATEWAY_TIMEOUT -> ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
            else -> ErrorNetMessage.MESSAGE_ERROR_DEFAULT
        }
    }
}