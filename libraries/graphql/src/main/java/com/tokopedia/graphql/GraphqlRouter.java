package com.tokopedia.graphql;

import okhttp3.Interceptor;

public interface GraphqlRouter {
    Interceptor getChuckInterceptor();
}
