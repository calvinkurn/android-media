package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_FEATURE_ENGINE_REQUEST
import com.tokopedia.thankyou_native.domain.model.FeatureEngineResponse
import com.tokopedia.thankyou_native.domain.model.ValidateEngineResponse
import javax.inject.Inject
import javax.inject.Named

class FeatureEngineRequestUseCase @Inject constructor(
        @Named(GQL_FEATURE_ENGINE_REQUEST) val query: String,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<FeatureEngineResponse>(graphqlRepository) {

    fun getFeatureEngineData(onSuccess: (ValidateEngineResponse) -> Unit,
                             onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(FeatureEngineResponse::class.java)
            this.setRequestParams(getRequestParams())
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.validateEngineResponse)
                    }, { error ->
                onError(error)
            }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(): Map<String, Any> {
        return mapOf(PARAM_REQUEST to "{\"merchant_code\": \"tokopedia\",\"profile_code\": \"TEST_DEFAULT\",\"engine_id\": 1,\"limit\": 2,\"parameters\": {\"amount\":\"300000\"},\"operators\":{},\"thresholds\":{}}")
    }

    companion object {
        const val PARAM_REQUEST = "request"
    }
}