package com.tokopedia.test.application.matcher

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (resources != null) {
                    idDescription = try {
                        resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        String.format("%s (resource name not found)",
                                Integer.valueOf(recyclerViewId))
                    }
                }
                description.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View): Boolean {
                resources = view.getResources()
                if (childView == null) {
                    val recyclerView: RecyclerView = view.rootView.findViewById(recyclerViewId) as RecyclerView
                    childView = if (recyclerView.id == recyclerViewId) {
                        recyclerView.findViewHolderForAdapterPosition(position)?.itemView ?: return false
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView: View = childView!!.findViewById(targetViewId)
                    view === targetView
                }
            }
        }
    }
}

/**
 * match total item in recyclerview.
 *
 * @param expectedCount total expected item in recyclerview.
 *
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

/**
 * check if the specified viewholder class [expectedClass] is exist in the recyclerview
 *
 * @param expectedClass expected viewholder class in recyclerview
 *
 * Example usage:
 * onView(withId(R.id.recycler_view)).check(
 *      matches(
 *          hasViewHolderOf(NotificationTopAdsBannerViewHolder::class.java)
 *      )
 * )
 */
fun hasViewHolderOf(expectedClass: Class<*>): BoundedMatcher<View, RecyclerView> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("recyclerview should has viewholder of $expectedClass")
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val itemCount = item!!.adapter!!.itemCount
            for (i in 0 until itemCount) {
                val adapter = item.findViewHolderForAdapterPosition(i)
                if (expectedClass.isInstance(adapter)) {
                    return true
                }
            }
            return false
        }
    }
}

/**
 * Check if the given [position] in recyclerview is instance of [expectedClass]
 *
 * @param position the viewholder position in recyclerview
 * @param expectedClass the expected instance of the viewholder
 *
 * Example usage:
 * onView(withId(R.id.recycler_view)).check(
 *      matches(
 *          hasViewHolderItemAtPosition(6, NotificationTopAdsBannerViewHolder::class.java)
 *      )
 * )
 */
fun hasViewHolderItemAtPosition(
    position: Int, expectedClass: Class<*>
): BoundedMatcher<View, RecyclerView> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("$expectedClass should be found at position $position")
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val vh = item!!.findViewHolderForAdapterPosition(position)
            return expectedClass.isInstance(vh)
        }
    }
}