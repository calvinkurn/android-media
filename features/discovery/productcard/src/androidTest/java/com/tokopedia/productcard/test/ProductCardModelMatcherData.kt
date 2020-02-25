package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.test.utils.*
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.withDrawable
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.*
import org.hamcrest.Matcher

internal val productCardModelMatcherData: List<ProductCardModelMatcher> = mutableListOf<ProductCardModelMatcher>().also {
    it.add(testOneLineProductName())
    it.add(testSlashPrice())
    it.add(testTwoLinesProductName())
    it.add(testMaximumInfoAndLabel())
    it.add(testLabelGimmickNumberOfStock())
    it.add(testLabelSoldOut())
    it.add(testLabelNewProduct())
    it.add(testLabelIntegrity())
    it.add(testLabelShippingInfo())
    it.add(testNoShopBadge())
    it.add(testNoRatingButHasReviewCount())
    it.add(testProductRatingStar1())
    it.add(testProductRatingStar2())
    it.add(testProductRatingStar3())
    it.add(testProductRatingStar4())
    it.add(testProductRatingStar5())
    it.add(testPriceRange())
    it.add(testAddToCartButton())
    it.add(testAddToCartButtonAndShortContent())
}

private fun testOneLineProductName(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Name",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasOptions = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testSlashPrice(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Slash Price",
            productImageUrl = productImageUrl,
            discountPercentage = "20%",
            slashedPrice = "Rp8.499.000",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasOptions = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testTwoLinesProductName(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasOptions = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testMaximumInfoAndLabel(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = DARK_GREY)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasOptions = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
            }
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
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelGimmickNumberOfStock(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Sisa 5", type = "#ef144a")

    val productCardModel = ProductCardModel(
            productName = "Label Gimmick Number of Stock",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            hasOptions = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelSoldOut(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Stok habis", type = DARK_GREY)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
            productName = "Label Sold Out",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            hasOptions = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelNewProduct(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Terbaru", type = "#ff8b00")

    val productCardModel = ProductCardModel(
            productName = "Label New Product",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            hasOptions = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelIntegrity(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")

    val productCardModel = ProductCardModel(
            productName = "Label Integrity",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            hasOptions = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.textViewIntegrity] = isDisplayedWithText(labelIntegrity.title)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelShippingInfo(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Label Shipping Info",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            hasOptions = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelPrice)
                labelGroups.add(labelShipping)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testNoShopBadge(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "No Shop Badge",
            productImageUrl = productImageUrl,
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = false, imageUrl = "https://ecs7.tokopedia.net/img/blank.gif"))
            },
            formattedPrice = "Rp7.999.000",
            shopLocation = "DKI Jakarta"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testNoRatingButHasReviewCount(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "No Rating but Has Review Count",
            productImageUrl = productImageUrl,
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            formattedPrice = "Rp7.999.000",
            shopLocation = "DKI Jakarta",
            reviewCount = 60
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductRatingStar1(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Rating Star 1",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 1,
            reviewCount = 60
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductRatingStar2(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Rating Star 2",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 2,
            reviewCount = 60
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductRatingStar3(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Rating Star 3",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 3,
            reviewCount = 60
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductRatingStar4(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Rating Star 4",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 4,
            reviewCount = 60
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductRatingStar5(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Rating Star 5",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 5,
            reviewCount = 60
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
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

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testPriceRange(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Test Price range",
            productImageUrl = productImageUrl,
            priceRange = "Rp25.999.000 - Rp28.499.000",
            formattedPrice = "this string does not matter, should take price range"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.priceRange)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButton(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = DARK_GREY)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = true
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
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButtonAndShortContent(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button and short content",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            hasAddToCartButton = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.buttonAddToCart] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}