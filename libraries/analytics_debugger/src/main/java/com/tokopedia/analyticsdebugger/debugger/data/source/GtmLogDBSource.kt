package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context

import com.tokopedia.analyticsdebugger.database.GtmLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData

import java.util.Date
import java.util.HashMap

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class GtmLogDBSource @Inject
constructor(context: Context) {

    private val gtmLogDao: GtmLogDao

    init {
        gtmLogDao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            gtmLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(data: AnalyticsLogData): Observable<Boolean> {
        return Observable.just(data).map { analyticsLogData ->
            val gtmLogDB = GtmLogDB()
            gtmLogDB.name = analyticsLogData.name
            gtmLogDB.category = analyticsLogData.category
            gtmLogDB.data = analyticsLogData.data
            gtmLogDB.timestamp = Date().time
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

}
