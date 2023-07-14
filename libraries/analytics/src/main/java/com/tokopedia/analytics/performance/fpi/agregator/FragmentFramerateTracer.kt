package com.tokopedia.analytics.performance.fpi.agregator

import android.app.Activity
import androidx.core.app.FrameMetricsAggregator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

class FragmentFramerateTracer(
    private val scope: LifecycleCoroutineScope, // from depencency : lifecycle-runtime-ktx
    private val tag: String = "SampleFragmentName"
) : CoroutineScope by scope {

    // a mutex lock for thread safe
    private val mutex = Mutex()

    private val aggregator: FrameMetricsAggregator = FrameMetricsAggregator()

    private var totalFrames = 0L
    private var slowFrames = 0L
    private var frozenFrames = 0L

    //, callback: suspend FragmentFramerateTracer.() -> Unit
    fun start(context: Activity) = launch {
        Timber.tag(tag).d("/tracer/start")
        mutex.withLock {
            aggregator.add(context)
        }
    }

    fun stop() = launch {
        Timber.tag(tag).d("/tracer/stop")
        mutex.withLock {
            val data = aggregator.stop() ?: return@launch
            totalFrames = 0L
            slowFrames = 0L
            frozenFrames = 0L

            // the K-V of this sparse array means there're V frames has duration K ms
            data[FrameMetricsAggregator.TOTAL_INDEX].let { distributions ->
                for (i in 0 until distributions.size()) {
                    val duration = distributions.keyAt(i)
                    val frameCount = distributions.valueAt(i)
                    totalFrames += frameCount
                    if (duration > 16)
                        slowFrames += frameCount
                    if (duration > 700)
                        frozenFrames += frameCount
                }
            }
            aggregator.reset()
            val frameRateData = FrameRateData(totalFrames, slowFrames, frozenFrames)
            Timber.tag(tag).d("\n/tracer/result : \n$frameRateData")
        }
    }
}

/**
 * this is an extension function for our BaseFragments
 */
fun BaseFpiAggregatorFragment.registerTracer() {
    val tag = "Tracer${this::class.java.simpleName}"
    var tracer: FragmentFramerateTracer? = null

    val onHiddenChangedListener = object : OnHiddenChangedListener {
        override fun onHidden(hidden: Boolean) {
            if (!hidden) {
                // this fragment is showing
                if (tracer == null) {
                    // if tracer is null, set one
                    tracer = FragmentFramerateTracer(lifecycleScope, tag)
                }
                tracer?.start(context as Activity)
            } else {
                // this fragment is not visible
                tracer?.stop()
            }
        }
    }

    lifecycle.addObserver(
        object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        tracer = FragmentFramerateTracer(lifecycleScope, tag)
                        tracer?.start(context as Activity)
                        addOnHiddenChangedListener(onHiddenChangedListener)
                    }
                    Lifecycle.Event.ON_STOP -> {
                        tracer?.stop()
                        tracer = null
                        removeOnHiddenChangedListener(onHiddenChangedListener)
                    }
                    Lifecycle.Event.ON_DESTROY -> {
                        lifecycle.removeObserver(this)
                    }
                    else -> { /* handle those else */
                    }
                }
            }
        }
    )
}
