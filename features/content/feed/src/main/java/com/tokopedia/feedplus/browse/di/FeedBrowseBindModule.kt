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
import com.tokopedia.feedplus.browse.presentation.FeedLocalSearchFragment
import com.tokopedia.feedplus.browse.presentation.FeedSearchResultFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by meyta.taliti on 11/08/23.
 */
@Module
internal abstract class FeedBrowseBindModule {

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

    @Binds
    @IntoMap
    @FragmentKey(FeedLocalSearchFragment::class)
    abstract fun getFeedLocalSearchFragment(fragment: FeedLocalSearchFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FeedSearchResultFragment::class)
    abstract fun getFeedSearchResultFragment(fragment: FeedSearchResultFragment): Fragment
}
