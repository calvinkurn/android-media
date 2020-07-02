package com.tokopedia.thankyou_native.recommendationdigital.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.thankyou_native.GQL_DIGITAL_RECOMMENDATION
import com.tokopedia.thankyou_native.recommendationdigital.model.DigitalRecommendationList
import com.tokopedia.thankyou_native.recommendationdigital.model.RecommendationResponse
import javax.inject.Inject
import javax.inject.Named

class DigitalRecommendationUseCase  @Inject constructor(
        @Named(GQL_DIGITAL_RECOMMENDATION) val query: String, graphqlRepository: GraphqlRepository)
        : GraphqlUseCase<RecommendationResponse>(graphqlRepository){


    fun getDigitalRecommendationData(onSuccess: (DigitalRecommendationList) -> Unit,
                                     onError: (Throwable) -> Unit, deviceId: Int, categoryId: String) {
        try {
            this.setTypeClass(RecommendationResponse::class.java)
            this.setRequestParams(getRequestParams(deviceId, categoryId))
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        result.digitalRecommendationList?.let { onSuccess(it) }
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(deviceId: Int, categoryId: String): Map<String, Any> {
        return mapOf(PARAM_PAYMENT_ID to deviceId,
                PARAM_MERCHANT to categoryId)
    }

    companion object {
        const val PARAM_PAYMENT_ID = "device_id"
        const val PARAM_MERCHANT = "category_ids"
    }


}