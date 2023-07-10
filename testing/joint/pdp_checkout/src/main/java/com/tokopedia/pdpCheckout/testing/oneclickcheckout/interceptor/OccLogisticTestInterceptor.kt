package com.tokopedia.pdpCheckout.testing.oneclickcheckout.interceptor

import com.tokopedia.pdpCheckout.testing.oneclickcheckout.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class OccLogisticTestInterceptor : BaseOccInterceptor() {

    var customRatesResponsePath: String? = null
    var customRatesThrowable: IOException? = null

    var customGetAddressListResponsePath: String? = null
    var customGetAddressListThrowable: IOException? = null

    var customGetShippingDurationResponsePath: String? = null
    var customGetShippingDurationThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(RATES_QUERY)) {
            if (customRatesThrowable != null) {
                throw customRatesThrowable!!
            } else if (customRatesResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customRatesResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(RATES_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(GET_ADDRESS_LIST_QUERY)) {
            if (customGetAddressListThrowable != null) {
                throw customGetAddressListThrowable!!
            } else if (customGetAddressListResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetAddressListResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_ADDRESS_LIST_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(GET_SHIPPING_DURATION_QUERY)) {
            if (customGetShippingDurationThrowable != null) {
                throw customGetShippingDurationThrowable!!
            } else if (customGetShippingDurationResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetShippingDurationResponsePath!!))
            }
            return mockResponse(
                copy,
                getJsonFromResource(
                    GET_SHIPPING_DURATION_DEFAULT_RESPONSE_PATH
                )
            )
        }
        if (requestString.contains(SET_CHOSEN_ADDRESS_QUERY)) {
            return mockResponse(copy, getJsonFromResource(SET_CHOSEN_ADDRESS_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customRatesResponsePath = null
        customRatesThrowable = null
        customGetAddressListResponsePath = null
        customGetAddressListThrowable = null
        customGetShippingDurationResponsePath = null
        customGetShippingDurationThrowable = null
    }
}

const val RATES_QUERY = "ratesV3"
const val GET_ADDRESS_LIST_QUERY = "keroAddressCorner"
const val GET_SHIPPING_DURATION_QUERY = "ongkir_shipper_service"
const val SET_CHOSEN_ADDRESS_QUERY = "keroAddrSetStateChosenAddress"

const val RATES_DEFAULT_RESPONSE_PATH = "logistic/rates_default_response.json"
const val RATES_WITH_INSURANCE_RESPONSE_PATH = "logistic/rates_with_insurance_response.json"
const val RATES_WITH_NO_PROFILE_DURATION_RESPONSE_PATH = "logistic/rates_no_profile_duration_response.json"
const val RATES_ETA_RESPONSE_PATH = "logistic/rates_eta_response.json"
const val RATES_ETA_WITH_BOE_RESPONSE_PATH = "logistic/rates_eta_with_boe_response.json"

const val RATES_TOKONOW_RP0_RESPONSE_PATH = "logistic/rates_tokonow_rp0_response.json"
const val RATES_TOKONOW_DISCOUNT_RESPONSE_PATH = "logistic/rates_tokonow_discount_response.json"
const val RATES_TOKONOW_NO_DISCOUNT_RESPONSE_PATH = "logistic/rates_tokonow_no_discount_response.json"

const val GET_ADDRESS_LIST_DEFAULT_RESPONSE_PATH = "logistic/get_address_list_default_response.json"

const val GET_SHIPPING_DURATION_DEFAULT_RESPONSE_PATH = "logistic/get_shipping_duration_list_default_response.json"

const val SET_CHOSEN_ADDRESS_DEFAULT_RESPONSE_PATH = "logistic/set_chosen_address_response.json"
