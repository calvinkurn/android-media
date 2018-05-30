package com.tokopedia.imageuploader;

import okhttp3.Interceptor;

public interface ImageUploaderRouter {
    Interceptor getChuckInterceptor();

    Interceptor getAuthInterceptor();
}
