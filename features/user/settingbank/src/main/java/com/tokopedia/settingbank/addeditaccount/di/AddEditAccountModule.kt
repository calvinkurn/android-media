package com.tokopedia.settingbank.addeditaccount.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.settingbank.addeditaccount.data.AddEditAccountApi
import com.tokopedia.settingbank.addeditaccount.data.AddEditAccountUrl
import com.tokopedia.settingbank.banklist.data.SettingBankUrl
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
class AddEditAccountModule{

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
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @AddEditAccountScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @AddEditAccountScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface)
            : TkpdAuthInterceptor = TkpdAuthInterceptor(context, networkRouter, userSession)

    @AddEditAccountScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface)
            : FingerprintInterceptor =  FingerprintInterceptor(networkRouter, userSession)


    @AddEditAccountScope
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor
            = ChuckInterceptor(context)

    @AddEditAccountScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor = DebugInterceptor()

    @AddEditAccountScope
    @Provides
    fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            headerErrorResponseInterceptor: HeaderErrorResponseInterceptor,
                            chuckInterceptor: ChuckInterceptor,
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