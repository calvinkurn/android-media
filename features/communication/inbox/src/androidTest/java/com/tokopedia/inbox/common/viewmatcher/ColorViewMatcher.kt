package com.tokopedia.inbox.common.viewmatcher

import android.content.res.Resources
import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import android.graphics.drawable.ColorDrawable
import androidx.core.content.res.ResourcesCompat

/**
 * onView(withId(R.id.viewId))
 * .check(matches(matchesBackgroundColor(R.color.colorId)));
 */
fun matchesBackgroundColor(expectedResourceId: Int?): Matcher<View?> {

    return object : BoundedMatcher<View?, View>(View::class.java) {
        var actualColor = 0
        var expectedColor = 0
        var message: String? = null

        override fun matchesSafely(item: View): Boolean {
            if (item.background == null) {
                return if (expectedResourceId == null) {
                    true
                } else {
                    message = item.id.toString() + " does not have a background"
                    false
                }
            }
            val resources: Resources = item.context.resources
            expectedColor = ResourcesCompat.getColor(resources, expectedResourceId?: 0, null)
            actualColor = (item.background as ColorDrawable).color
            return actualColor == expectedColor
        }

        override fun describeTo(description: Description) {
            if (actualColor != 0) {
                message = ("Background color did not match: Expected "
                        + String.format("#%06X", (0xFFFFFF and expectedColor)
                ) + " was " + String.format("#%06X", (0xFFFFFF and actualColor)))
            }
            description.appendText(message)
        }
    }
}