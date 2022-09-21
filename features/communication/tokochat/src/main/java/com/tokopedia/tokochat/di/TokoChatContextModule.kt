package com.tokopedia.tokochat.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class TokoChatContextModule(private val context: Context) {

    @Provides
    @TokoChatScope
    @TokoChatContext
    fun provideContext(): Context = context

}
