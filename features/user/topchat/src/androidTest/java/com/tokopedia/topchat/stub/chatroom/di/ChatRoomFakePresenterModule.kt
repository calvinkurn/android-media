package com.tokopedia.topchat.stub.chatroom.di

import android.content.SharedPreferences
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.TopchatAndroidTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.di.ChatScope
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.topchat.stub.chatroom.view.presenter.TopChatRoomPresenterStub
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import dagger.Module
import dagger.Provides

@Module
class ChatRoomFakePresenterModule {

    @Provides
    @ChatScope
    fun provideTestDispatcher(): TopchatCoroutineContextProvider {
        return TopchatAndroidTestCoroutineContextDispatcher()
    }

    @Provides
    @ChatScope
    fun provideGetChatUseCase(
            tkpdAuthInterceptor: TkpdAuthInterceptor,
            fingerprintInterceptor: FingerprintInterceptor,
            userSession: UserSessionInterface,
            webSocketUtil: RxWebSocketUtil,
            getChatUseCase: GetChatUseCase,
            topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
            getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
            replyChatUseCase: ReplyChatUseCase,
            getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
            deleteMessageListUseCase: DeleteMessageListUseCase,
            getShopFollowingUseCase: GetShopFollowingUseCase,
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
            sharedPref: SharedPreferences,
            dispatchers: TopchatCoroutineContextProvider
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
                getExistingMessageIdUseCase,
                deleteMessageListUseCase,
                getShopFollowingUseCase,
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
                sharedPref,
                dispatchers
        )
    }

}