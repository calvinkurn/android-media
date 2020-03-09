package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeDeclineAboveTheFoldRecommendation
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by resakemal on 2020-03-09
 */

class GetRechargeRecommendationUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<RechargeDeclineAboveTheFoldRecommendation.Response>)
    : UseCase<RechargeDeclineAboveTheFoldRecommendation>() {
    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(RechargeDeclineAboveTheFoldRecommendation.Response::class.java)
    }
    companion object {
        const val PARAM_TYPE = "type"
    }

    private var params: RequestParams = RequestParams.create()

    //region query
    private val query by lazy {
        val type = "\$type"

        """
            query rechargeRecommendation($type: Int!) {
              rechargeRecommendation(recommendationType: $type) {
                #LIST OF RECOMMENDATION
                UUID # recharge_watf_userID, used as recommendation hash_key
                recommendations: Recommendations {
                  contentID: ContentID
                  mainText: MainText
                  subText: SubText
                  applink: AppLink
                  link: Link
                  iconURL: IconURL
                }
              }
            }
        """.trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): RechargeDeclineAboveTheFoldRecommendation {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setRequestParams(params.parameters)
        return graphqlUseCase.executeOnBackground().response
    }

    fun setParams(type: Int) {
        params.parameters.clear()
        params.putInt(PARAM_TYPE, type)
    }
}