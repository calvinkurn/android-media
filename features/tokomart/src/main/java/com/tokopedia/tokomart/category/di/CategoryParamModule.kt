package com.tokopedia.tokomart.category.di

import com.tokopedia.tokomart.category.utils.CATEGORY_ID
import com.tokopedia.tokomart.category.utils.CATEGORY_QUERY_PARAM_MAP
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CategoryParamModule(
        private val categoryId: Int,
        private val queryParamMap: Map<String, String>,
) {

    @CategoryScope
    @Provides
    @Named(CATEGORY_ID)
    fun provideCategoryId() = categoryId

    @CategoryScope
    @Provides
    @Named(CATEGORY_QUERY_PARAM_MAP)
    fun provideQueryParamMap() = queryParamMap
}