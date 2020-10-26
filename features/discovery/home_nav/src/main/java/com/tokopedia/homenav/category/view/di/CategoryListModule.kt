package com.tokopedia.homenav.category.view.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.domain.usecases.GetCategoryGroupUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

/**
 * Created by Lukas on 21/10/20.
 */

@Module
class CategoryListModule {
    @CategoryListScope
    @Provides
    fun provideGetCategoryListUseCase(graphqlRepository: GraphqlRepository): GetCategoryGroupUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<DynamicHomeIconEntity>(graphqlRepository)
        return GetCategoryGroupUseCase(useCase)
    }

    @CategoryListScope
    @Provides
    fun provideDispatcher() = Dispatchers.IO

}