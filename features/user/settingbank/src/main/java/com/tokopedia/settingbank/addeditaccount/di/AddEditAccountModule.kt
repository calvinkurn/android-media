package com.tokopedia.settingbank.addeditaccount.di

import android.app.Activity
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.settingbank.addeditaccount.data.AddEditAccountApi
import com.tokopedia.settingbank.addeditaccount.data.AddEditAccountUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by Ade Fulki on 2019-05-15.
 */

@AddEditAccountScope
@Module
class AddEditAccountModule(val activity: Activity) {

    @Provides
    fun getContext(): Context = activity

    @AddEditAccountScope
    @Provides
    fun provideAddEditAccountRetrofit(retrofitBuilder: Retrofit.Builder,
                                      okHttpClient: OkHttpClient): Retrofit{
        return retrofitBuilder.baseUrl(AddEditAccountUrl.BASE_URL).client(okHttpClient).build()
    }

    @AddEditAccountScope
    @Provides
    fun provideAddEditAccountApi(retrofit: Retrofit): AddEditAccountApi{
        return retrofit.create(AddEditAccountApi::class.java)
    }

    @AddEditAccountScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @AddEditAccountScope
    @Provides
    fun provideNetworkRouter(context: Context): NetworkRouter {
        return (context.applicationContext as NetworkRouter)
    }

    @AddEditAccountScope
    @Provides
    fun provideTkpdAuthInterceptor(context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface)
            : TkpdAuthInterceptor = TkpdAuthInterceptor(context, networkRouter, userSession)

    @AddEditAccountScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface)
            : FingerprintInterceptor =  FingerprintInterceptor(networkRouter, userSession)


    @AddEditAccountScope
    @Provides
    fun provideChuckerInterceptor(context: Context): ChuckerInterceptor
            = ChuckerInterceptor(context)

    @AddEditAccountScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor = DebugInterceptor()

    @AddEditAccountScope
    @Provides
    fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            headerErrorResponseInterceptor: HeaderErrorResponseInterceptor,
                            chuckInterceptor: ChuckerInterceptor,
                            debugInterceptor: DebugInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        val builder = OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(headerErrorResponseInterceptor)

        if(GlobalConfig.isAllowDebuggingTools()){
            builder
                .addInterceptor(chuckInterceptor)
                .addInterceptor(debugInterceptor)
                .addInterceptor(httpLoggingInterceptor)
        }

        return builder.build()
    }
}