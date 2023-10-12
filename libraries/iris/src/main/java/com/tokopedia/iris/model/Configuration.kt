package com.tokopedia.iris.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.iris.util.DEFAULT_MAX_ROW
import com.tokopedia.iris.util.DEFAULT_PERF_SAMPLE
import com.tokopedia.iris.util.DEFAULT_SERVICE_TIME
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

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

data class PerfConfiguration(
    /**
     * sampling percentage, 0 to 100%
     */
    @SerializedName("sampling_rate") var samplingRate: Float = DEFAULT_PERF_SAMPLE
) {
    val samplingRateInt: Int = if (samplingRate > 100F) {
        100
    } else if (samplingRate < 0F) {
        0
    } else {
        samplingRate.roundToInt()
    }
}