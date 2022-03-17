package com.tokopedia.shopadmin.common.utils

import android.graphics.Color
import android.text.method.LinkMovementMethod
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifyprinciples.Typography

fun Typography.setTextMakeHyperlink(text: String, onClick: () -> Unit) {
    val htmlString = HtmlLinkHelper(context, text)
    this.movementMethod = LinkMovementMethod.getInstance()
    this.highlightColor = Color.TRANSPARENT
    this.text = htmlString.spannedString
    htmlString.urlList.getOrNull(0)?.setOnClickListener {
        onClick()
    }
}