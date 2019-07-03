package com.tokopedia.search.result.domain.usecase.searchshop;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.repository.favoriteshoplist.FavoriteShopListRepositoryModule;
import com.tokopedia.search.result.data.repository.shoplist.SearchShopRepositoryModule;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        SearchShopRepositoryModule.class,
        FavoriteShopListRepositoryModule.class
})
public class SearchShopUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchShop.SEARCH_SHOP_USE_CASE)
    UseCase<SearchShopModel> provideSearchShopModel(Repository<SearchShopModel> searchShopModelRepository,
                                                    Repository<FavoriteShopListModel> favoriteShopListModelRepository) {

        return new SearchShopUseCase(searchShopModelRepository, favoriteShopListModelRepository);
    }
}
