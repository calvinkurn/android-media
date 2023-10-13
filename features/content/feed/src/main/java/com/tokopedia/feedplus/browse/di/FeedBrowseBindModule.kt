package com.tokopedia.feedplus.browse.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.FeedBrowseRepositoryImpl
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseChannelTracker
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseChannelTrackerImpl
import com.tokopedia.feedplus.browse.presentation.FeedBrowseFragment
import com.tokopedia.feedplus.browse.presentation.FeedBrowseModelFetcher
import com.tokopedia.feedplus.browse.presentation.FeedCategoryInspirationFragment
import com.tokopedia.feedplus.browse.presentation.FeedContentSlotFetcher
import com.tokopedia.feedplus.browse.presentation.FeedWidgetRecommendationFetcher
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet

/**
 * Created by meyta.taliti on 11/08/23.
 */
@Module
internal abstract class FeedBrowseBindModule {

    /**
     * Repository
     */
    @ActivityScope
    @Binds
    abstract fun bindRepository(
        feedBrowseRepositoryImpl: FeedBrowseRepositoryImpl
    ): FeedBrowseRepository

    /**
     * Analytics
     */
    @ActivityScope
    @Binds
    abstract fun bindChannelTrackerFactory(
        feedBrowseChannelTrackerImpl: FeedBrowseChannelTrackerImpl.Factory
    ): FeedBrowseChannelTracker.Factory

    /**
     * Fragment
     */
    @Binds
    @IntoMap
    @FragmentKey(FeedBrowseFragment::class)
    abstract fun getFeedBrowseFragment(fragment: FeedBrowseFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FeedCategoryInspirationFragment::class)
    abstract fun getFeedCategoryInspirationFragment(fragment: FeedCategoryInspirationFragment): Fragment

    /**
     * Fetchers
     */
    @Binds
    @IntoSet
    abstract fun getContentSlotFetcher(fetcher: FeedContentSlotFetcher): FeedBrowseModelFetcher

    @Binds
    @IntoSet
    abstract fun getWidgetRecommendationFetcher(fetcher: FeedWidgetRecommendationFetcher): FeedBrowseModelFetcher
}
