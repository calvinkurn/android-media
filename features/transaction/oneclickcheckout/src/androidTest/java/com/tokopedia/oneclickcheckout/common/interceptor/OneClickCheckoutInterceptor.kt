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
}
