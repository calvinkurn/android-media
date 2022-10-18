package com.tokopedia.journeydebugger.data.source

import android.content.Context
import com.tokopedia.journeydebugger.database.JourneyLogDB
import com.tokopedia.journeydebugger.domain.model.JourneyLogModel

import java.util.Date
import java.util.HashMap

import javax.inject.Inject

import rx.Observable

class JourneyLogDBSource @Inject
constructor(context: Context) {

    private val journeyLogDao: JourneyLogDao

    init {
        journeyLogDao = TkpdAnalyticsDatabase.getInstance(context).applinkLogDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            journeyLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(data: JourneyLogModel): Observable<Boolean> {
        return Observable.just(data).map { analyticsLogData ->
            val applinkLogDB = JourneyLogDB()
            applinkLogDB.applink = data.applink
            applinkLogDB.traces = data.traces
            applinkLogDB.timestamp = Date().time

            journeyLogDao.insertAll(applinkLogDB)
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
            journeyLogDao.getData(search, offset)
        }
    }
}
