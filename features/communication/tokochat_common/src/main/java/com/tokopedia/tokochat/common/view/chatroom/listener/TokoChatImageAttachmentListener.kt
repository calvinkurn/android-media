package com.tokopedia.tokochat.common.view.chatroom.listener

import android.widget.ImageView
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatImageBubbleUiModel

interface TokoChatImageAttachmentListener {
    fun loadImage(
        imageView: ImageView,
        element: TokoChatImageBubbleUiModel,
        isFromRetry: Boolean
    )
    fun onClickImage(element: TokoChatImageBubbleUiModel)
    fun resendImage(element: TokoChatImageBubbleUiModel)
    fun onImageDelivered(element: TokoChatImageBubbleUiModel)
}
