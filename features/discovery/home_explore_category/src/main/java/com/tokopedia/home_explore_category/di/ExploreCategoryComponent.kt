package com.tokopedia.home_explore_category.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.home_explore_category.presentation.activity.ExploreCategoryActivity
import dagger.Component

@ExploreCategoryScope
@Component(modules = [ExploreCategoryViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ExploreCategoryComponent {

    @get:ApplicationContext
    val context: Context

    fun inject(exploreCategoryActivity: ExploreCategoryActivity)
}
