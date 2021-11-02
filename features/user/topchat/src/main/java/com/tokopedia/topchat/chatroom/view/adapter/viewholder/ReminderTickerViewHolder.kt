package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.ReminderTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker

class ReminderTickerViewHolder(
    itemView: View?
) : AbstractViewHolder<ReminderTickerUiModel>(itemView) {

    private val ticker: Ticker? = itemView?.findViewById(R.id.tk_prompt)

    override fun bind(element: ReminderTickerUiModel) {
        bindTitle(element)
        bindDesc(element)
    }

    private fun bindTitle(element: ReminderTickerUiModel) {
        ticker?.tickerTitle = MethodChecker.fromHtml(element.mainText).toString()
    }

    private fun bindDesc(element: ReminderTickerUiModel) {
        val sb = SpannableStringBuilder(element.subText)
        val ctaStartIdx = sb.indexOf(element.urlLabel)
        val ctaEndIdx = ctaStartIdx + element.urlLabel.length
        if (ctaStartIdx == -1) return
        sb.setSpan(
            StyleSpan(Typeface.BOLD),
            ctaStartIdx, ctaEndIdx,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val color = MethodChecker.getColor(
            itemView.context, com.tokopedia.unifycomponents.R.color.Unify_GN500
        )
        sb.setSpan(
            ForegroundColorSpan(color),
            ctaStartIdx, ctaEndIdx,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ticker?.setOnClickListener {
            RouteManager.route(it.context, element.url)
        }
        ticker?.setTextDescription(sb)
    }

    companion object {
        val LAYOUT = R.layout.item_chat_reminder_ticker
    }
}