package com.tokopedia.tokopedianow.category.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.category.presentation.view.CategoryFragment
import com.tokopedia.tokopedianow.searchcategory.di.UserSessionModule
import dagger.Component

@CategoryScope
@Component(
        modules = [
            CategoryViewModelModule::class,
            CategoryContextModule::class,
            CategoryParamModule::class,
            UserSessionModule::class,
                  ],
        dependencies = [BaseAppComponent::class])
interface CategoryComponent {

    fun inject(categoryFragment: CategoryFragment)
}