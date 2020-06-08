package com.tokopedia.favorite.di.modul;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.favorite.data.source.apis.interceptor.TopAdsAuthInterceptor;
import com.tokopedia.favorite.di.scope.FavoriteScope;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@FavoriteScope
@Module
public class TopAdsAuthInterceptorModule {

    @FavoriteScope
    @Provides
    public TopAdsAuthInterceptor provideTopAdsAuthInterceptor(@ApplicationContext Context context, UserSessionInterface userSessionInterface) {
        return new TopAdsAuthInterceptor(context, (NetworkRouter)context, userSessionInterface);
    }
}
