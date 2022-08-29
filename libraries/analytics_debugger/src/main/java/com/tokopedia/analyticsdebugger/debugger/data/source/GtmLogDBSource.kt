package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context
import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource
import com.tokopedia.analyticsdebugger.database.CassavaDatabase
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Emitter
import rx.Observable
import javax.inject.Inject

/**
 * @author okasurya on 5/16/18.
 */
class GtmLogDBSource @Inject
constructor(context: Context) {

    private val gtmLogDao: GtmLogDao = CassavaDatabase.getInstance(context).cassavaDao()

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            gtmLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun getAllLogs(): Observable<List<GtmLogDB>> {
        return Observable.create({ emitter ->
            val results = gtmLogDao.getAll()
            emitter.onNext(results)
        }, Emitter.BackpressureMode.NONE)
    }

    suspend fun getLogs(@AnalyticsSource analyticsSource: String = AnalyticsSource.ALL): List<GtmLogDB> {
        return withContext(Dispatchers.IO) {
            when (analyticsSource) {
                AnalyticsSource.ALL -> gtmLogDao.getAll()
                else -> gtmLogDao.getAll(analyticsSource)
            }

        }
    }

}
