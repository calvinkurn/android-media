package com.tokopedia.feedplus.browse.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.content.common.di.ContentFragmentFactoryModule
import com.tokopedia.feedplus.browse.presentation.CategoryInspirationActivity
import com.tokopedia.feedplus.browse.presentation.FeedBrowseActivity
import com.tokopedia.feedplus.browse.presentation.FeedLocalSearchActivity
import com.tokopedia.feedplus.browse.presentation.FeedSearchResultActivity
import com.tokopedia.stories.internal.di.StoriesSeenStorageModule
import dagger.Component

/**
 * Created by meyta.taliti on 11/08/23.
 */
@ActivityScope
@Component(
    modules = [
        FeedBrowseModule::class,
        FeedBrowseViewModelModule::class,
        FeedBrowseBindModule::class,
        FeedBrowseDataModule::class,
        ContentFragmentFactoryModule::class,
        StoriesSeenStorageModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedBrowseComponent {

    fun inject(activity: FeedBrowseActivity)

    fun inject(activity: CategoryInspirationActivity)

    fun inject(activity: FeedLocalSearchActivity)

    fun inject(activity: FeedSearchResultActivity)
}
