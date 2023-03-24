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
import com.tokopedia.favorite.domain.interactor.*
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
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
    fun provideFavoriteShopUseCaseWithCoroutine(
            favoriteRepository: FavoriteRepository): GetFavoriteShopUseCaseWithCoroutine {
        return GetFavoriteShopUseCaseWithCoroutine(favoriteRepository);
    }

    @FavoriteScope
    @Provides
    fun provideGetAllDataFavoriteUseCaseWithCoroutine(
            @ApplicationContext context: Context,
            favUseCase: GetFavoriteShopUseCaseWithCoroutine,
            topAdsShopUseCase: GetTopAdsShopUseCaseWithCoroutine,
            topAdsAddressHelper: TopAdsAddressHelper
    ): GetAllDataFavoriteUseCaseWithCoroutine {
        return GetAllDataFavoriteUseCaseWithCoroutine(context, favUseCase, topAdsShopUseCase, topAdsAddressHelper)
    }

    @FavoriteScope
    @Provides
    fun provideGetTopAdsShopUseCaseWithCoroutine(favoriteRepository: FavoriteRepository, topAdsAddressHelper: TopAdsAddressHelper): GetTopAdsShopUseCaseWithCoroutine {
        return GetTopAdsShopUseCaseWithCoroutine(favoriteRepository)
    }

    @FavoriteScope
    @Provides
    fun provideGetInitialDataPageUseCaseWithCoroutine(
            @ApplicationContext context: Context,
            getFavoriteShopUsecase: GetFavoriteShopUseCaseWithCoroutine,
            getTopAdsShopUseCase: GetTopAdsShopUseCaseWithCoroutine,
            topAdsAddressHelper: TopAdsAddressHelper): GetInitialDataPageUseCaseWithCoroutine {
        return GetInitialDataPageUseCaseWithCoroutine(
                context,
                getFavoriteShopUsecase,
                getTopAdsShopUseCase,
                topAdsAddressHelper
        )
    }

    @FavoriteScope
    @Provides
    fun provideTopAdsService(@TopAdsQualifier retrofit: Retrofit): TopAdsService {
        return retrofit.create(TopAdsService::class.java)
    }
}
