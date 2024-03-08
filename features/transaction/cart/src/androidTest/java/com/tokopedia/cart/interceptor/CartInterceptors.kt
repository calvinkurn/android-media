package com.tokopedia.cart.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object CartInterceptors {

    val cartInterceptor = CartTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(
                cartInterceptor
            ),
            context
        )
    }

    fun resetAllCustomResponse() {
        cartInterceptor.resetInterceptor()
    }
}
