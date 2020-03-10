package com.tokopedia.logger.utils

import java.util.concurrent.TimeUnit

class Constants {
    companion object{

        @JvmField
        val ONLINE_TAG_THRESHOLD: Long = TimeUnit.MINUTES.toMillis(2)
        @JvmField
        val OFFLINE_TAG_THRESHOLD: Long = TimeUnit.DAYS.toMillis(1)

        const val MAX_BUFFER = 3900
        const val SERVER_URL = "https://us.webhook.logs.insight.rapid7.com/v1/noformat/"
        const val SCALYR_SERVER_URL = "https://app.scalyr.com/addEvents"
        const val ENCRYPTION_KEY = "q(e#%Gf@oi>lkB~h"
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS"
        const val ENCRYPTION_MODE = "AES"
        const val ENCRYPTION_ALGORITHM = "AES"
        const val SEND_PRIORITY_OFFLINE = 3
        const val SEND_PRIORITY_ONLINE = 2

        const val SEVERITY_HIGH = 1
        const val SEVERITY_MEDIUM = 2
        const val SEVERITY_NONE = 0

        val LOG_SERVICE_MIN_LATENCY = TimeUnit.SECONDS.toMillis(5)
        val LOG_SERVICE_MAX_LATENCY = TimeUnit.SECONDS.toMillis(30)

        const val CLIENT_LOGENTRIES = "logentries"
        const val CLIENT_SCALYR = "scalyr"

        const val LOGENTRIES_SUCCESS_CODE = 204
        const val SCALYR_SUCCESS_CODE = 200
        const val LOG_DEFAULT_ERROR_CODE = 404

        val SCALYR_TOKEN = intArrayOf(48, 100, 49, 101, 104, 72, 98, 119, 110, 73, 101, 48, 83, 108, 57, 87, 71, 89, 115, 77, 105, 106, 89, 79, 47, 48, 87, 112, 88, 121, 49, 77, 79, 110, 107, 99, 95, 79, 85, 52, 56, 110, 54, 52, 45)
        const val SCALYR_PREF_NAME = "scalyr_sp"
        const val SCALYR_SESSION_KEY = "session"
        const val ANDROID_APP_VALUE = "androidApp"
        const val SCALYR_PARSER = "androidAppParser"

        const val METHOD_POST = "POST"
    }
}