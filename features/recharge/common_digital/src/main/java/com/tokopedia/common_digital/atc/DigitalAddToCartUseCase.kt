package com.tokopedia.common_digital.atc

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.atc.data.request.Attributes
import com.tokopedia.common_digital.atc.data.request.RequestBodyAtcDigital
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ResponseCartData
import com.tokopedia.common_digital.atc.utils.DeviceUtil
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.common_digital.common.di.DigitalAddToCartQualifier
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.usecase.RequestParams
import java.lang.reflect.Type
import javax.inject.Inject

class DigitalAddToCartUseCase @Inject constructor(@DigitalAddToCartQualifier val repository: RestRepository)
    : RestRequestUseCase(repository) {

    private val url = DigitalUrl.CART
    var requestParams = RequestParams()

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val requestBodyAtcDigital = requestParams.getObject(PARAM_REQUEST_BODY_ATC_DIGITAL) as RequestBodyAtcDigital
        val jsonElement = JsonParser().parse(Gson().toJson(requestBodyAtcDigital))
        val requestBody = JsonObject()
        requestBody.add(PARAM_DATA, jsonElement)

        val idemPotencyKeyHeader = requestParams.getString(PARAM_IDEM_POTENCY_KEY, "")
        val mapHeader = mutableMapOf<String, String>()
        mapHeader[KEY_IDEM_POTENCY_KEY] = idemPotencyKeyHeader
        mapHeader[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE

        val token = object : TypeToken<DataResponse<ResponseCartData>>() {}.type
        val restRequest = RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(requestBody)
                .setHeaders(mapHeader)
                .build()


        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    fun setRequestParams(requestBodyAtcDigital: RequestBodyAtcDigital?,
                         idemPotencyKeyHeader: String?) {
        requestParams = RequestParams.create()
        requestParams.putObject(PARAM_REQUEST_BODY_ATC_DIGITAL, requestBodyAtcDigital)
        requestParams.putString(PARAM_IDEM_POTENCY_KEY, idemPotencyKeyHeader)
    }

    companion object {
        private const val PARAM_REQUEST_BODY_ATC_DIGITAL = "PARAM_REQUEST_BODY_ATC_DIGITAL"
        private const val PARAM_IDEM_POTENCY_KEY = "PARAM_IDEM_POTENCY_KEY"
        private const val PARAM_ADD_TO_CART = "add_cart"
        private const val PARAM_DATA = "data"
        private const val PARAM_CLIENT_NUMBER = "client_number"
        private const val PARAM_ZONE_ID = "zone_id"

        private const val KEY_IDEM_POTENCY_KEY = "Idempotency-Key"
        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val VALUE_CONTENT_TYPE = "application/json"
        private const val VALUE_ANDROID_DEVICE_ID = 5
        private const val VALUE_INSTANT_CHECKOUT_ID = "1"

        fun getRequestBodyAtcDigital(digitalCheckoutPassData: DigitalCheckoutPassData,
                                     userId: Int,
                                     digitalIdentifierParam: RequestBodyIdentifier,
                                     digitalSubscriptionParams: DigitalSubscriptionParams): RequestBodyAtcDigital {

            val requestBodyAtcDigital = RequestBodyAtcDigital()
            val fieldList: MutableList<Attributes.Field> = ArrayList()

            digitalCheckoutPassData.clientNumber?.let { clientNumber ->
                val field: Attributes.Field = Attributes.Field(PARAM_CLIENT_NUMBER, clientNumber)
                fieldList.add(field)
            }

            digitalCheckoutPassData.zoneId?.let { zoneId ->
                if (zoneId.isNotEmpty()) {
                    val field: Attributes.Field = Attributes.Field(PARAM_ZONE_ID, zoneId)
                    fieldList.add(field)
                }
            }

            for ((key, value) in digitalCheckoutPassData.fields ?: hashMapOf()) {
                val field: Attributes.Field = Attributes.Field(key, value)
                fieldList.add(field)
            }

            val attributes = Attributes()
            attributes.deviceId = VALUE_ANDROID_DEVICE_ID
            attributes.instantCheckout = digitalCheckoutPassData.instantCheckout == VALUE_INSTANT_CHECKOUT_ID
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

            subParams.showSubscribePopUp?.let { attributes.showSubscribePopUp = it }
            subParams.autoSubscribe?.let { attributes.autoSubscribe = it }

            requestBodyAtcDigital.type = PARAM_ADD_TO_CART
            requestBodyAtcDigital.attributes = attributes
            return requestBodyAtcDigital
        }
    }
}