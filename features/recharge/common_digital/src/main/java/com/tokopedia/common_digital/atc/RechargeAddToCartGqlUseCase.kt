package com.tokopedia.common_digital.atc

import com.tokopedia.common_digital.atc.data.gql.request.RechargeATCField
import com.tokopedia.common_digital.atc.data.gql.request.RechargeATCIdentifier
import com.tokopedia.common_digital.atc.data.gql.request.RechargeATCRequest
import com.tokopedia.common_digital.atc.data.gql.response.RechargeATCResponse
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
@GqlQuery(
    RechargeAddToCartGqlUseCase.QUERY_NAME_RECHARGE_ATC,
    RechargeAddToCartGqlUseCase.QUERY_RECHARGE_ATC
)
class RechargeAddToCartGqlUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<RechargeATCResponse.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(RechargeAddToCartQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(RechargeATCResponse.Response::class.java)
    }

    fun setParams(
        digitalCheckoutPassData: DigitalCheckoutPassData,
        userId: String,
        digitalIdentifierParam: RequestBodyIdentifier
    ) {
        lateinit var requestParams: RechargeATCRequest

        val fieldList: MutableList<RechargeATCField> = arrayListOf()

        digitalCheckoutPassData.clientNumber?.let {
            fieldList.add(
                RechargeATCField(
                    name = DigitalAddToCartRestUseCase.PARAM_CLIENT_NUMBER,
                    value = it
                )
            )
        }
        digitalCheckoutPassData.zoneId?.let {
            if (it.isNotEmpty()) {
                fieldList.add(
                    RechargeATCField(
                        name = DigitalAddToCartRestUseCase.PARAM_ZONE_ID,
                        value = it
                    )
                )
            }
        }
        digitalCheckoutPassData.fields?.map {
            fieldList.add(RechargeATCField(name = it.key, value = it.value))
        }?.toList()

        requestParams = RechargeATCRequest(
            deviceId = digitalCheckoutPassData.deviceId.toLong(),
            fields = fieldList,
            identifier = RechargeATCIdentifier(
                deviceToken = digitalIdentifierParam.deviceToken ?: "",
                osType = digitalIdentifierParam.osType ?: "",
                userId = digitalIdentifierParam.userId ?: ""
            ),
            instantCheckout = digitalCheckoutPassData.instantCheckout == VALUE_INSTANT_CHECKOUT_ID,
            ipAddress = DeviceUtil.localIpAddress,
            productId = if (!digitalCheckoutPassData.productId.isNullOrEmpty())
                digitalCheckoutPassData.productId?.toLong() ?: 0 else 0,
            userAgent = DeviceUtil.userAgentForApiCall,
            userId = userId.toLong(),
            orderId = if (!digitalCheckoutPassData.orderId.isNullOrEmpty())
                digitalCheckoutPassData.orderId?.toLong() ?: 0 else 0,
            atcSource = digitalCheckoutPassData.atcSource ?: ""
        )

        setRequestParams(mapOf(PARAMS_KEY to requestParams))
    }

    override suspend fun executeOnBackground(): RechargeATCResponse.Response {
        GraphqlClient.moduleName = RECHARGE_MODULE_NAME
        return super.executeOnBackground()
    }

    companion object {
        private const val VALUE_INSTANT_CHECKOUT_ID = "1"

        private const val PARAMS_KEY = "request"
        private const val RECHARGE_MODULE_NAME = "recharge"

        const val QUERY_NAME_RECHARGE_ATC = "RechargeAddToCartQuery"
        const val QUERY_RECHARGE_ATC = """
            mutation rechargeAddToCartV2(${'$'}request:RechargeAddToCartRequestV2!) {
                  rechargeAddToCartV2(body: ${'$'}request) {
                    data {
                      type
                      id
                      attributes {
                        user_id
                        client_number
                        title
                        category_name
                        operator_name
                        icon
                        price
                        price_plain
                        instant_checkout
                        need_otp
                        sms_state
                        voucher_autocode
                        user_input_price
                        user_open_payment {
                          min_payment_text
                          max_payment_text
                          min_payment
                          max_payment
                          max_payment_error_text
                          min_payment_error_text
                        }
                        enable_voucher
                        is_coupon_active
                        default_promo_dialog_tab
                        main_info {
                          label
                          value
                        }
                      }
                    }
                    errors {
                      status
                      title
                      applink_url
                      atc_error_page{
                        show_error_page
                        title
                        sub_title
                        image_url
                        buttons{
                          label
                          url
                          applink_url
                          action_type
                        }
                      }
                    }
                  }
                }
            """
    }

}