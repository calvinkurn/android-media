package com.tokopedia.homenav.category.view.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.category.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.category.domain.usecases.GetCategoryListUseCase
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
    fun provideGetCategoryListUseCase(graphqlRepository: GraphqlRepository): GetCategoryListUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<DynamicHomeIconEntity>(graphqlRepository)
        return GetCategoryListUseCase(useCase)
    }

    @CategoryListScope
    @Provides
    fun provideDispatcher() = Dispatchers.IO

}