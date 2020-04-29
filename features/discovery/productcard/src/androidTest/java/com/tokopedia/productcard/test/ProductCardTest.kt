package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import com.tokopedia.productcard.test.grid.ProductCardGridActivityTest
import com.tokopedia.productcard.test.utils.productCardInPosition
import org.hamcrest.Matcher

internal class ProductCardTest(
        private val recyclerViewViewInteraction: ViewInteraction,
        private val productCardModelMatcherData: List<ProductCardModelMatcher>
) {

    fun startTest() {
        productCardModelMatcherData.forEachIndexed { index, productCardModelMatcher ->
            recyclerViewViewInteraction.checkProductCardAtPosition(index, productCardModelMatcher.productCardMatcher)
        }
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return perform(scrollToPosition<ProductCardGridActivityTest.ViewHolder>(position))
                .check(matches(productCardInPosition(position, elementMatchers)))
    }
}