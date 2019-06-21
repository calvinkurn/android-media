package com.tokopedia.settingbank.banklist.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.settingbank.addeditaccount.di.AddEditAccountScope
import com.tokopedia.settingbank.banklist.data.SettingBankApi
import com.tokopedia.settingbank.banklist.data.SettingBankUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by Ade Fulki on 2019-05-16.
 * ade.hadian@tokopedia.com
 */

@SettingBankScope
@Module
class SettingBankModule{

    @SettingBankScope
    @Provides
    fun provideSettingBankRetrofit(retrofitBuilder: Retrofit.Builder,
                                   okHttpClient: OkHttpClient): Retrofit{
        return retrofitBuilder.baseUrl(SettingBankUrl.BASE_URL).client(okHttpClient).build()
    }

    @SettingBankScope
    @Provides
    fun provideSettingBankApi(retrofit: Retrofit): SettingBankApi{
        return retrofit.create(SettingBankApi::class.java)
    }

    @SettingBankScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SettingBankScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @SettingBankScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface)
            : TkpdAuthInterceptor = TkpdAuthInterceptor(context, networkRouter, userSession)

    @SettingBankScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface)
            : FingerprintInterceptor =  FingerprintInterceptor(networkRouter, userSession)


    @SettingBankScope
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor
            = ChuckInterceptor(context)

    @SettingBankScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor = DebugInterceptor()

    @SettingBankScope
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