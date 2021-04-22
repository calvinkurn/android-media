package com.tokopedia.graphql.util

import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber

object LoggingUtils {
    const val DEFAULT_RESP_SIZE_THRES = 10000L
    var gqlResponseSizeThreshold = DEFAULT_RESP_SIZE_THRES

    @JvmStatic
    fun setResponseSize(responseSize: Long) {
        var sizeToSet = responseSize
        if (sizeToSet < 0) {
            sizeToSet = DEFAULT_RESP_SIZE_THRES
        }
        gqlResponseSizeThreshold = sizeToSet
    }

    @JvmStatic
    fun logGqlError(classType: String, request: String, throwable: Throwable) {
        ServerLogger.log(Priority.P1, "GQL_RESP_CODE", mapOf("type" to classType, "err" to Log.getStackTraceString(throwable).take(Const.GQL_ERROR_MAX_LENGTH).trim(), "resp" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim()))
    }

    @JvmStatic
    fun logGqlResponseCode(respCode: Int, request: String, response: String) {
        val sampleRequest = request.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
        val sampleResponse = response.take(Const.GQL_ERROR_MAX_LENGTH).trim()
        ServerLogger.log(Priority.P1, "GQL_RESP_CODE", mapOf("type" to respCode.toString(), "req" to sampleRequest, "resp" to sampleResponse))
    }

    @JvmStatic
    fun logGqlSize(classType: String, request: List<GraphqlRequest>, response: String) {
        val responseSize = response.length
        if (responseSize >= gqlResponseSizeThreshold) {
            val requestString = request.toString()
            val sampleRequest = requestString.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
            val variable = requestString.substringAfter("variables=").substringBefore(", operationName").trim()
            ServerLogger.log(Priority.P1, "GQL_SIZE", mapOf("type" to classType, "req_size" to sampleRequest.length.toString(), "resp_size" to response.length.toString(), "req" to sampleRequest, "var" to variable))
        }
    }

    @JvmStatic
    fun logGqlSizeCached(classType: String, request: String, response: String) {
        val sampleRequest = request.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
        ServerLogger.log(Priority.P1, "GQL_CACHED_RESP", mapOf("type" to classType, "req_size" to sampleRequest.length.toString(), "resp_size" to response.length.toString(), "req" to sampleRequest))
    }

    @JvmStatic
    fun logGqlNullChecker(errorMessage: String, className: String, request: String) {
        val sampleRequest = request.take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
        ServerLogger.log(Priority.P1, "GQL_NULL", mapOf("type" to className, "err" to errorMessage, "req" to sampleRequest))
    }
}