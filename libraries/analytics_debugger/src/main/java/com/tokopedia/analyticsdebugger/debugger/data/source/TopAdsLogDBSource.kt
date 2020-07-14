package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.database.STATUS_PENDING
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel
import com.tokopedia.analyticsdebugger.debugger.domain.model.TopAdsLogModel

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.Date
import java.util.HashMap

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class TopAdsLogDBSource @Inject
constructor(context: Context) {

    private val topAdsLogDao: TopAdsLogDao

    init {
        topAdsLogDao = TkpdAnalyticsDatabase.getInstance(context).topAdsLogDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            topAdsLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(data: TopAdsLogModel): Observable<Boolean> {
        return Observable.just(data).map { analyticsLogData ->
            val topAdsLogDB = TopAdsLogDB()
            topAdsLogDB.url = data.url
            topAdsLogDB.eventType = data.eventType
            topAdsLogDB.sourceName = data.sourceName
            topAdsLogDB.timestamp = Date().time
            topAdsLogDB.eventStatus = STATUS_PENDING
            topAdsLogDao.insertAll(topAdsLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<TopAdsLogDB>> {
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
            topAdsLogDao.getData(search, offset)
        }
    }
}
