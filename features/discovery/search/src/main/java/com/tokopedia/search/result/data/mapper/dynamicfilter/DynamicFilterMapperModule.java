package com.tokopedia.search.result.data.mapper.dynamicfilter;

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
    public Func1<Response<String>, DynamicFilterModel> provideDynamicFilterMapper(Gson gson) {
        return new DynamicFilterMapper(gson);
    }
}
