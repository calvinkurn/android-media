package com.tokopedia.iris.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.iris.util.DEFAULT_MAX_ROW
import com.tokopedia.iris.util.DEFAULT_SERVICE_TIME
import java.util.concurrent.TimeUnit

/**
 * @author okasurya on 10/18/18.
 */
data class Configuration(
        @SerializedName("row_limit") var maxRow: Int = DEFAULT_MAX_ROW,
        @SerializedName("interval") var intervals: Long = DEFAULT_SERVICE_TIME,
        var isEnabled: Boolean = true
) {
    val intervalSeconds = TimeUnit.MINUTES.toSeconds(intervals)
}