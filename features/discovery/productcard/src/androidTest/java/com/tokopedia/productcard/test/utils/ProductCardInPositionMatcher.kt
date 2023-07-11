package com.tokopedia.productcard.test.utils

import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.LABEL_VARIANT_TAG
import org.hamcrest.Description
import org.hamcrest.Matcher

internal fun productCardInPosition(position: Int, itemMatcherList: Map<Int, Matcher<View?>>): Matcher<View?>? {
    return ProductCardInPositionMatcher(position, itemMatcherList)
}

private class ProductCardInPositionMatcher(
        private val position: Int,
        private val itemMatcherList: Map<Int, Matcher<View?>>
): BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {

    private var currentViewComponentName = ""
    private var currentMatcher : Matcher<View?>? = null

    override fun describeTo(description: Description?) {
        description?.appendText("Product Card Position: $position, $currentViewComponentName ")
        currentMatcher?.describeTo(description)
    }

    override fun matchesSafely(recyclerView: RecyclerView): Boolean {
        val productCard = recyclerView.getProductCardInPosition() ?: return false

        val checkedChildrenMatches = productCard.checkChildrenInMatchers()

        if (!checkedChildrenMatches) return false

        return productCard.getUncheckedChildren().checkNotDisplayed()
    }

    private fun RecyclerView.getProductCardInPosition(): ViewGroup? {
        return this.findViewHolderForAdapterPosition(position)?.itemView as ViewGroup
    }

    private fun ViewGroup.checkChildrenInMatchers(): Boolean {
        return itemMatcherList.all {
            val viewToMatch = this.findViewById<View>(it.key)
                    ?: throw AssertionError("View with id ${resources.getResourceEntryName(it.key)} not found")
            it.value.matchProductCardComponent(viewToMatch)
        }
    }

    private fun Matcher<View?>.matchProductCardComponent(view: View): Boolean {
        currentViewComponentName = getResourceEntryName(view)
        currentMatcher = this

        return this.matches(view)
    }

    private fun getResourceEntryName(view: View) =
            try {
                view.resources.getResourceEntryName(view.id)
            } catch (throwable: Throwable) {
                ""
            }

    private fun ViewGroup.getUncheckedChildren(): List<View> {
        return this.getChildren().filter { productCardComponent ->
            !itemMatcherList.any {
                productCardComponent.id == it.key
                    // Ignore, because Label Variant does not have Id
                    || productCardComponent.tag == LABEL_VARIANT_TAG
                    // These layouts will always be shown
                    || productCardComponent.id == R.id.containerCardViewProductCard
                    || productCardComponent.id == R.id.cardViewProductCard
                    || productCardComponent.id == R.id.constraintLayoutProductCard
                    || productCardComponent.id == R.id.productCardContentLayout
                    || productCardComponent.id == R.id.productCardFooterLayout
                    || productCardComponent.id == R.id.productCardFooterLayoutContainer
                    // Ignore spaces, barriers, and not visible view helpers
                    || (productCardComponent is Space)
            }
        }
    }

    private fun List<View>.checkNotDisplayed(): Boolean {
        return this.all {
            isNotDisplayed().matchProductCardComponent(it)
        }
    }
}
