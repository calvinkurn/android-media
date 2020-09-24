package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_FEATURE_ENGINE_REQUEST
import com.tokopedia.thankyou_native.domain.model.*
import javax.inject.Inject
import javax.inject.Named

class FeatureEngineRequestUseCase @Inject constructor(
        @Named(GQL_FEATURE_ENGINE_REQUEST) val query: String,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<FeatureEngineResponse>(graphqlRepository) {

    fun getFeatureEngineData(merchantCode: String, profileCode: String, amount: Long,
                             onSuccess: (ValidateEngineResponse) -> Unit,
                             onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(FeatureEngineResponse::class.java)
            this.setRequestParams(getRequestParams(merchantCode, profileCode, amount))
            this.setGraphqlQuery(query)
            this.execute(
                    { result ->
                        onSuccess(result.validateEngineResponse)
                    },
                    { error ->
                        onError(error)
                    }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(merchantCode: String, profileCode: String, amount: Long): Map<String, Any> {
        return mapOf(PARAM_REQUEST to FeatureEngineRequest(
                merchantCode, "TEST_DEFAULT", 1, 2,
                FeatureEngineRequestParameters("300000"),
                FeatureEngineRequestOperators(),
                FeatureEngineRequestThresholds()
        ))
    }

    companion object {
        const val PARAM_REQUEST = "request"
    }
}


/*
*
*
*     private fun getRequestParams(): Map<String, Any> {
        return mapOf(PARAM_REQUEST to "{\"merchant_code\": \"tokopedia\",\"profile_code\": \"TEST_DEFAULT\",\"engine_id\": 1,\"limit\": 2,\"parameters\": {\"amount\":\"300000\"},\"operators\":{},\"thresholds\":{}}")
*/