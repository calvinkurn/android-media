package com.tokopedia.broadcaster.statsnerd.data

import android.annotation.SuppressLint
import android.content.Context
import com.tokopedia.broadcaster.statsnerd.data.entity.StatsNerdLog
import com.tokopedia.broadcaster.statsnerd.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.statsnerd.data.repository.ChuckerLogRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StatsNerdDataSource(
    private val context: Context
) : ChuckerLogRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private val dataSource by lazy {
        val dbBuilder = BroadcasterChuckerDb.instance(context)
        val dbDao = dbBuilder.chuckerDao()
        ChuckerLogRepositoryImpl(dbDao)
    }

    override fun chuckers(): List<StatsNerdLog> {
        return dataSource.chuckers()
    }

    override fun logChucker(log: StatsNerdLog) {
        launch { dataSource.logChucker(log) }
    }

    override fun delete() {
        return dataSource.delete()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: StatsNerdDataSource? = null

        fun instance(context: Context): StatsNerdDataSource {
            return instance ?: synchronized(StatsNerdDataSource::class) {
                StatsNerdDataSource(context).also {
                    instance = it
                }
            }
        }
    }

}