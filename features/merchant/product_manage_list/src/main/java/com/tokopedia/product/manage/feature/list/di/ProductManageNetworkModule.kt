package com.tokopedia.product.manage.feature.list.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.gm.common.constant.GMCommonUrl
import com.tokopedia.gm.common.data.interceptor.GMAuthInterceptor
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
class ProductManageNetworkModule {

    @Provides
    @ProductManageListScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
                context = context,
                showNotification = GlobalConfig.isAllowDebuggingTools()
        )

        return ChuckerInterceptor(
                context = context,
                collector = collector
        )
    }

    @GMProductManageQualifier
    @ProductManageListScope
    @Provides
    fun provideGMRetrofit(@GMProductManageQualifier okHttpClient: OkHttpClient,
                          retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @ProductManageListScope
    @Provides
    fun provideGMAuthInterceptor(@ApplicationContext context: Context,
                                 abstractionRouter: AbstractionRouter): GMAuthInterceptor {
        return GMAuthInterceptor(context, abstractionRouter)
    }

    @GMProductManageQualifier
    @Provides
    fun provideGMOkHttpClient(gmAuthInterceptor: GMAuthInterceptor,
                              chuckInterceptor: ChuckerInterceptor,
                              httpLoggingInterceptor: HttpLoggingInterceptor,
                              cacheApiInterceptor: CacheApiInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .addInterceptor(gmAuthInterceptor)
                .addInterceptor(cacheApiInterceptor)
                .addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
                .addInterceptor(httpLoggingInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor.apply
                    { level = HttpLoggingInterceptor.Level.BODY })
        }

        return builder.build()
    }


    @Provides
    @ProductManageListScope
    fun provideGmCommonApi(@GMProductManageQualifier retrofit: Retrofit): GMCommonApi {
        return retrofit.create(GMCommonApi::class.java)
    }

    @ProductManageListScope
    @Provides
    fun provideApiCacheInterceptor(@ApplicationContext context: Context): CacheApiInterceptor {
        return CacheApiInterceptor(context)
    }

}