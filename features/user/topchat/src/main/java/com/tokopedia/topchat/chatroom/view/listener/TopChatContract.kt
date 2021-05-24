package com.tokopedia.topchat.chatroom.view.listener

import androidx.collection.ArrayMap
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuView
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.usecase.RequestParams
import com.tokopedia.wishlist.common.listener.WishListActionListener

/**
 * @author : Steven 11/12/18
 */
interface TopChatContract {

    interface View : BaseChatContract.View {

        fun addDummyMessage(visitable: Visitable<*>)

        fun removeDummy(visitable: Visitable<*>)

        fun clearEditText()

        fun getStringResource(id: Int): String

        fun showSnackbarError(stringResource: String)

        fun onSuccessGetTemplate(list: List<Visitable<Any>>)

        fun onErrorGetTemplate()

        fun onErrorUploadImage(errorMessage: String, it: ImageUploadViewModel)

        fun focusOnReply()

        fun showAttachmentPreview(attachmentPreview: ArrayList<SendablePreview>)

        fun clearAttachmentPreviews()

        fun sendAnalyticAttachmentSent(attachment: SendablePreview)

        fun redirectToBrowser(url: String)

        fun renderOrderProgress(chatOrder: ChatOrderProgress)

        fun getChatMenuView(): ChatMenuView?

        fun updateAttachmentsView(attachments: ArrayMap<String, Attachment>)

        fun showUnreadMessage(newUnreadMessage: Int)

        fun hideUnreadMessage()

        fun onSendAndReceiveMessage()

        fun renderBackground(url: String)
        fun updateSrwState()
        fun shouldShowSrw(): Boolean
        fun hasProductPreviewShown(): Boolean
        fun hasNoSrw(): Boolean
        fun collapseSrw()
        fun expandSrw()
    }

    interface Presenter : BaseChatContract.Presenter<View> {
        fun connectWebSocket(messageId: String)

        fun startTyping()

        fun stopTyping()

        fun getExistingChat(
                messageId: String,
                onError: (Throwable) -> Unit,
                onSuccessGetExistingMessage: (ChatroomViewModel, ChatReplies) -> Unit)

        fun getMessageId(
                toUserId: String,
                toShopId: String,
                source: String,
                onError: (Throwable) -> Unit,
                onSuccessGetMessageId: (String) -> Unit
        )

        fun readMessage()

        fun startCompressImages(it: ImageUploadViewModel)

        fun startUploadImages(it: ImageUploadViewModel)

        fun loadTopChat(
                messageId: String,
                onError: (Throwable) -> Unit,
                onSuccessGetPreviousChat: (ChatroomViewModel, ChatReplies) -> Unit
        )

        fun loadBottomChat(
                messageId: String,
                onError: (Throwable) -> Unit,
                onsuccess: (ChatroomViewModel, ChatReplies) -> Unit
        )

        fun isUploading(): Boolean

        fun deleteChat(messageId: String,
                       onError: (Throwable) -> Unit,
                       onSuccessDeleteConversation: () -> Unit)

        fun getShopFollowingStatus(shopId: Long,
                                   onError: (Throwable) -> Unit,
                                   onSuccessGetShopFollowingStatus: (Boolean) -> Unit)

        fun copyVoucherCode(fromUid: String?, replyId: String, blastId: String, attachmentId: String, replyTime: String?)

        fun followUnfollowShop(
                shopId: String,
                onError: (Throwable) -> Unit,
                onSuccess: (Boolean) -> Unit,
                action: ToggleFavouriteShopUseCase.Action? = null
        )

        fun sendAttachmentsAndMessage(messageId: String, sendMessage: String,
                                      startTime: String, opponentId: String,
                                      onSendingMessage: () -> Unit)

        fun sendAttachmentsAndSticker(messageId: String, sticker: Sticker,
                                      startTime: String, opponentId: String,
                                      onSendingMessage: () -> Unit)

        fun sendAttachmentsAndSrw(messageId: String, question: QuestionUiModel,
                                  startTime: String, opponentId: String,
                                  onSendingMessage: () -> Unit)

        fun initAttachmentPreview()

        fun clearAttachmentPreview()

        fun initProductPreviewFromAttachProduct(resultProducts: ArrayList<ResultProduct>)

        fun onClickBannedProduct(liteUrl: String)

        fun loadChatRoomSettings(messageId: String, onSuccess: (List<Visitable<TopChatTypeFactory>>) -> Unit)

        fun addToWishList(
                productId: String,
                userId: String,
                wishlistActionListener: WishListActionListener
        )

        fun removeFromWishList(
                productId: String,
                userId: String,
                wishListActionListener: WishListActionListener
        )

        fun getOrderProgress(messageId: String)

        fun getStickerGroupList(chatRoom: ChatroomViewModel)

        fun loadAttachmentData(msgId: Long, chatRoom: ChatroomViewModel)

        fun setBeforeReplyTime(createTime: String)

        fun isInTheMiddleOfThePage(): Boolean

        fun resetChatUseCase()

        fun resetUnreadMessage()

        fun requestBlockPromo(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit)

        fun requestAllowPromo(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit)

        fun blockChat(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit)

        fun unBlockChat(messageId: String, onSuccess: (ChatSettingsResponse) -> Unit, onError: (Throwable) -> Unit)

        fun getBackground()

        fun addAttachmentPreview(sendablePreview: SendablePreview)

        fun hasEmptyAttachmentPreview(): Boolean

        fun addProductToCart(
                requestParams: RequestParams,
                onSuccessAddToCart: (data: DataModel) -> Unit,
                onError: (msg: String) -> Unit
        )

        fun addOngoingUpdateProductStock(
                productId: String,
                product: ProductAttachmentViewModel, adapterPosition: Int,
                parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
        )

        fun getSmartReplyWidget(msgId: String)
        fun initUserLocation(userLocation: LocalCacheModel?)
        fun getProductIdPreview(): List<String>
        fun getAttachmentsPreview(): List<SendablePreview>
        fun sendSrwBubble(
                messageId: String, question: QuestionUiModel,
                products: List<SendablePreview>, opponentId: String,
                onSendingMessage: () -> Unit
        )
    }
}