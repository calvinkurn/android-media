package com.tokopedia.topchat.chatroom.view.customview

import android.os.Parcelable
import com.tokopedia.chat_common.data.BlockedStatus
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.topchat.chatroom.view.viewmodel.ProductPreview

interface TopChatViewState : BaseChatViewState {

    fun showErrorWebSocket(b: Boolean)

    fun showRetryUploadImages(it: ImageUploadViewModel, b: Boolean)

    fun onSetCustomMessage(customMessage: String)

    fun getLastItem(): Parcelable?

    fun onCheckChatBlocked(opponentRole: String,
                           opponentName : String,
                           blockedStatus: BlockedStatus,
                           onUnblockChatClicked: () -> Unit)

    fun showAttachmentPreview(attachmentPreview: ArrayList<ProductPreview>)

    fun focusOnReply()

    fun sendAnalyticsClickBuyNow(element: ProductAttachmentViewModel)

    fun sendAnalyticsClickATC(element: ProductAttachmentViewModel)

}
