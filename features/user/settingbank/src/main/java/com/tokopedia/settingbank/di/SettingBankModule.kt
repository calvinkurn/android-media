package com.tokopedia.settingbank.di

import android.app.Activity
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.data.SettingBankApi
import com.tokopedia.settingbank.data.SettingBankUrl
import com.tokopedia.settingbank.view.adapter.BankAccountListAdapter
import com.tokopedia.settingbank.view.adapter.BankListAdapter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
class SettingBankModule(val activity: Activity) {

    @Provides
    fun getContext(): Context = activity

    @SettingBankScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SettingBankScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @SettingBankScope
    @Provides
    fun provideBankAccountAdapter(): BankAccountListAdapter = BankAccountListAdapter(arrayListOf())

    @SettingBankScope
    @Provides
    fun provideBankSettingAnalytics(): BankSettingAnalytics = BankSettingAnalytics()

    @SettingBankScope
    @Provides
    fun provideBankAdapter(): BankListAdapter = BankListAdapter(arrayListOf())


    @SettingBankScope
    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SettingBankScope
    @Provides
    fun provideSettingBankRetrofit(retrofitBuilder: Retrofit.Builder,
                                   okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(SettingBankUrl.BASE_URL).client(okHttpClient).build()
    }

    @SettingBankScope
    @Provides
    fun provideSettingBankApi(retrofit: Retrofit): SettingBankApi {
        return retrofit.create(SettingBankApi::class.java)
    }


    @SettingBankScope
    @Provides
    fun provideNetworkRouter(context: Context): NetworkRouter {
        return (context.applicationContext as NetworkRouter)
    }

    @SettingBankScope
    @Provides
    fun provideTkpdAuthInterceptor(context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface)
            : TkpdAuthInterceptor = TkpdAuthInterceptor(context, networkRouter, userSession)

    @SettingBankScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface)
            : FingerprintInterceptor = FingerprintInterceptor(networkRouter, userSession)


    @SettingBankScope
    @Provides
    fun provideChuckerInterceptor(context: Context): ChuckerInterceptor = ChuckerInterceptor(context)

    @SettingBankScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor = DebugInterceptor()

    @SettingBankScope
    @Provides
    fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            headerErrorResponseInterceptor: HeaderErrorResponseInterceptor,
                            chuckerInterceptor: ChuckerInterceptor,
                            debugInterceptor: DebugInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(headerErrorResponseInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckerInterceptor)
                    .addInterceptor(debugInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
        }

        return builder.build()
    }

}
