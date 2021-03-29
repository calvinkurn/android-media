package com.tokopedia.search.result.network.validator

import com.google.gson.Gson
import com.tokopedia.network.data.model.response.BaseResponseError
import com.tokopedia.cacheapi.util.CacheApiResponseValidator
import okhttp3.Response
import java.lang.Exception

class CacheApiTKPDResponseValidator<T : BaseResponseError>(
        private val typeParameterClass: Class<T>
) : CacheApiResponseValidator() {

    companion object {
        private const val BYTE_COUNT = Long.MAX_VALUE
    }

    override fun isResponseValidToBeCached(response: Response): Boolean {
        return if (!super.isResponseValidToBeCached(response)) {
            false
        } else !isStatusOkAndResponseError(response)
    }

    private fun isStatusOkAndResponseError(response: Response): Boolean {
        return try {
            val gson = Gson()
            val responseBody = response.peekBody(BYTE_COUNT)
            val responseError: BaseResponseError = gson.fromJson(responseBody.string(), typeParameterClass)
            responseError.isResponseErrorValid
        } catch (e: Exception) {
            false
        }
    }

}