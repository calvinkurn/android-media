package com.tokopedia.thankyou_native.recommendationdigital.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.thankyou_native.GQL_DIGITAL_RECOMMENDATION
import com.tokopedia.thankyou_native.data.mapper.DigitalThankPage
import com.tokopedia.thankyou_native.data.mapper.MARKET_PLACE
import com.tokopedia.thankyou_native.data.mapper.MarketPlaceThankPage
import com.tokopedia.thankyou_native.data.mapper.ThankPageType
import com.tokopedia.thankyou_native.recommendationdigital.model.RechargeRecommendationDigiPersoItem
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationDigiPersoResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class DigitalRecommendationUseCase  @Inject constructor(
        @Named(GQL_DIGITAL_RECOMMENDATION) val query: String, graphqlRepository: GraphqlRepository)
        : GraphqlUseCase<RecommendationDigiPersoResponse>(graphqlRepository){


    fun getDigitalRecommendationData(onSuccess: (RechargeRecommendationDigiPersoItem) -> Unit,
                                     onError: (Throwable) -> Unit,
                                     clientNumber: String,
                                     pgCategoryIds: List<Int>,
                                     pageType: ThankPageType
    ) {
        try {
            this.setTypeClass(RecommendationDigiPersoResponse::class.java)
            this.setRequestParams(getRequestParams(clientNumber, pgCategoryIds, pageType))
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

    private fun getRequestParams(clientNumber: String,
                                 pgCategoryIds: List<Int>,
                                 pageType: ThankPageType): Map<String, Any> {
        val channelName = when (pageType) {
            MarketPlaceThankPage -> PG_THANK_YOU_PAGE_RECOMMENDATION
            DigitalThankPage -> DG_THANK_YOU_PAGE_RECOMMENDATION
        }
        return mapOf(
                PARAM_INPUT to mapOf(
                        PARAM_CHANNEL_NAME to channelName,
                        PARAM_CLIENT_NUMBERS to listOf(clientNumber),
                        PARAM_DG_CATEGORY_IDS to listOf<Int>(),
                        PARAM_PG_CATEGORY_IDS to pgCategoryIds
                )
        )
    }

    companion object {
        const val PARAM_INPUT = "input"
        const val PARAM_CHANNEL_NAME = "channelName"
        const val PARAM_CLIENT_NUMBERS = "clientNumbers"
        const val PARAM_DG_CATEGORY_IDS = "dgCategoryIDs"
        const val PARAM_PG_CATEGORY_IDS = "pgCategoryIDs"

        const val DG_THANK_YOU_PAGE_RECOMMENDATION = "dg_thank_you_page_recommendation"
        const val PG_THANK_YOU_PAGE_RECOMMENDATION = "pg_thank_you_page_recommendation"
    }


}