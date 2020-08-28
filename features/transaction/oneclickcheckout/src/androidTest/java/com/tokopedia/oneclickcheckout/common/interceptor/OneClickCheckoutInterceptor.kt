package com.tokopedia.oneclickcheckout.common.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object OneClickCheckoutInterceptor {
    val cartInterceptor = CartTestInterceptor()
    val preferenceInterceptor = PreferenceTestInterceptor()
    val logisticInterceptor = LogisticTestInterceptor()
    val promoInterceptor = PromoTestInterceptor()
    val checkoutInterceptor = CheckoutTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
                listOf(cartInterceptor, preferenceInterceptor, logisticInterceptor, promoInterceptor, checkoutInterceptor),
                context)
    }

    fun resetAllCustomResponse() {
        cartInterceptor.customGetOccCartThrowable = null
        cartInterceptor.customGetOccCartResponseString = null
        cartInterceptor.customUpdateCartOccThrowable = null
        cartInterceptor.customUpdateCartOccResponseString = null

        preferenceInterceptor.customGetPreferenceListThrowable = null
        preferenceInterceptor.customGetPreferenceListResponseString = null
        preferenceInterceptor.customSetDefaultPreferenceThrowable = null
        preferenceInterceptor.customSetDefaultPreferenceResponseString = null

        logisticInterceptor.customRatesThrowable = null
        logisticInterceptor.customRatesResponseString = null

        promoInterceptor.customValidateUseThrowable = null
        promoInterceptor.customValidateUseResponseString = null

        checkoutInterceptor.customCheckoutThrowable = null
        checkoutInterceptor.customCheckoutResponseString = null
    }
}
