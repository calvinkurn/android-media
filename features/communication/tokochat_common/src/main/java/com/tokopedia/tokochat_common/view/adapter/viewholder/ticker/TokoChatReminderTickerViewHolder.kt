package com.tokopedia.tokochat_common.view.adapter.viewholder.ticker

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.ItemTokochatReminderTickerBinding
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokochatReminderTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class TokoChatReminderTickerViewHolder(
    view: View,
    private val listener: TokochatReminderTickerListener?
) : BaseViewHolder(view) {

    private val binding: ItemTokochatReminderTickerBinding? by viewBinding()

    fun bind(element: TokochatReminderTickerUiModel) {
        bindImpression(element)
        bindDescAndClose(element)
        bindTickerType(element)
    }

    private fun bindImpression(element: TokochatReminderTickerUiModel) {
        binding?.tokochatTkPrompt?.addOnImpressionListener(element.impressHolder) {
            listener?.trackSeenTicker(element)
        }
    }

    private fun bindDescAndClose(element: TokochatReminderTickerUiModel) {
        binding?.tokochatTkPrompt?.setHtmlDescription(element.message)
        binding?.tokochatTkPrompt?.setDescriptionClickEvent(object: TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                listener?.onClickLinkReminderTicker(element, linkUrl.toString())
            }
            override fun onDismiss() {
                listener?.onCloseReminderTicker(element, bindingAdapterPosition)
            }
        })
    }

    private fun bindTickerType(element: TokochatReminderTickerUiModel) {
        binding?.tokochatTkPrompt?.tickerType = element.tickerType
        binding?.tokochatTkPrompt?.show()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokochat_reminder_ticker
    }
}
