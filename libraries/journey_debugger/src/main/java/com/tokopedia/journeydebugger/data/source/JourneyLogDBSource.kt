package com.tokopedia.journeydebugger.data.source

import android.content.Context
import com.tokopedia.journeydebugger.JourneyDebuggerConst
import com.tokopedia.journeydebugger.database.JourneyDatabase
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
        journeyLogDao = JourneyDatabase.getInstance(context).journeyLogDao()
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
            applinkLogDB.journey = data.journey
            applinkLogDB.timestamp = Date().time

            journeyLogDao.insertAll(applinkLogDB)
            true
        }
    }

    fun getData(params: HashMap<String, Any>): Observable<List<JourneyLogDB>> {
        return Observable.just(params).map { params1 ->
            val page: Int

            if (params1.containsKey(JourneyDebuggerConst.PAGE))
                page = params1[JourneyDebuggerConst.PAGE] as Int
            else
                page = 0

            var search = "%%"

            if (params1.containsKey(JourneyDebuggerConst.KEYWORD)) {
                val keyword = params1[JourneyDebuggerConst.KEYWORD] as String?
                search = "%$keyword%"
            }

            val offset = 20 * page
            journeyLogDao.getData(search, offset)
        }
    }
}
