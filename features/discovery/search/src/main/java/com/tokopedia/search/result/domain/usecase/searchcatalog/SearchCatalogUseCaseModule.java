package com.tokopedia.search.result.domain.usecase.searchcatalog;

import com.tokopedia.discovery.common.Repository;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.repository.cataloglist.SearchCatalogRepositoryModule;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module(includes = {
        SearchCatalogRepositoryModule.class
})
public class SearchCatalogUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchCatalog.SEARCH_CATALOG_USE_CASE)
    UseCase<SearchCatalogModel> provideSearchCatalogModel(Repository<SearchCatalogModel> searchCatalogModelRepository) {
        return new SearchCatalogUseCase(searchCatalogModelRepository);
    }
}
