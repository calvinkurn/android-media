package com.tokopedia.analyticsdebugger.debugger.data.source

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.database.*
import com.tokopedia.analyticsdebugger.debugger.domain.model.TopAdsVerificationData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Observable
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class TopAdsVerificationNetworkSource @Inject
internal constructor(val context: Context, val graphqlUseCase: GraphqlUseCase) {

    var PENDING_DURATION_MS = 5 * 60 * 1000 // 5 minutes

    private val topAdsLogDao: TopAdsLogDao

    init {
        topAdsLogDao = TkpdAnalyticsDatabase.getInstance(context).topAdsLogDao()
    }

    fun appendVerificationStatus(logDBList: List<TopAdsLogDB>): Observable<List<TopAdsLogDB>> {
        return Observable.create { subscriber ->
            val urlCheckList = ArrayList<String>()
            val urlCheckMap = HashMap<String, TopAdsLogDB>()

            for (item in logDBList) {
                if (item.eventStatus == STATUS_PENDING && System.currentTimeMillis() - item.timestamp > PENDING_DURATION_MS) {
                    urlCheckList.add(item.url.trim())
                    urlCheckMap[item.url.trim()] = item
                }
            }

            verifyAndUpdateItems(urlCheckList, urlCheckMap)
            subscriber.onNext(logDBList)
            subscriber.onCompleted()
        }
    }

    fun verifyAndUpdateItems(urlCheckList: List<String>, urlCheckMap: HashMap<String, TopAdsLogDB>) {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_topads_verificator),
                TopAdsVerificationData::class.java
        )
        val variables = createParametersForQuery(urlCheckList)
        graphqlRequest.setVariables(variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        val response = graphqlUseCase.getData(RequestParams.EMPTY)
                .getData<TopAdsVerificationData>(TopAdsVerificationData::class.java)
        val resultList = response.topadsVerifyClicksViews.data
        for (result in resultList) {
            val item = urlCheckMap[result.url.trim()]
            item?.let {
                if (result.status) {
                    if (result.type == it.eventType) {
                        it.eventStatus = STATUS_MATCH
                        updateItem(it)
                    } else {
                        it.eventStatus = STATUS_NOT_MATCH
                        updateItem(it)
                    }
                } else {
                    it.eventStatus = STATUS_DATA_NOT_FOUND
                    updateItem(it)
                }
            }
        }
    }

    private fun createParametersForQuery(urlList: List<String>): Map<String, Any> {
        val variables: MutableMap<String, Any> = HashMap()
        variables["url"] = urlList.joinToString()
        return variables
    }

    private fun updateItem(item: TopAdsLogDB) {
        topAdsLogDao.updateItem(item)
    }
}
