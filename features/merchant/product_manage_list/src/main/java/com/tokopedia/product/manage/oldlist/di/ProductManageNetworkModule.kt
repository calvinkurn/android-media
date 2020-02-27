package com.tokopedia.product.manage.oldlist.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
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
    @OldProductManageScope
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools())
    }

    @OldGMProductManageQualifier
    @OldProductManageScope
    @Provides
    fun provideGMRetrofit(@OldGMProductManageQualifier okHttpClient: OkHttpClient,
                          retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(GMCommonUrl.BASE_URL).client(okHttpClient).build()
    }

    @OldProductManageScope
    @Provides
    fun provideGMAuthInterceptor(@ApplicationContext context: Context,
                                 abstractionRouter: AbstractionRouter): GMAuthInterceptor {
        return GMAuthInterceptor(context, abstractionRouter)
    }

    @OldGMProductManageQualifier
    @Provides
    fun provideGMOkHttpClient(gmAuthInterceptor: GMAuthInterceptor,
                              chuckInterceptor: ChuckInterceptor,
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
    @OldProductManageScope
    fun provideGmCommonApi(@OldGMProductManageQualifier retrofit: Retrofit): GMCommonApi {
        return retrofit.create(GMCommonApi::class.java)
    }

    @OldProductManageScope
    @Provides
    fun provideApiCacheInterceptor(@ApplicationContext context: Context): CacheApiInterceptor {
        return CacheApiInterceptor(context)
    }

}