package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.database.FpmLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel

import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.Date
import java.util.HashMap

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class FpmLogDBSource @Inject
constructor(context: Context) {

    private val fpmLogDao: FpmLogDao

    val allData: Observable<List<FpmLogDB>>
        get() = Observable.just(1).map { number -> fpmLogDao.allData }

    init {
        fpmLogDao = TkpdAnalyticsDatabase.getInstance(context).fpmLogDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            fpmLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(data: PerformanceLogModel): Observable<Boolean> {
        return Observable.just(data).map { analyticsLogData ->
            val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
            val fpmLogDB = FpmLogDB()
            fpmLogDB.traceName = data.traceName
            fpmLogDB.duration = data.endTime - data.startTime
            try {
                fpmLogDB.attributes = URLDecoder.decode(gson.toJson(data.getAttributes()), "UTF-8")
                fpmLogDB.metrics = URLDecoder.decode(gson.toJson(data.getMetrics()), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                fpmLogDB.attributes = "UnsupportedEncodingException"
                fpmLogDB.metrics = "UnsupportedEncodingException"
                e.printStackTrace()
            }

            fpmLogDB.timestamp = Date().time
            fpmLogDao.insertAll(fpmLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<FpmLogDB>> {
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
            fpmLogDao.getData(search, offset)
        }
    }
}
