package com.tokopedia.tokopedianow.category.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryHeaderUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CategoryUseCaseModule {
    companion object {
        const val MAIN_CATEGORY_HEADER_USE_CASE_NAME = "main_category_header_use_case"
    }

    @CategoryScope
    @Provides
    @Named(MAIN_CATEGORY_HEADER_USE_CASE_NAME)
    fun provideGetCategoryHeaderUseCase(): GetCategoryHeaderUseCase = GetCategoryHeaderUseCase(GraphqlInteractor.getInstance().multiRequestGraphqlUseCase)
}
