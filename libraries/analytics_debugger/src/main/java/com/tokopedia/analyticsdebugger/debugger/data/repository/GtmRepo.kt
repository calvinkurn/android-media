package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GtmRepo(val dao: GtmLogDao) {

    suspend fun getLogs(@AnalyticsSource analyticsSource: String = AnalyticsSource.ALL): List<GtmLogDB> {
        return withContext(Dispatchers.IO) {
            when (analyticsSource) {
                AnalyticsSource.ALL -> dao.getAll()
                else -> dao.getAll(analyticsSource)
            }

        }
    }

    suspend fun delete() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }
}