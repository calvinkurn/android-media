package com.tokopedia.search.result.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Response;
import rx.functions.Func1;

@SearchScope
@Module
public class DynamicFilterMapperModule {

    @SearchScope
    @Provides
    Func1<Response<String>, DynamicFilterModel> provideDynamicFilterMapperModule(Gson gson) {
        return new DynamicFilterMapper(gson);
    }
}
