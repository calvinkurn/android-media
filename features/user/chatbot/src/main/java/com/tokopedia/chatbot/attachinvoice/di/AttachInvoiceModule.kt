package com.tokopedia.chatbot.attachinvoice.di

import android.content.Context

import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chatbot.data.network.ChatBotApi
import com.tokopedia.chatbot.data.network.ChatbotUrl
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.AuthUtil

import java.util.concurrent.TimeUnit

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by Hendri on 05/04/18.
 */
@Module
class AttachInvoiceModule {

    @AttachInvoiceScope
    @Provides
    internal fun provideOkHttpClient(@ApplicationContext context: Context,
                                     retryPolicy: OkHttpRetryPolicy): OkHttpClient? {

        //        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
        //                .addInterceptor(new FingerprintInterceptor())
        //                .addInterceptor(new CacheApiInterceptor())
        //                .addInterceptor(new DigitalHmacAuthInterceptor(AuthUtil.KEY.KEY_WSV4))
        //                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
        //                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
        //                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS);
        //
        //        if (GlobalConfig.isAllowDebuggingTools()) {
        //            clientBuilder.addInterceptor(new HttpLoggingInterceptor());
        //            clientBuilder.addInterceptor(new ChuckInterceptor(context));
        //        }
        //
        //        return clientBuilder.build();
        return null
    }

    @AttachInvoiceScope
    @Provides
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY)
    }

    @AttachInvoiceScope
    @Provides
    internal fun provideChatRetrofit(okHttpClient: OkHttpClient,
                                     retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(ChatbotUrl.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @AttachInvoiceScope
    @Provides
    internal fun provideChatRatingApi(retrofit: Retrofit): ChatBotApi {
        return retrofit.create(ChatBotApi::class.java)
    }

    companion object {
        private val NET_READ_TIMEOUT = 60
        private val NET_WRITE_TIMEOUT = 60
        private val NET_CONNECT_TIMEOUT = 60
        private val NET_RETRY = 1
    }
}
