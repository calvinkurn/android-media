package com.tokopedia.logger.utils

import java.util.concurrent.TimeUnit

class Constants {
    companion object{

        @JvmField
        val ONLINE_TAG_THRESHOLD: Long = TimeUnit.MINUTES.toMillis(2)
        @JvmField
        val OFFLINE_TAG_THRESHOLD: Long = TimeUnit.DAYS.toMillis(1)

        const val MAX_BUFFER = 3900
        const val SCALYR_SERVER_URL = "https://app.scalyr.com/addEvents"
        const val ENCRYPTION_KEY = "q(e#%Gf@oi>lkB~h"
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS"

        const val SEVERITY_HIGH = 1
        const val SEVERITY_MEDIUM = 2

        val LOG_SERVICE_DELAY = 30L
        // to prevent bursting scheduling
        val SCHEDULE_MIN_GAP = 10_000L // ms

        const val CLIENT_SCALYR = "scalyr"

        const val SCALYR_SUCCESS_CODE = 200
        const val LOG_DEFAULT_ERROR_CODE = 404

        const val METHOD_POST = "POST"
    }
}