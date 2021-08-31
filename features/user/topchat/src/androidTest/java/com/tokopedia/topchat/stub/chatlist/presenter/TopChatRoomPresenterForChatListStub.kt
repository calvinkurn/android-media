package com.tokopedia.topchat.stub.chatlist.presenter

import android.content.SharedPreferences
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.ResultItem
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.background.ChatBackgroundResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.stub.chatroom.usecase.*
import com.tokopedia.topchat.stub.chatroom.view.presenter.TopChatRoomPresenterStub
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import javax.inject.Inject

class TopChatRoomPresenterForChatListStub @Inject constructor(
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
) : TopChatRoomPresenterStub (
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
    var firstPageChatAsBuyer = GetExistingChatPojo()
    var firstPageChatAsSeller = GetExistingChatPojo()
    var chatAttachmentResponse = ChatAttachmentResponse()
    var stickerGroupAsBuyer = ChatListGroupStickerResponse()
    var stickerListAsBuyer = StickerResponse()
    var firstPageChatBroadcastAsBuyer = GetExistingChatPojo()
    var getShopFollowingStatus = ShopFollowingPojo()
    var chatSrwResponse = ChatSmartReplyQuestionResponse()
    var uploadImageReplyResponse = ChatReplyPojo()
    var orderProgressResponse = OrderProgressResponse()
    var chatBackgroundResponse = ChatBackgroundResponse()
    var chatRoomSettingResponse = RoomSettingResponse()

    init {
        initResponses()
        (getChatRoomSettingUseCase as GetChatRoomSettingUseCaseStub).response = chatRoomSettingResponse
        (chatBackgroundUseCase as ChatBackgroundUseCaseStub).response = chatBackgroundResponse
        (getChatUseCase as GetChatUseCaseStub).response = firstPageChatAsBuyer
        (orderProgressUseCase as OrderProgressUseCaseStub).response = orderProgressResponse
        (chatAttachmentUseCase as ChatAttachmentUseCaseStub).response = chatAttachmentResponse
        (chatSrwUseCase as SmartReplyQuestionUseCaseStub).response = chatSrwResponse
        getShopFollowingStatus.apply {
            val item = ResultItem()
            item.favoriteData.alreadyFavorited = 1
            this.shopInfoById.result = arrayListOf(item)
            (getShopFollowingUseCase as GetShopFollowingUseCaseStub).response = this
        }
        (groupStickerUseCase as ChatListGroupStickerUseCaseStub).response = stickerGroupAsBuyer
        (getTemplateChatRoomUseCase as GetTemplateChatRoomUseCaseStub).response = generateTemplateResponse(true)
    }

    private fun initResponses() {
        firstPageChatAsBuyer = AndroidFileUtil.parse(
            "success_get_chat_first_page_as_buyer.json",
            GetExistingChatPojo::class.java
        )
        firstPageChatAsSeller = AndroidFileUtil.parse(
            "success_get_chat_first_page_as_seller.json",
            GetExistingChatPojo::class.java
        )
        chatAttachmentResponse = AndroidFileUtil.parse(
            "success_get_chat_attachments.json",
            ChatAttachmentResponse::class.java
        )
        stickerGroupAsBuyer = AndroidFileUtil.parse(
            "success_chat_group_sticker.json",
            ChatListGroupStickerResponse::class.java
        )
        stickerListAsBuyer = AndroidFileUtil.parse(
            "success_chat_bundle_sticker.json",
            StickerResponse::class.java
        )
        firstPageChatBroadcastAsBuyer = AndroidFileUtil.parse(
            "success_get_chat_broadcast.json",
            GetExistingChatPojo::class.java
        )
        getShopFollowingStatus = AndroidFileUtil.parse(
            "success_get_shop_following_status.json",
            ShopFollowingPojo::class.java
        )
        uploadImageReplyResponse = AndroidFileUtil.parse(
            "success_upload_image_reply.json",
            ChatReplyPojo::class.java
        )
    }

    private fun generateTemplateResponse(
        enable: Boolean = true,
        success: Boolean = true,
        templates: List<String> = listOf("Template Chat 1", "Template Chat 2")
    ): TemplateData {
        return TemplateData().apply {
            this.isIsEnable = enable
            this.isSuccess = success
            this.templates = templates
        }
    }

}