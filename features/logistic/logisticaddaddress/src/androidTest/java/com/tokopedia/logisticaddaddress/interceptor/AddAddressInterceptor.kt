package com.tokopedia.logisticaddaddress.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object AddAddressInterceptor {
    val logisticInterceptor = LogisticTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(logisticInterceptor),
            context
        )
    }

    fun resetAllCustomResponse() {
        logisticInterceptor.resetInterceptor()
    }
}
