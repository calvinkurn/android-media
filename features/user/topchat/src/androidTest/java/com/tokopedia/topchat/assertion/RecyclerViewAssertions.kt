package com.tokopedia.topchat.assertion

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import org.hamcrest.Matcher
import org.hamcrest.core.IsInstanceOf

fun atPositionIsInstanceOf(
        position: Int, expectedClass: Class<*>
): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView: RecyclerView = view as RecyclerView
        val adapter: BaseAdapter<*>? = recyclerView.adapter as BaseAdapter<*>
        val itemVisitable = adapter?.list?.get(position)
        assertThat(itemVisitable, IsInstanceOf(expectedClass))
    }
}

/**
 * How to use:
 * Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(withItemCount(greaterThan(5)))
 * Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(withItemCount(lessThan(5)))
 */
fun withItemCount(matcher: Matcher<Int>): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val adapter = (view as RecyclerView).adapter
        assertThat(adapter?.itemCount, matcher)
    }
}