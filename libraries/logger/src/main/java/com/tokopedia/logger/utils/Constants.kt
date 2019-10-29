package com.tokopedia.logger.utils

class Constants {
    companion object{
        const val MAX_BUFFER = 3900
        const val URL_LOGENTRIES = "https://us.webhook.logs.insight.rapid7.com/v1/noformat/"
        var defaultConfig = """
            {
              "enabled": true,
              "appVersionMin": 314400000,
              "tags": [
                "P1#TagA#offline",
                "P1#TagB#online",
                "P1#TagC",
                "P2#TagA#offline",
                "P2#TagB",
                "P2#TagC"
              ]
            }
        """.trimIndent()
        const val ONLINE_TAG_THRESHOLD: Long = 120000
        const val OFFLINE_TAG_THRESHOLD: Long = 10800000
        const val KEY = "q(e#%Gf@oi>lkB~h"
        const val TIMESTAMP_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS"
        const val ENCRYPTION_MODE = "AES"
        const val ENCRYPTION_ALGORITHM = "AES"
    }
}