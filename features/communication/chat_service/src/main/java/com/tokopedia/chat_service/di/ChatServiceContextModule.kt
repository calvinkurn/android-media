package com.tokopedia.chat_service.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ChatServiceContextModule(private val context: Context) {

    @Provides
    @ChatServiceScope
    @ChatServiceContext
    fun provideContext(): Context = context

}