package com.tokopedia.tkpd.feed_component.browse.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.FeedBrowseRepositoryImpl
import com.tokopedia.feedplus.browse.di.FeedBrowseDataModule
import dagger.Module
import dagger.Provides

/**
 * Created by Jonathan Darwin on 01 April 2024
 */
@Module
internal class FeedBrowseDataTestModule(
    private val mockRepository: FeedBrowseRepository
) {

    @ActivityScope
    @Provides
    fun provideRepository(feedBrowseRepositoryImpl: FeedBrowseRepositoryImpl): FeedBrowseRepository {
        return mockRepository
    }
}
