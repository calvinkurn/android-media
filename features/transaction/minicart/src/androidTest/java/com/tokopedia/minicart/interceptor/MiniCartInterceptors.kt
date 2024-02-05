package com.tokopedia.minicart.interceptor

import android.content.Context
import com.tokopedia.graphql.data.GraphqlClient

object MiniCartInterceptors {

    val miniCartInterceptor = MiniCartTestInterceptor()

    fun setupGraphqlMockResponse(context: Context) {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(
                miniCartInterceptor
            ),
            context
        )
    }

    fun resetAllCustomResponse() {
        miniCartInterceptor.resetInterceptor()
    }
}
