package com.tokopedia.tokochat_common.view.listener

import android.widget.ImageView
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify

interface TokoChatImageAttachmentListener {
    fun loadImage(
        imageView: ImageView,
        element: TokoChatImageBubbleUiModel,
        loader: LoaderUnify?,
        retryIcon: ImageUnify?,
        isFromRetry: Boolean
    )
    fun onClickImage(element: TokoChatImageBubbleUiModel)
}
