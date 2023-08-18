package com.tokopedia.stories.widget.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.stories.widget.StoriesWidgetViewModel
import dagger.Component

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
@StoriesWidgetScope
@Component(
    dependencies = [
        BaseAppComponent::class
    ],
    modules = [
        StoriesWidgetModule::class,
        StoriesRemoteConfigModule::class
    ]
)
internal interface StoriesWidgetComponent {

    fun storiesViewModelFactory(): StoriesWidgetViewModel.Factory
}
