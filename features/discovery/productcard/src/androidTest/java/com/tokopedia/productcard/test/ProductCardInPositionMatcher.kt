package com.tokopedia.productcard.test

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

internal class ProductCardInPositionMatcher(
        private val position: Int,
        private val itemMatcherList: Map<Int, Matcher<View?>>
): BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
    var itemDescription = ""
    var currentMatcher : Matcher<View?>? = null

    override fun describeTo(description: Description?) {
        description?.appendText("Product Card Position: $position, $itemDescription ")
        currentMatcher?.describeTo(description)
    }

    override fun matchesSafely(recyclerView: RecyclerView): Boolean {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        val itemView = viewHolder?.itemView ?: return false
        val uncheckedChildren = (itemView as ViewGroup).getChildren()

        val checkedChildrenMatches = itemMatcherList.all {
            uncheckedChildren.removeAll { child -> child.id == it.key }

            val view: View = itemView.findViewById(it.key)
            itemDescription = view.resources.getResourceEntryName(it.key)
            currentMatcher = it.value

            it.value.matches(view)
        }

        if (!checkedChildrenMatches) return false

        return uncheckedChildren.all {
            itemDescription = it.resources.getResourceEntryName(it.id)

            val matcher = isNotDisplayed()
            currentMatcher = matcher
            matcher.matches(it)
        }
    }
}