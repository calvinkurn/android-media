package com.tokopedia.analyticsdebugger.sse.di

import com.tokopedia.analyticsdebugger.sse.data.repository.SSELogRepositoryImpl
import com.tokopedia.analyticsdebugger.sse.domain.repository.SSELogRepository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
@Module
abstract class SSELoggingBindModule {

    @Binds
    abstract fun bindSSERepository(sseLogRepositoryImpl: SSELogRepositoryImpl): SSELogRepository
}