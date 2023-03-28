package com.tokopedia.iris.worker

import android.content.Context
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.util.Cache

object IrisServiceCore {
    suspend fun run(context: Context, maxRow: Int) {
        val cache = Cache(context)
        val trackingRepository = TrackingRepository.getInstance(context)
        var totalDataSent = 0
        if (cache.isEnabled()) {
            totalDataSent = trackingRepository.sendRemainingEvent(maxRow)
        }
        if (cache.isPerformanceEnabled()) {
            totalDataSent += trackingRepository.sendRemainingPerfEvent(maxRow)
        }
        // if successful data sent is 0, turn off the timer
        if (totalDataSent == 0) {
            IrisAnalytics.getInstance(context).setAlarm(false, force = true)
        } else {
            IrisAnalytics.getInstance(context).setAlarm(true, force = false)
        }
    }

}