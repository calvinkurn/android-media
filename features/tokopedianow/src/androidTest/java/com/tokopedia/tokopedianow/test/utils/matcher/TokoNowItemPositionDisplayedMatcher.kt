package com.tokopedia.tokopedianow.test.utils.matcher

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.kotlin.extensions.view.EMPTY
import org.hamcrest.Description
import org.hamcrest.Matcher

internal class TokoNowItemPositionDisplayedMatcher(
    private val position: Int,
    private val itemMatcherList: Map<Int, Matcher<View>>
): BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {

    private var currentViewName = ""

    override fun describeTo(description: Description?) {
        description?.appendText("The Product Card at position $position")
        if (currentViewName.isNotEmpty()) {
            description?.appendText(" must have matching matcher on $currentViewName")
        }
    }

    override fun matchesSafely(recyclerView: RecyclerView): Boolean {
        val productCard = recyclerView.findViewHolderForAdapterPosition(position)?.itemView as? ViewGroup
        var resultMatcher = false
        productCard?.apply {
            itemMatcherList.forEach { itemMatcher ->
                val (resourceId, matcher) = itemMatcher
                val currentView = findViewById<View>(resourceId)
                currentViewName = getResourceEntryName(currentView)
                resultMatcher = matcher.matches(currentView)
                if (!resultMatcher) {
                    return@apply
                }
            }
        }
        return resultMatcher
    }

    private fun getResourceEntryName(view: View): String {
        return try {
            view.resources.getResourceEntryName(view.id)
        } catch (throwable: Throwable) {
            String.EMPTY
        }
    }
}
