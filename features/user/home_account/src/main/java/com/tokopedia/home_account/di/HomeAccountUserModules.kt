package com.tokopedia.home_account.di

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/10/18.
 */
@Module
class HomeAccountUserModules(val context: Context) {

    @HomeAccountUserScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @Provides
    @HomeAccountUserContext
    fun provideContext(): Context = context

}