package com.tokopedia.thankyou_native.domain.usecase

import com.google.gson.Gson
import com.tokopedia.applink.teleporter.Teleporter
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.thankyou_native.data.mapper.PaymentItemKey
import com.tokopedia.thankyou_native.data.mapper.StoreItemKey
import com.tokopedia.thankyou_native.domain.model.*
import com.tokopedia.thankyou_native.domain.query.GQL_GYRO_RECOMMENDATION
import com.tokopedia.user.session.UserSessionInterface
import org.json.JSONObject
import javax.inject.Inject

@GqlQuery("GyroRecommendationQuery", GQL_GYRO_RECOMMENDATION)
class GyroEngineRequestUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface,
    val gson: Gson
) : GraphqlUseCase<FeatureEngineResponse>(graphqlRepository) {

    fun getFeatureEngineData(
        thanksPageData: ThanksPageData, walletBalance: WalletBalance?,
        onSuccess: (ValidateEngineResponse) -> Unit
    ) {
        onSuccess(
            Teleporter.gson.fromJson(
                "{\"validateEngineRequest\": {\n" +
                    "      \"success\": true,\n" +
                    "      \"error_code\": \"\",\n" +
                    "      \"message\": \"\",\n" +
                    "      \"data\": {\n" +
                    "        \"title\": \"Fitur spesial buat kamu\",\n" +
                    "        \"description\": \"Cobain fitur ini yuk\",\n" +
                    "        \"items\": [\n" +
                    "          {\n" +
                    "            \"id\" : 136,\n" +
                    "            \"detail\" : \"{\\\"type\\\":\\\"config\\\",\\\"widget_order\\\":\\\"dg, pg, feature, shopads, feature\\\"}\"\n" +
                    "          },{\n" +
                    "            \"id\": 15,\n" +
                    "            \"detail\": \"{\\\"type\\\":\\\"tdn_user\\\",\\\"ep\\\":\\\"banner\\\",\\\"inventory_id\\\":\\\"15\\\",\\\"item\\\":\\\"3\\\",\\\"dimen_id_desktop\\\":\\\"8\\\",\\\"dimen_id_mobile\\\":\\\"3\\\",\\\"title\\\":\\\"Sesuai seleramu\\\",\\\"desc\\\":\\\"Cobain yuk\\\",\\\"section_title\\\":\\\"Promo Brand Pilihan\\\"}\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"id\": 20,\n" +
                    "            \"detail\": \"{\\\"type\\\":\\\"tokomember\\\",\\\"section_title\\\":\\\"Fitur spesial buat kamu\\\",\\\"section_desc\\\":\\\"Cobain fitur ini yuk\\\",\\\"url\\\":\\\"https://www.tokopedia.com/membership/\\\",\\\"url_android\\\":\\\"tokopedia://webview?url=https://www.tokopedia.com/membership/\\\",\\\"url_ios\\\":\\\"tokopedia://webview?url=https://www.tokopedia.com/membership/\\\",\\\"title\\\":\\\"Tokomember hadir untukmu\\\",\\\"desc\\\":\\\"Selalu ada selalu bisa\\\",\\\"image\\\":\\\"https://images.tokopedia.net/img/blog/promo/2019/09/tokomember.png\\\"}\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"id\": 64,\n" +
                    "            \"detail\": \"{\\\"type\\\":\\\"list\\\",\\\"title\\\":\\\"Ambil Hadiahmu Sekarang!\\\",\\\"desc\\\":\\\"Buka Tap Tap Kotak untuk dapatkan kupon Diskon, Cashback hingga GoPay Coins!\\\",\\\"image\\\":\\\"https://ecs7.tokopedia.net/img/blog/promo/2023/02/FLOATING-ICON-NEW-YEAR.png\\\",\\\"url\\\":\\\"https://www.tokopedia.com/kotak-kejutan/pre-tap-tap\\\",\\\"url_android\\\":\\\"tokopedia://gamification_gift_daily\\\",\\\"url_ios\\\":\\\"tokopedia://gamification_gift_daily\\\"}\"\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }}",
                FeatureEngineResponse::class.java
            ).validateEngineResponse
        )
//        try {
//            this.setTypeClass(FeatureEngineResponse::class.java)
//            this.setRequestParams(getRequestParams(thanksPageData, walletBalance))
//            this.setGraphqlQuery(GyroRecommendationQuery.GQL_QUERY)
//            this.execute(
//                { result ->
//                    onSuccess(result.validateEngineResponse)
//                }, {
//                    it.printStackTrace()
//                }
//            )
//        } catch (throwable: Throwable) {
//            throwable.printStackTrace()
//        }
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
        return mapOf(PARAM_REQUEST to addWalletParameters(jsonStr, walletBalance))
    }

    private fun computeJsonFromFeatureEngine(thanksPageData: ThanksPageData, mainGatewayCode: String) =
        gson.toJson(
            FeatureEngineRequest(
                thanksPageData.merchantCode, thanksPageData.profileCode, 1, 5,
                concatMap(thanksPageData,mainGatewayCode),
                FeatureEngineRequestOperators(),
                FeatureEngineRequestThresholds()
            )
        )

    private fun concatMap(thanksPageData: ThanksPageData, mainGatewayCode: String): MutableMap<String, Any?>? {
        thanksPageData.gyroData?.put(IS_STATIC, "true")
        thanksPageData.gyroData?.put(AMOUNT, thanksPageData.amount.toString())
        thanksPageData.gyroData?.put(GATEWAY_CODE, mainGatewayCode)
        thanksPageData.gyroData?.put(EGOLD, isEGoldPurchased(thanksPageData).toString())
        thanksPageData.gyroData?.put(DONATION, isDonation(thanksPageData).toString())
        thanksPageData.gyroData?.put(USER_ID, userSession.userId)
        thanksPageData.gyroData?.put(IS_RM, isMarketplace(thanksPageData).toString())
        thanksPageData.gyroData?.put(IS_PM, isGoldMerchant(thanksPageData).toString())
        thanksPageData.gyroData?.put(IS_0S, isOfficialStore(thanksPageData).toString())
        thanksPageData.gyroData?.put(IS_ENJOY_PLUS_BENEFIT, thanksPageData.customDataOther?.isEnjoyPLus ?: "false")
        thanksPageData.gyroData?.put(IS_PLUS_TRANSACTION, thanksPageData.customDataOther?.isPlusTransaction ?: "false")
        return thanksPageData.gyroData

    }

    private fun addWalletParameters(jsonStr: String, walletBalance: WalletBalance?): String {
        return walletBalance?.let {
            val jsonObj = JSONObject(jsonStr)
            try {
                val parameterObj = (jsonObj[PARAM_WALLET_PARAMETERS] as JSONObject)
                walletBalance.balanceList.forEach { item ->
                    if (item.whitelisted == true)
                        parameterObj.put(item.walletCode, item.isActive.toString())
                }
                if (walletBalance.balanceList.isEmpty()) {
                    parameterObj.put(GATEWAY_CODE_PEMUDA, VALUE_FALSE)
                }
                jsonObj.toString()
            } catch (e: Exception) { jsonStr }
        } ?: run { jsonStr }
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

    private fun isMarketplace(thanksPageData: ThanksPageData) =
        thanksPageData.shopOrder.any {
            it.storeType == StoreItemKey.MARKETPLACE || it.storeType == StoreItemKey.MARKETPLACE_ALTERNATE
        }

    private fun isGoldMerchant(thanksPageData: ThanksPageData) =
        thanksPageData.shopOrder.any {
            it.storeType == StoreItemKey.GOLD_MERCHANT || it.storeType == StoreItemKey.GOLD_MERCHANT_ALTERNATE
        }

    private fun isOfficialStore(thanksPageData: ThanksPageData) =
        thanksPageData.shopOrder.any {
            it.storeType == StoreItemKey.OFFICIAL_STORE || it.storeType == StoreItemKey.OFFICIAL_STORE_ALTERNATE
        }

    companion object {
        const val PARAM_REQUEST = "request"
        const val PARAM_WALLET_PARAMETERS = "parameters"
        const val IS_STATIC = "static"
        const val AMOUNT = "amount"
        const val GATEWAY_CODE = "gateway_code"
        const val EGOLD = "egold"
        const val DONATION = "donation"
        const val USER_ID = "user_id"
        const val IS_RM = "is_RM"
        const val IS_PM = "is_PM"
        const val IS_0S = "is_OS"
        const val IS_ENJOY_PLUS_BENEFIT = "is_enjoy_plus_benefit"
        const val IS_PLUS_TRANSACTION = "is_plus_transaction"
        const val GATEWAY_CODE_PEMUDA = "PEMUDA"
        const val VALUE_FALSE = "false"

    }
}
