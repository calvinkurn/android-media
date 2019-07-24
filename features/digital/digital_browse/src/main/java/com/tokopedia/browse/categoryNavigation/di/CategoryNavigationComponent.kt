package com.tokopedia.browse.categoryNavigation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.browse.categoryNavigation.fragments.CategoryLevelTwoFragment
import com.tokopedia.browse.categoryNavigation.fragments.CategorylevelOneFragment
import com.tokopedia.browse.categoryNavigation.view.CategoryBrowseActivity
import dagger.Component

@CategoryNavigationScope
@Component(modules = [CategoryUseCaseModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryNavigationComponent {
    fun inject(activity: CategoryBrowseActivity)
    fun inject(categorylevelOneFragment: CategorylevelOneFragment)
    fun inject(categoryLevelTwoFragment: CategoryLevelTwoFragment)
}