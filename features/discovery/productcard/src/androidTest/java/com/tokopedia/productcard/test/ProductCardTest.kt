package com.tokopedia.productcard.test

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.productcard.test.grid.ProductCardGridActivityTest
import com.tokopedia.productcard.test.grid.productCardGridTestData
import com.tokopedia.productcard.test.list.ProductCardListActivityTest
import com.tokopedia.productcard.test.list.productCardListTestData
import com.tokopedia.productcard.test.utils.productCardInPosition
import org.hamcrest.Matcher
import org.junit.Test

internal class ProductCardTest {

    private lateinit var recyclerViewViewInteraction: ViewInteraction
    private lateinit var productCardModelMatcherData: List<ProductCardModelMatcher>

    @Test
    fun testProductCardGrid() {
        startTestActivity(ProductCardGridActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardGridTestRecyclerView))
        productCardModelMatcherData = productCardGridTestData

        startTest()
    }

    @Test
    fun testProductCardList() {
        startTestActivity(ProductCardListActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardListTestRecyclerView))
        productCardModelMatcherData = productCardListTestData

        startTest()
    }

    private fun startTestActivity(activityClassName: String) {
        val intent = Intent(Intent.ACTION_MAIN).also {
            it.setClassName(getInstrumentation().targetContext.packageName, activityClassName)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        getInstrumentation().startActivitySync(intent)
    }

    private fun startTest() {
        productCardModelMatcherData.forEachIndexed { index, productCardModelMatcher ->
            recyclerViewViewInteraction.checkProductCardAtPosition(index, productCardModelMatcher.productCardMatcher)
        }
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return perform(scrollToPosition<ProductCardGridActivityTest.ViewHolder>(position))
                .check(matches(productCardInPosition(position, elementMatchers)))
    }
}