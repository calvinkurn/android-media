package com.tokopedia.productcard.test.utils

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.LABEL_VARIANT_TAG
import org.hamcrest.Description
import org.hamcrest.Matcher
import com.tokopedia.productcard.test.R as productcardtestR

internal fun productCardInPosition(position: Int, itemMatcherList: Map<Int, Matcher<View?>>): Matcher<View?>? {
    return ProductCardInPositionMatcher(position, itemMatcherList)
}

private class ProductCardInPositionMatcher(
    private val position: Int,
    private val itemMatcherList: Map<Int, Matcher<View?>>
) : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {

    private var currentViewComponentName = ""
    private var currentMatcher: Matcher<View?>? = null

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
        return this.findViewHolderForAdapterPosition(position)?.itemView as? ViewGroup
    }

    private fun ViewGroup.checkChildrenInMatchers(): Boolean {
        if (itemMatcherList.containsKey(R.id.productCardGenericCtaMain)) {
            Log.d("ProductCardInPositionMatcher", "productCardGenericCtaMain ${itemMatcherList[R.id.productCardGenericCtaMain]}")
        }
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

    private val alwaysShownComponentId = listOf(
        R.id.containerCardViewProductCard,
        R.id.cardViewProductCard,
        R.id.constraintLayoutProductCard,
        R.id.productCardContentLayout,
        R.id.productCardFooterLayout,
        R.id.productCardFooterLayoutContainer,
        R.id.productCardCardUnifyContainer,
        R.id.productCardConstraintLayout,
        R.id.productCardPriceContainer,
        R.id.productCardOverlayContainer,
        productcardtestR.id.productCardReimagineTestDescription,
        productcardtestR.id.productCardReimagineGridCarouselView,
        productcardtestR.id.productCardReimagineGridView,
        productcardtestR.id.productCardReimagineListView,
        productcardtestR.id.productCardTestDivider,
        productcardtestR.id.productCardReimagineListCarouselView,
        productcardtestR.id.productCardContainerHeightCalculator
    )

    private fun ViewGroup.getUncheckedChildren(): List<View> {
        return this.getChildren().filter { productCardComponent ->
            !itemMatcherList.any {
                productCardComponent.id == it.key ||
                    // Ignore, because Label Variant does not have Id
                    productCardComponent.tag == LABEL_VARIANT_TAG ||
                    // These layouts will always be shown
                    productCardComponent.id in alwaysShownComponentId ||
                    // Ignore spaces, barriers, and not visible view helpers
                    productCardComponent is Space ||
                    productCardComponent is Group
            }
        }
    }

    private fun List<View>.checkNotDisplayed(): Boolean {
        return this.all {
            isNotDisplayed().matchProductCardComponent(it)
        }
    }
}
