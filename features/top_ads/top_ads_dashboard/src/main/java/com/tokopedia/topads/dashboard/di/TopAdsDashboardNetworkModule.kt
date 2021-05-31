package com.tokopedia.topads.dashboard.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.network.NetworkRouter
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.interceptor.TopAdsAuthInterceptor
import com.tokopedia.topads.common.data.interceptor.TopAdsResponseError
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class TopAdsDashboardNetworkModule {

    @TopAdsDashboardScope
    @Provides
    fun provideTopAdsAuthTempInterceptor(@ApplicationContext context: Context,
                                         userSession : UserSessionInterface ,
                                         abstractionRouter : NetworkRouter ): TopAdsAuthInterceptor {
        return TopAdsAuthInterceptor(context, userSession as UserSession?, abstractionRouter)
    }

    @TopAdsDashboardQualifier
    @TopAdsDashboardScope
    @Provides
    fun provideErrorInterceptor(): ErrorResponseInterceptor {
        return ErrorResponseInterceptor(TopAdsResponseError::class.java)
    }

    @TopAdsDashboardQualifier
    @Provides
    fun provideOkHttpClient(topAdsAuthInterceptor: TopAdsAuthInterceptor,
                            @TopAdsDashboardQualifier errorResponseInterceptor: ErrorResponseInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(topAdsAuthInterceptor)
                .addInterceptor(errorResponseInterceptor)
                .build()
    }

    @TopAdsDashboardQualifier
    @TopAdsDashboardScope
    @Provides
    fun provideRetrofit(@TopAdsDashboardQualifier okHttpClient: OkHttpClient,
                        retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(TopAdsCommonConstant.BASE_DOMAIN_URL).client(okHttpClient).build()
    }

    @TopAdsDashboardQualifier
    @TopAdsDashboardScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context) = UserSession(context)


}