package com.tokopedia.logisticCommon.util

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.logisticCommon.R
import com.tokopedia.logisticCommon.ui.userconsent.analytic.TncAnalytics

object LogisticUserConsentHelper {

    const val ANA_REVAMP_POSITIVE = "add new address positive"
    const val ANA_REVAMP_NEGATIVE = "add new address negative"
    const val ANA_POSITIVE = "add new address positive old"
    const val ANA_NEGATIVE = "add new address negative old"
    const val EDIT_ADDRESS = "edit address"

    fun displayUserConsent(context: Context, userId: String, textView: TextView?, buttonText: String, screenName: String = "") {
        val onTermsAndConditionClicked: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                if (screenName.isNotEmpty()) {
                    TncAnalytics.onClickTnC(userId, screenName)
                }
                RouteManager.route(context, ApplinkConstInternalLogistic.TNC_WEBVIEW)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_G500
                )
            }
        }
        val boldSpan = StyleSpan(Typeface.BOLD)

        val tncDescription = context.getString(R.string.tnc_description, buttonText)
        val firstIndex = tncDescription.indexOf("Syarat")
        val lastIndex = firstIndex.plus("Syarat & Ketentuan".length)

        val consentText = SpannableString(tncDescription).apply {
            setSpan(onTermsAndConditionClicked, firstIndex,  lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(boldSpan, firstIndex, lastIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textView?.run {
            movementMethod = LinkMovementMethod.getInstance()
            isClickable = true
            setText(consentText, TextView.BufferType.SPANNABLE)
        }
        if (screenName.isNotEmpty()) {
            TncAnalytics.onViewTnC(userId, screenName)
        }
    }
}