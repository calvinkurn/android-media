package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.data.repository.PlayBroProductRepositoryImpl
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastChannelRepositoryImpl
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastInteractiveRepositoryImpl
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastPinnedMessageRepositoryImpl
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastRepositoryImpl
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.domain.repository.PlayBroProductRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastInteractiveRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastPinnedMessageRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
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
    abstract fun bindRepository(
        repo: PlayBroadcastRepositoryImpl
    ): PlayBroadcastRepository
}