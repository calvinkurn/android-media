package com.tokopedia.topchat.chatroom.view.listener

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.BlockedStatus
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.topchat.chatroom.view.viewmodel.ProductPreview
import com.tokopedia.topchat.common.TopChatRouter
import kotlin.collections.ArrayList

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

        fun showErrorWebSocket(b: Boolean)

        fun onBackPressedEvent()

        fun getStringArgument(key: String, savedInstanceState: Bundle?): String

        fun focusOnReply()

        fun showAttachmentPreview(attachmentPreview: ArrayList<ProductPreview>)

        fun notifyAttachmentsSent()
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

        fun startUploadImages(it: ImageUploadViewModel)

        fun loadPreviousChat(messageId: String, page: Int, onError: (Throwable) -> Unit, onSuccessGetPreviousChat: (ChatroomViewModel) -> Unit)

        fun addProductToCart(router: TopChatRouter,
                             element: ProductAttachmentViewModel,
                             onError: (Throwable) -> Unit,
                             onSuccess: (addToCartResult: AddToCartDataModel) -> Unit,
                             shopId: Int)

        fun isUploading(): Boolean

        fun sendProductAttachment(messageId: String, item: ResultProduct,
                                  startTime: String, opponentId: String)

        fun deleteChat(messageId: String,
                       onError: (Throwable) -> Unit,
                       onSuccessDeleteConversation: () -> Unit)

        fun unblockChat(messageId : String,
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
                        onSendingMessage : () -> Unit)

        fun initProductPreview(savedInstanceState: Bundle?)

        fun initAttachmentPreview()

        fun clearAttachmentPreview()
    }
}