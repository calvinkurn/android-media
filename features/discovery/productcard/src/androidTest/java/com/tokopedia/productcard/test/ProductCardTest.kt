package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test


internal class ProductCardTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<ProductCardActivityTest>(ProductCardActivityTest::class.java)

    @Test
    fun testProductCardSmallGrid() {
        onView(withId(R.id.productCardSmallGridTestRecyclerView))
                .checkProductCardAtPosition(0, getProductCardMatchersPosition0())
                .checkProductCardAtPosition(1, getProductCardMatchersPosition1())
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return check(ViewAssertions.matches(productCardInPosition(position, elementMatchers)))
    }

    private fun getProductCardMatchersPosition0(): Map<Int, Matcher<View?>> {
        val productCardModel = productCardModelSmallGridList[0]

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
            it[R.id.imageRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
            it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
            it[R.id.imageFreeOngkirPromo] = isDisplayed()
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }

    private fun getProductCardMatchersPosition1(): Map<Int, Matcher<View?>> {
        val productCardModel = productCardModelSmallGridList[1]

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.labelDiscount] = isDisplayedWithText("${productCardModel.discountPercentage}%")
            it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
            it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
            it[R.id.imageRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
            it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
            it[R.id.imageFreeOngkirPromo] = isDisplayed()
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }
}