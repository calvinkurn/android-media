package com.tokopedia.stories.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.stories.view.activity.StoriesActivity
import dagger.Component

@StoriesScope
@Component(
    modules = [
        StoriesModule::class,
        StoriesBindModule::class,
        StoriesFragmentModule::class,
        StoriesViewModelModule::class,
    ],
    dependencies = [BaseAppComponent::class],
)
interface StoriesComponent {

    fun inject(activity: StoriesActivity)

}
