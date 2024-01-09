package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.callback
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.view.FrameMetrics
import android.view.Window
import timber.log.Timber

class FrameViewCallbackStrategy() : ViewCallbackStrategy {

    var listener: Window.OnFrameMetricsAvailableListener? = null
    var onRender: (() -> Unit) = {}
    override fun startObserving(context: Context) {
        startFrameMetrics(context)
    }

    override fun stopObserving(context: Context) {
        stopFrameMetrics(context)
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun startFrameMetrics(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val listener: Window.OnFrameMetricsAvailableListener = object : Window.OnFrameMetricsAvailableListener {
                override fun onFrameMetricsAvailable(window: Window, frameMetrics: FrameMetrics, dropCountSinceLastInvocation: Int) {
                    onRender.invoke()
                }
            }
            val window = (context as? Activity)?.window
            window?.addOnFrameMetricsAvailableListener(listener, Handler())
            this.listener = listener
        } else {
            Timber.d("FrameMetrics can work only with Android SDK 24 (Nougat) and higher")
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun stopFrameMetrics(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listener?.let {
                val window = (context as? Activity)?.window
                try {
                    window?.removeOnFrameMetricsAvailableListener(it)
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun registerCallback(onRender: () -> Unit) {
        this.onRender = onRender
    }
}
