package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.const.Unify.Green_G500
import com.tokopedia.settingnotif.usersetting.domain.pojo.SmsSection
import com.tokopedia.unifycomponents.ticker.Ticker

class SmsSectionViewHolder(itemView: View?): AbstractViewHolder<SmsSection>(itemView) {

    private val txtTickerDescription = itemView?.findViewById<Ticker>(R.id.txtTickerDescription)

    override fun bind(element: SmsSection?) {
        val content = itemView.context.getString(R.string.settingnotif_ticker_sms_desc)
        val inFull = itemView.context.getString(R.string.settingnotif_ticker_sms_more)
        val shorten = "$content\n$inFull"
        val spannable = SpannableString(shorten)
        val color = ContextCompat.getColor(itemView.context, Green_G500)
        spannable.setSpan(
                ForegroundColorSpan(color),
                shorten.indexOf(inFull),
                shorten.indexOf(inFull) + inFull.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
                StyleSpan(BOLD),
                shorten.indexOf(inFull),
                shorten.indexOf(inFull) + inFull.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        txtTickerDescription?.setTextDescription(spannable)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_sms_ticker_section
    }

}