package com.tokopedia.buyerorder.detail.domain

import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationDigiPersoResponse
import com.tokopedia.buyerorder.detail.domain.queries.QueryDigiPerso
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class DigiPersoUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface
): GraphqlUseCase<RecommendationDigiPersoResponse>(repository) {

    private var params: Map<String, Any> = mapOf()

    override suspend fun executeOnBackground(): RecommendationDigiPersoResponse {
        val gqlResponse = repository.response(listOf(createRequest()))
        val error = gqlResponse.getError(RecommendationDigiPersoResponse::class.java)
        if (error == null || error.isEmpty()){
            return gqlResponse.getData(RecommendationDigiPersoResponse::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    private fun createRequest(): GraphqlRequest{
        return GraphqlRequest(
            QueryDigiPerso(),
            RecommendationDigiPersoResponse::class.java,
            params
        )
    }

    fun createParams(){
        params = mapOf(Key.INPUT to mapOf(
                Key.CHANNEL_NAME to ORDER_DETAIL_CHANNEL_NAME,
                Key.CLIENT_NUMBERS to listOf(userSession.phoneNumber),
                Key.DG_CATEGORY_IDS to emptyList<Int>(),
                Key.PG_CATEGORY_IDS to emptyList<Int>()
            ))
    }

    companion object{
        private object Key{
            const val INPUT = "input"
            const val CHANNEL_NAME = "channelName"
            const val CLIENT_NUMBERS = "clientNumbers"
            const val DG_CATEGORY_IDS = "dgCategoryIDs"
            const val PG_CATEGORY_IDS = "pgCategoryIDs"
        }

        private const val ORDER_DETAIL_CHANNEL_NAME = "dg_order_detail_recommendation"
    }
}