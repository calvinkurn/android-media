package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.Espresso.onView
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

    private fun getProductCardMatchersPosition0(): Map<Int, Matcher<View?>> {
        val productCardModel = productCardModelSmallGridList[0]

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewProductName] = isDisplayed()
            it[R.id.textViewProductName] = withText(productCardModel.productName)
            it[R.id.textViewPrice] = isDisplayed()
            it[R.id.textViewPrice] = withText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayed()
            it[R.id.textViewShopLocation] = withText(productCardModel.shopLocation)
            it[R.id.imageRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = withText(productCardModel.ratingString)
            it[R.id.textViewReviewCount] = isDisplayed()
            it[R.id.textViewReviewCount] = withText("(${productCardModel.reviewCount})")
            it[R.id.imageFreeOngkirPromo] = isDisplayed()
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }

    private fun getProductCardMatchersPosition1(): Map<Int, Matcher<View?>> {
        val productCardModel = productCardModelSmallGridList[1]

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewProductName] = isDisplayed()
            it[R.id.textViewProductName] = withText(productCardModel.productName)
            it[R.id.labelDiscount] = isDisplayed()
            it[R.id.labelDiscount] = withText("${productCardModel.discountPercentage}%")
            it[R.id.textViewPrice] = isDisplayed()
            it[R.id.textViewPrice] = withText(productCardModel.formattedPrice)
            it[R.id.textViewSlashedPrice] = isDisplayed()
            it[R.id.textViewSlashedPrice] = withText(productCardModel.slashedPrice)
            it[R.id.textViewPrice] = isDisplayed()
            it[R.id.textViewPrice] = withText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayed()
            it[R.id.textViewShopLocation] = withText(productCardModel.shopLocation)
            it[R.id.imageRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = withText(productCardModel.ratingString)
            it[R.id.textViewReviewCount] = isDisplayed()
            it[R.id.textViewReviewCount] = withText("(${productCardModel.reviewCount})")
            it[R.id.imageFreeOngkirPromo] = isDisplayed()
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }
}