package com.tokopedia.topchat.chatroom.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.BlockedStatus
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.topchat.common.TopChatRouter
import com.tokopedia.transaction.common.sharedata.AddToCartResult

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
                             onSuccess: (addToCartResult: AddToCartResult) -> Unit,
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

    }
}