package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context

import com.tokopedia.analyticsdebugger.database.IrisSaveLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst

import java.util.HashMap

import javax.inject.Inject

import rx.Observable

class IrisSaveLogDBSource @Inject
constructor(context: Context) {

    private val irisLogSaveDao: IrisLogSaveDao

    init {
        irisLogSaveDao = TkpdAnalyticsDatabase.getInstance(context).irisLogSaveDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            irisLogSaveDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    fun insertAll(irisSaveLogDB: IrisSaveLogDB): Observable<Boolean> {
        return Observable.fromCallable {
            irisLogSaveDao.insertAll(irisSaveLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<IrisSaveLogDB>> {
        return Observable.fromCallable<List<IrisSaveLogDB>> {
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
            irisLogSaveDao.getData(search, offset) as List<IrisSaveLogDB>?
        }
    }

    fun count (): Int {
        return irisLogSaveDao.count
    }

}
