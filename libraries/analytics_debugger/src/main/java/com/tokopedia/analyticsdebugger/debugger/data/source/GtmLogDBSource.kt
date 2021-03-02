package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context
import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Emitter
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author okasurya on 5/16/18.
 */
class GtmLogDBSource @Inject
constructor(context: Context) {

    private val gtmLogDao: GtmLogDao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            gtmLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(data: AnalyticsLogData): Observable<Boolean> {
        return Observable.just(data).map { analyticsLogData ->
            val gtmLogDB = GtmLogDB(
                    data = analyticsLogData.data,
                    name = analyticsLogData.name,
                    timestamp = Date().time,
                    source =  analyticsLogData.source
            )
            gtmLogDao.insertAll(gtmLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<GtmLogDB>> {
        return Observable.just(params).map { params1 ->
            val page: Int

            if (params1.containsKey(AnalyticsDebuggerConst.PAGE))
                page = params1[AnalyticsDebuggerConst.PAGE] as Int
            else
                page = 0

            var search = "%%"

            if (params1.containsKey(AnalyticsDebuggerConst.KEYWORD)) {
                val keyword = params1[AnalyticsDebuggerConst.KEYWORD] as String?
                search = "%$keyword%"
            }

            val offset = 20 * page
            gtmLogDao.getData(search, offset)
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
