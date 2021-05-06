package com.tokopedia.topchat.matchers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.test.platform.app.InstrumentationRegistry
import androidx.transition.Slide
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?>? {
    return object : TypeSafeMatcher<View?>() {
        var currentIndex = 0
        override fun describeTo(description: Description) {
            description.appendText("with index: ")
            description.appendValue(index)
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            return matcher.matches(view) && currentIndex++ == index
        }
    }
}

fun withLinearLayoutGravity(@Slide.GravityFlag gravity: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View): Boolean {
            return (view.layoutParams as? LinearLayout.LayoutParams)?.gravity == gravity
        }

        override fun describeTo(description: Description?) {
            description?.appendText("Expected gravity: $gravity")
        }
    }
}

fun isKeyboardShown(): Boolean {
    val inputMethodManager = InstrumentationRegistry.getInstrumentation()
            .targetContext
            .getSystemService(
                    Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
    return inputMethodManager.isAcceptingText
}