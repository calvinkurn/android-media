package com.tokopedia.tokopedianow.category.di.module

import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CategoryParamModule(
        private val categoryL1: String,
        private val categoryL2: String,
        private val serviceType: String,
        private val queryParamMap: Map<String, String>,
) {
    companion object {
        const val NOW_CATEGORY_L1 = "now_category_l1"
        const val NOW_CATEGORY_L2 = "now_category_l2"
        const val NOW_CATEGORY_SERVICE_TYPE = "now_category_service_type"
        const val NOW_CATEGORY_QUERY_PARAM_MAP = "now_category_query_param_map"
    }

    @CategoryScope
    @Provides
    @Named(NOW_CATEGORY_L1)
    fun provideCategoryL1() = categoryL1

    @CategoryScope
    @Provides
    @Named(NOW_CATEGORY_L2)
    fun provideCategoryL2() = categoryL2

    @CategoryScope
    @Provides
    @Named(NOW_CATEGORY_SERVICE_TYPE)
    fun provideServiceType() = serviceType

    @CategoryScope
    @Provides
    @Named(NOW_CATEGORY_QUERY_PARAM_MAP)
    fun provideQueryParamMap() = queryParamMap
}
