package com.tokopedia.product.addedit.description.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.product.addedit.description.data.remote.ProductVariantService
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module(includes = [AddEditProductDescriptionViewModelModule::class])
@AddEditProductDescriptionScope
class AddEditProductDescriptionModule {

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @AddEditProductDescriptionScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSession: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface):
            FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideOkHttpClient(chuckInterceptor: ChuckerInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            debugInterceptor: DebugInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            tkpdAuthInterceptor: TkpdAuthInterceptor): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()

        builder.addInterceptor(fingerprintInterceptor)
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
            builder.addInterceptor(debugInterceptor)
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @AddEditProductDescriptionScope
    @Provides
    fun provideTickerRetrofit(builder: Retrofit.Builder,
                              @AddEditProductDescriptionScope okHttpClient: OkHttpClient): Retrofit {
        return builder.baseUrl(ProductVariantService.BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @AddEditProductDescriptionScope
    @Provides
    fun provideTickerService(retrofit: Retrofit): ProductVariantService {
        return retrofit.create(ProductVariantService::class.java)
    }
}
