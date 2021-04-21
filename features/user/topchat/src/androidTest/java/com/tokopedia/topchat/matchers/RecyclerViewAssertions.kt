package com.tokopedia.topchat.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`


fun hasTotalItemOf(expectedCount: Int): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        checkNoView(noViewFoundException)
        assertThat(getRecyclerViewAdapter(view)?.itemCount, `is`(expectedCount))
    }
}

private fun checkNoView(noViewFoundException: NoMatchingViewException?) {
    if (noViewFoundException != null) {
        throw noViewFoundException
    }
}

private fun getRecyclerViewAdapter(view: View): RecyclerView.Adapter<*>? {
    val recyclerView: RecyclerView = view as RecyclerView
    return recyclerView.adapter
}

/**
 * How to use:
 * Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(withItemCount(greaterThan(5)))
 * Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(withItemCount(lessThan(5)))
 */
fun withItemCount(matcher: Matcher<Int>): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        checkNoView(noViewFoundException)
        assertThat(getRecyclerViewAdapter(view)?.itemCount, matcher)
    }
}