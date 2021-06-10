package com.tokopedia.search.result.domain.usecase.getdynamicfilter

import com.tokopedia.search.result.data.mapper.dynamicfilter.DynamicFilterGqlMapperModule
import dagger.Provides
import com.tokopedia.discovery.common.constants.SearchConstant
import rx.functions.Func1
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import javax.inject.Named

@Module(includes = [DynamicFilterGqlMapperModule::class])
class GetDynamicFilterGqlUseCaseModule {
    @Provides
    @Named(SearchConstant.DynamicFilter.GET_DYNAMIC_FILTER_USE_CASE)
    fun provideGetDynamicFilterGqlUseCase(
            dynamicFilterModelMapper: Func1<GraphqlResponse?, DynamicFilterModel?>
    ): UseCase<DynamicFilterModel> {
        return GetDynamicFilterGqlUseCase(GraphqlUseCase(), dynamicFilterModelMapper)
    }
}