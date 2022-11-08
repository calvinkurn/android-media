package com.tokopedia.play.broadcaster.shorts.di

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Module
class PlayShortsModule(
    private val activityContext: Context
) {

    @Provides
    @PlayShortsScope
    fun provideActivityContext() = activityContext
}
