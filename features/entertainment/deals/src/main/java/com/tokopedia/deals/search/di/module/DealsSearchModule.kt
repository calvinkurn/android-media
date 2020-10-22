package com.tokopedia.deals.search.di.module

import com.tokopedia.deals.search.model.response.InitialLoadData
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.search.di.DealsSearchScope
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class DealsSearchModule {
    @DealsSearchScope
    @Provides
    fun provideGraphqlUseCaseDealsInitialLoadData(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<InitialLoadData> = GraphqlUseCase(graphqlRepository)

    @DealsSearchScope
    @Provides
    fun provideGraphqlUseCaseDealsSearch(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<SearchData> = GraphqlUseCase(graphqlRepository)
}