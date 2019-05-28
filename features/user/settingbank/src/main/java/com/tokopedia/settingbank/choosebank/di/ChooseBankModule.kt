package com.tokopedia.settingbank.choosebank.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.settingbank.addeditaccount.di.AddEditAccountScope
import com.tokopedia.settingbank.choosebank.data.BankListApi
import com.tokopedia.settingbank.choosebank.data.BankListUrl
import com.tokopedia.settingbank.choosebank.data.database.BankDao
import com.tokopedia.settingbank.choosebank.data.database.BankDatabase
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

@ChooseBankScope
@Module
class ChooseBankModule{
    @ChooseBankScope
    @Provides
    fun provideBankListRetrofit(retrofitBuilder: Retrofit.Builder,
                                   okHttpClient: OkHttpClient): Retrofit{
        return retrofitBuilder.baseUrl(BankListUrl.BASE_URL).client(okHttpClient).build()
    }

    @ChooseBankScope
    @Provides
    fun provideBankListApi(retrofit: Retrofit): BankListApi{
        return retrofit.create(BankListApi::class.java)
    }

    @ChooseBankScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ChooseBankScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @ChooseBankScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface)
            : TkpdAuthInterceptor = TkpdAuthInterceptor(context, networkRouter, userSession)

    @ChooseBankScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface)
            : FingerprintInterceptor =  FingerprintInterceptor(networkRouter, userSession)


    @ChooseBankScope
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor
            = ChuckInterceptor(context)

    @ChooseBankScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor = DebugInterceptor()

    @ChooseBankScope
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

    @ChooseBankScope
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler{
        return LocalCacheHandler(context, "FETCH_BANK")
    }

    @ChooseBankScope
    @Provides
    fun provideBankDatabase(@ApplicationContext context: Context): BankDatabase{
        return BankDatabase.getDataBase(context)!!
    }

    @ChooseBankScope
    @Provides
    fun provideBankDao(bankDatabase: BankDatabase): BankDao{
        return bankDatabase.bankDao()
    }
}