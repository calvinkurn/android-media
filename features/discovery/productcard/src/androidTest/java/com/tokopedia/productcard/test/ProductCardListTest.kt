package com.tokopedia.productcard.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.productcard.test.list.ProductCardListActivityTest
import com.tokopedia.productcard.test.list.productCardListTestData
import org.junit.Rule
import org.junit.Test

// Uncomment to enable, also uncomment build.gradle ln 24, 58-60
//internal class ProductCardListTest {
//
//    @Rule
//    @JvmField
//    val activityTestRule = ActivityTestRule<ProductCardListActivityTest>(ProductCardListActivityTest::class.java)
//
//    @Test
//    fun testProductCardList() {
//        val recyclerViewViewInteraction = onView(withId(R.id.productCardListTestRecyclerView))
//
//        ProductCardTest(recyclerViewViewInteraction, productCardListTestData).startTest()
//    }
//}