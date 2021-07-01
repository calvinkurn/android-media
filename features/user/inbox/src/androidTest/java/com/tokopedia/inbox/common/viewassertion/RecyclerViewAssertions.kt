package com.tokopedia.inbox.common.viewassertion

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat


fun hasHolderItemAtPosition(
    index: Int,
    viewHolderMatcher: Matcher<in RecyclerView.ViewHolder?>
): ViewAssertion {
    return ViewAssertion { view, e ->
        if (view !is RecyclerView) {
            throw e!!
        }
        assertThat(
            view.findViewHolderForAdapterPosition(index), viewHolderMatcher
        )
    }
}