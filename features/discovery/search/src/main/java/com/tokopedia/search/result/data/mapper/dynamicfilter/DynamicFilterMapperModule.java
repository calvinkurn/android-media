package com.tokopedia.search.result.data.mapper.dynamicfilter;

import com.google.gson.Gson;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.filter.common.data.DynamicFilterModel;

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
