package com.tokopedia.productcard.test.grid

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.R
import com.tokopedia.productcard.test.ProductCardModelMatcher
import com.tokopedia.productcard.test.getProductCardModelMatcherData
import com.tokopedia.productcard.test.utils.campaignLabelUrl
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.fulfillmentBadgeImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.isNotDisplayed
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.withDimensionRatio
import com.tokopedia.productcard.test.utils.withDrawable
import com.tokopedia.productcard.utils.DARK_GREY
import com.tokopedia.productcard.utils.LABEL_BEST_SELLER
import com.tokopedia.productcard.utils.LABEL_ETA
import com.tokopedia.productcard.utils.LABEL_FULFILLMENT
import com.tokopedia.productcard.utils.LABEL_GIMMICK
import com.tokopedia.productcard.utils.LABEL_ON_IMAGE
import com.tokopedia.productcard.utils.LABEL_OVERLAY
import com.tokopedia.productcard.utils.LABEL_PRICE
import com.tokopedia.productcard.utils.LABEL_PRODUCT_STATUS
import com.tokopedia.productcard.utils.LIGHT_GREEN
import com.tokopedia.productcard.utils.LIGHT_ORANGE
import com.tokopedia.productcard.utils.LONG_IMAGE_RATIO
import com.tokopedia.productcard.utils.SQUARE_IMAGE_RATIO
import com.tokopedia.productcard.utils.TRANSPARENT_BLACK
import com.tokopedia.productcard.utils.TYPE_VARIANT_COLOR
import com.tokopedia.productcard.utils.TYPE_VARIANT_CUSTOM
import com.tokopedia.productcard.utils.TYPE_VARIANT_SIZE
import org.hamcrest.Matcher

internal val productCardGridTestData =
    getProductCardModelMatcherData(false) +
        listOf(
            testSimilarProductButton(),
            testMixedVariant(),
            testMixedVariantWithLabelPrice(),
            testMixedVariantReposition(),
            testSizeVariantWithLabelPriceReposition(),
            testMixedVariantWithLabelPriceReposition(),
            testMixedVariantWithLabelPriceAndSlashPriceReposition(),
            testLabelGimmickReposition(),
            testLabelGimmickAndBestSellerReposition(),
            testShopLocationReposition(),
            testContentReposition(),
            testLongImage(),
            testPortraitImage(),
            testPortraitImageWithIsPortraitFalse(),
            testEta(),
            testInlineEta(),
            testCampaignOverlay(),
            testGimmickOverlay(),
            testStatusOverlay(),
            testBestSellerOverlay(),
            testTopStockBar(),
        )

internal val productCardGridViewStubTestData =
    getProductCardModelMatcherData(true) +
        listOf(
            testSimilarProductButton(),
            testMixedVariant(),
            testMixedVariantWithLabelPrice(),
            testMixedVariantReposition(),
            testSizeVariantWithLabelPriceReposition(),
            testMixedVariantWithLabelPriceReposition(),
            testMixedVariantWithLabelPriceAndSlashPriceReposition(),
            testLabelGimmickReposition(),
            testLabelGimmickAndBestSellerReposition(),
            testShopLocationReposition(),
            testContentReposition(),
            testLongImage(),
            testPortraitImage(),
            testPortraitImageWithIsPortraitFalse(),
            testEta(),
            testInlineEta(),
            testCampaignOverlay(),
            testGimmickOverlay(),
            testStatusOverlay(),
            testBestSellerOverlay(),
        )

