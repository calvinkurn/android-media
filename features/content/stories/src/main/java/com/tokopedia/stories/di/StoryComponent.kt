package com.tokopedia.stories.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.stories.view.activity.StoryActivity
import dagger.Component

@StoryScope
@Component(
    modules = [
        StoryModule::class,
        StoryBindModule::class,
        StoryFragmentModule::class,
        StoryViewModelModule::class,
    ],
    dependencies = [BaseAppComponent::class],
)
interface StoryComponent {

    fun inject(activity: StoryActivity)

}
