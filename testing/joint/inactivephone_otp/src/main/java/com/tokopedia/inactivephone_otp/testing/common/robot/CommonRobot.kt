package com.tokopedia.inactivephone_otp.testing.common.robot

import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ListView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.util.TreeIterables
import com.tokopedia.pin.PinUnify
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher

fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}

fun waitForData(millis: Long = 2000) {
    Thread.sleep(millis)
}

fun isDisplayed(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
}

fun isTextDisplayed(text: String) {
    onView(withText(containsString(text)))
        .check(matches(isDisplayed()))
}

fun clickOnButton(resId: Int) {
    onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(click())
}

fun clickOnButtonWithText(textButton: String) {
    onView(withText(containsString(textButton)))
        .check(matches(isDisplayed()))
        .perform(click())
}

fun scrollToView(resId: Int) {
    onView(withId(resId))
        .perform(scrollTo())
}

fun scrollToPosition(resId: Int, position: Int) {
    onView(withId(resId)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            position, scrollTo()
        )
    )
}

fun smoothScrollToPosition(resId: Int, position: Int) {
    onView(withId(resId)).perform(
        smoothScrollTo(position)
    )
}

fun scrollOnNestedView(resId: Int) {
    onView(withId(resId)).perform(NestedScrollViewExtension())
}

fun assertRecyclerViewItem(resId: Int, matcher: Matcher<in View>) {
    onView(withId(resId))
        .check(matches(matcher))
}

fun setText(resId: Int, text: String) {
    onView(withId(resId))
        .perform(typeText(text), closeSoftKeyboard())
}

fun smoothScrollTo(position: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isDisplayingAtLeast(90)
        }

        override fun getDescription(): String {
            return "smooth scroll order widget"
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as? RecyclerView)?.smoothScrollToPosition(position)
            uiController?.loopMainThreadForAtLeast(500)
        }

    }
}

class NestedScrollViewExtension(scrolltoAction: ViewAction = scrollTo()) :
    ViewAction by scrolltoAction {
    override fun getConstraints(): Matcher<View> {
        return CoreMatchers.allOf(
            withEffectiveVisibility(Visibility.VISIBLE),
            isDescendantOfA(
                CoreMatchers.anyOf(
                    isAssignableFrom(NestedScrollView::class.java),
                    isAssignableFrom(ScrollView::class.java),
                    isAssignableFrom(HorizontalScrollView::class.java),
                    isAssignableFrom(ListView::class.java),
                    isAssignableFrom(Typography::class.java)
                )
            )
        )
    }
}

object UnifyComponent {

    fun fillPinUnify(text: String): ViewAction {
        return actionWithAssertions(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return CoreMatchers.allOf(isDisplayed(), isAssignableFrom(PinUnify::class.java))
            }

            override fun getDescription(): String {
                return "Replace PinUnify value to $text"
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as? PinUnify?)?.let {
                    it.value = text
                }
            }
        })
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
            onView(isRoot()).perform(searchFor(viewMatcher))
            return onView(viewMatcher)
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
            return isRoot()
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

fun clickClickableSpan(textToClick: CharSequence): ViewAction {
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

            val spans =
                spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)
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
