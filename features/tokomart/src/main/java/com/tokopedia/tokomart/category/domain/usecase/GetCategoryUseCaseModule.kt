package com.tokopedia.tokomart.category.domain.usecase

import com.tokopedia.tokomart.category.di.CategoryScope
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.domain.usecase.GetCategoryFirstPageUseCase
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides

@Module
class GetCategoryUseCaseModule {

    @CategoryScope
    @Provides
    fun provideGetCategoryDataFirstPageUseCase(): UseCase<CategoryModel> {
        return GetCategoryFirstPageUseCase()
    }
}