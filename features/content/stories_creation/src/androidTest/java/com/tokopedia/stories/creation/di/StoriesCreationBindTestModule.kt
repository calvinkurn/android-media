package com.tokopedia.stories.creation.di

import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.content.common.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.stories.creation.analytic.StoriesCreationAnalytic
import com.tokopedia.stories.creation.analytic.StoriesCreationAnalyticImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on October 24, 2023
 */
@Module
abstract class StoriesCreationBindTestModule {

    @Binds
    @StoriesCreationTestScope
    abstract fun bindStoriesCreationAnalytic(storiesCreationAnalytic: StoriesCreationAnalyticImpl): StoriesCreationAnalytic

    @Binds
    @StoriesCreationTestScope
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): ContentDialogCustomizer
}
