package com.tokopedia.seller.active.common.di

import com.tokopedia.seller.active.common.domain.usecase.UpdateShopActiveUseCase
import com.tokopedia.graphql.coroutines.data.Interactor
import dagger.Module
import dagger.Provides
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

@Module
class UpdateShopActiveModule {
    @UpdateShopActiveScope
    @Provides
    fun provideUpdateShopActiveUseCase(graphqlRepository: GraphqlRepository): UpdateShopActiveUseCase {
        return UpdateShopActiveUseCase(graphqlRepository)
    }

    @UpdateShopActiveScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository
}