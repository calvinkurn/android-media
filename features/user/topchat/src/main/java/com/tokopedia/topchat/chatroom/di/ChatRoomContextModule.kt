package com.tokopedia.topchat.chatroom.di

import android.content.Context
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import dagger.Module
import dagger.Provides

@Module
class ChatRoomContextModule(val context: Context) {

    @Provides
    @ChatScope
    @TopchatContext
    fun provideContext(): Context = context

}