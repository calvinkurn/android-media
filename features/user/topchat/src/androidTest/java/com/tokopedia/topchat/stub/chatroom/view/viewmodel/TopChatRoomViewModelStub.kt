package com.tokopedia.topchat.stub.chatroom.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.topchat.common.websocket.TopchatWebSocket
import com.tokopedia.topchat.common.websocket.WebSocketParser
import com.tokopedia.topchat.common.websocket.WebSocketStateHandler
import com.tokopedia.topchat.common.websocket.WebsocketPayloadGenerator
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import javax.inject.Inject

class TopChatRoomViewModelStub @Inject constructor(
    getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
    getShopFollowingUseCase: GetShopFollowingUseCase,
    toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
    addToCartUseCase: AddToCartUseCase,
    seamlessLoginUsecase: SeamlessLoginUsecase,
    getChatRoomSettingUseCase: GetChatRoomSettingUseCase,
    orderProgressUseCase: OrderProgressUseCase,
    reminderTickerUseCase: GetReminderTickerUseCase,
    closeReminderTicker: CloseReminderTicker,
    addToCartOccUseCase: AddToCartOccMultiUseCase,
    chatToggleBlockChat: ChatToggleBlockChatUseCase,
    moveChatToTrashUseCase: MutationMoveChatToTrashUseCase,
    getChatBackgroundUseCase: GetChatBackgroundUseCase,
    chatAttachmentUseCase: ChatAttachmentUseCase,
    getChatListGroupStickerUseCase: GetChatListGroupStickerUseCase,
    chatSrwUseCase: GetSmartReplyQuestionUseCase,
    tokoNowWHUsecase: GetChatTokoNowWarehouseUseCase,
    addWishListUseCase: AddWishListUseCase,
    removeWishListUseCase: RemoveWishListUseCase,
    getChatUseCase: GetChatUseCase,
    unsendReplyUseCase: UnsendReplyUseCase,
    dispatcher: CoroutineDispatchers,
    remoteConfig: RemoteConfig,
    chatAttachmentMapper: ChatAttachmentMapper,
    existingChatMapper: TopChatRoomGetExistingChatMapper,
    chatWebSocket: TopchatWebSocket,
    webSocketStateHandler: WebSocketStateHandler,
    webSocketParser: WebSocketParser,
    topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
    payloadGenerator: WebsocketPayloadGenerator,
    uploadImageUseCase: TopchatUploadImageUseCase,
    compressImageUseCase: CompressImageUseCase,
    getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase
): TopChatViewModel(
    getExistingMessageIdUseCase,
    getShopFollowingUseCase,
    toggleFavouriteShopUseCase,
    addToCartUseCase,
    seamlessLoginUsecase,
    getChatRoomSettingUseCase,
    orderProgressUseCase,
    reminderTickerUseCase,
    closeReminderTicker,
    addToCartOccUseCase,
    chatToggleBlockChat,
    moveChatToTrashUseCase,
    getChatBackgroundUseCase,
    chatAttachmentUseCase,
    getChatListGroupStickerUseCase,
    chatSrwUseCase,
    tokoNowWHUsecase,
    addWishListUseCase,
    removeWishListUseCase,
    getChatUseCase,
    unsendReplyUseCase,
    dispatcher,
    remoteConfig,
    chatAttachmentMapper,
    existingChatMapper,
    chatWebSocket,
    webSocketStateHandler,
    webSocketParser,
    topChatRoomWebSocketMessageMapper,
    payloadGenerator,
    uploadImageUseCase,
    compressImageUseCase,
    getTemplateChatRoomUseCase
) {

    var errorValidateImage = false
    var errorValidateType: Int = -1

    override fun validateImageAttachment(imageUploadUiModel: ImageUploadUiModel): Pair<Boolean, Int> {
        return if (errorValidateImage) {
            Pair(false, errorValidateType)
        } else {
            Pair(true, ImageUtil.IMAGE_VALID)
        }
    }
}