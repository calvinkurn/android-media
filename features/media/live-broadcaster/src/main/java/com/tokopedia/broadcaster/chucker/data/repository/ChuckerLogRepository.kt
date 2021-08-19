package com.tokopedia.broadcaster.chucker.data.repository

import androidx.lifecycle.LiveData
import com.tokopedia.broadcaster.chucker.data.dao.ChuckerDao
import com.tokopedia.broadcaster.chucker.data.entity.ChuckerLog

interface ChuckerLogRepository {
    fun chuckers(): LiveData<List<ChuckerLog>>
    fun logChucker(log: ChuckerLog)
    fun delete()
}

open class ChuckerLogRepositoryImpl constructor(
    private val chuckerDao: ChuckerDao
) : ChuckerLogRepository {

    override fun chuckers(): LiveData<List<ChuckerLog>> {
        return chuckerDao.chuckers()
    }

    override fun logChucker(log: ChuckerLog) {
        return chuckerDao.logChucker(log)
    }

    override fun delete() {
        return chuckerDao.delete()
    }

}