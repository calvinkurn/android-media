package com.tokopedia.tokopedianow.category.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.category.di.module.CategoryContextModule
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule
import com.tokopedia.tokopedianow.category.di.module.CategoryViewModelModule
import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import com.tokopedia.tokopedianow.category.presentation.fragment.TokoNowCategoryFragment
import com.tokopedia.tokopedianow.searchcategory.di.UserSessionModule
import dagger.Component

@CategoryScope
@Component(
        modules = [
            CategoryViewModelModule::class,
            CategoryContextModule::class,
            CategoryParamModule::class,
            UserSessionModule::class
        ],
        dependencies = [BaseAppComponent::class])
interface CategoryComponent {
    fun inject(fragment: TokoNowCategoryFragment)
}
