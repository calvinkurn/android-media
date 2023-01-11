package com.tokopedia.oneclickcheckout.common.interceptor

import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class EthicalDrugTestInterceptor : BaseOccInterceptor() {

    var customGetPrescriptionIdsResponsePath: String? = null
    var customGetPrescriptionIdsThrowable: IOException? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val copy = chain.request().newBuilder().build()
        val requestString = readRequestString(copy)

        if (requestString.contains(GET_EPHARMACY_CHECKOUT_QUERY)) {
            if (customGetPrescriptionIdsThrowable != null) {
                throw customGetPrescriptionIdsThrowable!!
            } else if (customGetPrescriptionIdsResponsePath != null) {
                return mockResponse(
                    copy,
                    getJsonFromResource(customGetPrescriptionIdsResponsePath!!)
                )
            }
            return mockResponse(
                copy,
                getJsonFromResource(GET_EPHARMACY_CHECKOUT_DATA_EMPTY_RESPONSE_PATH)
            )
        }
        return chain.proceed(chain.request())
    }

    override fun resetInterceptor() {
        customGetPrescriptionIdsResponsePath = null
        customGetPrescriptionIdsThrowable = null
    }
}

const val GET_EPHARMACY_CHECKOUT_QUERY = "getEpharmacyCheckoutData"

const val GET_EPHARMACY_CHECKOUT_DATA_RESPONSE_PATH =
    "ethicaldrug/one_click_checkout_get_epharmacy_checkout_data_response.json"

const val GET_EPHARMACY_CHECKOUT_DATA_EMPTY_RESPONSE_PATH =
    "ethicaldrug/one_click_checkout_get_epharmacy_checkout_data_empty_response.json"

