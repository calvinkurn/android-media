package com.tokopedia.analyticsdebugger.websocket.di

import com.tokopedia.analyticsdebugger.websocket.data.repository.WebSocketLogRepositoryImpl
import com.tokopedia.analyticsdebugger.websocket.domain.repository.WebSocketLogRespository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */

@Module
abstract class WebSocketLoggingBindModule {

    @Binds
    @WebSocketLoggingScope
    abstract fun bindWebSocketRepository(webSocketLogRepositoryImpl: WebSocketLogRepositoryImpl): WebSocketLogRespository
}