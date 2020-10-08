package com.tokopedia.thankyou_native.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.GQL_FEATURE_ENGINE_REQUEST
import com.tokopedia.thankyou_native.data.mapper.PaymentItemKey
import com.tokopedia.thankyou_native.domain.model.*
import javax.inject.Inject
import javax.inject.Named

class FeatureEngineRequestUseCase @Inject constructor(
        @Named(GQL_FEATURE_ENGINE_REQUEST) val query: String,
        graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<FeatureEngineResponse>(graphqlRepository) {

    fun getFeatureEngineData(thanksPageData: ThanksPageData,
                             onSuccess: (ValidateEngineResponse) -> Unit,
                             onError: (Throwable) -> Unit) {
        try {
            this.setTypeClass(FeatureEngineResponse::class.java)
            this.setRequestParams(getRequestParams(thanksPageData))
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

    private fun getRequestParams(thanksPageData: ThanksPageData): Map<String, Any> {
        var mainGatewayCode : String = ""
        thanksPageData.paymentDetails?.forEach {
            if(it.gatewayName.equals(thanksPageData.gatewayName, true)){
                mainGatewayCode = it.gatewayCode
            }
        }
        return mapOf(PARAM_REQUEST to Gson().toJson(FeatureEngineRequest(
                thanksPageData.merchantCode, thanksPageData.profileCode, 1, 5,
                FeatureEngineRequestParameters(true.toString(), thanksPageData.amount.toString(),
                        mainGatewayCode, isEGoldPurchased(thanksPageData).toString(),
                        isDonation(thanksPageData).toString()),
                FeatureEngineRequestOperators(),
                FeatureEngineRequestThresholds())))
    }

    private fun isEGoldPurchased(thanksPageData: ThanksPageData): Boolean {
        thanksPageData.paymentItems?.forEach {
            when (it.itemName) {
                PaymentItemKey.E_GOLD -> return true
            }
        }
        return false
    }

    private fun isDonation(thanksPageData: ThanksPageData): Boolean {
        thanksPageData.paymentItems?.forEach {
            when (it.itemName) {
                PaymentItemKey.DONATION -> return true
            }
        }
        return false
    }

    companion object {
        const val PARAM_REQUEST = "request"
    }
}
