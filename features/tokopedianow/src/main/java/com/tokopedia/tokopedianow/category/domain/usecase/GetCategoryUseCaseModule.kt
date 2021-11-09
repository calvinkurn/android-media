package com.tokopedia.tokopedianow.category.domain.usecase

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.tokopedianow.category.di.CategoryScope
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.category.utils.CATEGORY_FIRST_PAGE_USE_CASE
import com.tokopedia.tokopedianow.category.utils.CATEGORY_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class GetCategoryUseCaseModule {

    @CategoryScope
    @Provides
    @Named(CATEGORY_FIRST_PAGE_USE_CASE)
    fun provideGetCategoryDataFirstPageUseCase(): UseCase<CategoryModel> {
        return GetCategoryFirstPageUseCase(GraphqlInteractor.getInstance().multiRequestGraphqlUseCase)
    }

    @CategoryScope
    @Provides
    @Named(CATEGORY_LOAD_MORE_PAGE_USE_CASE)
    fun provideGetCategoryDataLoadMorePageUseCase(): UseCase<CategoryModel> {
        return GetCategoryLoadMorePageUseCase(GraphqlInteractor.getInstance().multiRequestGraphqlUseCase)
    }
}