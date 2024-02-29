package com.tokopedia.search.utils

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.SEARCH_DEBUG_LOG
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

class SearchLogger(
    private val remoteConfig: RemoteConfig? = null,
    private val versionCode: Int? = null,
    private val userId: String? = null
) {

    fun logWarning(message: String?, throwable: Throwable?) {
        if (message == null || throwable == null || isExceptionExcluded(throwable)) return

        timberLogWarning(message, ExceptionUtils.getStackTrace(throwable))
    }

    private fun isExceptionExcluded(throwable: Throwable): Boolean {
        return when(throwable){
            is UnknownHostException -> true
            is SocketException -> true
            is InterruptedIOException -> true
            is ConnectionShutdownException -> true
            else -> false
        }
    }

    private fun timberLogWarning(message: String, stackTrace: String) {
        ServerLogger.log(
            Priority.P2,
            DISCOVERY_SEARCH_ERROR_TAG,
            mapOf(TYPE to message, ERROR to stackTrace),
        )
    }

    fun logTDNError(throwable: Throwable?) {
        if (throwable == null) return

        ServerLogger.log(
            Priority.P2,
            DISCOVERY_SEARCH_TDN_ERROR_TAG,
            mapOf("error" to ExceptionUtils.getStackTrace(throwable)),
        )
    }

    fun logAnomalyNoKeyword(message: String?) {
        if (message == null) return

        ServerLogger.log(
            Priority.P2,
            DISCOVERY_SEARCH_ANOMALY_TAG,
            mapOf("type" to "No keyword to search result. $message"),
        )
    }

    fun logSearchDebug(keyword: String?, filterParams: String? = null) {
        val hasKeyword = keyword != null
        val remoteConfigEnabled = isLogSearchDebugConfigEnabled()
        val enabledByUserId = isLogSearchDebugConfigByUserIdEnabled()
        val canSendLog = hasKeyword && remoteConfigEnabled && enabledByUserId

        if (!canSendLog) return

        ServerLogger.log(
            Priority.P2,
            DISCOVERY_SEARCH_DEBUG_TAG,
            mapOf(
                KEYWORD to (keyword ?: ""),
                FILTER to (filterParams ?: ""),
            )
        )
    }

    private fun isLogSearchDebugConfigByUserIdEnabled(): Boolean {
        val userIdList = getRemoteConfigSearchDebugValue().lastOrNull()?.split(VERSION_CODE_SEPARATOR)

        return userIdList?.contains(userId).orFalse()
    }

    private fun isLogSearchDebugConfigEnabled(): Boolean {
        val versionCode = versionCode?.toString() ?: return false
        val remoteConfigValue = getRemoteConfigSearchDebugValue()
        val remoteConfigVersionCodeList = remoteConfigValue.firstOrNull()?.split(VERSION_CODE_SEPARATOR)

        return remoteConfigVersionCodeList?.contains(versionCode).orFalse()
    }

    private fun getRemoteConfigSearchDebugValue() : List<String> {
        val remoteConfigValue = remoteConfig?.getString(SEARCH_DEBUG_LOG) ?: ""
        return remoteConfigValue.split(USER_VERSION_SEPARATOR)
    }

    companion object {
        const val DISCOVERY_SEARCH_ERROR_TAG = "DISCOVERY_SEARCH_ERROR"
        const val DISCOVERY_SEARCH_TDN_ERROR_TAG = "DISCOVERY_SEARCH_TDN_ERROR"
        const val DISCOVERY_SEARCH_ANOMALY_TAG = "DISCOVERY_SEARCH_ANOMALY"
        const val DISCOVERY_SEARCH_DEBUG_TAG = "DISCOVERY_SEARCH_DEBUG"
        const val TYPE = "type"
        const val ERROR = "error"
        const val KEYWORD = "keyword"
        const val FILTER = "filter"
        const val VERSION_CODE_SEPARATOR = ","
        const val USER_VERSION_SEPARATOR = ";"
    }
}
