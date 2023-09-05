package com.tokopedia.stories.creation.di

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Module
class StoriesCreationModule(
    private val activityContext: Context
) {

    @Provides
    @StoriesCreationScope
    fun provideActivityContext() = activityContext
}
