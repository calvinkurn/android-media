package com.tokopedia.tokopoints.view.pointhistory

import androidx.lifecycle.MutableLiveData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
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
class PointHistoryRepository @Inject constructor(private val repository: GraphqlRepository,private val queryMap: Map<String, String>) {

    private val cacheStrategy by lazy {
        GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
    }
    suspend fun getPointsDetail() = withContext(Dispatchers.IO) {
        val variables = HashMap<String, Any>()
        val request = GraphqlRequest(queryMap[CommonConstant.GQLQuery.TP_GQL_CURRENT_POINTS],
                TokoPointDetailEntity::class.java,
                variables, false)
        repository.getReseponse(listOf(request), cacheStrategy)

    }

    suspend fun getPointList(currentPageIndex: Int) = withContext(Dispatchers.IO) {
        //Adding request for main query
        val variablesMain = HashMap<String, Any>()
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE] = currentPageIndex
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE_SIZE] = CommonConstant.PAGE_SIZE
        val graphqlRequestMain = GraphqlRequest(queryMap[CommonConstant.GQLQuery.TP_GQL_HISTORY_POINTS],
                PointHistoryBase::class.java,
                variablesMain, false)
        repository.getReseponse(listOf(graphqlRequestMain), cacheStrategy)
    }
}