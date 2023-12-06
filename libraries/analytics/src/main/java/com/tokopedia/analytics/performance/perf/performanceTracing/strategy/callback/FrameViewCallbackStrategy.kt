package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback
import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.util.FrameRenderCallback

class FrameViewCallbackStrategy() : ViewCallbackStrategy {

    var frameRenderCallback: FrameRenderCallback? = null
    var onRender: (() -> Unit) = {}
    override fun startObserving(view: View) {
        frameRenderCallback = FrameRenderCallback {
            onRender.invoke()
        }
        frameRenderCallback?.start()
    }

    override fun stopObserving(view: View) {
        frameRenderCallback?.stop()
    }

    override fun registerCallback(onRender: () -> Unit) {
        this.onRender = onRender
    }
}
