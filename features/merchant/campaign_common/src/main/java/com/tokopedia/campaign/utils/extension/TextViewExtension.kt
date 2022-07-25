package com.tokopedia.campaign.utils.extension

import android.graphics.Paint
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat

fun AppCompatTextView.strikethrough() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun AppCompatTextView.textColor(@ColorRes resourceId: Int) {
    val color = ContextCompat.getColor(this.context, resourceId)
    this.setTextColor(color)
}