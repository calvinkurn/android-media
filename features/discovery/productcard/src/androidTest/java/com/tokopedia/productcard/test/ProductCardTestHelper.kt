package com.tokopedia.productcard.test

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

internal fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
    return check(matches(matchProductCardAtPosition(position, elementMatchers)))
}

internal fun matchProductCardAtPosition(position: Int, itemMatcherList: Map<Int, Matcher<View?>>): Matcher<View?>? {
    return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
        var itemDescription = ""
        var currentMatcher : Matcher<View?>? = null

        override fun describeTo(description: Description?) {
            description?.appendText("Product Card Position: $position, $itemDescription ")
            currentMatcher?.describeTo(description)
        }

        override fun matchesSafely(recyclerView: RecyclerView): Boolean {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
            return viewHolder != null && itemMatcherList.all {
                val view: View = viewHolder.itemView.findViewById(it.key)
                itemDescription = view.contentDescription?.toString() ?: ""
                currentMatcher = it.value

                it.value.matches(view)
            }
        }
    }
}