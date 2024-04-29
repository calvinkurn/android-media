package com.tokopedia.analytics.performance.perf

import androidx.recyclerview.widget.RecyclerView
import com.bytedance.apm.trace.fps.FpsTracer

fun RecyclerView.bindFpsTracer(sceneName: String) {
    val fpsTracer = FpsTracer(sceneName, true)

    this.addOnScrollListener(object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                fpsTracer.start();
            }else {
                fpsTracer.stop();
            }
        }
    })
}
