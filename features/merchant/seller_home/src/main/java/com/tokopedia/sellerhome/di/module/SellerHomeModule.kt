package com.tokopedia.sellerhome.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.HeaderErrorListResponse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.data.remote.TickerService
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@Module
class SellerHomeModule {

    @SellerHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerHomeScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @SellerHomeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @SellerHomeScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @SellerHomeScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SellerHomeScope
    @Provides
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

    @SellerHomeScope
    @Provides
    fun provideOkHttpClient(@SellerHomeScope chuckInterceptor: ChuckerInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor.apply
                    { level = HttpLoggingInterceptor.Level.BODY })
        }
        return builder.build()
    }

    @SellerHomeScope
    @Provides
    fun provideTickerRetrofit(builder: Retrofit.Builder,
                              @SellerHomeScope okHttpClient: OkHttpClient): Retrofit {
        return builder.baseUrl(TickerService.BASE_URL).client(okHttpClient).build()
    }

    @SellerHomeScope
    @Provides
    fun provideTickerService(retrofit: Retrofit): TickerService {
        return retrofit.create(TickerService::class.java)
    }

    @SellerHomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
            FirebaseRemoteConfigImpl(context)

    @SellerHomeScope
    @Provides
    fun provideSellerHomeRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): SellerHomeRemoteConfig {
        return SellerHomeRemoteConfig(remoteConfig)
    }
}