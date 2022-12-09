package com.tokopedia.feedplus.view.di

import com.tokopedia.feedplus.data.repository.FeedPlusRepositoryImpl
import com.tokopedia.feedplus.domain.repository.FeedPlusRepository
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

}
