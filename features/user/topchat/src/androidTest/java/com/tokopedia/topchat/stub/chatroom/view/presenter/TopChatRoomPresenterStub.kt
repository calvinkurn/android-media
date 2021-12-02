package com.tokopedia.topchat.stub.chatroom.view.presenter

import android.content.SharedPreferences
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
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
) : TopChatRoomPresenter(
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

    override fun startUploadImageWithService(image: ImageUploadUiModel) {
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