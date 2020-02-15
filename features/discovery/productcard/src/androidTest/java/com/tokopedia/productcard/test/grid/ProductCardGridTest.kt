package com.tokopedia.productcard.test.grid

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.checkProductCardGeneralCases
import org.junit.Rule
import org.junit.Test


internal class ProductCardGridTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<ProductCardGridActivityTest>(ProductCardGridActivityTest::class.java)

    @Test
    fun testProductCardGrid() {
        onView(withId(R.id.productCardGridTestRecyclerView)).checkProductCardGeneralCases()
    }
}