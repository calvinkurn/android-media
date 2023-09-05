package com.tokopedia.stories.creation.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.stories.creation.view.activity.StoriesCreationActivity
import dagger.Component

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Component(
    modules = [
        StoriesCreationModule::class,
        StoriesCreationBindModule::class,
        StoriesCreationViewModelModule::class,
        StoriesCreationFragmentModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
@StoriesCreationScope
interface StoriesCreationComponent {

    fun inject(activity: StoriesCreationActivity)

}
