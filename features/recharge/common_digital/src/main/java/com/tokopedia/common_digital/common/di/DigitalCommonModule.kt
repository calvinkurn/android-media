package com.tokopedia.common_digital.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.common_digital.cart.data.mapper.CartMapperData
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData
import com.tokopedia.common_digital.cart.domain.usecase.DigitalAddToCartUseCase
import com.tokopedia.common_digital.cart.domain.usecase.DigitalGetCartUseCase
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.data.api.DigitalResponseConverter
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Rizky on 13/08/18.
 */
@Module
class DigitalCommonModule {

    @Provides
    @DigitalCommonScope
    fun provideCartMapperData(): ICartMapperData {
        return CartMapperData()
    }

    @Provides
    @DigitalCommonScope
    fun provideRechargePushEventRecommendationUseCase(@ApplicationContext context: Context): RechargePushEventRecommendationUseCase {
        return RechargePushEventRecommendationUseCase(GraphqlUseCase(), context)
    }

    @Provides
    @DigitalCommonScope
    fun provideRechargeAnalytics(rechargePushEventRecommendationUseCase: RechargePushEventRecommendationUseCase): RechargeAnalytics {
        return RechargeAnalytics(rechargePushEventRecommendationUseCase)
    }

    @Provides
    @DigitalCommonScope
    fun provideDigitalAddToCartUseCase(listInterceptor: ArrayList<Interceptor>, @ApplicationContext context: Context): DigitalAddToCartUseCase {
        return DigitalAddToCartUseCase(listInterceptor, context)
    }

    @Provides
    @DigitalCommonScope
    fun provideDigitalGetCartUseCase(listInterceptor: ArrayList<Interceptor>, @ApplicationContext context: Context): DigitalGetCartUseCase {
        return DigitalGetCartUseCase(listInterceptor, context)
    }

    @Provides
    @DigitalCommonScope
    fun provideDigitalInterceptorNew(digitalInterceptor: DigitalInterceptor): ArrayList<Interceptor> {
        val listInterceptor = ArrayList<Interceptor>()
        listInterceptor.add(digitalInterceptor)
        listInterceptor.add(ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse::class.java))
        return listInterceptor
    }


    //region REST API FOR DIGITAL PRODUCT MODULE ----------------------------------

    @DigitalCommonScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        if (context is NetworkRouter) {
            return context
        }
        throw RuntimeException("Application must implement " + NetworkRouter::class.java.canonicalName)
    }

    @DigitalCommonScope
    @Provides
    @DigitalCommonChuckQualifier
    fun provideChuckInterceptor(@ApplicationContext context: Context): Interceptor {
        return ChuckerInterceptor(context)
    }

    @DigitalCommonScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

    @Provides
    @DigitalCommonScope
    fun provideDigitalInterceptor(@ApplicationContext context: Context,
                                  networkRouter: AbstractionRouter): DigitalInterceptor {
        return DigitalInterceptor(context, networkRouter)
    }

    @Provides
    @DigitalCommonScope
    @DigitalRestApiClient
    fun provideDigitalRestApiOkHttpClient(@ApplicationContext context: Context,
                                          @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                          digitalInterceptor: DigitalInterceptor,
                                          networkRouter: NetworkRouter,
                                          userSession: UserSession,
                                          @DigitalCommonChuckQualifier chuckerInterceptor: Interceptor): OkHttpClient {
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

    @DigitalCommonScope
    @DigitalRestApiRetrofit
    @Provides
    fun provideDigitalGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @DigitalCommonScope
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
    @DigitalCommonScope
    @DigitalRestApiRetrofit
    fun provideDigitalRestApiRetrofit(@DigitalRestApiClient okHttpClient: OkHttpClient,
                                      @DigitalRestApiRetrofit retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(DigitalUrl.BASE_URL).client(okHttpClient).build()
    }

    //endregion

    companion object {
        private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }
}
