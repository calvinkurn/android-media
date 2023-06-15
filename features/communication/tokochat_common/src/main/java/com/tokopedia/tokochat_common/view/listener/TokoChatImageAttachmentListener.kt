package com.tokopedia.tokochat_common.view.listener

import android.widget.ImageView
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel

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
