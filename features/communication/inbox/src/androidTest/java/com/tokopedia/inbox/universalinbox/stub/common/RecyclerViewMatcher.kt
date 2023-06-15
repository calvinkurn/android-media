package com.tokopedia.inbox.universalinbox.stub.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.inbox.universalinbox.view.adapter.UniversalInboxAdapter
import org.hamcrest.Description
import org.hamcrest.core.IsInstanceOf
import org.hamcrest.core.IsNot

fun atPositionCheckInstanceOf(
    position: Int,
    expectedClass: Class<*>,
    reverse: Boolean = false
): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) throw noViewFoundException
        val item = getItem(view, position)
        val matcher = if (reverse) {
            IsNot(IsInstanceOf(expectedClass))
        } else {
            IsInstanceOf(expectedClass)
        }
        ViewMatchers.assertThat(item, matcher)
    }
}

private fun getItem(view: View, position: Int): Any {
    val recyclerView: RecyclerView = view as RecyclerView
    val adapter: UniversalInboxAdapter = recyclerView.adapter as UniversalInboxAdapter
    return adapter.getItem(position)
}

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
