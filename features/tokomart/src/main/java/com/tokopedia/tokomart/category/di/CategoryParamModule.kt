package com.tokopedia.tokomart.category.di

import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_ID_L1
import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_ID_L2
import com.tokopedia.tokomart.category.utils.TOKONOW_CATEGORY_QUERY_PARAM_MAP
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CategoryParamModule(
        private val categoryIdL1: String,
        private val categoryIdL2: String,
        private val queryParamMap: Map<String, String>,
) {

    @CategoryScope
    @Provides
    @Named(TOKONOW_CATEGORY_ID_L1)
    fun provideCategoryIdL1() = categoryIdL1

    @CategoryScope
    @Provides
    @Named(TOKONOW_CATEGORY_ID_L2)
    fun provideCategoryIdL2() = categoryIdL2

    @CategoryScope
    @Provides
    @Named(TOKONOW_CATEGORY_QUERY_PARAM_MAP)
    fun provideQueryParamMap() = queryParamMap
}