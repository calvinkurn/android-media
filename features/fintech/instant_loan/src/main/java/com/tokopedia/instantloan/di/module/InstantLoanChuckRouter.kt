package com.tokopedia.instantloan.di.module

import okhttp3.Interceptor

interface InstantLoanChuckRouter {
    val chuckInterceptor: Interceptor
}
