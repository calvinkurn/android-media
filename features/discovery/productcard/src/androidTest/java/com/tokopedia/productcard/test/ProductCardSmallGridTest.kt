package com.tokopedia.productcard.test

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.productcard.v2.ProductCardView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test


internal class ProductCardSmallGridTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<ProductCardSmallGridActivityTest>(ProductCardSmallGridActivityTest::class.java)

    @Test
    fun testRunActivity() {
        onView(withId(R.id.productCardSmallGridTestRecyclerView))
                .checkProductCardAtPosition(0, R.id.textViewProductName, isDisplayed())
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementId: Int, matcher: Matcher<View?>): ViewInteraction {
        return check(matches(withViewAtPosition(position, productCardMatchers(elementId, matcher))))
    }

    private fun productCardMatchers(productCardElementId: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object: BoundedMatcher<View?, ProductCardView>(ProductCardView::class.java) {
            override fun describeTo(description: Description?) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(item: ProductCardView?): Boolean {
                return itemMatcher.matches(item?.findViewById(productCardElementId))
            }
        }
    }

    private fun withViewAtPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?>? {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }
}