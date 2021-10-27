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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.logisticCommon.R
import com.tokopedia.remoteconfig.RemoteConfigInstance

object LogisticCommonUtil {

    const val ANA_REVAMP_ROLLENCE = "revamp_ana"
    const val TNC_LINK = "https://www.google.com"

    /**
     * Rollence key
     */
    fun isRollOutUserANARevamp(): Boolean {
        val rollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(ANA_REVAMP_ROLLENCE, "")
        return rollenceValue == ANA_REVAMP_ROLLENCE
    }

    fun displayUserConsent(context: Context, textView: TextView?, buttonText: String) {
        val onTermsAndConditionClicked: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=${TNC_LINK}")
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
    }
}