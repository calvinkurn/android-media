package com.tokopedia.libra.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.libra.DebugLibraInstance
import com.tokopedia.libra.domain.usecase.GetLibraCacheUseCase
import com.tokopedia.libra.domain.usecase.GetLibraRemoteUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [LibraModule::class])
class DebugLibraModule {

    @Provides
    @LibraScope
    fun providesDebugLibraInstance(
        @ApplicationContext context: Context,
        remoteUseCase: GetLibraRemoteUseCase,
        cacheUseCase: GetLibraCacheUseCase
    ): DebugLibraInstance {
        return DebugLibraInstance(
            context,
            remoteUseCase,
            cacheUseCase
        )
    }
}
