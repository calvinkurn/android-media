package com.tokopedia.deals.brand.di.module

import com.tokopedia.deals.brand.di.DealsBrandScope
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class DealsBrandModule {
    @DealsBrandScope
    @Provides
    fun provideGraphqlUseCaseDealsSearch(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<SearchData> = GraphqlUseCase(graphqlRepository)
}