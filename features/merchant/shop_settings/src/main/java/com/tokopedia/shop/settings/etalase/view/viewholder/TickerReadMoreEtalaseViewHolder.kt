package com.tokopedia.shop.settings.etalase.view.viewholder

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreViewModel


class TickerReadMoreEtalaseViewHolder(
        itemView: View,
        private val tickerReadMoreListener: TickerReadMoreListener?
) : AbstractViewHolder<TickerReadMoreViewModel>(itemView) {

    private val textViewTitle: TextView
    private val textViewDescription: TextView

    init {
        textViewTitle = itemView.findViewById(R.id.txt_ticker_title)
        textViewDescription = itemView.findViewById(R.id.txt_ticker_description)
    }

    interface TickerReadMoreListener {
        fun onReadMoreClicked()
    }

    override fun bind(tickerReadMoreViewModel: TickerReadMoreViewModel) {
        textViewTitle.text = tickerReadMoreViewModel.tickerTitle
        textViewDescription.movementMethod = LinkMovementMethod.getInstance()
        textViewDescription.text = createDescriptionWithSpannable(
                tickerReadMoreViewModel.tickerDescription,
                tickerReadMoreViewModel.readMoreString
        )
    }

    private fun createDescriptionWithSpannable(originalText: String, readMoreText: String): SpannableStringBuilder {
        val spannableText = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableText.length
        val color = itemView.resources.getColor(R.color.merchant_green)
        spannableText.setSpan(color, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                tickerReadMoreListener?.onReadMoreClicked()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder(originalText).append(" ").append(spannableText)
    }

    companion object {

        val LAYOUT = R.layout.item_shop_etalase_ticker_idle_pm
    }

}
