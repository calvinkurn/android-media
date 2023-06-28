package com.tokopedia.notifcenter.stub.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import org.hamcrest.Description

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
