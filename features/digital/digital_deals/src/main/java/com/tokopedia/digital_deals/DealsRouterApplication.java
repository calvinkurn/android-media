package com.tokopedia.digital_deals;

import okhttp3.Interceptor;

public interface DealsRouterApplication {


    public Interceptor getChuckInterceptor();
}