private fun testSimilarProductButton(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Similar Product Button",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp8.999.000",
        shopLocation = "DKI Jakarta",
        hasSimilarProductButton = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.productCardImage] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.buttonSeeSimilarProduct] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testMixedVariant(): ProductCardModelMatcher {
    val labelColor1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#05056b")
    val labelColor2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#800000")
    val labelColor3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#f400a1")
    val labelColor4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#faf0be")
    val labelColor5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ebcca3")
    val labelCustom = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelSize1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S")
    val labelSize2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M")
    val labelSize3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "L")
    val labelSize4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XL")
    val labelSize5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXL")

    val productCardModel = ProductCardModel(
        productName = "Test mixed variants",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
            badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
        },
        shopLocation = "DKI Jakarta",
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        isTopAds = true,
        hasThreeDots = true,
        labelGroupVariantList = listOf(
            labelColor1,
            labelColor2,
            labelColor3,
            labelColor4,
            labelColor5,
            labelSize1,
            labelSize2,
            labelSize3,
            labelSize4,
            labelSize5,
            labelCustom,
        ),
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
        R.id.labelVariantContainer to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testMixedVariantWithLabelPrice(): ProductCardModelMatcher {
    val labelColor1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#05056b")
    val labelColor2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#800000")
    val labelColor3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#f400a1")
    val labelColor4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#faf0be")
    val labelColor5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ebcca3")
    val labelCustom = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelSize1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S")
    val labelSize2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M")
    val labelSize3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "L")
    val labelSize4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XL")
    val labelSize5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXL")

    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
        productName = "Test Mixed Variants with Label price",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        labelGroupVariantList = listOf(
            labelColor1,
            labelColor2,
            labelColor3,
            labelColor4,
            labelColor5,
            labelSize1,
            labelSize2,
            labelSize3,
            labelSize4,
            labelSize5,
            labelCustom,
        ),
        labelGroupList = listOf(labelPrice),
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelVariantContainer to isDisplayed(),
        R.id.labelPrice to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testMixedVariantReposition(): ProductCardModelMatcher {
    val labelColor1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#05056b")
    val labelColor2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#800000")
    val labelColor3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#f400a1")
    val labelColor4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#faf0be")
    val labelColor5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ebcca3")
    val labelCustom = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelSize1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S")
    val labelSize2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M")
    val labelSize3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "L")
    val labelSize4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XL")
    val labelSize5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXL")

    val productCardModel = ProductCardModel(
        productName = "Test Mixed Variants - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        labelGroupVariantList = listOf(
            labelColor1,
            labelColor2,
            labelColor3,
            labelColor4,
            labelColor5,
            labelSize1,
            labelSize2,
            labelSize3,
            labelSize4,
            labelSize5,
            labelCustom,
        ),
        productListType = ProductCardModel.ProductListType.REPOSITION,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelColorVariantReposition to isDisplayed(),
        R.id.labelSizeVariantReposition to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testSizeVariantWithLabelPriceReposition(): ProductCardModelMatcher {
    val labelSize1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S")
    val labelSize2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M")
    val labelSize3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "L")
    val labelSize4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XL")
    val labelSize5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXL")
    val labelCustom = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
        productName = "Test Size Variant and Price Label - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        labelGroupVariantList = listOf(
            labelSize1,
            labelSize2,
            labelSize3,
            labelSize4,
            labelSize5,
            labelCustom,
        ),
        labelGroupList = listOf(labelPrice),
        productListType = ProductCardModel.ProductListType.REPOSITION,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelSizeVariantReposition to isDisplayed(),
        R.id.labelPriceReposition to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}


