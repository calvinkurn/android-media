package com.tokopedia.usercomponents.common.utils

import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.util.TreeIterables
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher


fun setTextOnTextFieldUnify2(text: String, resId: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return withId(resId)
        }

        override fun getDescription(): String {
            return "Set text $text on TextFieldUnify2"
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as TextFieldUnify2).editText.setText(text)
        }

    }
}

fun <T : View?> clickChildViewWithId(resId: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return withId(resId)
        }

        override fun getDescription(): String {
            return "Click on a child view with id $resId"
        }

        override fun perform(uiController: UiController?, view: View?) {
            view?.findViewById<T>(resId)?.performClick()
        }

    }
}

fun waitOnView(
    viewMatcher: Matcher<View>,
    waitMillis: Int = 5000,
    waitMillisPerTry: Long = 100
): ViewInteraction {
    val maxTries = waitMillis / waitMillisPerTry.toInt()
    var tries = 0
    for (i in 0..maxTries)
        try {
            tries++
            Espresso.onView(ViewMatchers.isRoot()).perform(searchFor(viewMatcher))
            return Espresso.onView(viewMatcher)
        } catch (e: Exception) {
            if (tries == maxTries) {
                throw e
            }
            Thread.sleep(waitMillisPerTry)
        }
    throw NoSuchElementException()
}

fun searchFor(matcher: Matcher<View>): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isRoot()
        }

        override fun getDescription(): String {
            return "Searching for view $matcher in the root view"
        }

        override fun perform(uiController: UiController, view: View) {
            var tries = 0
            val childViews: Iterable<View> = TreeIterables.breadthFirstViewTraversal(view)
            childViews.forEach {
                tries++
                if (matcher.matches(it)) {
                    return
                }
            }
            throw NoMatchingViewException.Builder()
                .withRootView(view)
                .withViewMatcher(matcher)
                .build()
        }
    }
}

fun clickClickableSpanOnTypographyUnify(textToClick: CharSequence): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.instanceOf(Typography::class.java)
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
