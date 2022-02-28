package com.tokopedia.thankyou_native.domain.usecase

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.data.mapper.PaymentItemKey
import com.tokopedia.thankyou_native.domain.model.*
import com.tokopedia.thankyou_native.domain.query.GQL_GYRO_RECOMMENDATION
import com.tokopedia.user.session.UserSessionInterface
import org.json.JSONObject
import javax.inject.Inject

@GqlQuery("GyroRecommendationQuery", GQL_GYRO_RECOMMENDATION)
class GyroEngineRequestUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface
) : GraphqlUseCase<FeatureEngineResponse>(graphqlRepository) {

    fun getFeatureEngineData(
        thanksPageData: ThanksPageData, walletBalance: WalletBalance?,
        onSuccess: (ValidateEngineResponse) -> Unit
    ) {
        try {
            this.setTypeClass(FeatureEngineResponse::class.java)
            this.setRequestParams(getRequestParams(thanksPageData, walletBalance))
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

    private fun getRequestParams(
        thanksPageData: ThanksPageData,
        walletBalance: WalletBalance?
    ): Map<String, Any> {

        val mainGatewayCode = thanksPageData.paymentDetails?.find {
            it.gatewayName.equals(thanksPageData.gatewayName, true)
        }?.gatewayCode ?: ""

        val jsonStr = computeJsonFromFeatureEngine(thanksPageData, mainGatewayCode)

        // adding wallet balance parameters
        // else return unmodified jsonStr
        return addWalletParameters(jsonStr, walletBalance)?.let { modifiedJsonStr ->
            mapOf(PARAM_REQUEST to modifiedJsonStr)
        } ?: kotlin.run { mapOf(PARAM_REQUEST to jsonStr) }
    }

    private fun computeJsonFromFeatureEngine(thanksPageData: ThanksPageData, mainGatewayCode: String) =
        Gson().toJson(
            FeatureEngineRequest(
                thanksPageData.merchantCode, thanksPageData.profileCode, 1, 5,
                FeatureEngineRequestParameters(
                    true.toString(), thanksPageData.amount.toString(),
                    mainGatewayCode, isEGoldPurchased(thanksPageData).toString(),
                    isDonation(thanksPageData).toString(), userSession.userId
                ),
                FeatureEngineRequestOperators(),
                FeatureEngineRequestThresholds()
            )
        )

    private fun addWalletParameters(jsonStr: String, walletBalance: WalletBalance?): String? {
        walletBalance?.let {
            val jsonObj = JSONObject(jsonStr)
            try {
                val parameterObj = (jsonObj[PARAM_WALLET_PARAMETERS] as JSONObject)
                walletBalance.balanceList.forEach { item ->
                    if (item.whitelisted == true)
                        parameterObj.put(item.walletCode, item.isActive.toString())
                }
                return jsonObj.toString()
            } catch (e: Exception) { return "" }
        }
        return null
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
        const val PARAM_WALLET_PARAMETERS = "parameters"
    }
}
