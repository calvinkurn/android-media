package com.tokopedia.recentview.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.recentview.data.api.RecentViewApi
import com.tokopedia.recentview.data.api.RecentViewUrl
import com.tokopedia.recentview.data.mapper.RecentViewMapper
import com.tokopedia.recentview.domain.usecase.RecentViewUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.data.interceptor.MojitoInterceptor
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author by milhamj on 15/08/18.
 */
@Module
class RecentViewModule {
    @Provides
    @RecentViewQualifier
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(
                context, collector)
    }

    @RecentViewScope
    @Provides
    @RecentViewQualifier
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @RecentViewScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @RecentViewScope
    @Provides
    fun provideMojitoInterceptor(@ApplicationContext context: Context,
                                 networkRouter: NetworkRouter,
                                 userSession: UserSessionInterface): MojitoInterceptor {
        return MojitoInterceptor(context, networkRouter, userSession)
    }

    @RecentViewScope
    @Provides
    @RecentViewQualifier
    fun provideMojitoOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                  @RecentViewQualifier retryPolicy: OkHttpRetryPolicy,
                                  @RecentViewQualifier chuckInterceptor: ChuckerInterceptor,
                                  errorResponseInterceptor: HeaderErrorResponseInterceptor,
                                  mojitoInterceptor: MojitoInterceptor): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(mojitoInterceptor)
                .addInterceptor(errorResponseInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
            clientBuilder.addInterceptor(chuckInterceptor)
        }
        return clientBuilder.build()
    }

    @RecentViewScope
    @Provides
    fun provideRecentProductService(builder: Retrofit.Builder,
                                    @RecentViewQualifier okHttpClient: OkHttpClient): RecentViewApi {
        return builder.baseUrl(RecentViewUrl.MOJITO_DOMAIN)
                .client(okHttpClient)
                .build()
                .create(RecentViewApi::class.java)
    }

    @RecentViewScope
    @Provides
    fun providesTkpTkpdAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @RecentViewScope
    @Provides
    fun providesTkpdRemoveWishListUseCase(@ApplicationContext context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @RecentViewScope
    fun providesGraphqlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @Provides
    @RecentViewScope
    fun provideRecentViewUseCase(graphqlRepository: GraphqlRepository, recentViewMapper: RecentViewMapper): RecentViewUseCase {
        return RecentViewUseCase(graphqlRepository, recentViewMapper )
    }

    @Provides
    @RecentViewScope
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @Provides
    @RecentViewScope
    fun provideRecentViewRecentViewMapper(): RecentViewMapper {
        return RecentViewMapper()
    }
}