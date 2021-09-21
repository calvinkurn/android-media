package com.tokopedia.inbox.fake.di.chat

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.chat.websocket.FakeTopchatWebSocket
import com.tokopedia.topchat.chatlist.di.ChatListScope
import com.tokopedia.topchat.chatlist.domain.websocket.*
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * equivalent to
 * [com.tokopedia.topchat.chatlist.di.ChatListNetworkModule]
 */
@Module
class FakeChatListNetworkModule {
    @ChatListScope
    @Provides
    fun provideWebSocketParser(): WebSocketParser {
        return DefaultWebSocketParser()
    }

    @ChatListScope
    @Provides
    fun provideWebSocketStateHandler(): WebSocketStateHandler {
        return DefaultWebSocketStateHandler()
    }

    // -- separator -- //

    @ChatListScope
    @Provides
    fun provideUserSession(
            fakeSession: FakeUserSession
    ): UserSessionInterface {
        return fakeSession
    }

    @ChatListScope
    @Provides
    fun provideFakeUserSession(
            @ApplicationContext context: Context
    ): FakeUserSession {
        return FakeUserSession(context)
    }

    // -- separator -- //

    @ChatListScope
    @Provides
    fun provideTopChatWebSocket(
            fakeTopchatWebSocket: FakeTopchatWebSocket
    ): TopchatWebSocket {
        return fakeTopchatWebSocket
    }

    @ChatListScope
    @Provides
    fun provideFakeTopChatWebSocket(): FakeTopchatWebSocket {
        return FakeTopchatWebSocket()
    }

}