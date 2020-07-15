package com.tokopedia.fakeresponse

import android.content.Context
import com.tokopedia.fakeresponse.data.interceptor.GqlTestingInterceptor
import okhttp3.Interceptor

class FakeResponseInterceptorProvider {
    fun getInterceptor(context: Context): Interceptor? {
        return GqlTestingInterceptor(context)
    }
}