package com.tokopedia.otp.common.action

import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.Matcher
import org.hamcrest.Matchers


object SpannableTypographyAction {

    fun clickClickableSpan(textToClick: CharSequence): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return Matchers.instanceOf(Typography::class.java)
            }

            override fun getDescription(): String {
                return "Click ClickableSpan on $textToClick text"
            }

            override fun perform(uiController: UiController?, view: View) {
                val typography = view as Typography
                val spannableString = typography.text as SpannableString
                if (spannableString.isEmpty()) {
                    throw NoMatchingViewException.Builder()
                            .includeViewHierarchy(true)
                            .withRootView(typography)
                            .build()
                }

                val spans = spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)
                if (spans.isNotEmpty()) {
                    var spanCandidate: ClickableSpan?
                    for (span in spans) {
                        spanCandidate = span
                        val start = spannableString.getSpanStart(spanCandidate)
                        val end = spannableString.getSpanEnd(spanCandidate)
                        val sequence = spannableString.subSequence(start, end)
                        if (textToClick.toString() == sequence.toString()) {
                            span.onClick(typography)
                            return
                        }
                    }
                }

                throw NoMatchingViewException.Builder()
                        .includeViewHierarchy(true)
                        .withRootView(typography)
                        .build()
            }
        }
    }
}