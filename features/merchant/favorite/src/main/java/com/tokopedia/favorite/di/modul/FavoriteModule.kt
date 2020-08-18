package com.tokopedia.favorite.di.modul

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.favorite.data.FavoriteDataRepository
import com.tokopedia.favorite.data.FavoriteFactory
import com.tokopedia.favorite.data.source.apis.service.TopAdsService
import com.tokopedia.favorite.di.qualifier.TopAdsQualifier
import com.tokopedia.favorite.di.scope.FavoriteScope
import com.tokopedia.favorite.domain.FavoriteRepository
import com.tokopedia.favorite.domain.interactor.GetAllDataFavoriteUseCase
import com.tokopedia.favorite.domain.interactor.GetFavoriteShopUsecase
import com.tokopedia.favorite.domain.interactor.GetInitialDataPageUsecase
import com.tokopedia.favorite.domain.interactor.GetTopAdsShopUseCase
import com.tokopedia.favorite.view.DispatcherProvider
import com.tokopedia.favorite.view.DispatcherProviderImpl
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @author Kulomady on 1/20/17.
 */
@Module(includes = [TopAdsServiceModule::class, FavoriteViewModelModule::class])
class FavoriteModule {

    @FavoriteScope
    @Provides
    fun provideFavoriteFactory(@ApplicationContext context: Context,
                               gson: Gson,
                               topAdsService: TopAdsService
    ): FavoriteFactory {
        return FavoriteFactory(context, gson, topAdsService)
    }

    @FavoriteScope
    @Provides
    fun provideToggleFavouriteShopUseCase(@ApplicationContext context: Context): ToggleFavouriteShopUseCase {
        return ToggleFavouriteShopUseCase(GraphqlUseCase(), context.resources)
    }

    @FavoriteScope
    @Provides
    fun provideFavoriteRepository(favoriteFactory: FavoriteFactory): FavoriteRepository {
        return FavoriteDataRepository(favoriteFactory)
    }

    @FavoriteScope
    @Provides
    fun provideFavoriteShopUsecase(favoriteRepository: FavoriteRepository): GetFavoriteShopUsecase {
        return GetFavoriteShopUsecase(favoriteRepository)
    }

    @FavoriteScope
    @Provides
    fun provideAllDataFavoriteUsecase(@ApplicationContext context: Context,
                                      favUseCase: GetFavoriteShopUsecase,
                                      topAdsShopUseCase: GetTopAdsShopUseCase): GetAllDataFavoriteUseCase {
        return GetAllDataFavoriteUseCase(context, favUseCase, topAdsShopUseCase)
    }

    @FavoriteScope
    @Provides
    fun provideGetTopAdsShopUsecase(favoriteRepository: FavoriteRepository): GetTopAdsShopUseCase {
        return GetTopAdsShopUseCase(favoriteRepository)
    }

    @FavoriteScope
    @Provides
    fun provideFavoriteWishlitUsecase(
            @ApplicationContext context: Context,
            getFavoriteShopUsecase: GetFavoriteShopUsecase,
            getTopAdsShopUseCase: GetTopAdsShopUseCase): GetInitialDataPageUsecase {
        return GetInitialDataPageUsecase(
                context,
                getFavoriteShopUsecase,
                getTopAdsShopUseCase)
    }

    @FavoriteScope
    @Provides
    fun provideTopAdsService(@TopAdsQualifier retrofit: Retrofit): TopAdsService {
        return retrofit.create(TopAdsService::class.java)
    }

    @FavoriteScope
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl()
    }

}
