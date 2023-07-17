package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.chat_history

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemImageBubbleBinding
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatImageBubbleViewHolderBinder.generateLeftBg
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatImageBubbleViewHolderBinder.generateRightBg
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatImageBubbleViewHolderBinder.generateTextButtonBg
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder
import com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.binder.TokoChatMessageBubbleViewHolderBinder.bindChatReadStatus
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatImageAttachmentListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class TokoChatImageBubbleViewHolder(
    itemView: View,
    private val tokoChatImageAttachmentListener: TokoChatImageAttachmentListener
) : BaseViewHolder(itemView) {

    private val binding: TokochatItemImageBubbleBinding? by viewBinding()

    private val bgLeft = generateLeftBg(binding?.tokochatLayoutImageBubbleContainer)
    private val bgRight = generateRightBg(binding?.tokochatLayoutImageBubbleContainer)
    private val bgTextButton = generateTextButtonBg(binding?.tokochatTvImageBubbleError)

    fun bind(element: TokoChatImageBubbleUiModel) {
        bindBackgroundBubble(element)
        bindBackgroundImage(element)
        bindImage(element)
        bindRetryButton(element)
        bindLoader(element)
        bindStatus(element)
        bindTime(element)
        bindOnClick(element)
        bindRetryUploadButton(element)
        handleImageDelivered(element)
    }

    private fun bindImage(element: TokoChatImageBubbleUiModel, isFromRetry: Boolean = false) {
        if (element.state == TokoChatImageBubbleUiModel.ImageState.LOADING_LOAD) {
            binding?.tokochatTvImageBubbleError?.hide()
            binding?.tokochatImageBubble?.let {
                it.setImageDrawable(null)
                tokoChatImageAttachmentListener.loadImage(
                    it,
                    element,
                    isFromRetry
                )
            }
        }
    }

    private fun bindBackgroundBubble(element: TokoChatImageBubbleUiModel) {
        if (element.isSender) {
            bindLayoutGravity(Gravity.END)
            binding?.tokochatLayoutImageBubbleContainer?.background = bgRight
        } else {
            bindLayoutGravity(Gravity.START)
            binding?.tokochatLayoutImageBubbleContainer?.background = bgLeft
        }
    }

    private fun bindBackgroundImage(element: TokoChatImageBubbleUiModel) {
        val placeholder = if (element.state == TokoChatImageBubbleUiModel.ImageState.ERROR_LOAD) {
            ERROR_RES_UNIFY
        } else {
            null
        }

        val background = when (element.state) {
            TokoChatImageBubbleUiModel.ImageState.LOADING_LOAD,
            TokoChatImageBubbleUiModel.ImageState.ERROR_LOAD -> {
                R.drawable.tokochat_bg_image_bubble_gradient
            }
            TokoChatImageBubbleUiModel.ImageState.LOADING_UPLOAD,
            TokoChatImageBubbleUiModel.ImageState.ERROR_UPLOAD -> {
                R.drawable.tokochat_bg_image_bubble_white_loading
            }
            TokoChatImageBubbleUiModel.ImageState.SUCCESS -> null
        }

        loadImagePlaceholder(
            placeholder = placeholder,
            background = background
        )
    }

    private fun loadImagePlaceholder(
        @DrawableRes placeholder: Int?,
        @DrawableRes background: Int?
    ) {
        try {
            if (placeholder != null) {
                binding?.tokochatImageBubble?.loadImage(placeholder)
            }
            if (background != null) {
                binding?.tokochatImageBubbleDim?.loadImage(background)
                binding?.tokochatImageBubbleDim?.show()
            } else {
                binding?.tokochatImageBubbleDim?.hide()
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = binding?.tokochatLayoutImageBubble as LinearLayout
        containerLp.gravity = gravity
    }

    private fun bindRetryButton(element: TokoChatImageBubbleUiModel) {
        val shouldShow = element.state == TokoChatImageBubbleUiModel.ImageState.ERROR_LOAD
        binding?.tokochatTvImageBubbleError?.showWithCondition(shouldShow)
        binding?.tokochatTvImageBubbleError?.background = bgTextButton
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
        binding?.tokochatTvImageBubbleError?.setOnClickListener {
            element.updateImageState(TokoChatImageBubbleUiModel.ImageState.LOADING_LOAD)
            bindLoader(element)
            bindImage(element, isFromRetry = true)
        }
        binding?.tokochatIconImageBubbleErrorUpload?.setOnClickListener {
            tokoChatImageAttachmentListener.resendImage(element)
        }
    }

    private fun bindTime(element: TokoChatImageBubbleUiModel) {
        val time = TokoChatMessageBubbleViewHolderBinder.getHourTime(element.messageTime)
        binding?.tokochatImageBubbleHour?.text = time
    }

    private fun bindLoader(element: TokoChatImageBubbleUiModel) {
        val shouldShow = element.state == TokoChatImageBubbleUiModel.ImageState.LOADING_LOAD ||
            element.state == TokoChatImageBubbleUiModel.ImageState.LOADING_UPLOAD
        binding?.tokochatLoaderImageBubble?.showWithCondition(shouldShow)
        if (shouldShow) {
            binding?.tokochatLoaderImageBubble?.type = LoaderUnify.TYPE_CIRCULAR
        }
    }

    private fun bindRetryUploadButton(element: TokoChatImageBubbleUiModel) {
        val shouldShow = element.state == TokoChatImageBubbleUiModel.ImageState.ERROR_UPLOAD
        binding?.tokochatIconImageBubbleErrorUpload?.showWithCondition(shouldShow)
    }

    private fun handleImageDelivered(element: TokoChatImageBubbleUiModel) {
        if (element.isSent() || element.isRead()) {
            tokoChatImageAttachmentListener.onImageDelivered(element)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_image_bubble
    }
}
