package com.tokopedia.broadcaster.log.data

import android.content.Context
import com.tokopedia.broadcaster.log.data.entity.NetworkLog

class NetworkLogDataSource {

    fun logChucker(log: NetworkLog) {}

    companion object {
        fun instance(context: Context): NetworkLogDataSource {
            return NetworkLogDataSource()
        }
    }

}