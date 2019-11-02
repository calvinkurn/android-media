package com.tokopedia.logger.utils

import java.util.concurrent.TimeUnit

class Constants {
    companion object{

        @JvmField
        val ONLINE_TAG_THRESHOLD: Long = TimeUnit.MINUTES.toMillis(2)
        @JvmField
        val OFFLINE_TAG_THRESHOLD: Long = TimeUnit.HOURS.toMillis(3)

        const val MAX_BUFFER = 3900
        const val URL_LOGENTRIES = "https://us.webhook.logs.insight.rapid7.com/v1/noformat/"
        const val KEY = "q(e#%Gf@oi>lkB~h"
        const val TIMESTAMP_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS"
        const val ENCRYPTION_MODE = "AES"
        const val ENCRYPTION_ALGORITHM = "AES"
        const val OFFLINE_LOGS = 3
        const val ONLINE_LOGS = 2

        const val SEVERITY_HIGH = 1
        const val SEVERITY_MEDIUM = 2
        const val NO_SEVERITY = 0
    }
}