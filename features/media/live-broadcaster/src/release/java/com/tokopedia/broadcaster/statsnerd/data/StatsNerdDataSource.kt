package com.tokopedia.broadcaster.statsnerd.data

import android.content.Context
import com.tokopedia.broadcaster.statsnerd.data.entity.StatsNerdLog

class StatsNerdDataSource {

    fun logChucker(log: StatsNerdLog) {}

    companion object {
        fun instance(context: Context): StatsNerdDataSource {
            return StatsNerdDataSource()
        }
    }

}