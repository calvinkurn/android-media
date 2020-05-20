package com.tokopedia.play.broadcaster.di

import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by jegul on 20/05/20
 */
@Module
class PlayBroadcasterModule {

    @Provides
    @PlayBroadcasterScope
    @Named(PlayBroadcastDispatcher.MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @PlayBroadcasterScope
    @Named(PlayBroadcastDispatcher.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}