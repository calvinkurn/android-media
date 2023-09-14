package com.tokopedia.tokochat.common.view.chatroom.adapter.viewholder.ticker

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatItemReminderTickerBinding
import com.tokopedia.tokochat.common.view.chatroom.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatReminderTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatReminderTickerViewHolder(
    view: View,
    private val listener: TokochatReminderTickerListener?
) : BaseViewHolder(view) {

    private val binding: TokochatItemReminderTickerBinding? by viewBinding()

    fun bind(element: TokoChatReminderTickerUiModel) {
        bindImpression(element)
        bindDescAndClose(element)
        bindTickerType(element)
    }

    private fun bindImpression(element: TokoChatReminderTickerUiModel) {
        binding?.tokochatTkPrompt?.addOnImpressionListener(element.impressHolder) {
            listener?.trackSeenTicker(element)
        }
    }

    private fun bindDescAndClose(element: TokoChatReminderTickerUiModel) {
        binding?.tokochatTkPrompt?.setHtmlDescription(element.message)
        binding?.tokochatTkPrompt?.setDescriptionClickEvent(object: TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listener?.onClickLinkReminderTicker(element, linkUrl.toString())
            }
            override fun onDismiss() {
                listener?.onCloseReminderTicker(element, bindingAdapterPosition)
            }
        })

        if (element.showCloseButton) {
            binding?.tokochatTkPrompt?.closeButtonVisibility = View.VISIBLE
        } else {
            binding?.tokochatTkPrompt?.closeButtonVisibility = View.GONE
        }
    }

    private fun bindTickerType(element: TokoChatReminderTickerUiModel) {
        binding?.tokochatTkPrompt?.tickerType = element.tickerType
        binding?.tokochatTkPrompt?.show()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.tokochat_item_reminder_ticker
    }
}
