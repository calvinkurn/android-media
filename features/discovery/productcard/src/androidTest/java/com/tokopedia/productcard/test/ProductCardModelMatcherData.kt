package com.tokopedia.productcard.test

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.*
import com.tokopedia.productcard.test.utils.*
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.utils.*
import org.hamcrest.Matcher

internal val productCardModelMatcherData: List<ProductCardModelMatcher> = mutableListOf<ProductCardModelMatcher>().also {
    it.add(testOneLineProductName())
    it.add(testLabelDiscountAndSlashPrice())
    it.add(testSlashPriceWithoutLabelDiscount())
    it.add(testLabelDiscountWithoutSlashPrice())
    it.add(testLabelPriceAndSlashPrice())
    it.add(testTwoLinesProductName())
    it.add(testMaximumInfoAndLabel())
    it.add(testLabelGimmickNumberOfStock())
    it.add(testLabelSoldOut())
    it.add(testLabelNewProduct())
    it.add(testLabelIntegrity())
    it.add(testLabelFulfillment())
    it.add(testLabelFulfillmentWithNoShopBadge())
    it.add(testLabelShippingInfo())
    it.add(testNoShopBadge())
    it.add(testNoRatingButHasReviewCount())
    it.add(testHasRatingButNoReviewCount())
    it.add(testProductRatingStar1())
    it.add(testProductRatingStar2())
    it.add(testProductRatingStar3())
    it.add(testProductRatingStar4())
    it.add(testProductRatingStar5())
    it.add(testPriceRange())
    it.add(testAddToCartButton())
    it.add(testAddToCartButtonAndShortContent())
    it.add(testProductCardWithNameAndPdpView())
    it.add(testProductCardWithNameAndStockBar())
    it.add(testProductCardWithNameAndStockBarTwoLine())
    it.add(testProductCardWithNameAndStockBarTwoLineEmptyStock())
    it.add(testProductCardWithNameAndStockBarPdpView())
    it.add(testProductCardWithNameAndStockBarPdpViewBebasOngkir())
    it.add(testProductCardWithSpoilerPrice())
    it.add(testProductCardWithSpoilerPriceAndViewCount())
    it.add(testProductCardWithSpoilerPriceAndStockBar())
    it.add(testHasBadgeNoLocation())
    it.add(testHasRatingReviewAndLabelIntegrity())
    it.add(testHasFreeOngkirAndLabelShipping())
    it.add(testShopRatingBlue())
    it.add(testShopRatingBlue2())
    it.add(testShopRatingBlue3())
    it.add(testShopRatingBlue4())
    it.add(testShopRatingBlue5())
    it.add(testShopRatingYellow())
    it.add(testHasRatingSales())
    it.add(testNoLabelIntegrityAndHasRatingFloat())
    it.add(testPriorityRatingAverage())
    it.add(testLabelCampaign())
    it.add(testNotifyMeButton())
    it.add(testLabelVariantColor())
    it.add(testLabelVariantSize())
    it.add(testLabelBestSeller())
    it.add(testProductCardWithNameAndStockBarAndStockBarLabelColor())
    it.add(testLabelETA())
    it.add(testLabelCategory())
    it.add(testLabelCostPerUnit())
    it.add(testLabelCategoryAndCostPerUnit())
    it.add(testLabelVariantPrioritizedOverCategoryAndCostPerUnit())
    it.add(testAddToCartButtonNonVariant())
    it.add(testAddToCartButtonNonVariantWithQuantity())
    it.add(testAddToCartVariantWithNoQuantity())
    it.add(testAddToCartVariantWithQuantity())
    it.add(testAddToCartVariantWithQuantity2())
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
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndPdpView(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With Pdp View Gaes",
            productImageUrl = productImageUrl,
            pdpViewCount = "17.9k view gaes"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPdpView] = isDisplayedWithText(productCardModel.pdpViewCount)
        it[R.id.imageViewPdpView] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBar(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With Stock",
            productImageUrl = productImageUrl,
            stockBarLabel = "Banyak Sisa",
            stockBarPercentage = 20
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewStockLabel] = isDisplayedWithText(productCardModel.stockBarLabel)
        it[R.id.progressBarStock] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBarTwoLine(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With Stock 2 Line Yang Pasti",
            productImageUrl = productImageUrl,
            stockBarLabel = "Banyak Sisa",
            stockBarPercentage = 20
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewStockLabel] = isDisplayedWithText(productCardModel.stockBarLabel)
        it[R.id.progressBarStock] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBarTwoLineEmptyStock(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With 2 Line Empty Stock",
            productImageUrl = productImageUrl,
            stockBarLabel = "Tersedia",
            stockBarPercentage = 0
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewStockLabel] = isDisplayedWithText(productCardModel.stockBarLabel)
        it[R.id.progressBarStock] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBarPdpView(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With 2 Line Empty Stock",
            productImageUrl = productImageUrl,
            stockBarLabel = "Tersedia",
            stockBarPercentage = 0,
            pdpViewCount = "17.7k View"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewStockLabel] = isDisplayedWithText(productCardModel.stockBarLabel)
        it[R.id.progressBarStock] = isDisplayed()
        it[R.id.textViewPdpView] = isDisplayedWithText(productCardModel.pdpViewCount)
        it[R.id.imageViewPdpView]= isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBarPdpViewBebasOngkir(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With 2 Line Empty Stock",
            productImageUrl = productImageUrl,
            stockBarLabel = "Tersedia",
            stockBarPercentage = 0,
            pdpViewCount = "17.7k View",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl)
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewStockLabel] = isDisplayedWithText(productCardModel.stockBarLabel)
        it[R.id.progressBarStock] = isDisplayed()
        it[R.id.textViewPdpView] = isDisplayedWithText(productCardModel.pdpViewCount)
        it[R.id.imageViewPdpView]= isDisplayed()
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithSpoilerPrice(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productImageUrl = productImageUrl,
            slashedPrice = "Rp 1.000.000",
            formattedPrice = "Rp ???.??0"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithSpoilerPriceAndViewCount(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productImageUrl = productImageUrl,
            pdpViewCount = "171k View Gaes",
            slashedPrice = "Rp 1.000.000",
            formattedPrice = "Rp ???.??0"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.imageViewPdpView] = isDisplayed()
        it[R.id.textViewPdpView] = isDisplayedWithText(productCardModel.pdpViewCount)
        it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithSpoilerPriceAndStockBar(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productImageUrl = productImageUrl,
            slashedPrice = "Rp 1.000.000",
            formattedPrice = "Rp ???.??0",
            stockBarLabel = "Banyak Sisa",
            stockBarPercentage = 20
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewStockLabel] = isDisplayedWithText(productCardModel.stockBarLabel)
        it[R.id.progressBarStock] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelDiscountAndSlashPrice(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Label Discount and Slash Price",
            productImageUrl = productImageUrl,
            discountPercentage = "20%",
            slashedPrice = "Rp8.499.000",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelDiscount] = isDisplayedWithText(productCardModel.discountPercentage)
        it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testSlashPriceWithoutLabelDiscount(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Slash Price without label discount",
            productImageUrl = productImageUrl,
            slashedPrice = "Rp8.499.000",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelDiscountWithoutSlashPrice(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Label Discount without slash price",
            productImageUrl = productImageUrl,
            discountPercentage = "20%",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelDiscount] = isDisplayedWithText(productCardModel.discountPercentage)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelPriceAndSlashPrice(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
            productName = "Slash Price prioritized over Label Price",
            productImageUrl = productImageUrl,
            discountPercentage = "20%",
            slashedPrice = "Rp8.499.000",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            hasThreeDots = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelPrice)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelDiscount] = isDisplayedWithText(productCardModel.discountPercentage)
        it[R.id.textViewSlashedPrice] = isDisplayedWithText(productCardModel.slashedPrice)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
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
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testMaximumInfoAndLabel(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
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
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
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
            ratingCount = 4,
            reviewCount = 60,
            hasThreeDots = true,
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelSoldOut(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Stok habis", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
            productName = "Label Sold Out",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            hasThreeDots = true,
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
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
            hasThreeDots = true,
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
            hasThreeDots = true,
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

private fun testLabelFulfillment(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)
    val labelFulfillment = LabelGroup(position = LABEL_FULFILLMENT, title = "TokoCabang", type = DARK_GREY, imageUrl = fulfillmentBadgeImageUrl)

    val productCardModel = ProductCardModel(
            productName = "Label Fulfillment",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            hasThreeDots = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelPrice)
                labelGroups.add(labelFulfillment)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewFulfillment] = isDisplayedWithText(labelFulfillment.title)
        it[R.id.imageFulfillment] = isDisplayed()
        it[R.id.dividerFulfillment] = isDisplayed()
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelFulfillmentWithNoShopBadge(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)
    val labelFulfillment = LabelGroup(position = LABEL_FULFILLMENT, title = "TokoCabang", type = DARK_GREY, imageUrl = fulfillmentBadgeImageUrl)

    val productCardModel = ProductCardModel(
            productName = "Label Fulfillment with no Shop Badge",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            hasThreeDots = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelPrice)
                labelGroups.add(labelFulfillment)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewFulfillment] = isDisplayedWithText(labelFulfillment.title)
        it[R.id.imageFulfillment] = isDisplayed()
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
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
            ratingCount = 4,
            reviewCount = 60,
            hasThreeDots = true,
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
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
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testHasRatingButNoReviewCount(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Has Rating but No Review Count",
            productImageUrl = productImageUrl,
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            formattedPrice = "Rp7.999.000",
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 0
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
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
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
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
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
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
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
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
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
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
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
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
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
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
            ratingCount = 4,
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
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

private fun testHasBadgeNoLocation(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "No shop location",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testHasRatingReviewAndLabelIntegrity(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")

    val productCardModel = ProductCardModel(
            productName = "Has Rating, Review, and Label Integrity",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testHasRatingSales(): ProductCardModelMatcher {
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")

    val productCardModel = ProductCardModel(
            productName = "Product with rating sales and label integrity",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.6",
            isTopAds = true,
            hasThreeDots = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelIntegrity)
            }
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textTopAds] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.salesRatingFloatLine] = isDisplayed()
        it[R.id.textViewSales] = isDisplayedWithText(labelIntegrity.title)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testNoLabelIntegrityAndHasRatingFloat(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product with count sold rating but no label integrity",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.6",
            isTopAds = true,
            hasThreeDots = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl)
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textTopAds] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testPriorityRatingAverage(): ProductCardModelMatcher {
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")

    val productCardModel = ProductCardModel(
            productName = "Product prio count sold rating",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.6",
            ratingCount = 4,
            reviewCount = 30,
            labelGroupList = listOf(labelIntegrity)
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.salesRatingFloatLine] = isDisplayed()
        it[R.id.textViewSales] = isDisplayedWithText(labelIntegrity.title)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testHasFreeOngkirAndLabelShipping(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Has Free Ongkir and Label Shipping",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
                labelGroups.add(labelShipping)
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testShopRatingBlue(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Shop Rating Blue - Bold tag start",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            shopRating = "<b>14.5</b> Rating Toko",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
                labelGroups.add(labelShipping)
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageShopRating] = withDrawable(R.drawable.product_card_ic_shop_rating)
        it[R.id.textViewShopRating] = isDisplayedWithText(MethodChecker.fromHtml(productCardModel.shopRating).toString())
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testShopRatingBlue2(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Shop Rating Blue 2 - Bold tag end",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            shopRating = "Nilai Toko <b>14.5</b>",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
                labelGroups.add(labelShipping)
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
        it[R.id.textViewIntegrity] = isDisplayedWithText(labelIntegrity.title)
        it[R.id.imageShopRating] = withDrawable(R.drawable.product_card_ic_shop_rating)
        it[R.id.textViewShopRating] = isDisplayedWithText("Nilai Toko 14.5 ")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testShopRatingBlue3(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Shop Rating Blue 3 - Incorrect bold tag",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            shopRating = "Nilai Toko <b>14.5<b>",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
                labelGroups.add(labelShipping)
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
        it[R.id.textViewIntegrity] = isDisplayedWithText(labelIntegrity.title)
        it[R.id.imageShopRating] = withDrawable(R.drawable.product_card_ic_shop_rating)
        it[R.id.textViewShopRating] = isDisplayedWithText("Nilai Toko 14.5")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testShopRatingBlue4(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Shop Rating Blue 4 - Bold tag middle",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            shopRating = "Nilai <b>14.5</b> Toko",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
                labelGroups.add(labelShipping)
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
        it[R.id.textViewIntegrity] = isDisplayedWithText(labelIntegrity.title)
        it[R.id.imageShopRating] = withDrawable(R.drawable.product_card_ic_shop_rating)
        it[R.id.textViewShopRating] = isDisplayedWithText("Nilai 14.5 Toko")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testShopRatingBlue5(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Shop Rating Blue 5 - Multiple Bold tag",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            shopRating = "<b>14.5</b> Nilai <b>bold</b> Toko <b>bold</b>",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
                labelGroups.add(labelShipping)
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
        it[R.id.textViewIntegrity] = isDisplayedWithText(labelIntegrity.title)
        it[R.id.imageShopRating] = withDrawable(R.drawable.product_card_ic_shop_rating)
        it[R.id.textViewShopRating] = isDisplayedWithText("14.5 Nilai bold Toko bold")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testShopRatingYellow(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelShipping = LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b")

    val productCardModel = ProductCardModel(
            productName = "Shop Rating Yellow",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            shopRating = "<b>14.5</b> Rating Toko",
            isShopRatingYellow = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
                labelGroups.add(labelIntegrity)
                labelGroups.add(labelShipping)
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageShopRating] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.textViewShopRating] = isDisplayedWithText(MethodChecker.fromHtml(productCardModel.shopRating).toString())
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelCampaign(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelCampaign = LabelGroup(position = LABEL_CAMPAIGN, title = "Waktu Indonesia Belanja", imageUrl = campaignLabelUrl)

    val productCardModel = ProductCardModel(
            productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = listOf(labelProductStatus, labelGimmick, labelPrice, labelCampaign)
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textTopAds] = isDisplayed()
        it[R.id.labelCampaignBackground] = isDisplayed()
        it[R.id.textViewLabelCampaign] = isDisplayedWithText(labelCampaign.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelVariantColor(): ProductCardModelMatcher {
    val labelColor1 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#05056b")
    val labelColor2 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#800000")
    val labelColor3 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#f400a1")
    val labelColor4 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#faf0be")
    val labelColor5 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ebcca3")
    val labelCustom = LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val productCardModel = ProductCardModel(
            productName = "Test Variant Color 1",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupVariantList = listOf(labelColor1, labelColor2, labelColor3, labelColor4, labelColor5, labelCustom)
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textTopAds] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelVariantContainer] = isDisplayedWithChildCount(6)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelVariantSize(): ProductCardModelMatcher {
    val labelSize1 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S", type = LIGHT_GREY)
    val labelSize2 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M", type = LIGHT_GREY)
    val labelSize3 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXXL", type = LIGHT_GREY)
    val labelSize4 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "All size", type = LIGHT_GREY)
    val labelCustom = LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val productCardModel = ProductCardModel(
            productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupVariantList = listOf(labelSize1, labelSize2, labelSize3, labelSize4, labelCustom)
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textTopAds] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelVariantContainer] = isDisplayedWithChildCount(4)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.linearLayoutImageRating] = isDisplayedWithChildCount(5)
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testNotifyMeButton(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Notify me button",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp8.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 70,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasNotifyMeButton = true
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.buttonNotify] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelBestSeller(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelCampaign = LabelGroup(position = LABEL_CAMPAIGN, title = "Waktu Indonesia Belanja", imageUrl = campaignLabelUrl)
    val labelBestSeller = LabelGroup(position = LABEL_BEST_SELLER, title = "Terlaris", type = "#E1AA1D")

    val productCardModel = ProductCardModel(
            productName = "Test Label Best Seller",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = listOf(labelProductStatus, labelGimmick, labelPrice, labelCampaign, labelBestSeller)
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textTopAds] = isDisplayed()
        it[R.id.labelBestSeller] = isDisplayedWithText(labelBestSeller.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBarAndStockBarLabelColor(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With Stock label color",
            productImageUrl = productImageUrl,
            pdpViewCount = "17.7k View",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            stockBarLabel = "Segera Habis",
            stockBarPercentage = 80,
            stockBarLabelColor = "#ef144a",
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.imageViewPdpView]= isDisplayed()
        it[R.id.textViewPdpView] = isDisplayedWithText(productCardModel.pdpViewCount)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.textViewStockLabel] = isDisplayedWithText(productCardModel.stockBarLabel)
        it[R.id.progressBarStock] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelETA(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelETA = LabelGroup(position = LABEL_ETA, title = "Tiba 28 Feb - 1 Mar", type = TEXT_DARK_GREY)

    val productCardModel = ProductCardModel(
            productName = "Label ETA",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true,
            labelGroupList = listOf(labelPrice, labelETA),
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.labelPrice] = isDisplayedWithText(labelPrice.title)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
        it[R.id.textViewETA] = isDisplayedWithText(labelETA.title)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelCategory(): ProductCardModelMatcher {
    val labelCategory = LabelGroup(position = LABEL_CATEGORY, title = "Halal", type = TEXT_GREEN)

    val productCardModel = ProductCardModel(
            productName = "Label Category",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true,
            labelGroupList = listOf(labelCategory),
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewCategory] = isDisplayedWithText(labelCategory.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelCostPerUnit(): ProductCardModelMatcher {
    val labelCostPerUnit = LabelGroup(position = LABEL_COST_PER_UNIT, title = "Rp6.500/100 g", type = TEXT_DARK_GREY)

    val productCardModel = ProductCardModel(
            productName = "Label Cost per Unit",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true,
            labelGroupList = listOf(labelCostPerUnit),
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewCostPerUnit] = isDisplayedWithText(labelCostPerUnit.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelCategoryAndCostPerUnit(): ProductCardModelMatcher {
    val labelCategory = LabelGroup(position = LABEL_CATEGORY, title = "Halal", type = TEXT_GREEN)
    val labelCostPerUnit = LabelGroup(position = LABEL_COST_PER_UNIT, title = "Rp6.500/100 g", type = TEXT_DARK_GREY)

    val productCardModel = ProductCardModel(
            productName = "Label Category and Cost per Unit",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true,
            labelGroupList = listOf(labelCategory, labelCostPerUnit),
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewCategory] = isDisplayedWithText(labelCategory.title)
        it[R.id.dividerCategory] = isDisplayed()
        it[R.id.textViewCostPerUnit] = isDisplayedWithText(labelCostPerUnit.title)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelVariantPrioritizedOverCategoryAndCostPerUnit(): ProductCardModelMatcher {
    val labelSize1 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S", type = LIGHT_GREY)
    val labelSize2 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M", type = LIGHT_GREY)
    val labelSize3 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXXL", type = LIGHT_GREY)
    val labelCustom = LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelCategory = LabelGroup(position = LABEL_CATEGORY, title = "Halal", type = TEXT_GREEN)
    val labelCostPerUnit = LabelGroup(position = LABEL_COST_PER_UNIT, title = "Rp6.500/100 g", type = TEXT_DARK_GREY)

    val productCardModel = ProductCardModel(
            productName = "Label Variant prioritized over Category and Cost per Unit",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasThreeDots = true,
            labelGroupList = listOf(labelCategory, labelCostPerUnit),
            labelGroupVariantList = listOf(labelSize1, labelSize2, labelSize3, labelCustom),
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.labelVariantContainer] = isDisplayedWithChildCount(4)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.imageShopBadge] = isDisplayed()
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.imageSalesRatingFloat] = isDisplayed()
        it[R.id.salesRatingFloat] = isDisplayedWithText(productCardModel.countSoldRating)
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.imageThreeDots] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButtonNonVariant(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Non Variant",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            nonVariant = NonVariant(
                quantity = 0,
                minQuantity = 1,
                maxQuantity = 100
            )
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.buttonAddToCart] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButtonNonVariantWithQuantity(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Non Variant",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            nonVariant = NonVariant(
                    quantity = 30,
                    minQuantity = 1,
                    maxQuantity = 100
            )
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.quantityEditorNonVariant] = isDisplayedWithText("30")
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartVariantWithNoQuantity(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Variant with No Quantity",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            variant = Variant(
                    quantity = 0
            )
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.buttonAddVariant] = isDisplayedWithText("Pilih Varian")
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartVariantWithQuantity(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Variant with Quantity under 99",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            variant = Variant(
                    quantity = 30
            )
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.buttonAddVariant] = isDisplayedWithText("+ varian lain")
        it[R.id.dividerVariantQuantity] = isDisplayed()
        it[R.id.textVariantQuantity] = isDisplayedWithText("${productCardModel.variant?.quantity} pcs")
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartVariantWithQuantity2(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Variant with Quantity Above 99",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            variant = Variant(
                    quantity = 230
            )
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
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.imageFreeOngkirPromo] = isDisplayed()
        it[R.id.buttonAddVariant] = isDisplayedWithText("+ varian lain")
        it[R.id.dividerVariantQuantity] = isDisplayed()
        it[R.id.textVariantQuantity] = isDisplayedWithText("99+ pcs")
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}