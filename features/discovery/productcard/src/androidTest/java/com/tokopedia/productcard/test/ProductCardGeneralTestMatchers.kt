package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.withDrawable
import org.hamcrest.Matcher

internal val productCardGeneralTestMatchers: List<Map<Int, Matcher<View?>>> = mutableListOf<Map<Int, Matcher<View?>>>().also {
    it.add(getProductCardMatchersPosition0())
    it.add(getProductCardMatchersPosition1())
    it.add(getProductCardMatchersPosition2())
    it.add(getProductCardMatchersPosition3())
    it.add(getProductCardMatchersPosition4())
    it.add(getProductCardMatchersPosition5())
    it.add(getProductCardMatchersPosition6())
    it.add(getProductCardMatchersPosition7())
    it.add(getProductCardMatchersPosition8())
    it.add(getProductCardMatchersPosition9())
    it.add(getProductCardMatchersPosition10())
    it.add(getProductCardMatchersPosition11())
    it.add(getProductCardMatchersPosition12())
    it.add(getProductCardMatchersPosition13())
    it.add(getProductCardMatchersPosition14())
    it.add(getProductCardMatchersPosition15())
    it.add(getProductCardMatchersPosition16())
    it.add(getProductCardMatchersPosition17())
}

private fun getProductCardMatchersPosition0(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[0]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition1(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[1]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelDiscount] = isDisplayedWithText(productCardModel.discountPercentage)
        it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition2(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[2]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition3(): Map<Int, Matcher<View?>> {
    val position = 3
    val productCardModel = productCardGeneralTestData[position]

    val labelProductStatus = productCardModel.getLabelProductStatus() ?: throw Exception("Product Card Position $position has no label status")
    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelGimmick = productCardModel.getLabelGimmick() ?: throw Exception("Product Card Position $position has no label gimmick")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textTopAds] = ViewMatchers.isDisplayed()
        it[R.id.textViewGimmick] = isDisplayedWithText(labelGimmick.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition4(): Map<Int, Matcher<View?>> {
    val position = 4
    val productCardModel = productCardGeneralTestData[position]

    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelGimmick = productCardModel.getLabelGimmick() ?: throw Exception("Product Card Position $position has no label gimmick")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewGimmick] = isDisplayedWithText(labelGimmick.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition5(): Map<Int, Matcher<View?>> {
    val position = 5
    val productCardModel = productCardGeneralTestData[position]

    val labelProductStatus = productCardModel.getLabelProductStatus() ?: throw Exception("Product Card Position $position has no label status")
    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition6(): Map<Int, Matcher<View?>> {
    val position = 6
    val productCardModel = productCardGeneralTestData[position]

    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelGimmick = productCardModel.getLabelGimmick() ?: throw Exception("Product Card Position $position has no label gimmick")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewGimmick] = isDisplayedWithText(labelGimmick.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition7(): Map<Int, Matcher<View?>> {
    val position = 7
    val productCardModel = productCardGeneralTestData[position]

    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelCredibility = productCardModel.getLabelCredibility2() ?: throw Exception("Product Card Position $position has no label credibility")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.textViewCredibility] = isDisplayedWithText(labelCredibility.title)
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition8(): Map<Int, Matcher<View?>> {
    val position = 8
    val productCardModel = productCardGeneralTestData[position]

    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelShipping = productCardModel.getLabelShipping() ?: throw Exception("Product Card Position $position has no text shipping")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.textViewShipping] = isDisplayedWithText(labelShipping.title)
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition9(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[9]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
    }
}

private fun getProductCardMatchersPosition10(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[10]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
    }
}

private fun getProductCardMatchersPosition11(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[11]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = ViewMatchers.isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition12(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[12]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = ViewMatchers.isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition13(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[13]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = ViewMatchers.isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition14(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[14]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = ViewMatchers.isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
    }
}

private fun getProductCardMatchersPosition15(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[15]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.linearLayoutImageRating] = ViewMatchers.isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_active)
    }
}

private fun getProductCardMatchersPosition16(): Map<Int, Matcher<View?>> {
    val position = 16
    val productCardModel = productCardGeneralTestData[position]

    val labelProductStatus = productCardModel.getLabelProductStatus() ?: throw Exception("Product Card Position $position has no label status")
    val labelPrice = productCardModel.getLabelPrice() ?: throw Exception("Product Card Position $position has no label price")
    val labelGimmick = productCardModel.getLabelGimmick() ?: throw Exception("Product Card Position $position has no label gimmick")

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textTopAds] = ViewMatchers.isDisplayed()
        it[R.id.textViewGimmick] = isDisplayedWithText(labelGimmick.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = ViewMatchers.isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageRatingString] = ViewMatchers.isDisplayed()
        it[R.id.textViewRatingString] = isDisplayedWithText(productCardModel.ratingString)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = ViewMatchers.isDisplayed()
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
        it[R.id.buttonAddToCart] = ViewMatchers.isDisplayed()
    }
}

private fun getProductCardMatchersPosition17(): Map<Int, Matcher<View?>> {
    val productCardModel = productCardGeneralTestData[17]

    return mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageThreeDots] = ViewMatchers.isDisplayed()
        it[R.id.buttonAddToCart] = ViewMatchers.isDisplayed()
    }
}