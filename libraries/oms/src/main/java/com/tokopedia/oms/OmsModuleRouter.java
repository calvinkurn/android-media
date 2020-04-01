package com.tokopedia.oms;

import okhttp3.Interceptor;

public interface OmsModuleRouter {
    Interceptor getChuckerInterceptor();
}
