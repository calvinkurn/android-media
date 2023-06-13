package com.tokopedia.play.di

import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play_common.websocket.PlayWebSocket
import dagger.Module
import dagger.Provides
import io.mockk.mockk

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
@Module
class PlayTestRepositoryModule(
    val repo: PlayViewerRepository = mockk(relaxed = true),
    val webSocket: PlayWebSocket = mockk(relaxed = true),
) {

    @Provides
    fun provideViewerRepository(): PlayViewerRepository {
        return repo
    }

    @Provides
    fun provideSocket(): PlayWebSocket = webSocket
}
