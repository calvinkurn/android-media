package com.tokopedia.topchat.chatroom.view.listener

import androidx.collection.ArrayMap
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.ChatOrderProgress
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.custom.ChatMenuView
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
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

        fun onErrorUploadImage(errorMessage: String, it: ImageUploadUiModel)

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
        fun updateSrwPreviewState()
        fun shouldShowSrw(): Boolean
        fun hasProductPreviewShown(): Boolean
        fun hasNoSrw(): Boolean
        fun collapseSrw()
        fun expandSrw()
        fun removeSrwBubble()

        /**
         * Remove SRW bubble if [productId] is not relevant with
         * current visible SRW bubble
         */
        fun removeSrwBubble(productId: String)
        fun expandSrwBubble()
        fun showPreviewMsg(previewMsg: SendableUiModel)
        fun clearReferredMsg()
        fun notifyPreviewRemoved(model: SendablePreview)
    }

    interface Presenter : BaseChatContract.Presenter<View> {
        fun connectWebSocket(messageId: String)

        fun startTyping()

        fun stopTyping()

        fun getExistingChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccessGetExistingMessage: (ChatroomViewModel, ChatReplies) -> Unit
        )

        fun readMessage()

        fun startCompressImages(it: ImageUploadUiModel)

        fun startUploadImages(image: ImageUploadUiModel)

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

        fun deleteChat(
            messageId: String,
            onError: (Throwable) -> Unit,
            onSuccessDeleteConversation: () -> Unit
        )

        fun sendAttachmentsAndMessage(
            sendMessage: String, referredMsg: ParentReply? = null
        )

        fun sendAttachmentsAndSticker(
            sticker: Sticker, referredMsg: ParentReply?
        )

        fun sendAttachmentsAndSrw(
            question: QuestionUiModel, referredMsg: ParentReply?
        )

        fun initAttachmentPreview()

        fun clearAttachmentPreview()

        fun initProductPreviewFromAttachProduct(resultProducts: ArrayList<ResultProduct>)

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

        fun getStickerGroupList(chatRoom: ChatroomViewModel)

        fun loadAttachmentData(msgId: Long, chatRoom: ChatroomViewModel)

        fun setBeforeReplyTime(createTime: String)

        fun isInTheMiddleOfThePage(): Boolean

        fun resetChatUseCase()

        fun resetUnreadMessage()

        fun requestBlockPromo(
            messageId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
        )

        fun requestAllowPromo(
            messageId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
        )

        fun blockChat(
            messageId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
        )

        fun unBlockChat(
            messageId: String,
            onSuccess: (ChatSettingsResponse) -> Unit,
            onError: (Throwable) -> Unit
        )

        fun getBackground()

        fun addAttachmentPreview(sendablePreview: SendablePreview)

        fun hasEmptyAttachmentPreview(): Boolean

        fun addOngoingUpdateProductStock(
            productId: String,
            product: ProductAttachmentUiModel, adapterPosition: Int,
            parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
        )

        fun getSmartReplyWidget(msgId: String, productIds: String)
        fun initUserLocation(userLocation: LocalCacheModel?)
        fun getProductIdPreview(): List<String>
        fun getAttachmentsPreview(): List<SendablePreview>
        fun sendSrwBubble(
            question: QuestionUiModel, products: List<SendablePreview>
        )
        fun adjustInterlocutorWarehouseId(msgId: String)
        fun sendSrwFrom(attachment: HeaderCtaButtonAttachment)
    }
}