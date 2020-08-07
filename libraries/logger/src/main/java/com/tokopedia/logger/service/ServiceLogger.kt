package com.tokopedia.logger.service

import androidx.work.ListenableWorker
import com.tokopedia.logger.LogManager

class ServiceLogger {
    companion object {
        suspend fun run(): ListenableWorker.Result {
            try {
                LogManager.sendLogToServer()
                LogManager.deleteExpiredLogs()
            } catch (e:Exception) {
                e.printStackTrace()
            }
            return ListenableWorker.Result.success()
        }
    }
}