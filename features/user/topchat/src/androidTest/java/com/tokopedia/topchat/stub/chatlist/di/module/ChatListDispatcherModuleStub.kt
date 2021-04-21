package com.tokopedia.topchat.stub.chatlist.di.module

import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import dagger.Module
import dagger.Provides

@Module
class ChatListDispatcherModuleStub {

    @Provides
    @ChatListScope
    fun provideTestDispatcher(): CoroutineDispatchers = CoroutineTestDispatchersProvider
}