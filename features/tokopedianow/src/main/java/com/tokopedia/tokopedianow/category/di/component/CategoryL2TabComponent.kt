package com.tokopedia.tokopedianow.category.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.di.module.CategoryL2TabViewModelModule
import com.tokopedia.tokopedianow.category.di.scope.CategoryL2TabScope
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryL2TabFragment
import com.tokopedia.tokopedianow.searchcategory.di.UserSessionModule
import dagger.Component

@CategoryL2TabScope
@Component(
    modules = [
        CategoryL2TabViewModelModule::class,
        CategoryContextModule::class,
        UserSessionModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CategoryL2TabComponent {
    fun inject(fragment: TokoNowCategoryL2TabFragment)
}
