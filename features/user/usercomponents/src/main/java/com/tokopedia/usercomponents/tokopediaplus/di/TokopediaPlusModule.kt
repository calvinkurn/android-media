package com.tokopedia.usercomponents.tokopediaplus.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object TokopediaPlusModule {

    @Provides
    @ActivityScope
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}