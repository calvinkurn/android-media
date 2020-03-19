package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context

import com.tokopedia.analyticsdebugger.database.IrisSendLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst

import java.util.HashMap

import javax.inject.Inject

import rx.Observable

class IrisSendLogDBSource @Inject
constructor(context: Context) {

    private val irisLogSendDao: IrisLogSendDao

    init {
        irisLogSendDao = TkpdAnalyticsDatabase.getInstance(context).irisLogSendDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            irisLogSendDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(irisSendLogDB: IrisSendLogDB): Observable<Boolean> {
        return Observable.fromCallable {
            irisLogSendDao.insertAll(irisSendLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<IrisSendLogDB>> {
        return Observable.fromCallable<List<IrisSendLogDB>> {
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
            irisLogSendDao.getData(search, offset) as List<IrisSendLogDB>?
        }
    }

}
