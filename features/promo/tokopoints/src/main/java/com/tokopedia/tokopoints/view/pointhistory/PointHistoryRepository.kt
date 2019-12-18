package com.tokopedia.tokopoints.view.pointhistory

import androidx.lifecycle.MutableLiveData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.PointHistoryBase
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.model.TokoPointEntity
import com.tokopedia.tokopoints.view.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.util.HashMap
import javax.inject.Inject

@TokoPointScope
class PointHistoryRepository @Inject constructor(private val mGetPointHistory: GraphqlUseCase, private val mGetHomePageData: GraphqlUseCase, val queryMap: Map<String, String>) {

    suspend fun getPointsDetail(subscriber: Subscriber<GraphqlResponse>) = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        val request = GraphqlRequest(queryMap[CommonConstant.GQLQuery.TP_GQL_CURRENT_POINTS],
                TokoPointDetailEntity::class.java,
                variables, false)
        mGetPointHistory.clearRequest()
        mGetPointHistory.addRequest(request)
        mGetPointHistory.execute(subscriber)
    }

    suspend fun getPointList(currentPageIndex: Int, subscriber: Subscriber<GraphqlResponse>) = withContext(Dispatchers.IO) {
        mGetHomePageData.clearRequest()
        //Adding request for main query
        val variablesMain = HashMap<String, Any>()
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE] = currentPageIndex
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE_SIZE] = CommonConstant.PAGE_SIZE
        val graphqlRequestMain = GraphqlRequest(queryMap[CommonConstant.GQLQuery.TP_GQL_HISTORY_POINTS],
                PointHistoryBase::class.java,
                variablesMain, false)
        mGetHomePageData.addRequest(graphqlRequestMain)
        mGetHomePageData.execute(subscriber)
    }

    fun onCleared() {
        mGetPointHistory.unsubscribe()
        mGetHomePageData.unsubscribe()
    }
}