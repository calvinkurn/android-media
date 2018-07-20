package com.tokopedia.oms;

import java.io.IOException;

import okhttp3.Interceptor;

public interface OmsModuleRouter {


    Interceptor getChuckInterceptor();

    void handleOmsPromoError(String bodyResponse) throws IOException;
}
