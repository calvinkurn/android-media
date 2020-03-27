package com.tokopedia.favorite.di.modul;

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
import com.tokopedia.favorite.data.FavoriteDataRepository;
import com.tokopedia.favorite.data.FavoriteFactory;
import com.tokopedia.favorite.di.scope.FavoriteScope;
import com.tokopedia.favorite.domain.FavoriteRepository;
import com.tokopedia.favorite.domain.interactor.AddFavoriteShopUseCase;
import com.tokopedia.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.favorite.domain.interactor.GetInitialDataPageUsecase;
import com.tokopedia.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;

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
    AddFavoriteShopUseCase providePostFavoriteUsecase(FavoriteRepository favorite) {

        return new AddFavoriteShopUseCase(favorite);
    }

    @FavoriteScope
    @Provides
    GetAllDataFavoriteUseCase provideAllDataFavoriteUsecase(@ApplicationContext Context context,
                                                            GetFavoriteShopUsecase favUseCase,
                                                            GetTopAdsShopUseCase topAdsShopUseCase) {
        return new GetAllDataFavoriteUseCase(context, favUseCase, topAdsShopUseCase);
    }

    @FavoriteScope
    @Provides
    GetTopAdsShopUseCase provideGetTopAdsShopUsecase(FavoriteRepository favoriteRepository) {

        return new GetTopAdsShopUseCase(favoriteRepository);
    }

    @FavoriteScope
    @Provides
    GetInitialDataPageUsecase provideFavoriteWishlitUsecase(
            @ApplicationContext Context context,
            GetFavoriteShopUsecase getFavoriteShopUsecase,
            GetTopAdsShopUseCase getTopAdsShopUseCase) {

        return new GetInitialDataPageUsecase(
                context,
                getFavoriteShopUsecase,
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
