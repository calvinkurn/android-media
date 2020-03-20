package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context

import com.tokopedia.analyticsdebugger.database.GtmErrorLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst

import java.util.HashMap

import javax.inject.Inject

import rx.Observable

/**
 * @author okasurya on 5/16/18.
 */
class GtmErrorLogDBSource @Inject
constructor(context: Context) {

    private val gtmErrorLogDao: GtmErrorLogDao

    init {
        gtmErrorLogDao = TkpdAnalyticsDatabase.getInstance(context).gtmErrorLogDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            gtmErrorLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(gtmErrorLogDB: GtmErrorLogDB): Observable<Boolean> {
        return Observable.fromCallable {
            gtmErrorLogDao.insertAll(gtmErrorLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<GtmErrorLogDB>> {
        return Observable.fromCallable<List<GtmErrorLogDB>> {
            val page: Int

            if (params.containsKey(AnalyticsDebuggerConst.PAGE))
                page = params[AnalyticsDebuggerConst.PAGE] as Int
            else
                page = 0

            var search = "%%"

            if (params.containsKey(AnalyticsDebuggerConst.KEYWORD)) {
                val keyword = params[AnalyticsDebuggerConst.KEYWORD] as String?
                search = "%$keyword%"
            }

            val offset = 100 * page
            gtmErrorLogDao.getData(search, offset) as List<GtmErrorLogDB>?
        }
    }

}
