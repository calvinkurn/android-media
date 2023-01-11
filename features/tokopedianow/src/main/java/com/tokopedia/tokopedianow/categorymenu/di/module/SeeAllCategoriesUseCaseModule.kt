package com.tokopedia.tokopedianow.categorymenu.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.categorymenu.di.scope.SeeAllCategoriesScope
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import dagger.Module
import dagger.Provides

@Module
class SeeAllCategoriesUseCaseModule {

    @SeeAllCategoriesScope
    @Provides
    fun provideGetCategoryMenuUseCase(graphqlRepository: GraphqlRepository): GetCategoryListUseCase {
        return GetCategoryListUseCase(graphqlRepository)
    }
}
