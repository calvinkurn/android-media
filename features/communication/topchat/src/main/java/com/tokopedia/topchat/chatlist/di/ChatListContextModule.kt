package com.tokopedia.topchat.chatlist.di

import android.content.Context
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import dagger.Module
import dagger.Provides

@Module
class ChatListContextModule(val context: Context) {

    @Provides
    @ChatListScope
    @TopchatContext
    fun provideContext(): Context = context

}