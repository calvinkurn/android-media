package com.tokopedia.product.manage.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@ProductManageScope
class ProductManageModule  {

    @ProductManageScope
    @Provides
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @ProductManageScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ProductManageScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}