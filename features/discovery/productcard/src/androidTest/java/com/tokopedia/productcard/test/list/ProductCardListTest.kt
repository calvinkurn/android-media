package com.tokopedia.productcard.test.list

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.productcard.test.ProductCardTest
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test


internal class ProductCardListTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule<ProductCardListActivityTest>(ProductCardListActivityTest::class.java)

    @Test
    fun testProductCardList() {
        val recyclerViewViewInteraction = onView(withId(R.id.productCardListTestRecyclerView))
        val additionalProductCardMatchers = mutableListOf<Map<Int, Matcher<View?>>>().also {
            it.add(getProductCardMatchersPosition18())
        }

        ProductCardTest(recyclerViewViewInteraction, additionalProductCardMatchers)
                .startTest()
    }

    private fun getProductCardMatchersPosition18(): Map<Int, Matcher<View?>> {
        val position = 18
        val productCardModel = productCardListTestData[position]

        val labelProductStatus = productCardModel.getLabelProductStatus() ?: throw Exception("Product Card Position $position has no label status")
        val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
        val labelGimmick = productCardModel.getLabelGimmick() ?: throw Exception("Product Card Position $position has no label gimmick")

        return mutableMapOf<Int, Matcher<View?>>().also {
            it[R.id.imageProduct] = isDisplayed()
            it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
            it[R.id.textTopAds] = isDisplayed()
            it[R.id.textViewGimmick] = isDisplayedWithText(labelGimmick.title)
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
            it[R.id.buttonAddToCart] = isDisplayed()
            it[R.id.buttonRemoveFromWishlist] = isDisplayed()
            it[R.id.imageRemoveFromWishlist] = isDisplayed()
        }
    }
}