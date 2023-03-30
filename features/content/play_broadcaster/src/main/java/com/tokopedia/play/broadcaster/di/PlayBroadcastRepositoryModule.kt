package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.data.repository.*
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.domain.repository.*
import dagger.Binds
import dagger.Module

/**
 * Created by jegul on 01/10/21
 */
@Module
abstract class PlayBroadcastRepositoryModule {

    @Binds
    @ActivityRetainedScope
    abstract fun bindChannelRepository(
        repo: PlayBroadcastChannelRepositoryImpl
    ): PlayBroadcastChannelRepository

    @Binds
    @ActivityRetainedScope
    abstract fun bindPinnedMessageRepository(
        repo: PlayBroadcastPinnedMessageRepositoryImpl
    ): PlayBroadcastPinnedMessageRepository


    @Binds
    @ActivityRetainedScope
    abstract fun bindInteractiveRepository(
        repo: PlayBroadcastInteractiveRepositoryImpl
    ): PlayBroadcastInteractiveRepository

    @Binds
    @ActivityRetainedScope
    abstract fun bindProductRepository(
        repo: PlayBroProductRepositoryImpl
    ): PlayBroProductRepository

    @Binds
    @ActivityRetainedScope
    abstract fun bindBeautificationRepository(
        repo: PlayBroadcastBeautificationRepositoryImpl
    ): PlayBroadcastBeautificationRepository

    @Binds
    @ActivityRetainedScope
    abstract fun bindRepository(
        repo: PlayBroadcastRepositoryImpl
    ): PlayBroadcastRepository
}
