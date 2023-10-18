package com.tokopedia.checkout.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

class CheckoutInterceptor {

    lateinit var cartInterceptor: CartTestInterceptor
    lateinit var logisticInterceptor: LogisticTestInterceptor
    lateinit var promoInterceptor: PromoTestInterceptor
    lateinit var checkoutInterceptor: CheckoutTestInterceptor
    lateinit var paymentInterceptor: PaymentTestInterceptor
    lateinit var ethicalDrugTestInterceptor: EthicalDrugTestInterceptor

    fun setupGraphqlMockResponse(context: Context) {
        cartInterceptor = CartTestInterceptor(context)
        logisticInterceptor = LogisticTestInterceptor(context)
        promoInterceptor = PromoTestInterceptor(context)
        checkoutInterceptor = CheckoutTestInterceptor(context)
        paymentInterceptor = PaymentTestInterceptor(context)
        ethicalDrugTestInterceptor = EthicalDrugTestInterceptor(context)

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
