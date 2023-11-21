package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.util.FrameRenderCallback

class FrameViewCallbackStrategy() : ViewCallbackStrategy {

    var frameRenderCallback: FrameRenderCallback? = null
    var onRender: (() -> Unit) = {}
    override fun startObserving() {
        frameRenderCallback = FrameRenderCallback {
            onRender.invoke()
        }
        frameRenderCallback?.start()
    }

    override fun stopObserving() {
        frameRenderCallback?.stop()
    }

    override fun registerCallback(onRender: () -> Unit) {
        this.onRender = onRender
    }
}
