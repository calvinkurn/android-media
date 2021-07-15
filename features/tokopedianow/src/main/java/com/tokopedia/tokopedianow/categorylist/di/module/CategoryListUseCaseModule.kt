package com.tokopedia.tokopedianow.categorylist.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.categorylist.di.scope.CategoryListScope
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import dagger.Module
import dagger.Provides

@Module
class CategoryListUseCaseModule {

    @CategoryListScope
    @Provides
    fun provideGetCategoryListUseCase(graphqlRepository: GraphqlRepository): GetCategoryListUseCase {
        return GetCategoryListUseCase(graphqlRepository)
    }
}