package com.tokopedia.search.result.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@Module
public class TopAdsAuthInterceptorModule {

    @SearchScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthInterceptor(@ApplicationContext Context context, UserSessionInterface userSessionInterface) {
        return new TopAdsAuthInterceptor(context, (NetworkRouter)context, userSessionInterface);
    }
}
