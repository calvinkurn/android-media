package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.productcard.test.ProductCardGridActivityTest.ViewHolder
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.productCardInPosition
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test


internal class ProductCardGridTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<ProductCardGridActivityTest>(ProductCardGridActivityTest::class.java)

    @Test
    fun testProductCardGrid() {
        onView(withId(R.id.productCardSmallGridTestRecyclerView))
                .checkProductCardAtPosition(0, getProductCardMatchersPosition0())
                .checkProductCardAtPosition(1, getProductCardMatchersPosition1())
                .checkProductCardAtPosition(2, getProductCardMatchersPosition2())
                .checkProductCardAtPosition(3, getProductCardMatchersPosition3())
                .checkProductCardAtPosition(4, getProductCardMatchersPosition4())
                .checkProductCardAtPosition(5, getProductCardMatchersPosition5())
                .checkProductCardAtPosition(6, getProductCardMatchersPosition6())
                .checkProductCardAtPosition(7, getProductCardMatchersPosition7())
                .checkProductCardAtPosition(8, getProductCardMatchersPosition8())
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return perform(scrollToPosition<ViewHolder>(position))
                .check(matches(productCardInPosition(position, elementMatchers)))
    }

    private fun getProductCardMatchersPosition0(): Map<Int, Matcher<View?>> {
        val productCardModel = productCardModelTestData[0]

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
        val productCardModel = productCardModelTestData[1]

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

    private fun getProductCardMatchersPosition2(): Map<Int, Matcher<View?>> {
        val productCardModel = productCardModelTestData[2]

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

    private fun getProductCardMatchersPosition3(): Map<Int, Matcher<View?>> {
        val position = 3
        val productCardModel = productCardModelTestData[position]

        val labelProductStatus = productCardModel.getLabelProductStatus() ?: throw Exception("Product Card Position $position has no label status")
        val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")

        val textGimmick = productCardModel.getTextGimmick() ?: throw Exception("Product Card Position $position has no text gimmick")

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
            it[R.id.textTopAds] = isDisplayed()
            it[R.id.textViewGimmick] = isDisplayedWithText(textGimmick.title)
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
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

    private fun getProductCardMatchersPosition4(): Map<Int, Matcher<View?>> {
        val position = 4
        val productCardModel = productCardModelTestData[position]

        val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")

        val textGimmick = productCardModel.getTextGimmick() ?: throw Exception("Product Card Position $position has no text gimmick")

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewGimmick] = isDisplayedWithText(textGimmick.title)
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
            it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
            it[R.id.imageRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
            it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }

    private fun getProductCardMatchersPosition5(): Map<Int, Matcher<View?>> {
        val position = 5
        val productCardModel = productCardModelTestData[position]

        val labelProductStatus = productCardModel.getLabelProductStatus() ?: throw Exception("Product Card Position $position has no label status")
        val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
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

    private fun getProductCardMatchersPosition6(): Map<Int, Matcher<View?>> {
        val position = 6
        val productCardModel = productCardModelTestData[position]

        val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")

        val textGimmick = productCardModel.getTextGimmick() ?: throw Exception("Product Card Position $position has no text gimmick")

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewGimmick] = isDisplayedWithText(textGimmick.title)
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
            it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
            it[R.id.imageFreeOngkirPromo] = isDisplayed()
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }

    private fun getProductCardMatchersPosition7(): Map<Int, Matcher<View?>> {
        val position = 7
        val productCardModel = productCardModelTestData[position]

        val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")

        val textCredibility = productCardModel.getTextCredibility() ?: throw Exception("Product Card Position $position has no text credibility")

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
            it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
            it[R.id.textViewCredibility] = isDisplayedWithText(textCredibility.title)
            it[R.id.imageFreeOngkirPromo] = isDisplayed()
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }

    private fun getProductCardMatchersPosition8(): Map<Int, Matcher<View?>> {
        val position = 8
        val productCardModel = productCardModelTestData[position]

        val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")

        val textShipping = productCardModel.getTextShipping() ?: throw Exception("Product Card Position $position has no text shipping")

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
            it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
            it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
            it[R.id.imageShopBadge] = isDisplayed()
            it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
            it[R.id.imageRatingString] = isDisplayed()
            it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
            it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
            it[R.id.textViewShipping] = isDisplayedWithText(textShipping.title)
            it[R.id.imageThreeDots] = isDisplayed()
        }
    }
}