package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.ReminderTickerUiModel
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
                RouteManager.route(itemView.context, element.url)
            }
            override fun onDismiss() {
                commonListener.getAnalytic().eventCloseTickerReminder(
                    commonListener.getCommonShopId()
                )
                listener?.closeReminderTicker(element, adapterPosition)
            }
        })
    }

    companion object {
        val LAYOUT = R.layout.item_chat_reminder_ticker
    }
}