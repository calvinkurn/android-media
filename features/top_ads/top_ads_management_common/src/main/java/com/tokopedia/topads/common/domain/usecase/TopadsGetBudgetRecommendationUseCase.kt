package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.TopadsGetBudgetRecommendationResponse
import com.tokopedia.topads.common.domain.query.TopadsGetBudgetRecommendationQuery
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class TopadsGetBudgetRecommendationUseCase @Inject constructor(
    private val userSession: UserSession,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopadsGetBudgetRecommendationResponse>(graphqlRepository){

    init {
        setTypeClass(TopadsGetBudgetRecommendationResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(TopadsGetBudgetRecommendationQuery)
    }

    fun setParams(source: String, requestType: String){
        val params = mutableMapOf(
            ParamObject.SHOP_ID to userSession.shopId,
            ParamObject.REQUEST_TYPE to requestType,
            ParamObject.SOURCE to source
        )
        setRequestParams(params)
    }
}
