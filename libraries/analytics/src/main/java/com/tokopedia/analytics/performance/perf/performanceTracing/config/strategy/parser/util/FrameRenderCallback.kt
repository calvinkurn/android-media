package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.util

import android.view.Choreographer

class FrameRenderCallback(val doFrame: (frameTimeNanos: Long) -> Unit) {

    private var callback: (Long) -> Unit = {}

    fun start() {
        callback = {
            doFrame(it)
            Choreographer.getInstance().postFrameCallback(callback)
        }
        Choreographer.getInstance().postFrameCallback(callback)
    }

    fun stop() {
        Choreographer.getInstance().removeFrameCallback(callback)
        callback = {}
    }
}
