package com.tokopedia.topchat.chatlist.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ChatListContextModule(val context: Context) {

    @Provides
    @ChatListScope
    fun provideContext(): Context = context

}