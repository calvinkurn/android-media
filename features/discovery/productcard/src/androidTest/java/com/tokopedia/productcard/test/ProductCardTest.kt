package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import com.tokopedia.productcard.test.grid.ProductCardGridActivityTest
import com.tokopedia.productcard.test.utils.productCardInPosition
import org.hamcrest.Matcher

internal class ProductCardTest(
        private val recyclerViewViewInteraction: ViewInteraction,
        additionalProductCardTestMatchers: List<Map<Int, Matcher<View?>>> = listOf()
) {

    private val productCardMatchers = productCardGeneralTestMatchers + additionalProductCardTestMatchers

    fun startTest() {
        productCardMatchers.forEachIndexed { index, matcher ->
            recyclerViewViewInteraction.checkProductCardAtPosition(index, matcher)
        }
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return perform(RecyclerViewActions.scrollToPosition<ProductCardGridActivityTest.ViewHolder>(position))
                .check(ViewAssertions.matches(productCardInPosition(position, elementMatchers)))
    }
}