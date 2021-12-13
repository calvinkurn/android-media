package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.stub.chatroom.view.presenter.TopChatRoomPresenterStub
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import dagger.Module
import dagger.Provides

@Module
class ChatRoomFakePresenterModule {

    @ChatScope
    @Provides
    fun provideRemoteConfig(@TopchatContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @ChatScope
    fun provideTopChatRoomPresenter(
            tkpdAuthInterceptor: TkpdAuthInterceptor,
            fingerprintInterceptor: FingerprintInterceptor,
            userSession: UserSessionInterface,
            webSocketUtil: RxWebSocketUtil,
            topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
            getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
            replyChatUseCase: ReplyChatUseCase,
            compressImageUseCase: CompressImageUseCase,
            uploadImageUseCase: TopchatUploadImageUseCase,
            dispatchers: CoroutineDispatchers,
            remoteConfig: RemoteConfig
    ): TopChatRoomPresenter {
        return TopChatRoomPresenterStub(
                        tkpdAuthInterceptor,
                        fingerprintInterceptor,
                        userSession,
                        webSocketUtil,
                        topChatRoomWebSocketMessageMapper,
                        getTemplateChatRoomUseCase,
                        replyChatUseCase,
                        compressImageUseCase,
                        uploadImageUseCase,
                        dispatchers,
                        remoteConfig
                )
    }

}