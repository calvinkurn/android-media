package com.tokopedia.topchat.stub.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.presenter.TopChatRoomPresenter
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
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
    tokoNowWHUsecase: ChatTokoNowWarehouseUseCase,
    moveChatToTrashUseCase: MutationMoveChatToTrashUseCase,
    sharedPref: SharedPreferences,
    dispatchers: CoroutineDispatchers,
    remoteConfig: RemoteConfig
) : TopChatRoomPresenter(
    tkpdAuthInterceptor,
    fingerprintInterceptor,
    userSession,
    webSocketUtil,
    getChatUseCase,
    topChatRoomWebSocketMessageMapper,
    getTemplateChatRoomUseCase,
    replyChatUseCase,
    getExistingMessageIdUseCase,
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
    tokoNowWHUsecase,
    moveChatToTrashUseCase,
    sharedPref,
    dispatchers,
    remoteConfig
) {

    override fun sendMessageWebSocket(messageText: String) {
        webSocketUtil.send(messageText)
    }

    override fun sendMessageJsonObjWebSocket(msgObj: JsonObject) {
        webSocketUtil.send(msgObj.toString())
    }

    override fun isEnableUploadImageService(): Boolean {
        return true
    }

    override fun startUploadImageWithService(image: ImageUploadViewModel) {
        UploadImageChatServiceStub.enqueueWork(
            InstrumentationRegistry.getInstrumentation().context,
            ImageUploadMapper.mapToImageUploadServer(image),
            thisMessageId
        )
    }

    companion object {
        const val exStartTime = "123123123"
    }
}