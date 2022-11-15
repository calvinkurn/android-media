package com.tokopedia.play.di

import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play_common.websocket.PlayWebSocket
import dagger.Module
import dagger.Provides

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
@Module
class PlayTestRepositoryModule(val repo: PlayViewerRepository, val webSocket: PlayWebSocket) {

    @Provides
    fun provideViewerRepository(): PlayViewerRepository {
        return repo
    }

    @Provides
    fun provideSocket(): PlayWebSocket = webSocket
}
