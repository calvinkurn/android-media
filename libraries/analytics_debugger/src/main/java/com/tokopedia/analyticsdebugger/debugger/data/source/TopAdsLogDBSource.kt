package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context
import com.tokopedia.analyticsdebugger.database.STATUS_DATA_NOT_FOUND
import com.tokopedia.analyticsdebugger.database.STATUS_PENDING
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.database.TopAdsLogDB
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst
import com.tokopedia.analyticsdebugger.debugger.domain.model.TopAdsLogModel
import rx.Observable
import java.util.*
import javax.inject.Inject

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
            topAdsLogDB.productId = data.productId
            topAdsLogDB.productName = data.productName
            topAdsLogDB.imageUrl = data.imageUrl
            topAdsLogDB.componentName = data.componentName
            topAdsLogDB.timestamp = Date().time
            if (data.url.isNotBlank()) {
                topAdsLogDB.eventStatus = STATUS_PENDING
            } else {
                topAdsLogDB.eventStatus = STATUS_DATA_NOT_FOUND
            }
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

            if (params1.containsKey(AnalyticsDebuggerConst.ENVIRONMENT) &&
                    params1.get(AnalyticsDebuggerConst.ENVIRONMENT) == AnalyticsDebuggerConst.ENVIRONMENT_TEST) {
                topAdsLogDao.getAllData(search)
            } else {
                val offset = 20 * page
                topAdsLogDao.getData(search, offset)
            }
        }
    }
}
