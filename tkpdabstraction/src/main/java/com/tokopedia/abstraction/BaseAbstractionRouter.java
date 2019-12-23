package com.tokopedia.abstraction;

import okhttp3.Interceptor;

public interface BaseAbstractionRouter {
    Interceptor getChuckerInterceptor();
}
