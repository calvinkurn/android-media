package com.tokopedia.fakeresponse

import android.content.Context
import okhttp3.Interceptor

class FakeResponseInterceptorProvider {
    fun getGqlInterceptor(context: Context): Interceptor? {
        return null
    }

    fun getRestInterceptor(context: Context): Interceptor? {
        return null
    }
}