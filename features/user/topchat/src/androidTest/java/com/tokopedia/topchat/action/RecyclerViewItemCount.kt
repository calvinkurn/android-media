package com.tokopedia.topchat.action

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matcher
import org.hamcrest.Matchers


class RecyclerViewItemCountAssertion private constructor(private val matcher: Matcher<Int>) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView?
        val adapter = recyclerView?.adapter
        assertThat(adapter?.itemCount, matcher)
    }

    companion object {
        /**
         * How to use:
         * Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(RecyclerViewItemCountAssertion.withItemCount(greaterThan(5)))
         * Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(RecyclerViewItemCountAssertion.withItemCount(lessThan(5)))
         */
        fun withItemCount(matcher: Matcher<Int>): RecyclerViewItemCountAssertion {
            return RecyclerViewItemCountAssertion(matcher)
        }

        fun withExactItemCount(expected: Int): RecyclerViewItemCountAssertion {
            return withItemCount(Matchers.`is`(expected))
        }
    }
}