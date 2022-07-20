package com.tokopedia.pushnotif.util

import android.util.Log
import com.tokopedia.logger.ServerLogger.log
import com.tokopedia.logger.utils.Priority

object LoggerUtil {
    private const val TAG = "LOG_PUSH_NOTIF"
    private const val MAX_VALUE = 1000
    private const val LOG_TYPE = "type"
    private const val LOG_ERR = "err"

    @JvmStatic
    fun recordErrorServerLog(type: String, throwable: Throwable){
        val messageMap: MutableMap<String, String> = HashMap()
        messageMap[LOG_TYPE] = "PushNotification::$type"
        messageMap[LOG_ERR] = Log.getStackTraceString(throwable).substring(0,
            Log.getStackTraceString(throwable).length.coerceAtMost(MAX_VALUE))
        log(Priority.P2, TAG, messageMap)
    }
}