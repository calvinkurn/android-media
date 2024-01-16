package com.tokopedia.analyticsdebugger.debugger.data.source

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

import com.tokopedia.analyticsdebugger.database.IrisSendLogDB
import com.tokopedia.analyticsdebugger.database.ServerLogDB
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.kotlin.extensions.clear

import java.util.HashMap

import javax.inject.Inject

import rx.Observable

class ServerLogDBSource @Inject
constructor(context: Context) {

    private val serverLogDao: ServerLogDao

    init {
        serverLogDao = TkpdAnalyticsDatabase.getInstance(context).serverLogDao()
    }

    fun deleteAll(): Observable<Boolean> {
        return Observable.unsafeCreate { subscriber ->
            serverLogDao.deleteAll()
            subscriber.onNext(true)
        }
    }

    @SuppressLint("ApplySharedPref")
    fun insertAll(serverLogDB: ServerLogDB): Observable<Boolean> {
        return Observable.fromCallable {
            serverLogDao.insertAll(serverLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<ServerLogDB>> {
        return Observable.fromCallable<List<ServerLogDB>> {

            val page: Int = if (params.containsKey(AnalyticsDebuggerConst.PAGE))
                params[AnalyticsDebuggerConst.PAGE] as Int
            else
                0

            var search = "%%"

            if (params.containsKey(AnalyticsDebuggerConst.KEYWORD)) {
                val keyword = params[AnalyticsDebuggerConst.KEYWORD] as String?
                search = "%$keyword%"
            }

            val offset = 100 * page
            serverLogDao.getData(search, offset) as List<ServerLogDB>?
        }
    }
}
