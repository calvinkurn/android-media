package com.tokopedia.stories.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.stories.view.activity.StoriesActivity
import dagger.Component

@Component(
    modules = [
        StoriesModule::class,
        StoriesBindModule::class,
        StoriesFragmentModule::class,
        StoriesViewModelModule::class,
    ],
    dependencies = [BaseAppComponent::class],
)
@StoriesScope
interface StoriesComponent {

    fun inject(activity: StoriesActivity)

}
