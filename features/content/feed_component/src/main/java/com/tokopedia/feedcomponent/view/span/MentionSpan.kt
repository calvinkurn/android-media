package com.tokopedia.feedcomponent.view.span

import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * Created by jegul on 2019-08-05.
 */

class MentionSpan(
        @ColorInt private val color: Int,
        val fullText: String,
        val userId: String,
        val fullName: String
) : ClickableSpan() {

    val length = fullName.length

    override fun onClick(p0: View) {

    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.typeface = Typeface.DEFAULT_BOLD
        ds.isUnderlineText = false
    }
}