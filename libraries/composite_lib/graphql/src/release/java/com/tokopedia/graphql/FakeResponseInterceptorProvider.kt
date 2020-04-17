package com.tokopedia.graphql

import android.content.Context
import okhttp3.Interceptor

class FakeResponseInterceptorProvider {
    fun getInterceptor(context: Context): Interceptor? {
        return null
    }
}