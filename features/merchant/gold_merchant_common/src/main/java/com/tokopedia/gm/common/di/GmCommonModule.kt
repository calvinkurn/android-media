package com.tokopedia.gm.common.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by milhamj on 12/06/19.
 */
@Module
class GmCommonModule {

    @GmCommonQualifier
    @Provides
    fun provideAccountsAuthorizationInterceptor(@ApplicationContext context: Context): AccountsAuthorizationInterceptor {
        return AccountsAuthorizationInterceptor(context)
    }

    @GmCommonQualifier
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools())
    }

    @GmCommonQualifier
    @Provides
    fun provideOkHttpClient(@GmCommonQualifier chuckInterceptor: ChuckInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            @GmCommonQualifier accountsAuthorizationInterceptor: AccountsAuthorizationInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(accountsAuthorizationInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @GmCommonQualifier
    @Provides
    fun provideVoteRetrofit(retrofitBuilder: Retrofit.Builder,
                            @GmCommonQualifier okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @Provides
    fun provideGMCommonApi(@GmCommonQualifier retrofit: Retrofit): GMCommonApi {
        return retrofit.create(GMCommonApi::class.java)
    }
}