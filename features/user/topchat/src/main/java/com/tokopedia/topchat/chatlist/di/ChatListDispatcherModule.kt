package com.tokopedia.topchat.chatlist.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import dagger.Module
import dagger.Provides

@Module
class ChatListDispatcherModule {

    @Provides
    @ChatListScope
    fun provideTestDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider
}