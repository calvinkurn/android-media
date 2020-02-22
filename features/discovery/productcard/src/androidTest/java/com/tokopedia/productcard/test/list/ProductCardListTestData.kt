package com.tokopedia.productcard.test.list

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.test.ProductCardModelMatcher
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.productCardModelMatcherData
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.v2.ProductCardModel
import org.hamcrest.Matcher

internal val productCardListTestData = productCardModelMatcherData + mutableListOf<ProductCardModelMatcher>().also {
    it.add(testAddToCartAndRemoveFromWishlist())
}

private fun testAddToCartAndRemoveFromWishlist(): ProductCardModelMatcher {
    val labelProductStatus = ProductCardModel.LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = DARK_GREY)
    val labelGimmick = ProductCardModel.LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelPrice = ProductCardModel.LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button and Remove from Wishlist",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<ProductCardModel.LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
            },
            hasAddToCartButton = true,
            hasRemoveFromWishlistButton = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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
        it[R.id.buttonAddToCart] = isDisplayed()
        it[R.id.buttonRemoveFromWishlist] = isDisplayed()
        it[R.id.imageRemoveFromWishlist] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}