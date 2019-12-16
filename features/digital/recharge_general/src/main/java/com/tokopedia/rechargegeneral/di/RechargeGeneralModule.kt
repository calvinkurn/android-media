package com.tokopedia.rechargegeneral.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.rechargegeneral.domain.GetProductUseCase
import dagger.Module
import dagger.Provides

@Module
class RechargeGeneralModule {

    @RechargeGeneralScope
    @Provides
    fun provideGetProductUseCase(graphqlRepository: GraphqlRepository): GetProductUseCase = GetProductUseCase(graphqlRepository)

}
