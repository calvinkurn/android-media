package com.tokopedia.flight.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.BaseResponseError
import java.io.IOException

/**
 * @author by furqan on 09/06/2021
 */
data class FlightErrorResponse(
        @SerializedName(ERROR_KEY)
        @Expose
        val errorList: List<FlightError> = arrayListOf())
    : BaseResponseError() {

    override fun getErrorKey(): String = ERROR_KEY

    override fun hasBody(): Boolean =
            errorList.isNotEmpty()

    override fun createException(): IOException {
        val message = getConcattedMessage()
        return FlightException(message, errorList)
    }

    private fun getConcattedMessage(): String {
        val results: MutableList<String> = arrayListOf()
        for (errorItem in errorList) {
            results.add(errorItem.title)
        }
        return results.joinToString(separator = ", ")
    }

    companion object {
        private const val ERROR_KEY = "errors"
    }
}