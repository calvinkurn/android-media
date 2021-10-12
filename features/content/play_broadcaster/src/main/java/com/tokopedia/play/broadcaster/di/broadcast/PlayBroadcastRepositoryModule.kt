package com.tokopedia.play.broadcaster.di.broadcast

import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastChannelRepositoryImpl
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastPinnedMessageRepositoryImpl
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastRepositoryImpl
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
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
    @PlayBroadcastScope
    abstract fun bindChannelRepository(
        repo: PlayBroadcastChannelRepositoryImpl
    ): PlayBroadcastChannelRepository

    @Binds
    @PlayBroadcastScope
    abstract fun bindPinnedMessageRepository(
        repo: PlayBroadcastPinnedMessageRepositoryImpl
    ): PlayBroadcastPinnedMessageRepository

    @Binds
    @PlayBroadcastScope
    abstract fun bindRepository(
        repo: PlayBroadcastRepositoryImpl
    ): PlayBroadcastRepository
}