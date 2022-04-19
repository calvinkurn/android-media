package com.tokopedia.analyticsdebugger.debugger.data.source

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

import com.tokopedia.analyticsdebugger.database.IrisSendLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.kotlin.extensions.clear

import java.util.HashMap

import javax.inject.Inject

import rx.Observable

class IrisSendLogDBSource @Inject
constructor(context: Context) {

    private val irisLogSendDao: IrisLogSendDao
    private val sp: SharedPreferences

    companion object {
        const val IRIS_COUNT_DEBUG_PREF = "iris_count_debug_pref"
        const val ROW_COUNT = "row_count"
    }

    init {
        irisLogSendDao = TkpdAnalyticsDatabase.getInstance(context).irisLogSendDao()
        sp = context.getSharedPreferences(IRIS_COUNT_DEBUG_PREF, Context.MODE_PRIVATE)
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            irisLogSendDao.deleteAll()
            sp.clear()
            subscriber.onNext(true)
        }
    }

    @SuppressLint("ApplySharedPref")
    fun insertAll(irisSendLogDB: IrisSendLogDB, rowCount: Int): Observable<Boolean> {
        return Observable.fromCallable {
            irisLogSendDao.insertAll(irisSendLogDB)
            val currentCount = sp.getInt(ROW_COUNT, 0)
            sp.edit().putInt(ROW_COUNT, currentCount + rowCount).commit()
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

    fun getCount(): Int {
        return sp.getInt(ROW_COUNT, 0)
    }
}
