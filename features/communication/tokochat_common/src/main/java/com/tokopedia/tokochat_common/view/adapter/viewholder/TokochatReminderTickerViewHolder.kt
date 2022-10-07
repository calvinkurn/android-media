package com.tokopedia.tokochat_common.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.ItemTokochatReminderTickerBinding
import com.tokopedia.tokochat_common.view.listener.TokochatReminderTickerListener
import com.tokopedia.tokochat_common.view.uimodel.TokochatReminderTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class TokochatReminderTickerViewHolder(
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
        binding?.tokochatTkPrompt?.tickerType = getTickerType(element)
        binding?.tokochatTkPrompt?.show()
    }

    private fun getTickerType(element: TokochatReminderTickerUiModel): Int {
        return when (element.tickerType) {
            TYPE_WARNING -> Ticker.TYPE_WARNING
            TYPE_ANNOUNCEMENT -> Ticker.TYPE_ANNOUNCEMENT
            TYPE_ERROR -> Ticker.TYPE_ERROR
            TYPE_INFORMATION -> Ticker.TYPE_INFORMATION
            else -> Ticker.TYPE_ANNOUNCEMENT
        }
    }

    companion object {
        private const val TYPE_WARNING = "warning"
        private const val TYPE_ANNOUNCEMENT = "announcement"
        private const val TYPE_ERROR = "error"
        private const val TYPE_INFORMATION = "information"

        @LayoutRes
        val LAYOUT = R.layout.item_tokochat_reminder_ticker
    }
}
