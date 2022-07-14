package com.tokopedia.feedcomponent.di

import com.tokopedia.feedcomponent.util.manager.FeedFloatingButtonManager
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on May 25, 2022
 */
@Module
class FeedFloatingButtonManagerModule {

    @Provides
    fun provideFeedFloatingButtonManager(): FeedFloatingButtonManager = FeedFloatingButtonManager()
}