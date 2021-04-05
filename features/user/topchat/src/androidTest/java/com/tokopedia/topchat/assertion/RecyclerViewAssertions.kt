package com.tokopedia.topchat.assertion

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsInstanceOf


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