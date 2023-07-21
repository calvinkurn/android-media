package com.tokopedia.people.utils

import android.content.Context
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifyprinciples.R

/**
 * Created By : Jonathan Darwin on May 16, 2023
 */
fun getClickableSpan(onClick: () -> Unit): ClickableSpan {
    return object : ClickableSpan() {
        override fun onClick(p0: View) {
            onClick()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
}

internal fun getBoldSpan() = StyleSpan(Typeface.BOLD)

internal fun getGreenColorSpan(context: Context) = ForegroundColorSpan(MethodChecker.getColor(context, R.color.Unify_GN500))
