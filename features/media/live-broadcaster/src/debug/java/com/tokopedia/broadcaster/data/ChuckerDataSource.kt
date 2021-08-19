package com.tokopedia.broadcaster.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.tokopedia.broadcaster.chucker.data.BroadcasterChuckerDb
import com.tokopedia.broadcaster.chucker.data.entity.ChuckerLog
import com.tokopedia.broadcaster.chucker.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.chucker.data.repository.ChuckerLogRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ChuckerDataSource(
    private val context: Context
) : ChuckerLogRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private val dataSource by lazy {
        val dbBuilder = BroadcasterChuckerDb.instance(context)
        val dbDao = dbBuilder.chuckerDao()
        ChuckerLogRepositoryImpl(dbDao)
    }

    override fun chuckers(): LiveData<List<ChuckerLog>> {
        return dataSource.chuckers()
    }

    override fun logChucker(log: ChuckerLog) {
        launch { dataSource.logChucker(log) }
    }

    override fun delete() {
        return dataSource.delete()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var instance: ChuckerDataSource? = null

        fun instance(context: Context): ChuckerDataSource {
            return instance ?: synchronized(ChuckerDataSource::class) {
                ChuckerDataSource(context).also {
                    instance = it
                }
            }
        }
    }

}