package com.tokopedia.feedplus.view.di;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
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
import com.tokopedia.feedplus.view.listener.FeedPlus;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 5/15/17.
 */

@Module
public class FeedPlusModule {

    private static final String NAME_CLOUD = "CLOUD";
    private static final String NAME_LOCAL = "LOCAL";

    private final FeedPlus.View view;

    public FeedPlusModule(FeedPlus.View view) {
        this.view = view;
    }

    @FeedPlusScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @FeedPlusScope
    @Provides
    ApolloClient providesApolloClient(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                .build();
    }

    @FeedPlusScope
    @Provides
    MojitoService provideRecentProductService(@MojitoQualifier Retrofit retrofit) {
        return retrofit.create(MojitoService.class);
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
                                   ApolloClient apolloClient,
                                   FeedListMapper feedListMapper,
                                   @Named(NAME_CLOUD) FeedResultMapper feedResultMapperCloud,
                                   @Named(NAME_LOCAL) FeedResultMapper feedResultMapperLocal,
                                   FeedDetailListMapper feedDetailListMapper,
                                   GlobalCacheManager globalCacheManager,
                                   MojitoService mojitoService,
                                   RecentProductMapper recentProductMapper,
                                   CheckNewFeedMapper checkNewFeedMapper) {
        return new FeedFactory(
                context,
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
