package com.tokopedia.broadcaster.log.data.repository

import com.tokopedia.broadcaster.log.data.dao.NetworkLogDao
import com.tokopedia.broadcaster.log.data.entity.NetworkLog

interface ChuckerLogRepository {
    fun chuckers(): List<NetworkLog>
    fun logChucker(log: NetworkLog)
    fun delete()
}

open class ChuckerLogRepositoryImpl constructor(
    private val networkLogDao: NetworkLogDao
) : ChuckerLogRepository {

    override fun chuckers(): List<NetworkLog> {
        return networkLogDao.chuckers()
    }

    override fun logChucker(log: NetworkLog) {
        return networkLogDao.logChucker(log)
    }

    override fun delete() {
        return networkLogDao.delete()
    }

}