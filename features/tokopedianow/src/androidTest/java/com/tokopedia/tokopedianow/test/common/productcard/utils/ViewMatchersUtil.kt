@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.tokopedianow.test.common.productcard.utils

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.Visibility.GONE
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardView
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowQuantityEditorView
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowWishlistButtonView
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal object ViewMatchersUtil {

    fun isDisplayedWithText(
        text: String
    ) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) { /* nothing to do */ }

        override fun matchesSafely(item: View): Boolean = isDisplayed().matches(item) && withText(text).matches(item)
    }

    fun isNotDisplayed() = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) { /* nothing to do */ }

        override fun matchesSafely(item: View): Boolean = withEffectiveVisibility(GONE).matches(item)
    }

    fun isQuantityEditorDisplayed(
        minOrder: Int,
        maxOrder: Int,
        orderQuantity: Int
    ) = object : TypeSafeMatcher<TokoNowQuantityEditorView>() {
        override fun describeTo(description: Description) { /* nothing to do */ }

        override fun matchesSafely(item: TokoNowQuantityEditorView): Boolean = isDisplayed().matches(item) &&
            `is`(minOrder).matches(item.minQuantity) &&
            `is`(maxOrder).matches(item.maxQuantity) &&
            `is`(orderQuantity).matches(item.getQuantity())
    } as Matcher<View?>

    fun isProductNameTypographyDisplayed(
        productName: String,
        needToChangeMaxLinesName: Boolean,
        promoLabelAvailable: Boolean
    ) = object : TypeSafeMatcher<Typography>() {
        override fun describeTo(description: Description) { /* nothing to do */ }

        override fun matchesSafely(item: Typography): Boolean {
            val maxLines = if (needToChangeMaxLinesName && promoLabelAvailable) TokoNowProductCardView.MAX_LINES_NEEDED_TO_CHANGE else TokoNowProductCardView.DEFAULT_MAX_LINES
            return isDisplayed().matches(item) && withText(productName).matches(item) && `is`(maxLines).matches(item.maxLines)
        }
    } as Matcher<View?>

    fun isProgressBarDisplayed(
        progressBarPercentage: Int
    ) = object : TypeSafeMatcher<ProgressBarUnify>() {
        override fun describeTo(description: Description) { /* nothing to do */ }

        override fun matchesSafely(item: ProgressBarUnify): Boolean = isDisplayed().matches(item) && `is`(progressBarPercentage).matches(item.getValue())
    } as Matcher<View?>

    fun isWishlistButtonDisplayed(
        hasBeenSelected: Boolean
    ): Matcher<View?> = object : TypeSafeMatcher<TokoNowWishlistButtonView>() {
        override fun describeTo(description: Description) { /* nothing to do */ }

        override fun matchesSafely(item: TokoNowWishlistButtonView): Boolean = isDisplayed().matches(item) && `is`(hasBeenSelected).matches(item.getValue())
    } as Matcher<View?>

    fun withComponentsInProductCardMatched(
        position: Int,
        matchers: Map<Int, Matcher<View?>>
    ): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {

            private var currentViewName = ""

            override fun describeTo(description: Description?) {
                description?.appendText("The Item at position $position")
                if (currentViewName.isNotEmpty()) {
                    description?.appendText(" must have matching matcher on $currentViewName")
                }
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val productCard = recyclerView.findViewHolderForAdapterPosition(position)?.itemView as? ViewGroup
                var resultMatcher = false
                productCard?.apply {
                    matchers.forEach { itemMatcher ->
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
    }

}
