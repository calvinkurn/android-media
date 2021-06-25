package com.tokopedia.tokomart.categorylist.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.categorylist.di.scope.TokoMartCategoryListScope
import com.tokopedia.tokomart.categorylist.domain.usecase.GetCategoryListUseCase
import dagger.Module
import dagger.Provides

@Module
class TokoMartCategoryListUseCaseModule {

    @TokoMartCategoryListScope
    @Provides
    fun provideGetCategoryListUseCase(graphqlRepository: GraphqlRepository): GetCategoryListUseCase {
        return GetCategoryListUseCase(graphqlRepository)
    }
}