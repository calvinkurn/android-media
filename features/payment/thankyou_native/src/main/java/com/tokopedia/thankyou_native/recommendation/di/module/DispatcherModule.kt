package com.tokopedia.thankyou_native.recommendation.di.module

import com.tokopedia.thankyou_native.recommendation.di.scope.RecommendationScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named


@Module
class DispatcherModule {

    @RecommendationScope
    @Provides
    @Named(MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @RecommendationScope
    @Provides
    @Named(IO)
    fun provideWorkerDispatcher(): CoroutineDispatcher = Dispatchers.IO

    companion object {
        const val MAIN = "MAIN"
        const val IO = "IO"
    }

}