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
        cartInterceptor.customGetOccCartResponsePath = null
        cartInterceptor.customUpdateCartOccThrowable = null
        cartInterceptor.customUpdateCartOccResponsePath = null

        preferenceInterceptor.customGetPreferenceListThrowable = null
        preferenceInterceptor.customGetPreferenceListResponsePath = null
        preferenceInterceptor.customSetDefaultPreferenceThrowable = null
        preferenceInterceptor.customSetDefaultPreferenceResponsePath = null

        logisticInterceptor.customRatesThrowable = null
        logisticInterceptor.customRatesResponsePath = null

        promoInterceptor.customValidateUseThrowable = null
        promoInterceptor.customValidateUseResponsePath = null

        checkoutInterceptor.customCheckoutThrowable = null
        checkoutInterceptor.customCheckoutResponsePath = null
    }
}
