package com.tokopedia.graphql

import android.content.Context
import com.rahullohra.fakeresponse.data.interceptor.GqlTestingInterceptor
import okhttp3.Interceptor

class TestingInterceptorProvider {

    fun getGqlTestingInterceptor(context: Context):Interceptor?{
        return GqlTestingInterceptor(context)
    }

}