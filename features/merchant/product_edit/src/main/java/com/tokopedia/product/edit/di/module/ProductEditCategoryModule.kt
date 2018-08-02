package com.tokopedia.product.edit.di.module

import com.tokopedia.core.network.di.qualifier.MerlinQualifier
import com.tokopedia.product.edit.data.repository.CategoryRecommRepositoryImpl
import com.tokopedia.product.edit.data.source.CategoryRecommDataSource
import com.tokopedia.product.edit.data.source.cloud.api.MerlinApi
import com.tokopedia.product.edit.di.scope.ProductAddScope
import com.tokopedia.product.edit.domain.CategoryRecommRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@ProductAddScope
@Module
class ProductEditCategoryModule{

    @ProductAddScope
    @Provides
    fun provideMerlinApi(@MerlinQualifier retrofit: Retrofit)= retrofit.create(MerlinApi::class.java)

    @ProductAddScope
    @Provides
    fun provideCategoryRecommRepository(categoryRecommDataSource: CategoryRecommDataSource): CategoryRecommRepository {
        return CategoryRecommRepositoryImpl(categoryRecommDataSource)
    }
}