private fun testMixedVariantWithLabelPriceReposition(): ProductCardModelMatcher {
    val labelColor1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#05056b")
    val labelColor2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#800000")
    val labelColor3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#f400a1")
    val labelColor4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#faf0be")
    val labelColor5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ebcca3")
    val labelCustom = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelSize1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S")
    val labelSize2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M")
    val labelSize3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "L")
    val labelSize4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XL")
    val labelSize5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXL")

    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
        productName = "Test Mixed Variants and Price Label - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        labelGroupVariantList = listOf(
            labelColor1,
            labelColor2,
            labelColor3,
            labelColor4,
            labelColor5,
            labelSize1,
            labelSize2,
            labelSize3,
            labelSize4,
            labelSize5,
            labelCustom,
        ),
        labelGroupList = listOf(labelPrice),
        productListType = ProductCardModel.ProductListType.REPOSITION,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelColorVariantReposition to isDisplayed(),
        R.id.labelPriceReposition to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testMixedVariantWithLabelPriceAndSlashPriceReposition(): ProductCardModelMatcher {
    val labelColor1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#05056b")
    val labelColor2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#800000")
    val labelColor3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#f400a1")
    val labelColor4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#faf0be")
    val labelColor5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ebcca3")
    val labelCustom = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelSize1 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S")
    val labelSize2 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M")
    val labelSize3 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "L")
    val labelSize4 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XL")
    val labelSize5 = ProductCardModel.LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXL")

    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
        productName = "Test Mixed Variants, Price Label, and slash price - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        discountPercentage = "20%",
        slashedPrice = "Rp8.499.000",
        labelGroupVariantList = listOf(
            labelColor1,
            labelColor2,
            labelColor3,
            labelColor4,
            labelColor5,
            labelSize1,
            labelSize2,
            labelSize3,
            labelSize4,
            labelSize5,
            labelCustom,
        ),
        labelGroupList = listOf(labelPrice),
        productListType = ProductCardModel.ProductListType.REPOSITION,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelGimmickReposition(): ProductCardModelMatcher {
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Sisa 5", type = "#ef144a")

    val productCardModel = ProductCardModel(
        productName = "Label Gimmick - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        labelGroupList = listOf(labelGimmick),
        productListType = ProductCardModel.ProductListType.REPOSITION,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelReposition to isDisplayedWithText(labelGimmick.title),
        R.id.labelRepositionBackground to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelGimmickAndBestSellerReposition(): ProductCardModelMatcher {
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Sisa 5", type = "#ef144a")
    val labelBestSeller = LabelGroup(position = LABEL_BEST_SELLER, title = "Terlaris", type = "#E1AA1D")

    val productCardModel = ProductCardModel(
        productName = "Label Gimmick and Best Seller - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        labelGroupList = listOf(labelGimmick, labelBestSeller),
        productListType = ProductCardModel.ProductListType.REPOSITION,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelReposition to isDisplayedWithText(labelBestSeller.title),
        R.id.labelRepositionBackground to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testShopLocationReposition(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Shop Location - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        countSoldRating = "4.5",
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
            badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
        },
        shopLocation = "DKI Jakarta",
        productListType = ProductCardModel.ProductListType.REPOSITION,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testContentReposition(): ProductCardModelMatcher {
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN)
    val labelFulfillment = LabelGroup(position = LABEL_FULFILLMENT, title = "TokoCabang", type = DARK_GREY, imageUrl = fulfillmentBadgeImageUrl)

    val productCardModel = ProductCardModel(
        productName = "Content - Reposition",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
            badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
        },
        shopLocation = "DKI Jakarta",
        productListType = ProductCardModel.ProductListType.REPOSITION,
        ratingCount = 4,
        reviewCount = 60,
        labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
            labelGroups.add(labelPrice)
            labelGroups.add(labelFulfillment)
        }
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.textViewFulfillment to isDisplayedWithText(labelFulfillment.title),
        R.id.imageFulfillment to isDisplayed(),
        R.id.dividerFulfillment to isDisplayed(),
        R.id.labelPriceReposition to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLongImage(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.LONG_IMAGE,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardImage to withDimensionRatio(LONG_IMAGE_RATIO),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testPortraitImage(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.PORTRAIT,
        isPortrait = true
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardImage to withDimensionRatio(LONG_IMAGE_RATIO),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testPortraitImageWithIsPortraitFalse(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.PORTRAIT,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.productCardImage to withDimensionRatio(SQUARE_IMAGE_RATIO),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testEta(): ProductCardModelMatcher {
    val labelEta = LabelGroup(position = LABEL_ETA, title = "Tiba Besok")
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.CONTROL,
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        labelGroupList = listOf(
            labelEta,
        )
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.textViewETA to isDisplayedWithText(labelEta.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testInlineEta(): ProductCardModelMatcher {
    val labelEta = LabelGroup(position = LABEL_ETA, title = "Tiba Besok")
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.ETA,
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        labelGroupList = listOf(
            labelEta,
        )
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.textViewInlineETA to isDisplayedWithText(labelEta.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testCampaignOverlay(): ProductCardModelMatcher {
    val labelCampaignOverlay =
        LabelGroup(position = LABEL_OVERLAY, title = "Semarak Ramadan", imageUrl = campaignLabelUrl)
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.GIMMICK,
        labelGroupList = listOf(
            labelCampaignOverlay,
        )
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelOverlayBackground to isDisplayed(),
        R.id.labelOverlay to isDisplayedWithText(labelCampaignOverlay.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testGimmickOverlay(): ProductCardModelMatcher {
    val labelGimmickOverlay = LabelGroup(
        position = LABEL_OVERLAY,
        title = "Terbaru",
        type = LABEL_ON_IMAGE
    )
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.GIMMICK,
        labelGroupList = listOf(
            labelGimmickOverlay,
        )
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelOverlayBackground to isNotDisplayed(),
        R.id.labelOverlay to isNotDisplayed(),
        R.id.labelOverlayStatus to isDisplayedWithText(labelGimmickOverlay.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testBestSellerOverlay(): ProductCardModelMatcher {
    val labelGimmickOverlay = LabelGroup(
        position = LABEL_OVERLAY,
        title = "Terlaris",
        type = LIGHT_ORANGE
    )
    val labelBestSeller = LabelGroup(
        position = LABEL_BEST_SELLER,
        title = "Terlaris",
        type = LIGHT_ORANGE
    )
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.GIMMICK,
        labelGroupList = listOf(
            labelGimmickOverlay,
            labelBestSeller,
        )
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelOverlayBackground to isNotDisplayed(),
        R.id.labelOverlay to isNotDisplayed(),
        R.id.labelOverlayStatus to isDisplayedWithText(labelGimmickOverlay.title),
        R.id.labelBestSeller to isNotDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testStatusOverlay(): ProductCardModelMatcher {
    val labelStatusOverlay = LabelGroup(
        position = LABEL_OVERLAY,
        title = "PreOrder",
        type = TRANSPARENT_BLACK
    )
    val labelProductStatus = LabelGroup(
        position = LABEL_PRODUCT_STATUS,
        title = "PreOrder",
        type = TRANSPARENT_BLACK
    )
    val productCardModel = ProductCardModel(
        productName = "Long Image",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        productListType = ProductCardModel.ProductListType.GIMMICK,
        labelGroupList = listOf(
            labelStatusOverlay,
            labelProductStatus
        )
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelOverlayBackground to isNotDisplayed(),
        R.id.labelOverlay to isNotDisplayed(),
        R.id.labelOverlayStatus to isDisplayedWithText(labelStatusOverlay.title),
        R.id.labelProductStatus to isNotDisplayed()
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testTopStockBar(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Product Card With Top Stock Bar",
        productImageUrl = productImageUrl,
        stockBarLabel = "Segera Habis",
        stockBarPercentage = 20,
        isTopStockBar = true,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewTopStockBar to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarTopStockBar to isDisplayed(),
        R.id.bgTopStockBarView to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}
