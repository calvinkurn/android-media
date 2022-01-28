package com.tokopedia.analyticsdebugger.serverlogger.di.module

import androidx.annotation.Nullable
import com.tokopedia.analyticsdebugger.serverlogger.di.scope.ServerLoggerScope
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.repository.LoggerRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ServerLoggerViewModelModule::class])
class ServerLoggerModule {

    @Provides
    @ServerLoggerScope
    @Nullable
    fun provideLoggerRepository(): LoggerRepository? {
        return LogManager.instance?.getLogger()
    }

}