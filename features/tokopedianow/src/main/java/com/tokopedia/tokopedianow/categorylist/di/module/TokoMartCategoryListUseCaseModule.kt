package com.tokopedia.tokopedianow.categorylist.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.categorylist.di.scope.TokoMartCategoryListScope
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
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