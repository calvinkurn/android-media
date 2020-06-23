package com.tokopedia.gamification.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ActivityContextModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

}