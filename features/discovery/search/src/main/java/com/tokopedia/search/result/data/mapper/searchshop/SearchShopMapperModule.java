package com.tokopedia.search.result.data.mapper.searchshop;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.data.response.SearchShopResponse;
import com.tokopedia.search.result.domain.model.SearchShopModel;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module
public class SearchShopMapperModule {

    @SearchScope
    @Provides
    Func1<Response<SearchShopResponse>, SearchShopModel> provideSearchShopMapper() {
        return new SearchShopMapper();
    }
}
