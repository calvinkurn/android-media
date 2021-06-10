package com.tokopedia.thankyou_native.recommendationdigital.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.thankyou_native.GQL_DIGITAL_RECOMMENDATION
import com.tokopedia.thankyou_native.recommendationdigital.model.RechargeRecommendationDigiPersoItem
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationDigiPersoResponse
import javax.inject.Inject
import javax.inject.Named

class DigitalRecommendationUseCase  @Inject constructor(
        @Named(GQL_DIGITAL_RECOMMENDATION) val query: String, graphqlRepository: GraphqlRepository)
        : GraphqlUseCase<RecommendationDigiPersoResponse>(graphqlRepository){


    fun getDigitalRecommendationData(onSuccess: (RechargeRecommendationDigiPersoItem) -> Unit,
                                     onError: (Throwable) -> Unit, categoryId: String) {
        try {
            this.setTypeClass(RecommendationDigiPersoResponse::class.java)
            this.setRequestParams(getRequestParams())
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        result.rechargeRecommendationDigiPersoItem?.let { onSuccess(it) }
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(): Map<String, Any> {
        // TODO: [Misael] isi categoryId pake ini mgkn ya
        return mapOf(
                PARAM_INPUT to mapOf(
                        PARAM_CHANNEL_NAME to "dg_thank_you_page_recommendation",
                        PARAM_CLIENT_NUMBERS to listOf<String>(),
                        PARAM_DG_CATEGORY_IDS to listOf(1),
                        PARAM_PG_CATEGORY_IDS to listOf<Int>()
                )
        )
    }

    companion object {
        const val PARAM_INPUT = "input"
        const val PARAM_CHANNEL_NAME = "channelName"
        const val PARAM_CLIENT_NUMBERS = "clientNumbers"
        const val PARAM_DG_CATEGORY_IDS = "dgCategoryIDs"
        const val PARAM_PG_CATEGORY_IDS = "pgCategoryIDs"
    }


}