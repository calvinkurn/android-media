package com.tokopedia.digital_checkout.usecase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_checkout.data.DigitalCheckoutConst
import com.tokopedia.digital_checkout.data.model.CollectionPointMetadata
import com.tokopedia.digital_checkout.data.request.DigitalCheckoutDataParameter
import com.tokopedia.digital_checkout.data.request.checkout.RechargeCheckoutFintechProduct
import com.tokopedia.digital_checkout.data.request.checkout.RechargeCheckoutRequest
import com.tokopedia.digital_checkout.data.response.checkout.RechargeCheckoutResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.util.GqlParamDeserializer
import javax.inject.Inject

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
@GqlQuery(
    DigitalCheckoutGqlUseCase.QUERY_NAME_RECHARGE_CHECKOUT,
    DigitalCheckoutGqlUseCase.QUERY_RECHARGE_CHECKOUT
)
class DigitalCheckoutGqlUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) :
    GraphqlUseCase<RechargeCheckoutResponse.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(RechargeCheckoutQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(RechargeCheckoutResponse.Response::class.java)
    }

    fun setParams(
        requestCheckoutParams: DigitalCheckoutDataParameter,
        digitalIdentifierParams: RequestBodyIdentifier
    ) {
        val builder = GsonBuilder().apply {
            registerTypeAdapter(
                object : TypeToken<HashMap<String, Any>>() {}.type,
                GqlParamDeserializer()
            )
        }
        val customGson = builder.create()
        val requestParams = RechargeCheckoutRequest(
            voucherCode = requestCheckoutParams.voucherCode,
            transactionAmount = requestCheckoutParams.transactionAmount.toLong(),
            language = "id",
            ipAddress = requestCheckoutParams.ipAddress,
            userAgent = requestCheckoutParams.userAgent,
            deviceId = requestCheckoutParams.deviceId.toLong(),
            isHiddenCart = false,
            userId = digitalIdentifierParams.userId ?: "",
            cartType = DigitalCheckoutConst.RequestBodyParams.REQUEST_BODY_CHECKOUT_TYPE,
            cartId = requestCheckoutParams.cartId,
            createSubscription = requestCheckoutParams.isSubscriptionChecked,
            fintechProducts = requestCheckoutParams.crossSellProducts.map {
                val checkoutType = object : TypeToken<HashMap<String, Any>>() {}.type
                val checkoutMetadataMap = customGson.fromJson<HashMap<String, Any>>(it.value.product.crossSellMetadata, checkoutType)

                if (it.value.isSubscription) {
                    try {
                        val additionalMetadata = customGson.toJson(it.value.additionalMetadata)
                        val additionalType = object : TypeToken<JsonElement>() {}.type
                        val additionalMetadataJson = customGson.fromJson<JsonElement>(additionalMetadata, additionalType)

                        // checking metadata
                        val metadataKey = checkoutMetadataMap[KEY_METADATA]
                        val consentMetadata = customGson.fromJson(metadataKey.toString(), CollectionPointMetadata::class.java)
                        consentMetadata.consentPayload = additionalMetadataJson.toString()

                        checkoutMetadataMap[KEY_METADATA] = customGson.toJson(consentMetadata)
                    } catch (e: JsonSyntaxException) {
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                }
                RechargeCheckoutFintechProduct(
                    transactionType = it.value.product.transactionType,
                    checkoutMetadata = customGson.toJson(checkoutMetadataMap)
                )
            }.toList(),
            instant = requestCheckoutParams.isInstantCheckout,
            appVersion = GlobalConfig.VERSION_NAME,
            consentPayload = requestCheckoutParams.productConsentPayload
        )

        setRequestParams(mapOf(PARAMS_KEY to requestParams))
    }

    override suspend fun executeOnBackground(): RechargeCheckoutResponse.Response {
        GraphqlClient.moduleName = RECHARGE_MODULE_NAME
        return super.executeOnBackground()
    }

    companion object {
        private const val PARAMS_KEY = "request"
        private const val RECHARGE_MODULE_NAME = "recharge"
        private const val KEY_METADATA = "metadata"
        private const val KEY_CONSENT_PAYLOAD = "consent_payload"

        const val QUERY_NAME_RECHARGE_CHECKOUT = "RechargeCheckoutQuery"
        const val QUERY_RECHARGE_CHECKOUT = """
        mutation rechargeCheckoutV3(${'$'}request: RechargeCheckoutRequestV3!) {
          rechargeCheckoutV3(body: ${'$'}request) {
            meta {
              order_id
            }
            data {
              type
              id
              attributes {
                redirect_url
                callback_url_success
                callback_url_failed
                thanks_url
                query_string
                parameter {
                  merchant_code
                  profile_code
                  transaction_id
                  transaction_code
                  transaction_date
                  customer_name
                  customer_email
                  customer_msisdn
                  amount
                  currency
                  items_name
                  items_quantity
                  items_price
                  signature
                  language
                  user_defined_value
                  nid
                  state
                  fee
                  payments_amount
                  payments_name
                  pid
                }
              }
            }
            errors {
              status
              title
            }
          }
        }
    """
    }
}
