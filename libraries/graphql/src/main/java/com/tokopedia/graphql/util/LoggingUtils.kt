package com.tokopedia.graphql.util

import android.util.Log
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.util.Const.GQL_LONG_RESPONSE_LOG_MAX_LENGTH
import com.tokopedia.graphql.util.Const.GQL_REQUEST_LOG_MAX_LENGTH
import com.tokopedia.graphql.util.Const.GQL_SHORTENED_ERROR_LOG_MAX_LENGTH
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.Priority

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
    fun oldLogGqlParseError(type: String, err: String, request: String) {
        ServerLogger.log(
            Priority.P1, "GQL_PARSE_ERROR",
            mapOf(
                "type" to type,
                "err" to err.take(Const.GQL_ERROR_MAX_LENGTH).trim(),
                "req" to request.take(Const.GQL_ERROR_MAX_LENGTH).trim()
            )
        )
    }

    @JvmStatic
    fun logGqlParseError(type: String, err: String, request: GraphqlRequest, response: String) {
        val regex = ".*(?=\\)).+\\n.*".toRegex()
        val requestWithParam = regex.find(request.query)?.value.orEmpty() to request.variables.toString()
        ServerLogger.log(
            Priority.P1, "GQL_PARSE_ERROR",
            mutableMapOf(
                "type" to type,
                "err" to err.take(GQL_SHORTENED_ERROR_LOG_MAX_LENGTH).trim(),
                "req" to requestWithParam.toString().take(GQL_REQUEST_LOG_MAX_LENGTH).trim(),
            ).apply {
                response.take(GQL_LONG_RESPONSE_LOG_MAX_LENGTH).chunked(Constants.MAX_LENGTH_PER_ITEM)
                    .forEachIndexed { index, chunk ->
                        put("response_${index + 1}", chunk)
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
