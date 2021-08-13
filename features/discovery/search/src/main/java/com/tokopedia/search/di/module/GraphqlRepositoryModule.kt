package com.tokopedia.search.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class GraphqlRepositoryModule {

    @SearchScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}