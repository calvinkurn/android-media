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
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS"

        const val EVENT_TYPE_NEW_RELIC = "eventType"
        const val EVENT_ANDROID_NEW_RELIC = "android"

        const val SEVERITY_HIGH = 1
        const val SEVERITY_MEDIUM = 2

        val LOG_SERVICE_DELAY = 30L
        // to prevent bursting scheduling
        val SCHEDULE_MIN_GAP = 10_000L // ms

        const val CLIENT_SCALYR = "scalyr"

        const val RESPONSE_SUCCESS_CODE = 200
        const val LOG_DEFAULT_ERROR_CODE = 404

        const val METHOD_POST = "POST"

        const val TAG_LOG = "log_tag"
        const val PRIORITY_LOG = "log_priority"

        private const val URL_NEW_RELIC_EVENT_UID_PLACEHOLDER = "{uid}"
        const val NEW_RELIC_SERVER_URL = "https://insights-collector.newrelic.com/v1/accounts/$URL_NEW_RELIC_EVENT_UID_PLACEHOLDER/events"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_CONTENT_ENCODING = "Content-Encoding"
        const val HEADER_CONTENT_LENGTH = "Content-Length"
        const val HEADER_NEW_RELIC_KEY = "X-Insert-Key"
        const val HEADER_CONTENT_TYPE_JSON = "application/json"
        const val HEADER_CONTENT_ENCODING_GZIP = "gzip"

        fun getNewRelicEventURL(userId: String): String {
            return NEW_RELIC_SERVER_URL.replace(URL_NEW_RELIC_EVENT_UID_PLACEHOLDER, userId)
        }
    }
}