package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject.DATA
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.INSIGHT_SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_id
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION
import com.tokopedia.topads.dashboard.data.model.FinalAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.Keyword
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 22/7/20.
 */

class TopAdsEditKeywordUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<FinalAdResponse>(graphqlRepository) {

    fun setParam(groupId: String, query: String, data: List<MutationData>) {
        val variable: HashMap<String, Any> = HashMap()
        variable[SHOP_id] = userSession.shopId.toString()
        variable[GROUP_ID] = groupId
        variable[SOURCE] = INSIGHT_SOURCE
        variable[DATA] = data
        setRequestParams(variable)
        setGraphqlQuery(query)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (FinalAdResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(FinalAdResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}