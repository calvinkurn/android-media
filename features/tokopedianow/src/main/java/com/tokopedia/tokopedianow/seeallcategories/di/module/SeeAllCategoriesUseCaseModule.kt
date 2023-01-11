package com.tokopedia.tokopedianow.seeallcategories.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.seeallcategories.di.scope.SeeAllCategoriesScope
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
