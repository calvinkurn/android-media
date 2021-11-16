package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import android.content.SharedPreferences
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
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.stub.chatroom.view.presenter.TopChatRoomPresenterStub
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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
            getChatUseCase: GetChatUseCase,
            topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
            getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
            replyChatUseCase: ReplyChatUseCase,
            compressImageUseCase: CompressImageUseCase,
            addWishListUseCase: AddWishListUseCase,
            removeWishListUseCase: RemoveWishListUseCase,
            uploadImageUseCase: TopchatUploadImageUseCase,
            groupStickerUseCase: ChatListGroupStickerUseCase,
            chatAttachmentUseCase: ChatAttachmentUseCase,
            chatToggleBlockChat: ChatToggleBlockChatUseCase,
            chatBackgroundUseCase: ChatBackgroundUseCase,
            chatSrwUseCase: SmartReplyQuestionUseCase,
            tokoNowWHUsecase: ChatTokoNowWarehouseUseCase,
            moveChatToTrashUseCase: MutationMoveChatToTrashUseCase,
            sharedPref: SharedPreferences,
            dispatchers: CoroutineDispatchers,
            remoteConfig: RemoteConfig
    ): TopChatRoomPresenter {
        return TopChatRoomPresenterStub(
                        tkpdAuthInterceptor,
                        fingerprintInterceptor,
                        userSession,
                        webSocketUtil,
                        getChatUseCase,
                        topChatRoomWebSocketMessageMapper,
                        getTemplateChatRoomUseCase,
                        replyChatUseCase,
                        compressImageUseCase,
                        addWishListUseCase,
                        removeWishListUseCase,
                        uploadImageUseCase,
                        groupStickerUseCase,
                        chatAttachmentUseCase,
                        chatToggleBlockChat,
                        chatBackgroundUseCase,
                        chatSrwUseCase,
                        tokoNowWHUsecase,
                        moveChatToTrashUseCase,
                        sharedPref,
                        dispatchers,
                        remoteConfig
                )
    }

}