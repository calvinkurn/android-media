package com.tokopedia.search.result.data.mapper.searchcatalog;

import com.google.gson.Gson;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.domain.model.SearchCatalogModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module
public class SearchCatalogMapperModule {

    @SearchScope
    @Provides
    Func1<Response<String>, SearchCatalogModel> provideSearchCatalogModelMapper(Gson gson) {
        return new SearchCatalogMapper(gson);
    }
}
