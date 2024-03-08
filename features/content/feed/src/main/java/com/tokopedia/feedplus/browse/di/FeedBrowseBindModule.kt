package com.tokopedia.feedplus.browse.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.FeedBrowseRepositoryImpl
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTracker
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTrackerImpl
import com.tokopedia.feedplus.browse.presentation.CategoryInspirationFragment
import com.tokopedia.feedplus.browse.presentation.FeedBrowseFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

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
     * Fragment
     */
    @Binds
    @IntoMap
    @FragmentKey(FeedBrowseFragment::class)
    abstract fun getFeedBrowseFragment(fragment: FeedBrowseFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CategoryInspirationFragment::class)
    abstract fun getFeedCategoryInspirationFragment(fragment: CategoryInspirationFragment): Fragment
}
