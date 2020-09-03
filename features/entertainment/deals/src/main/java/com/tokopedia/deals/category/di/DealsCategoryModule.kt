package com.tokopedia.deals.category.di

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@DealsCategoryScope
@Module
class DealsCategoryModule{
    @DealsCategoryScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)
}