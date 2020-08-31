package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context

import com.tokopedia.analyticsdebugger.database.ApplinkLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel

import java.util.Date
import java.util.HashMap

import javax.inject.Inject

import rx.Observable

class ApplinkLogDBSource @Inject
constructor(context: Context) {

    private val applinkLogDao: ApplinkLogDao

    init {
        applinkLogDao = TkpdAnalyticsDatabase.getInstance(context).applinkLogDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            applinkLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(data: ApplinkLogModel): Observable<Boolean> {
        return Observable.just(data).map { analyticsLogData ->
            val applinkLogDB = ApplinkLogDB()
            applinkLogDB.applink = data.applink
            applinkLogDB.traces = data.traces
            applinkLogDB.timestamp = Date().time

            applinkLogDao.insertAll(applinkLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<ApplinkLogDB>> {
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
            applinkLogDao.getData(search, offset)
        }
    }
}
