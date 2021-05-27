package com.tokopedia.logisticorder.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticorder.domain.service.GetDeliveryImageApi
import com.tokopedia.logisticorder.domain.service.GetDeliveryImageDataSource
import com.tokopedia.logisticorder.domain.service.GetDeliveryImageRepository
import com.tokopedia.logisticorder.domain.service.GetDeliveryImageRepositoryImpl
import com.tokopedia.logisticorder.usecase.GetDeliveryImageUseCase
import com.tokopedia.logisticorder.utils.TrackingPageUrl
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
object TrackingPageModule {
    @Provides
    @ActivityScope
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @ActivityScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @ActivityScope
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @Provides
    @ActivityScope
    fun provideGetDeliveryImageRepository(getDeliveryImageDataSource: GetDeliveryImageDataSource) : GetDeliveryImageRepository {
        return GetDeliveryImageRepositoryImpl(getDeliveryImageDataSource)
    }

    @Provides
    @ActivityScope
    fun provideGetDeliveryImageApi(retrofit: Retrofit) : GetDeliveryImageApi {
        return retrofit.create(GetDeliveryImageApi::class.java)
    }

    @ActivityScope
    @Provides
    fun provideGetDeliveryImageRetrofit(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder
                .baseUrl(TrackingPageUrl.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    @Provides
    @ActivityScope
    fun provideOkHttpClient(tkpdAuthInterceptor: TkpdAuthInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
                .addInterceptor(tkpdAuthInterceptor)

        return builder.build()
    }


    @Provides
    @ActivityScope
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @ActivityScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @ActivityScope
    @Provides
    fun provideAuthInterceptors(@ApplicationContext context: Context,
                                userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    }


    @Provides
    @ActivityScope
    fun provideInterceptors(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            chuckerInterceptor: ChuckerInterceptor): MutableList<Interceptor> {
        return mutableListOf(tkpdAuthInterceptor, fingerprintInterceptor, httpLoggingInterceptor, chuckerInterceptor)
    }

    @Provides
    @ActivityScope
    fun provideGetDeliveryImageUseCase(repository: RestRepository): GetDeliveryImageUseCase {
        return GetDeliveryImageUseCase(repository)
    }

}