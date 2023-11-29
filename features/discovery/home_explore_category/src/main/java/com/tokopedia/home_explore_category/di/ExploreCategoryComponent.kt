package com.tokopedia.home_explore_category.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_explore_category.presentation.activity.ExploreCategoryActivity
import dagger.Component

@ExploreCategoryScope
@Component(
    modules = [ExploreCategoryModule::class, ExploreCategoryViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ExploreCategoryComponent {

    fun inject(exploreCategoryActivity: ExploreCategoryActivity)
}
