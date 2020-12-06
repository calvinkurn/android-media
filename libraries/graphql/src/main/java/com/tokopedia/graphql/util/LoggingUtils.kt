package com.tokopedia.graphql.util

import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.remoteconfig.RemoteConfig
import timber.log.Timber

object LoggingUtils {
    const val DEFAULT_RESP_SIZE_THRES = 10000L
    var gqlResponseSizeThreshold = DEFAULT_RESP_SIZE_THRES

    @JvmStatic
    fun setResponseSize(responseSize: Long) {
        var sizeToSet = responseSize
        if (sizeToSet <= 0) {
            sizeToSet = DEFAULT_RESP_SIZE_THRES
        }
        gqlResponseSizeThreshold = sizeToSet
    }

    @JvmStatic
    fun logGqlError(classType: String, request: String, throwable: Throwable) {
        Timber.w("P1#GQL_ERROR#$classType;err='${Log.getStackTraceString(throwable).take(Const.GQL_ERROR_MAX_LENGTH).trim()}';req='${request.take(Const.GQL_ERROR_MAX_LENGTH).trim()}'")
    }

    @JvmStatic
    fun logGqlResponseCode(respCode: Int, request: String, response: String) {
        val sampleRequest = request.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
        val sampleResponse = response.take(Const.GQL_ERROR_MAX_LENGTH).trim()
        Timber.w("P1#GQL_RESP_CODE#$respCode;req='$sampleRequest';resp='$sampleResponse'")

    }

    @JvmStatic
    fun logGqlSize(classType: String, request: List<GraphqlRequest>, response: String) {
        val responseSize = response.length
        if (responseSize > gqlResponseSizeThreshold) {
            val requestString = request.toString()
            val sampleRequest = requestString.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
            val variable = requestString.substringAfter("variables=").substringBefore(", operationName").trim()
            Timber.w("P1#GQL_SIZE#$classType;req_size=${sampleRequest.length};resp_size=${response.length};req='$sampleRequest';var='$variable'")
        }
    }

    @JvmStatic
    fun logGqlSizeCached(classType: String, request: String, response: String) {
        val sampleRequest = request.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
        Timber.w("P1#GQL_CACHED_RESP#$classType;req_size=${sampleRequest.length};resp_size=${response.length};req='$sampleRequest'")
    }

    @JvmStatic
    fun logGqlNullChecker(errorMessage: String, className: String, request: String) {
        val sampleRequest = request.take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
        Timber.w("P1#GQL_NULL#'$className';err='$errorMessage';req='$sampleRequest'")
    }
}