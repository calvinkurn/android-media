package com.tokopedia.tkpd.home.favorite.di.modul;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.common.service.ServiceV4;
import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.data.FavoriteDataRepository;
import com.tokopedia.tkpd.home.favorite.data.FavoriteFactory;
import com.tokopedia.tkpd.home.favorite.di.scope.FavoriteScope;
import com.tokopedia.tkpd.home.favorite.domain.FavoriteRepository;
import com.tokopedia.tkpd.home.favorite.domain.interactor.AddFavoriteShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetInitialDataPageUsecase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.tkpd.home.favorite.domain.interactor.GetWishlistUtil;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Kulomady on 1/20/17.
 */

@Module
public class FavoriteModule {

    @FavoriteScope
    @Provides
    FavoriteFactory provideFavoriteFactory(@ApplicationContext Context context,
                                           Gson gson,
                                           ServiceV4 serviceVersion4,
                                           TopAdsService topAdsService,
                                           GlobalCacheManager cacheManager) {

        return new FavoriteFactory(
                context, gson, serviceVersion4, topAdsService, cacheManager);
    }

    @FavoriteScope
    @Provides
    ToggleFavouriteShopUseCase provideToggleFavouriteShopUseCase(@ApplicationContext Context context) {

        return new ToggleFavouriteShopUseCase(new GraphqlUseCase(), context.getResources());
    }

    @FavoriteScope
    @Provides
    FavoriteRepository provideFavoriteRepository(FavoriteFactory favoriteFactory) {
        return new FavoriteDataRepository(favoriteFactory);
    }

    @FavoriteScope
    @Provides
    GetFavoriteShopUsecase provideFavoriteShopUsecase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutor,
                                                      FavoriteRepository favoriteRepository) {

        return new GetFavoriteShopUsecase(threadExecutor, postExecutor, favoriteRepository);
    }

    @FavoriteScope
    @Provides
    AddFavoriteShopUseCase providePostFavoriteUsecase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutor,
                                                      FavoriteRepository favorite) {

        return new AddFavoriteShopUseCase(threadExecutor, postExecutor, favorite);
    }

    @FavoriteScope
    @Provides
    GetAllDataFavoriteUseCase provideAllDataFavoriteUsecase(@ApplicationContext Context context,
                                                            ThreadExecutor threadExecutor,
                                                            PostExecutionThread postExecutor,
                                                            GetFavoriteShopUsecase favUseCase,
                                                            GetWishlistUtil wishlistUseCase,
                                                            GetTopAdsShopUseCase topAdsShopUseCase) {
        return new GetAllDataFavoriteUseCase(context,
                threadExecutor, postExecutor, favUseCase, wishlistUseCase, topAdsShopUseCase);
    }

    @FavoriteScope
    @Provides
    GetTopAdsShopUseCase provideGetTopAdsShopUsecase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     FavoriteRepository favoriteRepository) {

        return new GetTopAdsShopUseCase(threadExecutor, postExecutionThread, favoriteRepository);
    }

    @FavoriteScope
    @Provides
    GetInitialDataPageUsecase provideFavoriteWishlitUsecase(
            @ApplicationContext Context context,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            GetFavoriteShopUsecase getFavoriteShopUsecase,
            GetWishlistUtil getWishlistUse,
            GetTopAdsShopUseCase getTopAdsShopUseCase) {

        return new GetInitialDataPageUsecase(
                context,
                threadExecutor,
                postExecutionThread,
                getFavoriteShopUsecase,
                getWishlistUse,
                getTopAdsShopUseCase);
    }


    @FavoriteScope
    @Provides
    ServiceV4 provideHomeService(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ServiceV4.class);
    }

    @FavoriteScope
    @Provides
    TopAdsService provideTopAdsService(@TopAdsQualifier Retrofit retrofit) {
        return retrofit.create(TopAdsService.class);
    }

    @FavoriteScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }
}
