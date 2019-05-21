package com.tokopedia.search.result.data.repository.favoriteshoplist;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.source.favoriteshoplist.FavoriteShopListDataSource;
import com.tokopedia.search.result.data.source.favoriteshoplist.FavoriteShopListDataSourceModule;
import com.tokopedia.search.result.domain.model.FavoriteShopListModel;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        FavoriteShopListDataSourceModule.class
})
public class FavoriteShopListRepositoryModule {

    @SearchScope
    @Provides
    Repository<FavoriteShopListModel> provideFavoriteShopListRepository(
            FavoriteShopListDataSource favoriteShopListDataSource
    ) {
        return new FavoriteShopListRepository(favoriteShopListDataSource);
    }
}
