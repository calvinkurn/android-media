package com.tokopedia.thankyou_native.domain.usecase

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.data.mapper.PaymentItemKey
import com.tokopedia.thankyou_native.domain.model.*
import com.tokopedia.thankyou_native.domain.query.GQL_GYRO_RECOMMENDATION
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("GyroRecommendationQuery", GQL_GYRO_RECOMMENDATION)
class GyroEngineRequestUseCase @Inject constructor(
        graphqlRepository: GraphqlRepository,val userSession: UserSessionInterface)
    : GraphqlUseCase<FeatureEngineResponse>(graphqlRepository) {

    fun getFeatureEngineData(thanksPageData: ThanksPageData,
                             onSuccess: (ValidateEngineResponse) -> Unit) {
        try {
            this.setTypeClass(FeatureEngineResponse::class.java)
            this.setRequestParams(getRequestParams(thanksPageData))
            this.setGraphqlQuery(GyroRecommendationQuery.GQL_QUERY)
            this.execute(
                    { result ->
                        onSuccess(result.validateEngineResponse)
                    }, {
                it.printStackTrace()
            }
            )
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
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
                thanksPageData.merchantCode, thanksPageData.profileCode, 1, 2,
                FeatureEngineRequestParameters(true.toString(), thanksPageData.amount.toString(),
                        mainGatewayCode, isEGoldPurchased(thanksPageData).toString(),
                        isDonation(thanksPageData).toString(), userSession.userId),
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
