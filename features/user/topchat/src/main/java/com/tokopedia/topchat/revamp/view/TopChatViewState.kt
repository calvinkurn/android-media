package com.tokopedia.topchat.revamp.view

import android.os.Parcelable
import com.tokopedia.chat_common.data.BlockedStatus
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState

interface TopChatViewState : BaseChatViewState {

    abstract fun showErrorWebSocket(b: Boolean)

    abstract fun showRetryUploadImages(it: ImageUploadViewModel, b: Boolean)

    fun onSetCustomMessage(customMessage: String)

    fun getLastItem(): Parcelable?

    fun showChatBlocked(it: BlockedStatus,
                        opponentRole: String,
                        opponentName: String,
                        onUnblockChatClicked : () -> Unit)

    fun removeChatBlocked()

}
