package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.DeclineRechargeRecommendation
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by resakemal on 2020-03-09
 */

class DeclineRechargeRecommendationUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<DeclineRechargeRecommendation.Response>)
    : UseCase<DeclineRechargeRecommendation>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(DeclineRechargeRecommendation.Response::class.java)
    }

    companion object {
        const val PARAM_REQUEST = "request"
        const val PARAM_UUID = "UUID"
        const val PARAM_CONTENT_ID = "ContentID"
    }

    private var params: RequestParams = RequestParams.create()

    //region query
    private val query by lazy {
        val request = "\$request"

        """
            mutation declineWATFRecommendation(
              $request:rechargeDeclineAboveTheFoldRecommendationRequest!
            ){
              rechargeDeclineAboveTheFoldRecommendation(declineRequest:$request){
                isError: IsError
                message: Message
              }
            }
        """.trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): DeclineRechargeRecommendation {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground().response
    }

    fun setParams(requestParams: Map<String, String>) {
        params.parameters.clear()
        // Validate params
        if (requestParams.containsKey(PARAM_UUID) && requestParams.containsKey(PARAM_CONTENT_ID)) {
            params.putObject(PARAM_REQUEST, requestParams)
        }
    }
}