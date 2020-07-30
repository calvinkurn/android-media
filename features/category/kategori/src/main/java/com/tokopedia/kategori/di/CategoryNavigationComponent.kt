package com.tokopedia.kategori.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.kategori.view.fragments.CategoryLevelTwoFragment
import com.tokopedia.kategori.view.fragments.CategoryLevelOneFragment
import com.tokopedia.kategori.view.activity.CategoryBrowseActivity
import dagger.Component

@CategoryNavigationScope
@Component(modules = [CategoryUseCaseModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryNavigationComponent {
    fun inject(activity: CategoryBrowseActivity)
    fun inject(categoryLevelOneFragment: CategoryLevelOneFragment)
    fun inject(categoryLevelTwoFragment: CategoryLevelTwoFragment)
}