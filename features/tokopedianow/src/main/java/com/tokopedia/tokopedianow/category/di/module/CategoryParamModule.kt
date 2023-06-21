package com.tokopedia.tokopedianow.category.di.module

import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CategoryParamModule(
        private val categoryL1: String,
        private val queryParamMap: Map<String, String>,
) {
    companion object {
        const val NOW_CATEGORY_L1 = "now_category_l1"
        const val NOW_CATEGORY_QUERY_PARAM_MAP = "now_category_query_param_map"
    }

    @CategoryScope
    @Provides
    @Named(NOW_CATEGORY_L1)
    fun provideCategoryL1() = categoryL1

    @CategoryScope
    @Provides
    @Named(NOW_CATEGORY_QUERY_PARAM_MAP)
    fun provideQueryParamMap() = queryParamMap
}
