package com.tokopedia.notifcenter.stub.common

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.*

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
            expectedColor = ResourcesCompat.getColor(resources, expectedResourceId ?: 0, null)
            actualColor = (item.background as ColorDrawable).color
            return actualColor == expectedColor
        }

        override fun describeTo(description: Description) {
            if (actualColor != 0) {
                message = (
                    "Background color did not match: Expected " +
                        String.format(
                            Locale.getDefault(),
                            "#%06X",
                            (0xFFFFFF and expectedColor)
                        ) + " was " + String.format(
                        Locale.getDefault(),
                        "#%06X",
                        (0xFFFFFF and actualColor)
                    )
                    )
            }
            description.appendText(message)
        }
    }
}

/**
 * Example usage:
 *         onView(
 *              withRecyclerView(R.id.recycler_view)
 *              .atPositionOnView(pos, R.id.btn_secondary)
 *         ).check(matches(ViewMatchers.withText("test")))
 */
fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
    return RecyclerViewMatcher(recyclerViewId)
}
