package com.tokopedia.gm.common.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.common.R
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.constant.GMParamConstant
import com.tokopedia.gm.common.data.interceptor.PowerMerchantSubscribeInterceptor
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named

/**
 * @author by milhamj on 12/06/19.
 */
@Module
class GmCommonModule {

    @GmCommonQualifier
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools())
    }

    @Provides
    fun providePowerMerchantSubscribeInterceptor(@GmCommonQualifier userSessionInterface: UserSessionInterface): PowerMerchantSubscribeInterceptor {
        return PowerMerchantSubscribeInterceptor(userSessionInterface)
    }

    @GmCommonQualifier
    @Provides
    fun provideOkHttpClient(@GmCommonQualifier chuckInterceptor: ChuckInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            powerMerchantSubscribeInterceptor: PowerMerchantSubscribeInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(powerMerchantSubscribeInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor.apply
                    { level = HttpLoggingInterceptor.Level.BODY })
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

    @GmCommonQualifier
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @Named(GMParamConstant.RAW_DEACTIVATION)
    fun providePmOffRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_turn_off)
    }

    @Provides
    @Named(GMParamConstant.RAW_ACTIVATION)
    fun providePmOnRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_activation)
    }

    @Provides
    @Named(GMParamConstant.RAW_GM_STATUS)
    fun provicePmStatusRaw(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gold_merchant_status)
    }

}