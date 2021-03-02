package com.tokopedia.analyticsdebugger.debugger.data.repository

import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

const val PER_PAGE = 50

class GtmRepo(val dao: GtmLogDao) {

    private val inMemoryCache = mutableListOf<GtmLogDB>()
    private var searchIndex = 0
    private var hasNext = true
    private var q = ""

    suspend fun search(query: String): List<GtmLogDB> {
        searchIndex = 0
        hasNext = true
        q = query
        inMemoryCache.clear()
        return queryAndSave()
    }

    suspend fun requestMore(): List<GtmLogDB> {
        if (!hasNext) return inMemoryCache
        searchIndex += PER_PAGE
        return queryAndSave()
    }

    suspend fun getLogs(@AnalyticsSource analyticsSource: String = AnalyticsSource.ALL): List<GtmLogDB> {
        return withContext(Dispatchers.IO) {
            dao.getAll(analyticsSource)
        }
    }

    private suspend fun queryAndSave(): List<GtmLogDB> = withContext(Dispatchers.IO) {
        val i = dao.getData("%$q%", searchIndex)
        Timber.d("Requesting $q from index $searchIndex got ${i.size}")
        if (i.isEmpty()) hasNext = false
        else inMemoryCache.addAll(i)
        inMemoryCache
    }

    suspend fun delete() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }
}