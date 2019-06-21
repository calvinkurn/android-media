package com.tokopedia.cachemanager.db

import java.util.concurrent.TimeUnit

class CacheDeletion {

    companion object {
        @JvmStatic
        var isPersistentJobActive: Boolean = false
        @JvmStatic
        var isSaveInstanceJobActive: Boolean = false

        private var persistentTimeStamp = 0L
        private var saveInstanceTimeStamp = 0L
        val SCHEDULE_INTERVAL by lazy {
            TimeUnit.HOURS.toMillis(1)
        }

        fun setPersistentLastDelete() {
            persistentTimeStamp = System.currentTimeMillis()
        }

        fun setSaveInstanceLastDelete() {
            saveInstanceTimeStamp = System.currentTimeMillis()
        }

        fun isPersistentNeedDeletion(): Boolean {
            val currentSecond = System.currentTimeMillis()
            return currentSecond - persistentTimeStamp > SCHEDULE_INTERVAL
        }

        fun isSaveInstanceNeedDeletion(): Boolean {
            val currentSecond = System.currentTimeMillis()
            return currentSecond - saveInstanceTimeStamp > SCHEDULE_INTERVAL
        }

    }

}
