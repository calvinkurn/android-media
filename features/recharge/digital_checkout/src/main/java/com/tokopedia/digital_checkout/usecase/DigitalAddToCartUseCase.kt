package com.tokopedia.digital_checkout.usecase

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_checkout.data.request.Attributes
import com.tokopedia.digital_checkout.data.request.RequestBodyAtcDigital
import com.tokopedia.digital_checkout.data.response.atc.DigitalSubscriptionParams
import com.tokopedia.digital_checkout.data.response.atc.ResponseCartData
import com.tokopedia.digital_checkout.utils.DeviceUtil
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import okhttp3.Interceptor
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class DigitalAddToCartUseCase @Inject constructor(authInterceptor: ArrayList<Interceptor>,
                                                  @ApplicationContext context: Context)
    : RestRequestSupportInterceptorUseCase(authInterceptor, context) {

    override fun buildRequest(requestParams: RequestParams): MutableList<RestRequest> {
        val networkRequest = mutableListOf<RestRequest>()
        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type

        val url = DigitalUrl.CART

        val requestBodyAtcDigital = requestParams.getObject(PARAM_REQUEST_BODY_ATC_DIGITAL) as RequestBodyAtcDigital
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyAtcDigital))
        val requestBody = JsonObject()
        requestBody.add("data", jsonElement)

        val idemPotencyKeyHeader = requestParams.getString(PARAM_IDEM_POTENCY_KEY, "")
        val mapHeader = mutableMapOf<String, String>()
        mapHeader[KEY_IDEM_POTENCY_KEY] = idemPotencyKeyHeader
        mapHeader[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE

        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(requestBody)
                .setHeaders(mapHeader)
                .build()
        networkRequest.add(restRequest)
        return networkRequest
    }

    fun createRequestParams(requestBodyAtcDigital: RequestBodyAtcDigital?,
                            idemPotencyKeyHeader: String?): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_REQUEST_BODY_ATC_DIGITAL, requestBodyAtcDigital)
        requestParams.putString(PARAM_IDEM_POTENCY_KEY, idemPotencyKeyHeader)
        return requestParams
    }

    companion object {
        private const val PARAM_REQUEST_BODY_ATC_DIGITAL = "PARAM_REQUEST_BODY_ATC_DIGITAL"
        private const val PARAM_IDEM_POTENCY_KEY = "PARAM_IDEM_POTENCY_KEY"
        private const val KEY_IDEM_POTENCY_KEY = "Idempotency-Key"
        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val VALUE_CONTENT_TYPE = "application/json"

        fun getRequestBodyAtcDigital(digitalCheckoutPassData: DigitalCheckoutPassData,
                                     userId: Int,
                                     digitalIdentifierParam: RequestBodyIdentifier,
                                     digitalSubscriptionParams: DigitalSubscriptionParams): RequestBodyAtcDigital {

            val requestBodyAtcDigital = RequestBodyAtcDigital()
            val fieldList: MutableList<Attributes.Field> = ArrayList()
            if (!digitalCheckoutPassData.clientNumber.isNullOrEmpty()) {
                val field: Attributes.Field = Attributes.Field("client_number", digitalCheckoutPassData.clientNumber)
                fieldList.add(field)
            }
            if (!digitalCheckoutPassData.zoneId.isNullOrEmpty()) {
                val field: Attributes.Field = Attributes.Field("zone_id", digitalCheckoutPassData.zoneId)
                fieldList.add(field)
            }
            for ((key, value) in digitalCheckoutPassData.fields ?: hashMapOf()) {
                val field: Attributes.Field = Attributes.Field(key, value)
                fieldList.add(field)
            }

            val attributes = Attributes()
            attributes.deviceId = 5
            attributes.instantCheckout = digitalCheckoutPassData.instantCheckout == "1"
            attributes.ipAddress = DeviceUtil.localIpAddress
            attributes.userAgent = DeviceUtil.userAgentForApiCall
            attributes.userId = userId
            attributes.productId = if (!digitalCheckoutPassData.productId.isNullOrEmpty()) digitalCheckoutPassData.productId?.toInt()
                    ?: 0 else 0
            val orderId = if (!digitalCheckoutPassData.orderId.isNullOrEmpty()) digitalCheckoutPassData.orderId?.toLong()
                    ?: 0 else 0
            if (orderId > 0L) attributes.orderId = orderId
            attributes.fields = fieldList
            if (GlobalConfig.isSellerApp()) {
                attributes.isReseller = true
            }
            attributes.identifier = digitalIdentifierParam
            attributes.showSubscribeFlag = true
            attributes.isThankyouNative = true
            attributes.isThankyouNativeNew = true
            // Handle subscription params
            val subParams: DigitalSubscriptionParams = digitalSubscriptionParams
            if (subParams.showSubscribePopUp != null) {
                attributes.showSubscribePopUp = subParams.showSubscribePopUp
            }
            if (subParams.autoSubscribe != null) {
                attributes.autoSubscribe = subParams.autoSubscribe
            }
            requestBodyAtcDigital.type = "add_cart"
            requestBodyAtcDigital.attributes = attributes
            return requestBodyAtcDigital
        }
    }
}