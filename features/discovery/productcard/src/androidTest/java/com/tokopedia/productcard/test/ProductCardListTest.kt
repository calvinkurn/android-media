package com.tokopedia.productcard.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test


internal class ProductCardListTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<ProductCardListActivityTest>(ProductCardListActivityTest::class.java)

    @Test
    fun testProductCardList() {
        onView(withId(R.id.productCardListTestRecyclerView)).checkProductCardPerPosition()
    }
}