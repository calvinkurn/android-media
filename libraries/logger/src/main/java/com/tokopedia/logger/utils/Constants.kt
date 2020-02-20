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
        const val ENCRYPTION_KEY = "q(e#%Gf@oi>lkB~h"
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS"
        const val ENCRYPTION_MODE = "AES"
        const val ENCRYPTION_ALGORITHM = "AES"
        const val SEND_PRIORITY_OFFLINE = 3
        const val SEND_PRIORITY_ONLINE = 2

        const val SEVERITY_HIGH = 1
        const val SEVERITY_MEDIUM = 2
        const val SEVERITY_NONE = 0
    }
}