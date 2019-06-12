package com.tokopedia.search.result.data.repository.cataloglist;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.source.searchcatalog.SearchCatalogDataSource;
import com.tokopedia.search.result.data.source.searchcatalog.SearchCatalogDataSourceModule;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        SearchCatalogDataSourceModule.class
})
public class SearchCatalogRepositoryModule {

    @SearchScope
    @Provides
    Repository<SearchCatalogModel> provideSearchCatalogRepository(SearchCatalogDataSource searchCatalogDataSource) {
        return new SearchCatalogRepository(searchCatalogDataSource);
    }
}
