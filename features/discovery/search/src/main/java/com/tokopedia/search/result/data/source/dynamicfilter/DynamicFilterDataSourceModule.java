package com.tokopedia.search.result.data.source.dynamicfilter;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.qualifier.AceQualifier;
import com.tokopedia.search.result.data.mapper.dynamicfilter.DynamicFilterMapperModule;
import com.tokopedia.search.result.network.service.BrowseApi;
import com.tokopedia.search.result.network.service.BrowseApiModule;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module(includes = {
        BrowseApiModule.class,
        DynamicFilterMapperModule.class
})
public class DynamicFilterDataSourceModule {

    @SearchScope
    @Provides
    DynamicFilterDataSource provideDynamicFilterDataSource(@AceQualifier BrowseApi browseApi,
                                                           Func1<Response<String>, DynamicFilterModel> dynamicFilterMapper) {
        return new DynamicFilterDataSource(browseApi, dynamicFilterMapper);
    }
}
