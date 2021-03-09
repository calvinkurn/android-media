package com.tokopedia.topchat.assertion

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.`is`


fun hasTotalItemOf(expectedCount: Int): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView: RecyclerView = view as RecyclerView
        val adapter: RecyclerView.Adapter<*>? = recyclerView.adapter
        assertThat(adapter?.itemCount, `is`(expectedCount))
    }
}