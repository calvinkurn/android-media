package com.tokopedia.feedcomponent.view.span

import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.feedcomponent.util.MentionTextHelper

/**
 * Created by jegul on 2019-08-05.
 */

class MentionSpan(
        @ColorInt private val color: Int,
        val fullText: String,
        val userId: String,
        val fullName: String,
        val start: Int,
        val onClickListener: OnClickListener
) : ClickableSpan() {

    interface OnClickListener {
        fun onClick(userId: String)
    }

    val length = fullName.length

    val end = start + length

    val displayedText: String
        get() = "${MentionTextHelper.MENTION_CHAR}$fullName"

    override fun onClick(p0: View) {
        onClickListener.onClick(userId)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.typeface = Typeface.DEFAULT_BOLD
        ds.isUnderlineText = false
    }
}