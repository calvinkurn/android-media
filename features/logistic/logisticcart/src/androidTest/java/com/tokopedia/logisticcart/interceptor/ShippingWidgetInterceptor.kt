package com.tokopedia.logisticcart.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object ShippingWidgetInterceptor {

    val shippingWidgetTestInterceptor = ShippingWidgetTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(
                shippingWidgetTestInterceptor
            ),
            context
        )
    }

    fun resetAllCustomResponse() {
        shippingWidgetTestInterceptor.resetInterceptor()
    }
}
