package com.tokopedia.productcard

import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class ProductCardTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<ProductCardSmallGridActivityTest>(ProductCardSmallGridActivityTest::class.java)

    @Test
    fun `testRunActivity`() {

    }
}