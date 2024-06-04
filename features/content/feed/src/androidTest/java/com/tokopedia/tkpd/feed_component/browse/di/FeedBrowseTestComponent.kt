package com.tokopedia.tkpd.feed_component.browse.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.content.common.di.ContentFragmentFactoryModule
import com.tokopedia.feedplus.browse.di.FeedBrowseBindModule
import com.tokopedia.feedplus.browse.di.FeedBrowseComponent
import com.tokopedia.feedplus.browse.di.FeedBrowseModule
import com.tokopedia.feedplus.browse.di.FeedBrowseViewModelModule
import com.tokopedia.stories.internal.di.StoriesSeenStorageModule
import com.tokopedia.tkpd.feed_component.browse.container.FeedBrowseTestActivity
import dagger.Component

/**
 * Created by Jonathan Darwin on 01 April 2024
 */
@ActivityScope
@Component(
    modules = [
        FeedBrowseModule::class,
        FeedBrowseViewModelModule::class,
        FeedBrowseBindModule::class,
        FeedBrowseDataTestModule::class,
        ContentFragmentFactoryModule::class,
        StoriesSeenStorageModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedBrowseTestComponent : FeedBrowseComponent
