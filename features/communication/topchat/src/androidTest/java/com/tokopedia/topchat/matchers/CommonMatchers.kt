package com.tokopedia.topchat.matchers

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.transition.Slide
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?>? {
    return object : TypeSafeMatcher<View?>() {
        var currentIndex = -1
        var foundIndex = arrayListOf<Int>()
        override fun describeTo(description: Description) {
            description.appendText("with index: $index\n")
            description.appendText("found index: ")
            description.appendValue(foundIndex)
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: View?): Boolean {
            return if (matcher.matches(view)) {
                currentIndex++
                Log.d("VIEW-TAB", "$view")
                foundIndex.add(currentIndex)
                return currentIndex == index
            } else {
                false
            }
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
