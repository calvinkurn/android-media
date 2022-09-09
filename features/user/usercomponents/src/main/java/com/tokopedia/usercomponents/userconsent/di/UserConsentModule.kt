package com.tokopedia.usercomponents.userconsent.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object UserConsentModule {

    @Provides
    @ActivityScope
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.Main
}