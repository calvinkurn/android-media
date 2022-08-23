package com.tokopedia.deals.pdp.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class DealsPDPModule {

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}