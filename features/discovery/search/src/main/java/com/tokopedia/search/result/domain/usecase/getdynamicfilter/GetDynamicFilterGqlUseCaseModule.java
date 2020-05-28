package com.tokopedia.search.result.domain.usecase.getdynamicfilter;

import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.search.result.data.mapper.dynamicfilter.DynamicFilterGqlMapperModule;
import com.tokopedia.usecase.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.functions.Func1;

@Module(includes = DynamicFilterGqlMapperModule.class)
public class GetDynamicFilterGqlUseCaseModule {

    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    UseCase<DynamicFilterModel> provideGetDynamicFilterGqlUseCase(
            Func1<GraphqlResponse, DynamicFilterModel> dynamicFilterModelMapper
    ) {
        return new GetDynamicFilterGqlUseCase(new GraphqlUseCase(), dynamicFilterModelMapper);
    }
}
