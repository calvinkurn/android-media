package com.tokopedia.topchat.stub.chatroom.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
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
            toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
            addToCartUseCase: AddToCartUseCase,
            compressImageUseCase: CompressImageUseCase,
            seamlessLoginUsecase: SeamlessLoginUsecase,
            getChatRoomSettingUseCase: GetChatRoomSettingUseCase,
            addWishListUseCase: AddWishListUseCase,
            removeWishListUseCase: RemoveWishListUseCase,
            uploadImageUseCase: TopchatUploadImageUseCase,
            orderProgressUseCase: OrderProgressUseCase,
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
                        toggleFavouriteShopUseCase,
                        addToCartUseCase,
                        compressImageUseCase,
                        seamlessLoginUsecase,
                        getChatRoomSettingUseCase,
                        addWishListUseCase,
                        removeWishListUseCase,
                        uploadImageUseCase,
                        orderProgressUseCase,
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