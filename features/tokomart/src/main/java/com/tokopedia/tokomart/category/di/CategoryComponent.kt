package com.tokopedia.tokomart.category.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokomart.category.presentation.view.CategoryFragment
import dagger.Component

@CategoryScope
@Component(
        modules = [CategoryViewModelModule::class, CategoryContextModule::class, CategoryParamModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryComponent {

    fun inject(categoryFragment: CategoryFragment)
}