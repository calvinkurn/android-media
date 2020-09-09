package com.tokopedia.oneclickcheckout.common.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object OneClickCheckoutInterceptor {
    val cartInterceptor = CartTestInterceptor()
    val preferenceInterceptor = PreferenceTestInterceptor()
    val logisticInterceptor = LogisticTestInterceptor()
    val promoInterceptor = PromoTestInterceptor()
    val checkoutInterceptor = CheckoutTestInterceptor()
    val paymentInterceptor = PaymentTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
                listOf(cartInterceptor, preferenceInterceptor, logisticInterceptor, promoInterceptor, checkoutInterceptor, paymentInterceptor),
                context)
    }

    fun resetAllCustomResponse() {
        cartInterceptor.resetInterceptor()
        preferenceInterceptor.resetInterceptor()
        logisticInterceptor.resetInterceptor()
        promoInterceptor.resetInterceptor()
        checkoutInterceptor.resetInterceptor()
        paymentInterceptor.resetInterceptor()
    }
}
