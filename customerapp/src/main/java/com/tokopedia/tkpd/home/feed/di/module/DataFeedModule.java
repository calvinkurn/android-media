package com.tokopedia.tkpd.home.feed.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tkpd.home.feed.data.factory.FeedDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.factory.HomeDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.factory.RecentProductSourceFactory;
import com.tokopedia.tkpd.home.feed.data.factory.TopAdsDataSourceFactory;
import com.tokopedia.tkpd.home.feed.data.repository.FeedRepositoryImpl;
import com.tokopedia.tkpd.home.feed.di.scope.DataFeedScope;
import com.tokopedia.tkpd.home.feed.domain.FeedRepository;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetAllFeedDataPageUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetDataFeedCacheUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetListShopIdUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetRecentProductUsecase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetTopAdsUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.LoadMoreFeedUseCase;
import com.tokopedia.tkpd.home.feed.view.FeedPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Kulomady on 1/9/17.
 */

@DataFeedScope
@Module(includes = {TopAdsModule.class, FeedModule.class, RecentProductModule.class, ShopIdModule.class})
public class DataFeedModule {


    @DataFeedScope
    @Provides
    FeedRepository provideFeedRepository(FeedDataSourceFactory feedDataStoreFactory,
                                         HomeDataSourceFactory homeDataSourceFactory,
                                         RecentProductSourceFactory recentProductSourceFactory,
                                         TopAdsDataSourceFactory topAdsDataSourceFactory) {

        return new FeedRepositoryImpl(
                feedDataStoreFactory,
                homeDataSourceFactory,
                topAdsDataSourceFactory,
                recentProductSourceFactory
        );
    }

    @DataFeedScope
    @Provides
    GetRecentProductUsecase provideRecentProductUsecase(ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        FeedRepository feedRepository) {

        return new GetRecentProductUsecase(threadExecutor, postExecutionThread, feedRepository);
    }

    @DataFeedScope
    @Provides
    GetTopAdsUseCase provideGetTopAdsUsecase(ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread,
                                             FeedRepository feedRepository) {

        return new GetTopAdsUseCase(threadExecutor, postExecutionThread, feedRepository);
    }

    @DataFeedScope
    @Provides
    GetListShopIdUseCase provideGetListShopIdUsecase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     FeedRepository feedRepository) {

        return new GetListShopIdUseCase(threadExecutor, postExecutionThread, feedRepository);
    }

    @DataFeedScope
    @Provides
    GetAllFeedDataPageUseCase provideAllFeedDataUsecase(ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        FeedRepository feedRepository,
                                                        GetRecentProductUsecase recentProductUsecase,
                                                        GetListShopIdUseCase getListShopIdUseCase,
                                                        GetTopAdsUseCase getTopAdsUseCase) {

        return new GetAllFeedDataPageUseCase(
                threadExecutor,
                postExecutionThread,
                feedRepository,
                recentProductUsecase,
                getListShopIdUseCase,
                getTopAdsUseCase
        );
    }

    @DataFeedScope
    @Provides
    LoadMoreFeedUseCase provideLoadMoreUsecase(ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread,
                                               FeedRepository feedRepository,
                                               GetListShopIdUseCase getListShopIdUseCase,
                                               GetTopAdsUseCase getTopAdsUseCase) {

        return new LoadMoreFeedUseCase(
                threadExecutor,
                postExecutionThread,
                feedRepository,
                getListShopIdUseCase,
                getTopAdsUseCase
        );
    }

    @DataFeedScope
    @Provides
    GetDataFeedCacheUseCase provideGetDataFeedCacheUseCase(ThreadExecutor threadExecutor,
                                                           PostExecutionThread postExecutionThread,
                                                           FeedRepository feedRepository) {

        return new GetDataFeedCacheUseCase(threadExecutor, postExecutionThread, feedRepository);
    }

    @DataFeedScope
    @Provides
    FeedPresenter provideFeedPresenter(GetAllFeedDataPageUseCase getAllFeedDataPageUseCase,
                                       GetDataFeedCacheUseCase getAllFeedDataCacheUseCase,
                                       LoadMoreFeedUseCase loadMoreFeedUseCase) {

        return new FeedPresenter(
                getAllFeedDataPageUseCase, getAllFeedDataCacheUseCase, loadMoreFeedUseCase);
    }


}
