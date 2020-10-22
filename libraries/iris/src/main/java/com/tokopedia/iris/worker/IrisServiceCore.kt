package com.tokopedia.iris.worker

import android.content.Context
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.util.Cache

object IrisServiceCore {
    suspend fun run(context: Context, maxRow: Int) {
        val cache = Cache(context)
        if (cache.isEnabled()) {
            val trackingRepository = TrackingRepository(context)
            val dataSize = trackingRepository.sendRemainingEvent(maxRow)
            if (dataSize == 0) {
                IrisAnalytics.getInstance(context).setAlarm(false, force = true)
            } else {
                IrisAnalytics.getInstance(context).setAlarm(true, force = false)
            }
        }
    }

}