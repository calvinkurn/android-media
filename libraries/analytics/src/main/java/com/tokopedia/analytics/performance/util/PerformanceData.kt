package com.tokopedia.analytics.performance.util

class PerformanceData(allFramesTag: String, jankyFramesTag: String, jankyFramesPercentageTag: String) {
    var allFramesTag = ""
    var jankyFramesTag = ""
    var jankyFramesPercentageTag = ""
    var allFrames = 0
    var jankyFrames = 0

    val jankyFramePercentage: Int
        get() = if (this.allFrames == 0) 0 else (this.jankyFrames.toFloat() / this.allFrames.toFloat() * 100).toInt()

    init {
        this.allFramesTag = allFramesTag
        this.jankyFramesTag = jankyFramesTag
        this.jankyFramesPercentageTag = jankyFramesPercentageTag
    }

    fun incrementAllFrames() {
        this.allFrames++
    }

    fun incremenetJankyFrames() {
        this.jankyFrames++
    }
}
