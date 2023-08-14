package com.tokopedia.pdpCheckout.testing.oneclickcheckout.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object OneClickCheckoutInterceptor {
    val cartInterceptor = OccCartTestInterceptor()
    val preferenceInterceptor = PreferenceTestInterceptor()
    val logisticInterceptor = OccLogisticTestInterceptor()
    val promoInterceptor = OccPromoTestInterceptor()
    val checkoutInterceptor = OccCheckoutTestInterceptor()
    val paymentInterceptor = OccPaymentTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(cartInterceptor, preferenceInterceptor, logisticInterceptor, promoInterceptor, checkoutInterceptor, paymentInterceptor),
            context
        )
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
