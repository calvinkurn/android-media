package com.tokopedia.cachemanager.db

import kotlinx.coroutines.experimental.Job
import java.util.concurrent.TimeUnit

class CacheDeletion {

    companion object {
        @JvmStatic
        val persistentExpireDeletionJob: Job = Job()
        @JvmStatic
        var isPersistentJobActive: Boolean = false
        @JvmStatic
        val saveInstanceExpireDeletionJob: Job = Job()
        @JvmStatic
        var isSaveInstanceJobActive: Boolean = false

        private var persistanceTimeStamp = 0L
        private var saveInstanceTimeStamp = 0L
        val SCHEDULE_INTERVAL by lazy {
            TimeUnit.MINUTES.toMillis(5)
        }

        fun setPersistentLastDelete() {
            persistanceTimeStamp = System.currentTimeMillis()
        }

        fun setSaveInstanceLastDelete() {
            saveInstanceTimeStamp = System.currentTimeMillis()
        }

        fun isPersistentNeedDeletion(): Boolean {
            val currentSecond = System.currentTimeMillis()
            return currentSecond - persistanceTimeStamp > SCHEDULE_INTERVAL
        }

        fun isSaveInstanceNeedDeletion(): Boolean {
            val currentSecond = System.currentTimeMillis()
            return currentSecond - saveInstanceTimeStamp > SCHEDULE_INTERVAL
        }

    }

}