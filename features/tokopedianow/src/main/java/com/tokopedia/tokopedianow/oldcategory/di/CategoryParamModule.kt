package com.tokopedia.tokopedianow.oldcategory.di

import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_L1
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_QUERY_PARAM_MAP
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_SERVICE_TYPE
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

    @CategoryScope
    @Provides
    @Named(TOKONOW_CATEGORY_L1)
    fun provideCategoryL1() = categoryL1

    @CategoryScope
    @Provides
    @Named(TOKONOW_CATEGORY_L2)
    fun provideCategoryL2() = categoryL2

    @CategoryScope
    @Provides
    @Named(TOKONOW_CATEGORY_SERVICE_TYPE)
    fun provideServiceType() = serviceType

    @CategoryScope
    @Provides
    @Named(TOKONOW_CATEGORY_QUERY_PARAM_MAP)
    fun provideQueryParamMap() = queryParamMap
}
