package com.tokopedia.analytics.performance.fpi

class FpiPerformanceData(
    var allFrames: Int = 0,
    var jankyFrames: Int = 0,
    var allTotalDurationMs: Double = 0.0,
    var totalDurationMs: Double = 0.0
) {

    companion object {
        private const val ONE_SECOND = 1000L // 1 second in millisecond
        private const val PERCENTAGE = 100
    }

    val jankyFramePercentage: Int
        get() = if (this.allFrames == 0) 0 else (this.jankyFrames.toFloat() / this.allFrames.toFloat() * PERCENTAGE).toInt()

    val framePerformanceIndex: Int
        get() = if (this.allFrames == 0) 0 else PERCENTAGE - jankyFramePercentage

    val fps: Double
        get() {
            if (allFrames == 0) return 0.0
            val averageFrameDuration = allTotalDurationMs.div(allFrames)
            return ONE_SECOND.div(averageFrameDuration)
        }

    fun incrementAllFrames() {
        this.allFrames++
    }

    fun incrementJankyFrames() {
        this.jankyFrames++
    }

    fun incrementDuration(duration: Double) {
        this.allTotalDurationMs += duration
        this.totalDurationMs = duration
    }

    fun reset() {
        allFrames = 0
        jankyFrames = 0
        allTotalDurationMs = 0.0
        totalDurationMs = 0.0
    }
}
