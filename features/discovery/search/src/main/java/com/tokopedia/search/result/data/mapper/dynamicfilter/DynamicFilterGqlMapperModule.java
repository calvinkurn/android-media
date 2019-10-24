package com.tokopedia.search.result.data.mapper.dynamicfilter;

import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@SearchScope
@Module
public class DynamicFilterGqlMapperModule {

    @SearchScope
    @Provides
    Func1<GraphqlResponse, DynamicFilterModel> provideDynamicFilterModelMapper() {
        return new DynamicFilterGqlMapper();
    }
}
