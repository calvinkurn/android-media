package com.tokopedia.analytics.performance.util

class NetworkData (
    /**
     * total network response size in milli seconds
     */
    val totalResponseSize: Int = 0,

    /**
     * total network duration in milli seconds
     */
    val totalResponseTime:Long = 0L,

    /**
     * Total network duration in milli seconds
     * Start from the first network is started and ended with last network call is ended.
     */
    val totalUserNetworkDuration:Long = 0L,

    /**
     * map of gql string key and its response size in bytes
     * example: homeData->110032, ticker->20123
     */
    val responseSizeDetailMap:Map<String, Int> = emptyMap(),
    /**
     * map of gql string key and its duration in milli seconds
     * example: homeData->300, ticker->100
     */
    val responseTimeDetailMap:Map<String, Long> = emptyMap()
) {
    val responseSizeDetailMapString:String
        get() = responseSizeDetailMap.toString().replaceCommaString()

    val responseTimeDetailMapString:String
        get() = responseTimeDetailMap.toString().replaceCommaString()

    private fun String.replaceCommaString() = this.replace(",", ";")
}