package com.tokopedia.logger.service

import android.content.Context
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.ConnectionUtil
import com.tokopedia.logger.utils.globalScopeLaunch

class ServiceLogger {
    companion object {
        var isRunning = false

        fun run(context: Context, onComplete: () -> Unit): Boolean {
            if (isRunning) {
                onComplete()
                return false
            }
            isRunning = true
            globalScopeLaunch({
                if (ConnectionUtil.isInternetAvailable(context.applicationContext)) {
                    LogManager.sendLogToServer()
                }
                LogManager.deleteExpiredLogs()
            }, {
                it.printStackTrace()
            }, {
                isRunning = false
                onComplete()
            })
            return false
        }
    }

}