package com.tokopedia.favorite.di.modul

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.favorite.data.source.apis.interceptor.TopAdsAuthInterceptor
import com.tokopedia.favorite.di.scope.FavoriteScope
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TopAdsAuthInterceptorModule {

    @FavoriteScope
    @Provides
    fun provideTopAdsAuthInterceptor(
            @ApplicationContext context: Context,
            userSessionInterface: UserSessionInterface
    ): TopAdsAuthInterceptor {
        return TopAdsAuthInterceptor(context, context as NetworkRouter?, userSessionInterface)
    }

}
