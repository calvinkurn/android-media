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
        const val SEND_PRIORITY_OFFLINE = 3
        const val SEND_PRIORITY_ONLINE = 2

        const val SEVERITY_HIGH = 1
        const val SEVERITY_MEDIUM = 2
        const val SEVERITY_NONE = 0

        val LOG_SERVICE_MIN_LATENCY = TimeUnit.SECONDS.toMillis(15)
        val LOG_SERVICE_MAX_LATENCY = TimeUnit.SECONDS.toMillis(60)

        const val CLIENT_LOGENTRIES = "logentries"
        const val CLIENT_SCALYR = "scalyr"

        const val LOGENTRIES_SUCCESS_CODE = 204
        const val SCALYR_SUCCESS_CODE = 200
        const val LOG_DEFAULT_ERROR_CODE = 404

        val SCALYR_TOKEN = intArrayOf(48, 115, 98, 97, 53, 108, 77, 56, 69, 49, 112, 121, 115, 110, 71, 86, 111, 66, 111, 108, 82, 67, 53, 109, 83, 51, 109, 98, 101, 95, 101, 110, 68, 110, 118, 107, 116, 98, 80, 78, 95, 100, 84, 77, 45)
        const val SCALYR_PREF_NAME = "scalyr_sp"
        const val SCALYR_SESSION_KEY = "session"
        const val ANDROID_APP_VALUE = "androidApp"
        const val SCALYR_PARSER = "androidAppParser"

        const val METHOD_POST = "POST"
    }
}