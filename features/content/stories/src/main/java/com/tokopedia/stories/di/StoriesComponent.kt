package com.tokopedia.stories.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.stories.internal.di.StoriesSeenStorageModule
import com.tokopedia.stories.view.activity.StoriesActivity
import dagger.Component

@StoriesScope
@Component(
    modules = [
        StoriesModule::class,
        StoriesBindModule::class,
        StoriesFragmentModule::class,
        StoriesSeenStorageModule::class,
    ],
    dependencies = [BaseAppComponent::class],
)
interface StoriesComponent {

    fun inject(activity: StoriesActivity)

}
