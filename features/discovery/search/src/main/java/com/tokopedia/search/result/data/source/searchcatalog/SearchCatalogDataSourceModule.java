package com.tokopedia.search.result.data.source.searchcatalog;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.qualifier.AceQualifier;
import com.tokopedia.search.result.data.mapper.searchcatalog.SearchCatalogMapperModule;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;
import com.tokopedia.search.result.network.service.BrowseApi;
import com.tokopedia.search.result.network.service.BrowseApiModule;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module(includes = {
        BrowseApiModule.class,
        SearchCatalogMapperModule.class
})
public class SearchCatalogDataSourceModule {

    @SearchScope
    @Provides
    SearchCatalogDataSource provideSearchCatalogDataSource(@AceQualifier BrowseApi browseApi,
                                                           Func1<Response<String>, SearchCatalogModel> searchCatalogModelMapper) {
        return new SearchCatalogDataSource(browseApi, searchCatalogModelMapper);
    }
}
