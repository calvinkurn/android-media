package com.tokopedia.topchat.chatroom.view.customview

import android.os.Parcelable
import com.tokopedia.chat_common.data.BlockedStatus
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview

interface TopChatViewState : BaseChatViewState {

    fun showRetryUploadImages(it: ImageUploadUiModel, b: Boolean)

    fun onSetCustomMessage(customMessage: String)

    fun getLastItem(): Parcelable?

    fun onCheckChatBlocked(
        opponentRole: String,
        opponentName: String,
        blockedStatus: BlockedStatus
    )

    fun showAttachmentPreview(attachmentPreview: ArrayList<SendablePreview>)

    fun focusOnReply()

    fun getChatRoomHeaderModel(): ChatRoomHeaderUiModel

    fun onStickerOpened()

    fun onStickerClosed()

    fun setChatBlockStatus(isBlocked: Boolean)

    fun setChatPromoBlockStatus(isBlocked: Boolean, due: String = "")

    fun showConfirmationBlockChat()

    fun hasProductPreviewShown(): Boolean
    fun showTemplateChatIfReady(
        lastMessageBroadcast: Boolean,
        lastMessageSrwBubble: Boolean,
        amIBuyer: Boolean
    )

    fun attachFragmentView(fragmentView: TopChatContract.View)
    fun hideKeyboard()
    fun hasVisibleSendablePreview(): Boolean
    fun isKeyboardOpen(): Boolean
}
