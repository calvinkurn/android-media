package com.tokopedia.analytics.performance.fpi

class FpiPerformanceData(var allFrames: Int = 0, var jankyFrames: Int = 0) {
    var allFramesTag = "all_frames"
    var jankyFramesTag = "janky_frames"
    var jankyFramesPercentageTag = "janky_frames_percentage"

    val jankyFramePercentage: Int
        get() = if (this.allFrames == 0) 0 else (this.jankyFrames.toFloat() / this.allFrames.toFloat() * 100).toInt()

    val framePerformanceIndex: Int
        get() = if (this.allFrames == 0) 0 else 100-jankyFramePercentage

    fun incrementAllFrames() {
        this.allFrames++
    }

    fun incremenetJankyFrames() {
        this.jankyFrames++
    }
}
