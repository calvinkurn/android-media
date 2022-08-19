package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.uimodel.ReminderTickerUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class ReminderTickerViewHolder(
    itemView: View?,
    private val listener: Listener?,
    private val commonListener: CommonViewHolderListener
) : AbstractViewHolder<ReminderTickerUiModel>(itemView) {

    private val ticker: Ticker? = itemView?.findViewById(R.id.tk_prompt)

    interface Listener {
        fun closeReminderTicker(element: ReminderTickerUiModel, position: Int)
    }

    override fun bind(element: ReminderTickerUiModel) {
        bindImpression(element)
        bindTitle(element)
        bindDescAndClose(element)
        bindTickerType(element)
    }

    private fun bindImpression(element: ReminderTickerUiModel) {
        ticker?.addOnImpressionListener(element.impressHolder) {
            commonListener.getAnalytic().eventViewTickerReminder(commonListener.getCommonShopId())
        }
    }

    private fun bindTitle(element: ReminderTickerUiModel) {
        ticker?.tickerTitle = MethodChecker.fromHtml(element.mainText).toString()
    }

    private fun bindDescAndClose(element: ReminderTickerUiModel) {
        ticker?.setHtmlDescription(element.subText)
        ticker?.setDescriptionClickEvent(object: TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                commonListener.getAnalytic().eventClickTickerReminderCta(
                    commonListener.getCommonShopId()
                )
                itemView.context?.let { context ->
                    val intent = RouteManager.getIntent(context, element.url)
                    context.startActivity(intent)
                }
            }
            override fun onDismiss() {
                commonListener.getAnalytic().eventCloseTickerReminder(
                    commonListener.getCommonShopId()
                )
                listener?.closeReminderTicker(element, adapterPosition)
            }
        })
    }

    private fun bindTickerType(element: ReminderTickerUiModel) {
        ticker?.tickerType = getTickerType(element)
    }

    private fun getTickerType(element: ReminderTickerUiModel): Int {
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

        val LAYOUT = R.layout.item_chat_reminder_ticker
    }
}