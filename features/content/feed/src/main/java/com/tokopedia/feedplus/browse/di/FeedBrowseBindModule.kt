package com.tokopedia.feedplus.browse.di

import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.FeedBrowseRepositoryImpl
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseChannelTracker
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseChannelTrackerImpl
import dagger.Binds
import dagger.Module

/**
 * Created by meyta.taliti on 11/08/23.
 */
@Module
abstract class FeedBrowseBindModule {

    /**
     * Repository
     */
    @Binds
    abstract fun bindRepository(
        feedBrowseRepositoryImpl: FeedBrowseRepositoryImpl
    ): FeedBrowseRepository

    /**
     * Analytics
     */
    @Binds
    abstract fun bindChannelTrackerFactory(
        feedBrowseChannelTrackerImpl: FeedBrowseChannelTrackerImpl.Factory
    ): FeedBrowseChannelTracker.Factory
}
