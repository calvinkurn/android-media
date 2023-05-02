package com.tokopedia.tokopedianow.seeallcategory.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.seeallcategory.di.scope.SeeAllCategoryScope
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import dagger.Module
import dagger.Provides

@Module
class SeeAllCategoryUseCaseModule {

    @SeeAllCategoryScope
    @Provides
    fun provideGetCategoryMenuUseCase(graphqlRepository: GraphqlRepository): GetCategoryListUseCase {
        return GetCategoryListUseCase(graphqlRepository)
    }
}
