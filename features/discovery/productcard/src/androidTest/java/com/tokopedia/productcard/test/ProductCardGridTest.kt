package com.tokopedia.productcard.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.productcard.test.grid.ProductCardGridActivityTest
import com.tokopedia.productcard.test.grid.productCardGridTestData
import org.junit.Rule
import org.junit.Test

// Uncomment to enable, also uncomment build.gradle ln 24, 58-60
//internal class ProductCardGridTest {
//
//    @Rule
//    @JvmField
//    val activityTestRule = ActivityTestRule<ProductCardGridActivityTest>(ProductCardGridActivityTest::class.java)
//
//    @Test
//    fun testProductCardGrid() {
//        val recyclerViewViewInteraction = onView(withId(R.id.productCardGridTestRecyclerView))
//
//        ProductCardTest(recyclerViewViewInteraction, productCardGridTestData).startTest()
//    }
//}