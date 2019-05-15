package com.tokopedia.search.result.data.source;

import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.qualifier.AceQualifier;
import com.tokopedia.search.result.data.mapper.DynamicFilterMapperModule;
import com.tokopedia.search.result.network.service.DynamicFilterService;
import com.tokopedia.search.result.network.service.DynamicFilterServiceModule;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module(includes = {
        DynamicFilterServiceModule.class,
        DynamicFilterMapperModule.class
})
public class DynamicFilterDataSourceModule {

    @SearchScope
    @Provides
    DynamicFilterDataSource provideDynamicFilterDataSource(@AceQualifier DynamicFilterService dynamicFilterService,
                                                           Func1<Response<String>, DynamicFilterModel> dynamicFilterMapper) {
        return new DynamicFilterDataSource(dynamicFilterService, dynamicFilterMapper);
    }
}
