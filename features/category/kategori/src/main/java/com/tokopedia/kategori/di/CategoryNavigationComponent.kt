package com.tokopedia.kategori.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.kategori.fragments.CategoryLevelTwoFragment
import com.tokopedia.kategori.fragments.CategorylevelOneFragment
import com.tokopedia.kategori.view.CategoryBrowseActivity
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