package com.tokopedia.pocnewrelic

private const val URL_NEW_RELIC_EVENT_UID_PLACEHOLDER = "{uid}"
private const val URL_NEW_RELIC_EVENT = "https://insights-collector.newrelic.com/v1/accounts/$URL_NEW_RELIC_EVENT_UID_PLACEHOLDER/events"
const val REQUEST_METHOD_POST = "POST"
const val HEADER_CONTENT_TYPE = "Content-Type"
const val HEADER_CONTENT_ENCODING = "Content-Encoding"
const val HEADER_CONTENT_LENGTH = "Content-Length"
const val HEADER_NEW_RELIC_KEY = "X-Insert-Key"
const val HEADER_CONTENT_TYPE_JSON = "application/json"
const val HEADER_CONTENT_ENCODING_GZIP = "gzip"

fun getNewRelicEventURL(userId: String): String {
    return URL_NEW_RELIC_EVENT.replace(URL_NEW_RELIC_EVENT_UID_PLACEHOLDER, userId)
}