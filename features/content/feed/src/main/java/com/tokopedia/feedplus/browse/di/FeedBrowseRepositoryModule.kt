package com.tokopedia.feedplus.browse.di

import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.FeedBrowseRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by meyta.taliti on 11/08/23.
 */
@Module
abstract class FeedBrowseRepositoryModule {

    @Binds
    abstract fun bindRepository(
        feedBrowseRepositoryImpl: FeedBrowseRepositoryImpl
    ): FeedBrowseRepository
}
