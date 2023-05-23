package com.tokopedia.feedplus.oldFeed.view.di

import com.tokopedia.feedplus.oldFeed.data.repository.FeedPlusRepositoryImpl
import com.tokopedia.feedplus.oldFeed.domain.repository.FeedPlusRepository
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPrefImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on September 22, 2022
 */
@Module
abstract class FeedContainerBindModule {

    @Binds
    @FeedContainerScope
    abstract fun bindFeedPlusRepository(feedPlusRepository: FeedPlusRepositoryImpl): FeedPlusRepository

    @Binds
    @FeedContainerScope
    abstract fun bindContentCoachMarkSharedPref(contentCoachMarkSharedPref: ContentCoachMarkSharedPrefImpl): ContentCoachMarkSharedPref
}
