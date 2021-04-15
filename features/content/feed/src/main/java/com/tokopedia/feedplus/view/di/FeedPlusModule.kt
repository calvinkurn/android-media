package com.tokopedia.feedplus.view.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.data.network.TopAdsApi
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedAuthInterceptor
import com.tokopedia.feedplus.data.api.FeedUrl
import com.tokopedia.feedplus.view.listener.DynamicFeedContract
import com.tokopedia.feedplus.view.presenter.DynamicFeedPresenter
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.play.widget.analytic.impression.DefaultImpressionValidator
import com.tokopedia.shop.common.data.repository.ShopCommonRepositoryImpl
import com.tokopedia.shop.common.data.source.ShopCommonDataSource
import com.tokopedia.shop.common.data.source.cloud.ShopCommonCloudDataSource
import com.tokopedia.shop.common.data.source.cloud.api.ShopCommonApi
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.domain.repository.ShopCommonRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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
    fun provideOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor?,
                            @FeedPlusQualifier retryPolicy: OkHttpRetryPolicy,
                            @FeedPlusChuckQualifier chuckInterceptor: Interceptor?,
                            feedAuthInterceptor: FeedAuthInterceptor?): OkHttpClient {
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
    fun providesTkpTkpdAddWishListUseCase(@ApplicationContext context: Context): AddWishListUseCase {
        return AddWishListUseCase(context)
    }

    @FeedPlusScope
    @Provides
    fun providesTkpdRemoveWishListUseCase(@ApplicationContext context: Context): RemoveWishListUseCase {
        return RemoveWishListUseCase(context)
    }

    @FeedPlusScope
    @Provides
    fun provideDynamicFeedPresenter(userSession: UserSessionInterface,
                                    getDynamicFeedUseCase: GetDynamicFeedUseCase,
                                    likeKolPostUseCase: LikeKolPostUseCase,
                                    trackAffiliateClickUseCase: TrackAffiliateClickUseCase): DynamicFeedContract.Presenter {
        return DynamicFeedPresenter(userSession, getDynamicFeedUseCase, likeKolPostUseCase, trackAffiliateClickUseCase)
    }

    //SHOP COMMON
    @FeedPlusScope
    @Named("WS")
    @Provides
    fun provideWsRetrofitDomain(okHttpClient: OkHttpClient,
                                retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(FeedUrl.BASE_DOMAIN)
                .client(okHttpClient)
                .build()
    }

    @FeedPlusScope
    @Named("TOME")
    @Provides
    fun provideTomeRetrofitDomain(okHttpClient: OkHttpClient,
                                  retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(FeedUrl.TOME_DOMAIN)
                .client(okHttpClient)
                .build()
    }

    @FeedPlusScope
    @Provides
    fun provideShopCommonApi(@Named("TOME") retrofit: Retrofit): ShopCommonApi {
        return retrofit.create(ShopCommonApi::class.java)
    }

    @FeedPlusScope
    @Provides
    fun provideShopCommonCloudDataSource(shopCommonApi: ShopCommonApi): ShopCommonCloudDataSource {
        return ShopCommonCloudDataSource(shopCommonApi)
    }

    @FeedPlusScope
    @Provides
    fun provideShopCommonDataSource(shopInfoCloudDataSource: ShopCommonCloudDataSource): ShopCommonDataSource {
        return ShopCommonDataSource(shopInfoCloudDataSource)
    }

    @FeedPlusScope
    @Provides
    fun provideShopCommonRepository(shopInfoDataSource: ShopCommonDataSource): ShopCommonRepository {
        return ShopCommonRepositoryImpl(shopInfoDataSource)
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

    @Provides
    @FeedPlusScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.atc_common.R.raw.mutation_add_to_cart)
    }

    @FeedPlusScope
    @Provides
    fun provideGraphQlRepository(@ApplicationContext context: Context): GraphqlRepository {
        GraphqlClient.init(context)
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @FeedPlusScope
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @Provides
    @FeedPlusScope
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @FeedPlusScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @FeedPlusScope
    @Provides
    fun provideBaseRepository(): BaseRepository {
        return BaseRepository()
    }

    @FeedPlusScope
    @Provides
    @Named(RawQueryKeyConstant.GQL_QUERY_FEED_DETAIL)
    fun provideFeedDetailQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_feed_detail)
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