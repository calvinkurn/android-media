package com.tokopedia.feedplus.di

import com.tokopedia.feedplus.data.repository.FeedRepositoryImpl
import com.tokopedia.feedplus.domain.repository.FeedRepository
import dagger.Binds
import dagger.Module

/**
 * Created by meyta.taliti on 09/08/23.
 */
@Module
abstract class FeedRepositoryModule {

    @Binds
    abstract fun bindRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository
}
