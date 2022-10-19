package com.tokopedia.tokochat_common.view.adapter.viewholder.chat_history

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemImageBubbleBinding
import com.tokopedia.tokochat_common.view.adapter.viewholder.binder.TokoChatImageBubbleViewHolderBinder.generateLeftBg
import com.tokopedia.tokochat_common.view.adapter.viewholder.binder.TokoChatImageBubbleViewHolderBinder.generateRightBg
import com.tokopedia.tokochat_common.view.uimodel.TokoChatImageBubbleUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatImageBubbleViewHolder(itemView: View): BaseViewHolder(itemView) {

    private val binding: TokochatItemImageBubbleBinding? by viewBinding()

    private val bgLeft = generateLeftBg(binding?.tokochatLayoutImageBubbleContainer)
    private val bgRight = generateRightBg(binding?.tokochatLayoutImageBubbleContainer)

    fun bind(element: TokoChatImageBubbleUiModel) {
        bindBackground(element)
        bindImage(element)
        bindRetryButton(element)
    }

    private fun bindImage(element: TokoChatImageBubbleUiModel) {
        binding?.tokochatImageBubble?.loadImage(element.imageUrl) {
            setPlaceHolder(R.drawable.tokochat_bg_image_bubble)
            this.listener(
                onSuccess = { bitmap, mediaDataSource ->
                    binding?.tokochatLoaderImageBubble?.hide()
                },
                onError = {
                    binding?.tokochatLoaderImageBubble?.hide()
                }
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
    }

    private fun bindLayoutGravity(gravity: Int) {
        val containerLp = binding?.tokochatLayoutImageBubble?.layoutParams as LinearLayout.LayoutParams
        containerLp.gravity = gravity
        binding?.tokochatLayoutImageBubble?.layoutParams = containerLp
    }

    private fun bindRetryButton(element: TokoChatImageBubbleUiModel) {
        if (element.shouldRetry) {
            binding?.tokochatImageBubbleError?.show()
        } else {
            binding?.tokochatImageBubbleError?.hide()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_image_bubble
    }
}
