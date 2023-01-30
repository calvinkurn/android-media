package com.tokopedia.feedplus.oldFeed.view.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.affiliatecommon.data.network.TopAdsApi
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.play.widget.analytic.impression.DefaultImpressionValidator
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * @author by nisie on 5/15/17.
 */
private const val NET_READ_TIMEOUT = 60
private const val NET_WRITE_TIMEOUT = 60
private const val NET_CONNECT_TIMEOUT = 60
private const val NET_RETRY = 1

@Module
class FeedPlusModule {
    @FeedPlusScope
    @Provides
    fun provideOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                            @FeedPlusQualifier retryPolicy: OkHttpRetryPolicy,
                            @FeedPlusChuckQualifier chuckInterceptor: Interceptor,
                            feedAuthInterceptor: com.tokopedia.feedplus.oldFeed.data.FeedAuthInterceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(feedAuthInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor)
            clientBuilder.addInterceptor(chuckInterceptor)
        }
        return clientBuilder.build()
    }

    @FeedPlusScope
    @FeedPlusQualifier
    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY)
    }

    @FeedPlusScope
    @FeedPlusChuckQualifier
    @Provides
    fun provideChuckInterceptory(@ApplicationContext context: Context): Interceptor {
        val collector = ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(
                context, collector)
    }

    @FeedPlusScope
    @Provides
    fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase {
        return AddToWishlistV2UseCase(graphqlRepository)
    }

    @FeedPlusScope
    @Provides
    fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase {
        return DeleteWishlistV2UseCase(graphqlRepository)
    }

    //SHOP COMMON
    @FeedPlusScope
    @Named("WS")
    @Provides
    fun provideWsRetrofitDomain(okHttpClient: OkHttpClient,
                                retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(TokopediaUrl.getInstance().WS)
                .client(okHttpClient)
                .build()
    }

    @FeedPlusScope
    @Provides
    fun provideToggleFavouriteShopUseCase(@ApplicationContext context: Context): ToggleFavouriteShopUseCase {
        return ToggleFavouriteShopUseCase(GraphqlUseCase(), context.resources)
    }

    @FeedPlusScope
    @Provides
    fun provideTopAdsApi(@Named("WS") retrofit: Retrofit): TopAdsApi {
        return retrofit.create(TopAdsApi::class.java)
    }

    @FeedPlusScope
    @Provides
    fun provideGraphQlRepository(@ApplicationContext context: Context): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @FeedPlusScope
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @FeedPlusScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
    @FeedPlusScope
    @Provides
    fun provideIrisSession(@ApplicationContext context: Context) : IrisSession =
            IrisSession(context)

    @FeedPlusScope
    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @FeedPlusScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }


    @FeedPlusScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSession)
    }

    @FeedPlusScope
    @Provides
    fun providePlayWidgetImpressionValidator(): DefaultImpressionValidator {
        return DefaultImpressionValidator()
    }
}
