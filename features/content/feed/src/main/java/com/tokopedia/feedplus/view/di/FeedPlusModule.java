package com.tokopedia.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.feedplus.FeedModuleRouter;
import com.tokopedia.feedplus.data.FeedAuthInterceptor;
import com.tokopedia.feedplus.data.api.FeedApi;
import com.tokopedia.feedplus.data.factory.FavoriteShopFactory;
import com.tokopedia.feedplus.data.factory.FeedFactory;
import com.tokopedia.feedplus.data.factory.WishlistFactory;
import com.tokopedia.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.feedplus.data.repository.FavoriteShopRepository;
import com.tokopedia.feedplus.data.repository.FavoriteShopRepositoryImpl;
import com.tokopedia.feedplus.data.repository.FeedRepository;
import com.tokopedia.feedplus.data.repository.FeedRepositoryImpl;
import com.tokopedia.feedplus.data.repository.WishlistRepository;
import com.tokopedia.feedplus.data.repository.WishlistRepositoryImpl;
import com.tokopedia.feedplus.data.source.KolSource;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.wishlist.common.data.interceptor.MojitoInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 5/15/17.
 */

@Module
public class FeedPlusModule {

    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    private static final String NAME_CLOUD = "CLOUD";
    private static final String NAME_LOCAL = "LOCAL";

    @FeedPlusScope
    @Provides
    OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                             httpLoggingInterceptor,
                                     @FeedPlusQualifier OkHttpRetryPolicy retryPolicy,
                                     @FeedPlusChuckQualifier Interceptor chuckInterceptor,
                                     FeedAuthInterceptor feedAuthInterceptor) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(feedAuthInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @FeedPlusScope
    @Provides
    @FeedMojitoQualifier
    OkHttpClient provideMojitoOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                             httpLoggingInterceptor,
                                           @FeedPlusQualifier OkHttpRetryPolicy retryPolicy,
                                           @FeedPlusChuckQualifier Interceptor chuckInterceptor,
                                           HeaderErrorResponseInterceptor errorResponseInterceptor,
                                           MojitoInterceptor mojitoInterceptor) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(mojitoInterceptor)
                .addInterceptor(errorResponseInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @FeedPlusScope
    @FeedPlusQualifier
    @Provides
    OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @FeedPlusScope
    @FeedPlusChuckQualifier
    @Provides
    Interceptor provideChuckInterceptory(@ApplicationContext Context context) {
        if (context instanceof FeedModuleRouter) {
            return ((FeedModuleRouter) context).getChuckInterceptor();
        }
        throw new RuntimeException("App should implement " + FeedModuleRouter.class.getSimpleName());
    }

    @FeedPlusScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @FeedPlusScope
    @Provides
    public FeedApi provideFeedApi(Retrofit.Builder retrofitBuilder,
                                  OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl("https://gql.tokopedia.com/")
                .client(okHttpClient)
                .build()
                .create(FeedApi.class);
    }

    @FeedPlusScope
    @Provides
    ApolloClient providesApolloClient(OkHttpClient okHttpClient) {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                .build();
    }

    @FeedPlusScope
    @Provides
    MojitoService provideRecentProductService(Retrofit.Builder builder,
                                              @FeedMojitoQualifier OkHttpClient okHttpClient) {
        return builder.baseUrl(TkpdBaseURL.MOJITO_DOMAIN)
                .client(okHttpClient)
                .build()
                .create(MojitoService.class);
    }

    @FeedPlusScope
    @Provides
    MojitoInterceptor provideMojitoInterceptor(@ApplicationContext Context context,
                                               AbstractionRouter abstractionRouter,
                                               UserSession userSession) {
        return new MojitoInterceptor(context, abstractionRouter, userSession);
    }

    @FeedPlusScope
    @Provides
    RecentProductMapper provideRecentProductMapper(Gson gson) {
        return new RecentProductMapper(gson);
    }

    @FeedPlusScope
    @Provides
    FeedRepository provideFeedRepository(FeedFactory feedFactory,
                                         KolSource kolSource) {
        return new FeedRepositoryImpl(feedFactory, kolSource);
    }

    @FeedPlusScope
    @Provides
    FeedFactory provideFeedFactory(@ApplicationContext Context context,
                                   FeedApi feedApi,
                                   ApolloClient apolloClient,
                                   FeedListMapper feedListMapper,
                                   @Named(NAME_LOCAL) FeedResultMapper feedResultMapperLocal,
                                   @Named(NAME_CLOUD) FeedResultMapper feedResultMapperCloud,
                                   FeedDetailListMapper feedDetailListMapper,
                                   GlobalCacheManager globalCacheManager,
                                   MojitoService mojitoService,
                                   RecentProductMapper recentProductMapper,
                                   CheckNewFeedMapper checkNewFeedMapper) {
        return new FeedFactory(
                context,
                feedApi,
                apolloClient,
                feedListMapper,
                feedResultMapperCloud,
                feedResultMapperLocal,
                globalCacheManager,
                feedDetailListMapper,
                mojitoService,
                recentProductMapper,
                checkNewFeedMapper
        );
    }

    @FeedPlusScope
    @Named(NAME_LOCAL)
    @Provides
    FeedResultMapper provideLocalFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_LOCAL);
    }

    @FeedPlusScope
    @Named(NAME_CLOUD)
    @Provides
    FeedResultMapper provideCloudFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_CLOUD);
    }

    @FeedPlusScope
    @Provides
    ActionService provideActionService() {
        return new ActionService();
    }

    @FeedPlusScope
    @Provides
    FavoriteShopRepository provideFavoriteShopRepository(FavoriteShopFactory favoriteShopFactory) {
        return new FavoriteShopRepositoryImpl(favoriteShopFactory);
    }

    @FeedPlusScope
    @Provides
    MojitoNoRetryAuthService provideMojitoNoRetryAuthService() {
        return new MojitoNoRetryAuthService();
    }

    @FeedPlusScope
    @Provides
    WishlistRepository provideWishlistRepository(WishlistFactory wishlistFactory) {
        return new WishlistRepositoryImpl(wishlistFactory);
    }

}
