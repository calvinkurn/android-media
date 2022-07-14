package com.tokopedia.campaign.utils.extension

import android.graphics.Paint
import android.widget.TextView

fun TextView.strikethrough() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}
