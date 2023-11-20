package com.tokopedia.iris

data class IrisPerformanceData(
    val screenName: String,
    /**
     * time to first layout =
     * duration of start open page until the first layout (like shimerring or loading) is placed
     */
    val ttflInMs: Long,
    /**
     * time to initial load =
     * duration of start open page until all initial views are loaded
     */
    val ttilInMs: Long
) {
    fun isDataInvalid() = ttflInMs <= 0L ||
            ttilInMs <= 0L ||
            screenName.isEmpty()
}