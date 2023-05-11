package com.tokopedia.tokopedianow.category.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import com.tokopedia.tokopedianow.category.domain.usecase.GetMainCategoryFirstPageUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CategoryUseCaseModule {
    companion object {
        const val MAIN_CATEGORY_FIRST_PAGE_USE_CASE_NAME = "main_category_first_page_use_case_name"
    }

    @CategoryScope
    @Provides
    @Named(MAIN_CATEGORY_FIRST_PAGE_USE_CASE_NAME)
    fun provideGetMainCategoryFirstPageUseCase(): GetMainCategoryFirstPageUseCase {
        return GetMainCategoryFirstPageUseCase(GraphqlInteractor.getInstance().multiRequestGraphqlUseCase)
    }
}
