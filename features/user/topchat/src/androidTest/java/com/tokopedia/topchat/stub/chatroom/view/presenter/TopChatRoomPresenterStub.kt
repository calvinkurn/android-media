package com.tokopedia.topchat.stub.chatroom.view.presenter

import android.content.SharedPreferences
import com.google.gson.JsonObject
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.data.UploadImageDummy
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.common.mapper.ImageUploadMapper
import com.tokopedia.topchat.stub.chatroom.view.service.UploadImageChatServiceStub
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import javax.inject.Inject

class TopChatRoomPresenterStub @Inject constructor(
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
        chatSrwUseCase: SmartReplyQuestionUseCase,
        sharedPref: SharedPreferences,
        dispatchers: CoroutineDispatchers,
        remoteConfig: RemoteConfig
) : TopChatRoomPresenter(tkpdAuthInterceptor,
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
        chatSrwUseCase,
        sharedPref,
        dispatchers,
        remoteConfig) {

    override fun sendAttachmentsAndMessage(
            messageId: String,
            sendMessage: String,
            startTime: String,
            opponentId: String,
            onSendingMessage: () -> Unit
    ) {
        super.sendAttachmentsAndMessage(
                messageId, sendMessage, exStartTime, opponentId, onSendingMessage
        )
    }

    override fun sendMessageWebSocket(messageText: String) {
        webSocketUtil.send(messageText)
    }

    override fun sendMessageJsonObjWebSocket(msgObj: JsonObject) {
        webSocketUtil.send(msgObj.toString())
    }

    override fun startUploadImages(image: ImageUploadViewModel) {
        //always upload with service
        addDummyToService(image)
        startUploadImageWithService(image)
    }

    private fun addDummyToService(image: ImageUploadViewModel) {
        view?.addDummyMessage(image)
        val uploadImageDummy = UploadImageDummy(messageId = thisMessageId, visitable = image)
        UploadImageChatService.dummyMap.add(uploadImageDummy)
    }

    private fun startUploadImageWithService(image: ImageUploadViewModel) {
        UploadImageChatServiceStub.enqueueWork(view.context, ImageUploadMapper.mapToImageUploadServer(image), thisMessageId)
    }

    companion object {
        const val exStartTime = "123123123"
    }
}