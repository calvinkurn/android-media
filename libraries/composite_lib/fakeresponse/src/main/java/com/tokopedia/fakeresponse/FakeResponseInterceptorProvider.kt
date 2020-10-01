package com.tokopedia.fakeresponse

import android.content.Context
import com.tokopedia.fakeresponse.data.interceptor.GqlTestingInterceptor
import com.tokopedia.fakeresponse.data.interceptor.RestInterceptor
import okhttp3.Interceptor

class FakeResponseInterceptorProvider {
    fun getGqlInterceptor(context: Context): Interceptor? {
        return GqlTestingInterceptor(context)
    }

    fun getRestInterceptor(context: Context): Interceptor? {
        return RestInterceptor(context)
    }
}