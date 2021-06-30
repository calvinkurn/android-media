package com.tokopedia.product_bundle.common.extension

import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.Label

internal fun TotalAmount.setTitleText(discount: String, slashPrice: String) {
    val labelView = Label(context).apply {
        text = discount
        setLabelType(Label.GENERAL_LIGHT_RED)
    }
    titleContainerView.addView(labelView, 0)
    labelTitleSuffixView.paintFlags = labelTitleSuffixView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    labelTitleView.text = " "
    labelTitleSuffixView.text = slashPrice
}


internal fun TotalAmount.setSubtitleText(prefix: String, price: String) {
    val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500)
    val spannable: Spannable = SpannableString("$prefix $price")
    spannable.setSpan(ForegroundColorSpan(color), prefix.length, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    labelSubTitleView.setText(spannable, TextView.BufferType.SPANNABLE)
}