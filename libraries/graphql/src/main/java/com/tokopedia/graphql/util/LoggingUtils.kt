package com.tokopedia.graphql.util

import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Constants.Companion.MAX_LENGTH_PER_ITEM
import com.tokopedia.logger.utils.Priority
import kotlin.math.min

object LoggingUtils {
    const val DEFAULT_RESP_SIZE_THRES = 10000L
    const val STATUS_CODE_ERROR = 500

    @JvmStatic
    fun logGqlSuccessRateBasedOnStatusCode(operationName: String, httpStatusCode: Int) {
        if (httpStatusCode >= STATUS_CODE_ERROR) {
            logGqlSuccessRate(operationName, "0")
        } else {
            logGqlSuccessRate(operationName, "1")
        }
    }

    @JvmStatic
    fun logGqlSuccessRate(operationName: String, gqlResult: String) {
        if (operationName.isNotBlank()) {
            ServerLogger.log(
                Priority.SF,
                "GP",
                mapOf(
                    "on" to operationName,
                    "s" to gqlResult
                )
            )
        }
    }

    @JvmStatic
    fun logGqlParseSuccess(classType: String, request: String) {
        ServerLogger.log(
            Priority.P1,
            "GQL_PARSE_SUCCESS",
            mapOf("type" to classType, "req" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim())
        )
    }

    @JvmStatic
    fun logGqlParseError(type: String, err: String, request: String, response: String, params: String) {
        val splitResponse = response.chunked(MAX_LENGTH_PER_ITEM)
        ServerLogger.log(
            Priority.P1, "GQL_PARSE_ERROR",
            mapOf(
                "type" to type,
                "err" to err.take(Const.GQL_ERROR_MAX_LENGTH).trim(),
                "req" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim(),
                "params" to params
            ).toMutableMap().apply {
                splitResponse.forEachIndexed { i, response ->
                    put("response_$i", response)
                }
            }
        )
    }

    @JvmStatic
    fun logGqlError(classType: String, request: String, throwable: Throwable) {
        ServerLogger.log(
            Priority.P1,
            "GQL_ERROR",
            mapOf(
                "type" to classType,
                "err" to Log.getStackTraceString(throwable).take(Const.GQL_ERROR_MAX_LENGTH).trim(),
                "req" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim()
            )
        )
    }

    @JvmStatic
    fun logGqlErrorSsl(
        classType: String,
        request: String,
        throwable: Throwable,
        tls: String,
        cipherSuites: String
    ) {
        ServerLogger.log(
            Priority.P1,
            "GQL_ERROR",
            mapOf(
                "type" to classType,
                "err" to Log.getStackTraceString(throwable).take(Const.GQL_ERROR_MAX_LENGTH).trim(),
                "req" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim(),
                "tls" to tls,
                "cipher" to cipherSuites
            )
        )
    }

    @JvmStatic
    fun logGqlErrorNetwork(classType: String, request: String, throwable: Throwable) {
        ServerLogger.log(
            Priority.P1,
            "GQL_ERROR_NETWORK",
            mapOf(
                "type" to classType,
                "err" to Log.getStackTraceString(throwable).take(Const.GQL_ERROR_MAX_LENGTH).trim(),
                "req" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim()
            )
        )
    }

    @JvmStatic
    fun logGqlErrorBackend(
        from: String,
        request: String,
        errorMessage: String,
        statusCode: String
    ) {
        ServerLogger.log(
            Priority.P1,
            "GQL_ERROR_BACKEND",
            mapOf(
                "from" to from,
                "code" to statusCode,
                "err" to errorMessage,
                "req" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim()
            )
        )
    }

    @JvmStatic
    fun logGqlResponseCode(respCode: Int, request: String, response: String) {
        val sampleRequest =
            request.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH)
                .trim()
        val sampleResponse = response.take(Const.GQL_ERROR_MAX_LENGTH).trim()
        ServerLogger.log(
            Priority.P1,
            "GQL_RESP_CODE",
            mapOf("type" to respCode.toString(), "req" to sampleRequest, "resp" to sampleResponse)
        )
    }

    @JvmStatic
    fun logGqlSize(classType: String, request: List<GraphqlRequest>, response: String) {
        val responseSize = response.length
        if (responseSize >= DEFAULT_RESP_SIZE_THRES) {
            val requestString = request.toString()
            val sampleRequest = requestString.substringAfter("[GraphqlRequest{query='")
                .take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
            val variable =
                requestString.substringAfter("variables=").substringBefore(", operationName").trim()
            ServerLogger.log(
                Priority.P1,
                "GQL_SIZE",
                mapOf(
                    "type" to classType,
                    "req_size" to sampleRequest.length.toString(),
                    "resp_size" to response.length.toString(),
                    "req" to sampleRequest,
                    "var" to variable
                )
            )
        }
    }

    @JvmStatic
    fun logGqlSizeCached(classType: String, request: String, response: String) {
        val sampleRequest =
            request.substringAfter("[GraphqlRequest{query='").take(Const.GQL_RESPONSE_MAX_LENGTH)
                .trim()
        ServerLogger.log(
            Priority.P1,
            "GQL_CACHED_RESP",
            mapOf(
                "type" to classType,
                "req_size" to sampleRequest.length.toString(),
                "resp_size" to response.length.toString(),
                "req" to sampleRequest
            )
        )
    }

    @JvmStatic
    fun logGqlNullChecker(errorMessage: String, className: String, request: String) {
        val sampleRequest = request.take(Const.GQL_RESPONSE_MAX_LENGTH).trim()
        ServerLogger.log(
            Priority.P1,
            "GQL_NULL",
            mapOf("type" to className, "err" to errorMessage, "req" to sampleRequest)
        )
    }
}
