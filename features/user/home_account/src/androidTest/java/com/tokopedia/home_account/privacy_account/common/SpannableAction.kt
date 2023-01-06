package com.tokopedia.home_account.privacy_account.common

import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matcher

fun clickOnSpannable(textToClick: CharSequence): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = instanceOf(TextView::class.java)

        override fun getDescription(): String = "clicking on Spannable"

        override fun perform(uiController: UiController?, view: View) {
            val textView = view as TextView
            val spannableString = textView.text as SpannableString
            if (spannableString.isEmpty()) {
                throwSpannable(textView)
            }

            // Get the links inside the TextView and check if we find textToClick
            val spans = spannableString.getSpans(
                0, spannableString.length,
                ClickableSpan::class.java
            )
            if (spans.isNotEmpty()) {
                var spanCandidate: ClickableSpan?
                for (span in spans) {
                    spanCandidate = span
                    val start = spannableString.getSpanStart(spanCandidate)
                    val end = spannableString.getSpanEnd(spanCandidate)
                    val sequence = spannableString.subSequence(start, end)
                    if (textToClick.toString() == sequence.toString()) {
                        span.onClick(textView)
                        return
                    }
                }
            }
            throwSpannable(textView)
        }
    }
}

fun throwSpannable(textView: TextView) {
    throw NoMatchingViewException.Builder()
        .includeViewHierarchy(true)
        .withRootView(textView)
        .build()
}
