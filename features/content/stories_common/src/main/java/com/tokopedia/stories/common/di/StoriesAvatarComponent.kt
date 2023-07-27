package com.tokopedia.stories.common.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
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
        StoriesAvatarModule::class
    ]
)
interface StoriesAvatarComponent {

    fun viewModelFactory(): ViewModelProvider.Factory
}
