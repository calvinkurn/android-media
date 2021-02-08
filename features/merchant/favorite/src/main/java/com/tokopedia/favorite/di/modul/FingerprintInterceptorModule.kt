package com.tokopedia.favorite.di.modul

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.favorite.di.scope.FavoriteScope
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class FingerprintInterceptorModule {

    @FavoriteScope
    @Provides
    fun provideFingerprintInterceptor(
            @ApplicationContext context: Context,
            userSession: UserSessionInterface?
    ): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter, userSession)
    }

}
