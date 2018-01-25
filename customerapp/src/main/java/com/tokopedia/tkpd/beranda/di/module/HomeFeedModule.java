package com.tokopedia.tkpd.beranda.di.module;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.MojitoService;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.MojitoQualifier;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.FavoriteShopFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.FeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.HomeFeedFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.factory.WishlistFactory;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.AddWishlistMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FavoriteShopMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedDetailListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedListMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.FollowKolMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.HomeFeedMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolDeleteCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.KolSendCommentMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.LikeKolMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RecentProductMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.mapper.RemoveWishlistMapper;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FavoriteShopRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FavoriteShopRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.HomeFeedRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.HomeFeedRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.WishlistRepository;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.repository.WishlistRepositoryImpl;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.KolCommentSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.data.source.KolSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.CheckNewFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.DeleteKolCommentUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsCloudUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetHomeFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetKolCommentsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetRecentViewUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.RefreshFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.RemoveWishlistUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.SendKolCommentUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.di.FeedPlusScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by henrypriyono on 12/27/17.
 */

@Module
public class HomeFeedModule {

    @Provides
    ApolloClient providesApolloClient(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl(TkpdBaseURL.GRAPHQL_DOMAIN)
                .build();
    }

    @Provides
    HomeFeedRepository provideHomeFeedRepository(HomeFeedFactory feedFactory) {
        return new HomeFeedRepositoryImpl(feedFactory);
    }

    @Provides
    HomeFeedFactory provideHomeFeedFactory(ApolloClient apolloClient,
                                           HomeFeedMapper homeFeedMapper,
                                           FeedResultMapper feedResultMapper) {
        return new HomeFeedFactory(
                apolloClient,
                homeFeedMapper,
                feedResultMapper
        );
    }

    @Provides
    HomeFeedMapper provideHomeFeedMapper() {
        return new HomeFeedMapper();
    }

    @Provides
    FeedResultMapper provideFeedResultMapper() {
        return new FeedResultMapper(FeedResult.SOURCE_CLOUD);
    }

    @Provides
    GetHomeFeedsUseCase provideGetHomeFeedsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            HomeFeedRepository feedRepository) {
        return new GetHomeFeedsUseCase(threadExecutor, postExecutionThread, feedRepository);
    }
}