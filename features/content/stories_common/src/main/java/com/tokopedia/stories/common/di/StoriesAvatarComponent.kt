package com.tokopedia.stories.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.stories.common.StoriesAvatarViewModel
import dagger.Component

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
@StoriesAvatarScope
@Component(
    dependencies = [
        BaseAppComponent::class
    ],
    modules = [
        StoriesAvatarModule::class,
        StoriesRemoteConfigModule::class
    ]
)
internal interface StoriesAvatarComponent {

    fun storiesViewModelFactory(): StoriesAvatarViewModel.Factory
}
