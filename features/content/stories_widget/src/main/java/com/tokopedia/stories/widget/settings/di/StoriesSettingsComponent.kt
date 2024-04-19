package com.tokopedia.stories.widget.settings.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.content.common.di.ContentFragmentFactoryModule
import com.tokopedia.stories.widget.settings.presentation.ui.StoriesSettingsActivity
import dagger.Component

/**
 * @author by astidhiyaa on 4/18/24
 */
@ActivityScope
@Component(
    modules = [
        StoriesCommonModule::class,
        StoriesSettingsModule::class,
        ContentFragmentFactoryModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface StoriesSettingsComponent {
    fun inject(activity: StoriesSettingsActivity)
}
