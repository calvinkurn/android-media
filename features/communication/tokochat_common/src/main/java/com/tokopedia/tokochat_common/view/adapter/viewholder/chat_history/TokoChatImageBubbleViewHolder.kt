package com.tokopedia.tokochat_common.view.adapter.viewholder.chat_history

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemImageBubbleBinding
import com.tokopedia.tokochat_common.view.adapter.viewholder.binder.TokoChatImageBubbleViewHolderBinder.generateLeftBg
import com.tokopedia.tokochat_common.view.adapter.viewholder.binder.TokoChatImageBubbleViewHolderBinder.generateRightBg
import com.tokopedia.tokochat_common.view.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder
import com.tokopedia.tokochat_common.view.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder.bindChatReadStatus
import com.tokopedia.tokochat_common.view.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatImageBubbleViewHolder(
    itemView: View,
    private val tokoChatImageAttachmentListener: TokoChatImageAttachmentListener
) : BaseViewHolder(itemView) {

    private val binding: TokochatItemImageBubbleBinding? by viewBinding()

    private val bgLeft = generateLeftBg(binding?.tokochatLayoutImageBubbleContainer)
    private val bgRight = generateRightBg(binding?.tokochatLayoutImageBubbleContainer)

    fun bind(element: TokoChatImageBubbleUiModel) {
        bindBackground(element)
        bindImage(element)
        bindRetryButton(element)
        bindStatus(element)
        bindTime(element)
        bindOnClick(element)
    }

    // Hide retry button, hide loading
    fun bindWithSuccessPayload(element: TokoChatImageBubbleUiModel) {
        bindRetryButton(element)
        bindLoader(false)
        bindRetryUploadButton(false)
    }

    // Show retry button, hide loading
    fun bindWithErrorLoadPayload(element: TokoChatImageBubbleUiModel) {
        bindRetryButton(element)
        bindLoader(false)
        bindRetryUploadButton(false)
    }

    // Show image locally, add retry upload button
    fun bindWithErrorUploadPayload(element: TokoChatImageBubbleUiModel) {
        bindRetryButton(element)
        bindLoader(false)
        bindRetryUploadButton(true)
    }

    private fun bindImage(element: TokoChatImageBubbleUiModel, isFromRetry: Boolean = false) {
        binding?.tokochatIconImageBubbleError?.hide()
        binding?.tokochatImageBubble?.let {
            tokoChatImageAttachmentListener.loadImage(
                it,
                element,
                isFromRetry
            )
        }
    }

    private fun bindBackground(element: TokoChatImageBubbleUiModel) {
        if (element.isSender) {
            bindLayoutGravity(Gravity.END)
            binding?.tokochatLayoutImageBubbleContainer?.background = bgRight
        } else {
            bindLayoutGravity(Gravity.START)
            binding?.tokochatLayoutImageBubbleContainer?.background = bgLeft
        }

        try {
            binding?.tokochatImageBubble?.loadImage(R.drawable.tokochat_bg_image_bubble_gradient)
        } catch (ignored: Throwable) {}
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = binding?.tokochatLayoutImageBubble as LinearLayout
        containerLp.gravity = gravity
    }

    private fun bindRetryButton(element: TokoChatImageBubbleUiModel) {
        if (element.shouldRetry) {
            binding?.tokochatIconImageBubbleError?.show()
        } else {
            binding?.tokochatIconImageBubbleError?.hide()
        }
    }

    private fun bindStatus(element: TokoChatImageBubbleUiModel) {
        binding?.tokochatImageBubbleStatus?.let {
            bindChatReadStatus(element, it)
        }
    }

    private fun bindOnClick(element: TokoChatImageBubbleUiModel) {
        binding?.tokochatImageBubble?.setOnClickListener {
            if (element.isImageReady) {
                tokoChatImageAttachmentListener.onClickImage(element)
            }
        }
        binding?.tokochatIconImageBubbleError?.setOnClickListener {
            bindImage(element, isFromRetry = true)
            bindLoader(true)
        }
        binding?.tokochatIconImageBubbleErrorUpload?.setOnClickListener {
            bindImagePlaceHolder()
            bindLoader(true)
            // Resend image
        }
    }

    private fun bindTime(element: TokoChatImageBubbleUiModel) {
        val time = TokoChatMessageBubbleViewHolderBinder.getHourTime(element.messageTime)
        binding?.tokochatImageBubbleHour?.text = time
    }

    private fun bindLoader(shouldShow: Boolean) {
        binding?.tokochatLoaderImageBubble?.showWithCondition(shouldShow)
        if (shouldShow) {
            binding?.tokochatLoaderImageBubble?.type = LoaderUnify.TYPE_CIRCULAR
        }
    }

    private fun bindRetryUploadButton(shouldShow: Boolean) {
        binding?.tokochatIconImageBubbleErrorUpload?.showWithCondition(shouldShow)
    }

    private fun bindImagePlaceHolder() {
        binding?.tokochatImageBubble?.loadImage(R.drawable.tokochat_bg_image_bubble_gradient)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_image_bubble
    }

    enum class PAYLOAD(val key: String) {
        PAYLOAD_SUCCESS("isSuccess"),
        PAYLOAD_ERROR_LOAD("isErrorLoad"),
        PAYLOAD_ERROR_UPLOAD("isErrorUpload")
    }
}
