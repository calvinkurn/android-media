package com.tokopedia.topchat.chatroom.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ChatRoomContextModule(val context: Context) {

    @Provides
    @ChatScope
    fun provideContext(): Context = context

}