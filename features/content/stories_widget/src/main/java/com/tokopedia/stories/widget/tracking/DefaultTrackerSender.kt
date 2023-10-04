package com.tokopedia.stories.widget.tracking

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by kenny.hadisaputra on 03/10/23
 */
class DefaultTrackerSender(
    lifecycleOwner: LifecycleOwner,
    private val trackingQueue: TrackingQueue,
) : StoriesWidgetTracker.Sender {

    init {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> sendAll()
                    else -> {}
                }
            }
        })
    }

    private val impressionMap = mutableMapOf<String, MutableSet<String>>()

    override fun sendTracker(data: StoriesWidgetTracker.Data) {
        if (data == StoriesWidgetTracker.Data.Empty) return
        Log.d("Tracker Sender", "Check: $data")
        val ids = impressionMap[data.eventAction] ?: mutableSetOf()
        if (ids.contains(data.shopId) && data.isImpression) return
        ids.add(data.shopId)
        impressionMap[data.eventAction] = ids

        if (data.map is HashMap<String, Any>) {
            Log.d("Tracker Sender", "Queue: $data")
            trackingQueue.putEETracking(data.map)
        }
    }

    override fun reset() {
        impressionMap.clear()
    }

    private fun sendAll() {
        Log.d("Tracker Sender", "Send All")
        trackingQueue.sendAll()
    }
}
