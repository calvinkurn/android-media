package com.tokopedia.search.result.data.repository.shoplist;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.source.searchshop.SearchShopDataSource;
import com.tokopedia.search.result.data.source.searchshop.SearchShopDataSourceModule;
import com.tokopedia.search.result.domain.model.SearchShopModel;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        SearchShopDataSourceModule.class
})
public class SearchShopRepositoryModule {

    @SearchScope
    @Provides
    Repository<SearchShopModel> provideSearchShopRepository(SearchShopDataSource searchShopDataSource) {
        return new SearchShopRepository(searchShopDataSource);
    }
}
