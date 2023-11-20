package com.tokopedia.checkout.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object CheckoutInterceptor {

    val cartInterceptor = CartTestInterceptor()
    val logisticInterceptor = LogisticTestInterceptor()
    val promoInterceptor = PromoTestInterceptor()
    val checkoutInterceptor = CheckoutTestInterceptor()
    val paymentInterceptor = PaymentTestInterceptor()
    val ethicalDrugTestInterceptor = EthicalDrugTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(
                cartInterceptor,
                logisticInterceptor,
                promoInterceptor,
                checkoutInterceptor,
                paymentInterceptor,
                ethicalDrugTestInterceptor
            ),
            context
        )
    }

    fun resetAllCustomResponse() {
        cartInterceptor.resetInterceptor()
        logisticInterceptor.resetInterceptor()
        promoInterceptor.resetInterceptor()
        checkoutInterceptor.resetInterceptor()
        paymentInterceptor.resetInterceptor()
        ethicalDrugTestInterceptor.resetInterceptor()
    }
}
