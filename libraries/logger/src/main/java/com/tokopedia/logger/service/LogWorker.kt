package com.tokopedia.logger.service

import com.tokopedia.logger.LogManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogWorker {

    companion object {
        var lastSheduleTimestamp = 0L
        const val THRESHOLD_SCHEDULE = 10_000L // 10 seconds

        suspend fun run() {
            try {
                var needRun = true
                if (lastSheduleTimestamp > 0L){
                    val diff = System.currentTimeMillis() - lastSheduleTimestamp
                    if (diff < THRESHOLD_SCHEDULE) {
                        needRun = false
                    }
                }
                if (!needRun) {
                    return
                }
                lastSheduleTimestamp = System.currentTimeMillis()
                withContext(Dispatchers.IO){
                    try {
                        LogManager.sendLogToServer()
                        LogManager.deleteExpiredLogs()
                    } catch (e:Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (ex: Exception) {

            }
        }

    }
}