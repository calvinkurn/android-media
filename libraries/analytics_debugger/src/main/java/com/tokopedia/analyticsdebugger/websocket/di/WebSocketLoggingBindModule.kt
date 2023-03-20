package com.tokopedia.analyticsdebugger.websocket.di

import com.tokopedia.analyticsdebugger.websocket.data.repository.PlayWebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.data.repository.TopchatWebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.domain.repository.PlayWebSocketLogRepository
import com.tokopedia.analyticsdebugger.websocket.domain.repository.TopchatWebSocketLogRepository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */

@Module
abstract class WebSocketLoggingBindModule {

    @Binds
    @WebSocketLoggingScope
    abstract fun bindPlayWebSocketRepository(
        repository: PlayWebSocketLogRepositoryImpl
    ): PlayWebSocketLogRepository

    @Binds
    @WebSocketLoggingScope
    abstract fun bindTopchatWebSocketRepository(
        repository: TopchatWebSocketLogRepositoryImpl
    ): TopchatWebSocketLogRepository
}
