package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.test.grid.ProductCardGridActivityTest
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.productCardInPosition
import com.tokopedia.productcard.test.utils.withDrawable
import org.hamcrest.Matcher

internal fun ViewInteraction.checkProductCardGeneralCases(): ViewInteraction {
    return this
            .checkProductCardAtPosition(0, getProductCardMatchersPosition0())
            .checkProductCardAtPosition(1, getProductCardMatchersPosition1())
            .checkProductCardAtPosition(2, getProductCardMatchersPosition2())
            .checkProductCardAtPosition(3, getProductCardMatchersPosition3())
            .checkProductCardAtPosition(4, getProductCardMatchersPosition4())
            .checkProductCardAtPosition(5, getProductCardMatchersPosition5())
            .checkProductCardAtPosition(6, getProductCardMatchersPosition6())
            .checkProductCardAtPosition(7, getProductCardMatchersPosition7())
            .checkProductCardAtPosition(8, getProductCardMatchersPosition8())
            .checkProductCardAtPosition(9, getProductCardMatchersPosition9())
            .checkProductCardAtPosition(10, getProductCardMatchersPosition10())
            .checkProductCardAtPosition(11, getProductCardMatchersPosition11())
            .checkProductCardAtPosition(12, getProductCardMatchersPosition12())
            .checkProductCardAtPosition(13, getProductCardMatchersPosition13())
            .checkProductCardAtPosition(14, getProductCardMatchersPosition14())
            .checkProductCardAtPosition(15, getProductCardMatchersPosition15())
}

private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
    return perform(RecyclerViewActions.scrollToPosition<ProductCardGridActivityTest.ViewHolder>(position))
            .check(ViewAssertions.matches(productCardInPosition(position, elementMatchers)))
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
        it[R.id.labelDiscount] = isDisplayedWithText(productCardModel.discountPercentage)
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
    }
}

private fun getProductCardMatchersPosition4(): Map<Int, Matcher<View?>> {
    val position = 4
    val productCardModel = productCardModelTestData[position]

    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelGimmick = productCardModel.getLabelGimmick() ?: throw Exception("Product Card Position $position has no label gimmick")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewGimmick] = isDisplayedWithText(labelGimmick.title)
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
    val labelGimmick = productCardModel.getLabelGimmick() ?: throw Exception("Product Card Position $position has no label gimmick")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewGimmick] = isDisplayedWithText(labelGimmick.title)
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
    val labelCredibility = productCardModel.getLabelCredibility2() ?: throw Exception("Product Card Position $position has no label credibility")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.textViewCredibility] = isDisplayedWithText(labelCredibility.title)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }
}

private fun getProductCardMatchersPosition8(): Map<Int, Matcher<View?>> {
    val position = 8
    val productCardModel = productCardModelTestData[position]

    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelShipping = productCardModel.getLabelShipping() ?: throw Exception("Product Card Position $position has no text shipping")

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
        it[R.id.textViewShipping] = isDisplayedWithText(labelShipping.title)
        it[R.id.imageThreeDots] = isDisplayed()
    }
}

private fun getProductCardMatchersPosition9(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardModelTestData[9]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
    }
}

private fun getProductCardMatchersPosition10(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardModelTestData[10]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
    }
}

private fun getProductCardMatchersPosition11(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardModelTestData[11]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition12(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardModelTestData[12]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition13(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardModelTestData[13]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition14(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardModelTestData[14]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition15(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardModelTestData[15]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_active)
    }
}