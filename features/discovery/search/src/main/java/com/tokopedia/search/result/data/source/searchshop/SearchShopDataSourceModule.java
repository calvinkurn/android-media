package com.tokopedia.search.result.data.source.searchshop;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.qualifier.AceQualifier;
import com.tokopedia.search.result.data.mapper.searchshop.SearchShopMapperModule;
import com.tokopedia.search.result.data.response.SearchShopResponse;
import com.tokopedia.search.result.domain.model.SearchShopModel;
import com.tokopedia.search.result.network.service.BrowseApi;
import com.tokopedia.search.result.network.service.BrowseApiModule;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module(includes = {
        BrowseApiModule.class,
        SearchShopMapperModule.class
})
public class SearchShopDataSourceModule {

    @SearchScope
    @Provides
    SearchShopDataSource provideSearchShopDataSource(@AceQualifier BrowseApi browseApi,
                                                     Func1<Response<SearchShopResponse>, SearchShopModel> searchShopMapper) {
        return new SearchShopDataSource(browseApi, searchShopMapper);
    }
}
