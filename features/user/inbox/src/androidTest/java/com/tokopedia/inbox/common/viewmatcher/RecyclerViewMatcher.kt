package com.tokopedia.inbox.common.viewmatcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

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