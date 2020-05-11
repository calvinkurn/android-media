package com.tokopedia.topchat.chatroom.view.listener

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.BlockedStatus
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
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

        fun onErrorUploadImage(errorMessage: String, it: ImageUploadViewModel)


        fun getStringArgument(key: String, savedInstanceState: Bundle?): String

        fun getBooleanArgument(key: String, savedInstanceState: Bundle?): Boolean

        fun focusOnReply()

        fun showAttachmentPreview(attachmentPreview: ArrayList<SendablePreview>)

        fun clearAttachmentPreviews()

        fun getShopName(): String

        fun sendAnalyticAttachmentSent(attachment: SendablePreview)

        fun redirectToBrowser(url: String)

        fun isUseNewCard(): Boolean

        fun isUseCarousel(): Boolean?
    }

    interface Presenter : BaseChatContract.Presenter<View> {
        fun connectWebSocket(messageId: String)

        fun startTyping()

        fun stopTyping()

        fun getExistingChat(
                messageId: String,
                onError: (Throwable) -> Unit,
                onSuccessGetExistingMessage: (ChatroomViewModel) -> Unit)

        fun getMessageId(
                toUserId: String,
                toShopId: String,
                source: String,
                onError: (Throwable) -> Unit,
                onSuccessGetMessageId: (String) -> Unit
        )

        fun startCompressImages(it: ImageUploadViewModel)

        fun startUploadImages(it: ImageUploadViewModel)

        fun loadPreviousChat(
                messageId: String,
                onError: (Throwable) -> Unit,
                onSuccessGetPreviousChat: (ChatroomViewModel) -> Unit
        )

        fun isUploading(): Boolean

        fun deleteChat(messageId: String,
                       onError: (Throwable) -> Unit,
                       onSuccessDeleteConversation: () -> Unit)

        fun unblockChat(messageId: String,
                        opponentRole: String,
                        onError: (Throwable) -> Unit,
                        onSuccessUnblockChat: (BlockedStatus) -> Unit)

        fun getShopFollowingStatus(shopId: Int,
                                   onError: (Throwable) -> Unit,
                                   onSuccessGetShopFollowingStatus: (Boolean) -> Unit)

        fun copyVoucherCode(fromUid: String?, replyId: String, blastId: String, attachmentId: String, replyTime: String?)

        fun followUnfollowShop(shopId: String,
                               onError: (Throwable) -> Unit,
                               onSuccess: (Boolean) -> Unit)

        fun sendAttachmentsAndMessage(messageId: String, sendMessage: String,
                                      startTime: String, opponentId: String,
                                      onSendingMessage: () -> Unit)

        fun initProductPreview(savedInstanceState: Bundle?)

        fun initAttachmentPreview()

        fun clearAttachmentPreview()

        fun initInvoicePreview(savedInstanceState: Bundle?)

        fun getAtcPageIntent(context: Context?, element: ProductAttachmentViewModel): Intent

        fun initProductPreviewFromAttachProduct(resultProducts: ArrayList<ResultProduct>)

        fun onClickBannedProduct(liteUrl: String)

        fun getBuyPageIntent(context: Context?, element: ProductAttachmentViewModel): Intent

        fun initVoucherPreview(extras: Bundle?)

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

        fun updateMinReplyTime(chatRoom: ChatroomViewModel)
    }
}