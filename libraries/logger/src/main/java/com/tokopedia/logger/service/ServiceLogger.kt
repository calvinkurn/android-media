package com.tokopedia.logger.service

import android.content.Context
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.ConnectionUtil

class ServiceLogger {
    companion object {
        var isRunning = false

        fun run(context: Context, onComplete: () -> Unit): Boolean {
            if (isRunning) {
                onComplete()
                return false
            }
            isRunning = true
            try {
                if (ConnectionUtil.isInternetAvailable(context.applicationContext)) {
                    LogManager.sendLogToServer()
                }
                LogManager.deleteExpiredLogs()
            } catch (e:Exception) {
                e.printStackTrace()
            } finally {
                isRunning = false
                onComplete()
            }
            return false
        }
    }

}