package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class PaymentTestInterceptor : BaseOccInterceptor() {

    var customGetListingParamResponsePath: String? = null
    var customGetListingParamThrowable: IOException? = null

    var customCreditCardTenorListResponsePath: String? = null
    var customCreditCardTenorListThrowable: IOException? = null

    var customGoCicilInstallmentOptionResponsePath: String? = null
    var customGoCicilInstallmentOptionThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_LISTING_PARAM_QUERY)) {
            if (customGetListingParamThrowable != null) {
                throw customGetListingParamThrowable!!
            } else if (customGetListingParamResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGetListingParamResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GET_LISTING_PARAM_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(GET_OVO_TOP_UP_URL_QUERY)) {
            return mockResponse(copy, getJsonFromResource(GET_OVO_TOP_UP_URL_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(CREDIT_CARD_TENOR_LIST_QUERY)) {
            if (customCreditCardTenorListThrowable != null) {
                throw customCreditCardTenorListThrowable!!
            } else if (customCreditCardTenorListResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customCreditCardTenorListResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(CREDIT_CARD_TENOR_LIST_DEFAULT_RESPONSE_PATH))
        }
        if (requestString.contains(GOCICIL_INSTALLMENT_OPTION_QUERY)) {
            if (customGoCicilInstallmentOptionThrowable != null) {
                throw customGoCicilInstallmentOptionThrowable!!
            } else if (customGoCicilInstallmentOptionResponsePath != null) {
                return mockResponse(copy, getJsonFromResource(customGoCicilInstallmentOptionResponsePath!!))
            }
            return mockResponse(copy, getJsonFromResource(GOCICIL_INSTALLMENT_OPTION_DEFAULT_RESPONSE_PATH))
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetListingParamResponsePath = null
        customGetListingParamThrowable = null
        customCreditCardTenorListResponsePath = null
        customCreditCardTenorListThrowable = null
    }
}

const val GET_LISTING_PARAM_QUERY = "getListingParams"

const val GET_LISTING_PARAM_DEFAULT_RESPONSE_PATH = "payment/get_payment_listing_default_response.json"

const val GET_OVO_TOP_UP_URL_QUERY = "fetchInstantTopupURL"

const val GET_OVO_TOP_UP_URL_DEFAULT_RESPONSE_PATH = "payment/get_ovo_top_up_url_default_response.json"

const val CREDIT_CARD_TENOR_LIST_QUERY = "creditCardTenorList"

const val CREDIT_CARD_TENOR_LIST_DEFAULT_RESPONSE_PATH = "payment/credit_card_tenor_list_default_response.json"
const val CREDIT_CARD_TENOR_LIST_ALL_ENABLED_RESPONSE_PATH = "payment/credit_card_tenor_list_all_enabled_response.json"

const val GOCICIL_INSTALLMENT_OPTION_QUERY = "getInstallmentInfo"

const val GOCICIL_INSTALLMENT_OPTION_DEFAULT_RESPONSE_PATH = "payment/gocicil_installment_option_default_response.json"
const val GOCICIL_INSTALLMENT_OPTION_SOME_INACTIVE_RESPONSE_PATH = "payment/gocicil_installment_option_some_inactive_response.json"