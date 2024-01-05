package com.tokopedia.deals.ui.search.di.module

import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.ui.search.model.response.InitialLoadData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class DealsSearchModule {
    @com.tokopedia.deals.ui.search.di.DealsSearchScope
    @Provides
    fun provideGraphqlUseCaseDealsInitialLoadData(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<InitialLoadData> = GraphqlUseCase(graphqlRepository)

    @com.tokopedia.deals.ui.search.di.DealsSearchScope
    @Provides
    fun provideGraphqlUseCaseDealsSearch(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<SearchData> = GraphqlUseCase(graphqlRepository)
}
