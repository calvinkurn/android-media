package com.tokopedia.mvcwidget.views.viewholders

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.TickerText
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class TickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvTitle: Typography = itemView.findViewById(R.id.tvTitleMvcDetailView)

    fun setData(tickerText: TickerText) {
        val hardcodedText = itemView.context.getString(R.string.mvc_title_prefix)
        val finalText = "$hardcodedText ${tickerText.text}."
        val spannableString = SpannableString(finalText)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), finalText.length - tickerText.text.length - 1, finalText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvTitle.text = spannableString
        if(tickerText.removeTickerTopMargin){
            ((tvTitle.parent as LinearLayout).layoutParams as ViewGroup.MarginLayoutParams).topMargin = 0
        }else{
            ((tvTitle.parent as LinearLayout).layoutParams as ViewGroup.MarginLayoutParams).topMargin = 16.toPx()
        }
    }
}