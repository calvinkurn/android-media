package com.tokopedia.feedplus.browse.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.FeedBrowseRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Jonathan Darwin on 01 April 2024
 */
@Module
internal abstract class FeedBrowseDataModule {

    /**
     * Repository
     */
    @ActivityScope
    @Binds
    abstract fun bindRepository(
        feedBrowseRepositoryImpl: FeedBrowseRepositoryImpl
    ): FeedBrowseRepository
}
