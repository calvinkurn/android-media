package com.tokopedia.favorite.di.modul;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.favorite.data.FavoriteDataRepository;
import com.tokopedia.favorite.data.FavoriteFactory;
import com.tokopedia.favorite.data.source.apis.service.TopAdsService;
import com.tokopedia.favorite.di.qualifier.TopAdsQualifier;
import com.tokopedia.favorite.di.scope.FavoriteScope;
import com.tokopedia.favorite.domain.FavoriteRepository;
import com.tokopedia.favorite.domain.interactor.GetAllDataFavoriteUseCase;
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase;
import com.tokopedia.favorite.domain.interactor.GetInitialDataPageUsecase;
import com.tokopedia.favorite.domain.interactor.GetTopAdsShopUseCase;
import com.tokopedia.favorite.view.DispatcherProvider;
import com.tokopedia.favorite.view.DispatcherProviderImpl;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Kulomady on 1/20/17.
 */

@Module(includes = {TopAdsServiceModule.class, FavoriteViewModelModule.class})
public class FavoriteModule {

    @FavoriteScope
    @Provides
    FavoriteFactory provideFavoriteFactory(@ApplicationContext Context context,
                                           Gson gson,
                                           TopAdsService topAdsService) {

        return new FavoriteFactory(
                context, gson, topAdsService);
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
    GetFavoriteShopUsecase provideFavoriteShopUsecase(FavoriteRepository favoriteRepository) {

        return new GetFavoriteShopUsecase(favoriteRepository);
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
    TopAdsService provideTopAdsService(@TopAdsQualifier Retrofit retrofit) {
        return retrofit.create(TopAdsService.class);
    }

    @FavoriteScope
    @Provides
    DispatcherProvider provideDispatcherProvider() {
        return new DispatcherProviderImpl();
    }

}
