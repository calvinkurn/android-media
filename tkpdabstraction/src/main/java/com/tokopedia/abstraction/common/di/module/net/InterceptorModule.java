package com.tokopedia.abstraction.common.di.module.net;

import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.exception.HeaderErrorResponse;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.config.GlobalConfig;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ricoharisin on 3/22/17.
 */

@Module
public class InterceptorModule {

    @ApplicationScope
    @Provides
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return logging;
    }

    @ApplicationScope
    @Provides
    HeaderErrorResponseInterceptor provideHeaderErrorResponseInterceptor() {
        return new HeaderErrorResponseInterceptor(HeaderErrorResponse.class);
    }

}
