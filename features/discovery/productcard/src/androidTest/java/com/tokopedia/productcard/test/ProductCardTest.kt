package com.tokopedia.productcard.test

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.productcard.test.ProductCardActivityTest.ViewHolder
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain


internal class ProductCardTest {

    private val screenshotWatcher = ScreenshotWatcher()
    private val grantPermissionRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
    private val activityTestRule = ActivityTestRule<ProductCardActivityTest>(ProductCardActivityTest::class.java)

    @Rule
    @JvmField
    val ruleChain: RuleChain =
            RuleChain.outerRule(screenshotWatcher)
            .around(grantPermissionRule)
            .around(activityTestRule)

    @Test
    fun testProductCardSmallGrid() {
        onView(withId(R.id.productCardSmallGridTestRecyclerView))
                .checkProductCardAtPosition(0, getProductCardMatchersPosition0())
                .checkProductCardAtPosition(1, getProductCardMatchersPosition1())
                .checkProductCardAtPosition(2, getProductCardMatchersPosition2())
                .checkProductCardAtPosition(3, getProductCardMatchersPosition3())
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return perform(scrollToPosition<ViewHolder>(position))
                .check(matches(productCardInPosition(position, elementMatchers)))
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

    private fun getProductCardMatchersPosition2(): Map<Int, Matcher<View?>> {
        val productCardModel = productCardModelSmallGridList[2]

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
        val productCardModel = productCardModelSmallGridList[position]

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
}