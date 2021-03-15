package com.tokopedia.digital.common.di


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.data.api.DigitalResponseConverter
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital.common.analytic.DigitalAnalytics
import com.tokopedia.digital.newcart.domain.mapper.CartMapperData
import com.tokopedia.digital.newcart.domain.mapper.ICartMapperData
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class DigitalModule {

    @Provides
    fun provideDigitalAnalytics(): DigitalAnalytics {
        return DigitalAnalytics()
    }

    //region REST API FOR DIGITAL PRODUCT MODULE ----------------------------------

    @Provides
    @DigitalScope
    fun provideCartMapperData(): ICartMapperData {
        return CartMapperData()
    }

    @DigitalScope
    @Provides
    @DigitalChuckQualifier
    fun provideChuckInterceptor(@ApplicationContext context: Context): Interceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @DigitalRestApiClient
    fun provideDigitalRestApiOkHttpClient(@ApplicationContext context: Context,
                                          @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                          digitalInterceptor: DigitalInterceptor,
                                          networkRouter: NetworkRouter,
                                          userSession: UserSessionInterface,
                                          @DigitalChuckQualifier chuckerInterceptor: Interceptor): OkHttpClient {
        val retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
        val builder = OkHttpClient.Builder()
                .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)

        builder.addInterceptor(digitalInterceptor)
        builder.addInterceptor(FingerprintInterceptor(networkRouter, userSession))
        builder.addInterceptor(ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse::class.java))
        builder.addInterceptor(AkamaiBotInterceptor(context))

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckerInterceptor)
        }

        return builder.build()
    }

    @DigitalRestApiRetrofit
    @Provides
    fun provideDigitalGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @Provides
    @DigitalRestApiRetrofit
    fun provideRetrofitBuilder(@DigitalRestApiRetrofit gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(DigitalResponseConverter())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    @Provides
    @DigitalRestApiRetrofit
    fun provideDigitalRestApiRetrofit(@DigitalRestApiClient okHttpClient: OkHttpClient,
                                      @DigitalRestApiRetrofit retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(DigitalUrl.BASE_URL).client(okHttpClient).build()
    }

    //endregion

    companion object {
        private const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }
}
