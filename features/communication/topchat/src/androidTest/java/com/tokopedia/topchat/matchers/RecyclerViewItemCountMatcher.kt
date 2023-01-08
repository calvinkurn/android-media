package com.tokopedia.topchat.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Example usage:
 * onView(withId(R.id.recyclerview)).check(matches(withTotalItem(1)))
 */
fun withTotalItem(expectedCount: Int): BoundedMatcher<View, RecyclerView> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("$expectedCount child-count")
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val adapter: RecyclerView.Adapter<*>? = item?.adapter
            return adapter?.itemCount == expectedCount
        }
    }
}

fun atPosition(position: Int, targetViewId: Int = -1, itemMatcher: Matcher<View>): Matcher<View?> {
    return object : BoundedMatcher<View?, RecyclerView>(
        RecyclerView::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?: return false
            return if (targetViewId == -1) {
                itemMatcher.matches(viewHolder.itemView)
            } else {
                val targetView: View = viewHolder.itemView.findViewById(targetViewId)
                itemMatcher.matches(targetView)
            }
        }
    }
}