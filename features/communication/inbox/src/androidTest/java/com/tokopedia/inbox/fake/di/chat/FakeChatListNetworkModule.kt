package com.tokopedia.inbox.fake.di.chat

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.chat.websocket.FakeTopchatWebSocket
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topchat.common.websocket.*
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * equivalent to
 * [com.tokopedia.topchat.chatlist.di.ChatListNetworkModule]
 */
@Module
class FakeChatListNetworkModule {
    @ActivityScope
    @Provides
    fun provideWebSocketParser(): WebSocketParser {
        return DefaultWebSocketParser()
    }

    @ActivityScope
    @Provides
    fun provideWebSocketStateHandler(): WebSocketStateHandler {
        return DefaultWebSocketStateHandler()
    }

    // -- separator -- //

    @ActivityScope
    @Provides
    fun provideUserSession(
        fakeSession: FakeUserSession
    ): UserSessionInterface {
        return fakeSession
    }

    // -- separator -- //

    @ActivityScope
    @Provides
    fun provideTopChatWebSocket(
        fakeTopchatWebSocket: FakeTopchatWebSocket
    ): TopchatWebSocket {
        return fakeTopchatWebSocket
    }

    // -- separator -- //

    @ActivityScope
    @Provides
    fun provideAbTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }
}
