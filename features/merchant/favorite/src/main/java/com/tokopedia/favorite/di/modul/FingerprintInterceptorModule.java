package com.tokopedia.favorite.di.modul;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.favorite.di.scope.FavoriteScope;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@FavoriteScope
@Module
public class FingerprintInterceptorModule {

    @FavoriteScope
    @Provides
    public FingerprintInterceptor provideFingerprintInterceptor(@ApplicationContext Context context, UserSessionInterface userSession) {
        return new FingerprintInterceptor((NetworkRouter) context, userSession);
    }
}
