package com.tokopedia.instantloan.di.module;

import okhttp3.Interceptor;

public interface InstantLoanChuckRouter {
    Interceptor getChuckInterceptor();
}
