package com.tokopedia.broadcaster.statsnerd.data.repository

import com.tokopedia.broadcaster.statsnerd.data.dao.StatsNerdDao
import com.tokopedia.broadcaster.statsnerd.data.entity.StatsNerdLog

interface ChuckerLogRepository {
    fun chuckers(): List<StatsNerdLog>
    fun logChucker(log: StatsNerdLog)
    fun delete()
}

open class ChuckerLogRepositoryImpl constructor(
    private val statsNerdDao: StatsNerdDao
) : ChuckerLogRepository {

    override fun chuckers(): List<StatsNerdLog> {
        return statsNerdDao.chuckers()
    }

    override fun logChucker(log: StatsNerdLog) {
        return statsNerdDao.logChucker(log)
    }

    override fun delete() {
        return statsNerdDao.delete()
    }

}