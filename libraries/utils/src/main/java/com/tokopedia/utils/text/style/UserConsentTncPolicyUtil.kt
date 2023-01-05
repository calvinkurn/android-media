package com.tokopedia.utils.text.style

import android.graphics.Typeface
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * This is temporary user consent for open shop & create shop
 * for long term solution: tribe needs to integrate with user consent widget
 */
object UserConsentTncPolicyUtil {

    fun generateText(
        message: String,
        tncTitle: String,
        policyTitle: String,
        actionTnc: () -> Unit,
        actionPolicy: () -> Unit,
        colorSpan: Int
    ): SpannableString {
        return SpannableString(String.format(message, tncTitle, policyTitle)).apply {
            setSpan(
                createClickableSpannable(actionTnc, colorSpan),
                this.indexOf(tncTitle),
                this.indexOf(tncTitle) + tncTitle.length,
                0
            )

            setSpan(
                createClickableSpannable(actionPolicy, colorSpan),
                this.indexOf(policyTitle),
                this.indexOf(policyTitle) + policyTitle.length,
                0
            )
        }
    }

    private fun createClickableSpannable(action: () -> Unit, colorSpan: Int): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                action.invoke()
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                ds.color =  colorSpan
            }
        }
    }
}
