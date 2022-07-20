package com.tokopedia.broadcaster.log.data

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.broadcaster.log.data.entity.NetworkLog
import com.tokopedia.broadcaster.log.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.log.data.repository.ChuckerLogRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NetworkLogDataSource(
    private val context: Context
) : ChuckerLogRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private val dataSource by lazy {
        val dbBuilder = BroadcasterChuckerDb.instance(context)
        val dbDao = dbBuilder.chuckerDao()
        ChuckerLogRepositoryImpl(dbDao)
    }

    override fun chuckers(): List<NetworkLog> {
        return dataSource.chuckers()
    }

    override fun logChucker(log: NetworkLog) {
        launch { dataSource.logChucker(log) }
    }

    override fun delete() {
        return dataSource.delete()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: NetworkLogDataSource? = null

        fun instance(context: Context): NetworkLogDataSource {
            return instance ?: synchronized(NetworkLogDataSource::class) {
                NetworkLogDataSource(context).also {
                    instance = it
                }
            }
        }
    }

}