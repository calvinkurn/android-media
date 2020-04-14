package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context
import com.tokopedia.analyticsdebugger.database.*
import rx.Observable
import javax.inject.Inject

class TopAdsVerificationNetworkSource @Inject
internal constructor(context: Context) {

    private val topAdsLogDao: TopAdsLogDao

    init {
        topAdsLogDao = TkpdAnalyticsDatabase.getInstance(context).topAdsLogDao()
    }

    fun appendVerificationStatus(inputList: List<TopAdsLogDB>): Observable<List<TopAdsLogDB>> {
        for (input in inputList) {
            if (input.eventStatus == STATUS_PENDING && System.currentTimeMillis() - input.timestamp > 60000) {
                input.eventStatus = STATUS_DATA_NOT_FOUND
                updateItem(input)
            } else if (input.eventStatus == STATUS_DATA_NOT_FOUND) {
                input.eventStatus = STATUS_NOT_MATCH
                updateItem(input)
            }  else if (input.eventStatus == STATUS_NOT_MATCH) {
                input.eventStatus = STATUS_MATCH
                updateItem(input)
            }
        }
        return Observable.just(inputList)
    }

    fun updateItem(item: TopAdsLogDB) {
        topAdsLogDao.updateItem(item)
    }
}
