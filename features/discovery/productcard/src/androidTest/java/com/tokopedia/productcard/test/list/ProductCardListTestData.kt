package com.tokopedia.productcard.test.list

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.test.ProductCardModelMatcher
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.test.getProductCardModelMatcherData
import com.tokopedia.productcard.test.utils.campaignLabelUrl
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.isNotDisplayed
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.withDrawable
import com.tokopedia.productcard.utils.GOLD
import com.tokopedia.productcard.utils.LABEL_GIMMICK
import com.tokopedia.productcard.utils.LABEL_INTEGRITY
import com.tokopedia.productcard.utils.LABEL_OVERLAY
import com.tokopedia.productcard.utils.LABEL_PRICE
import com.tokopedia.productcard.utils.LABEL_PRODUCT_STATUS
import com.tokopedia.productcard.utils.LABEL_RIBBON
import com.tokopedia.productcard.utils.LIGHT_GREEN
import com.tokopedia.productcard.utils.LIGHT_ORANGE
import com.tokopedia.productcard.utils.TRANSPARENT_BLACK
import org.hamcrest.Matcher

internal val productCardListTestData = getProductCardModelMatcherData(false) + mutableListOf<ProductCardModelMatcher>().also {
    it.add(testAddToCartAndRemoveFromWishlist())
    it.add(testDeleteProductButton())
    it.add(testSimilarProductButton(false))
    it.add(testBestSellerLayout())
    it.add(testListViewExperimentLayout())
    it.add(testListViewOverlayCampaignLayout())
    it.add(testListViewOverlayBestSellerLayout())
}

internal val productCardListViewStubTestData= getProductCardModelMatcherData(true) + mutableListOf<ProductCardModelMatcher>().also {
    it.add(testAddToCartAndRemoveFromWishlist())
    it.add(testDeleteProductButton())
    it.add(testSimilarProductButton(true))
}

private fun testAddToCartAndRemoveFromWishlist(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button and Remove from Wishlist",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
            },
            hasAddToCartButton = true,
            hasRemoveFromWishlistButton = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.productCardImage] = isDisplayed()
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
        it[R.id.buttonRemoveFromWishlist] = isDisplayed()
        it[R.id.imageRemoveFromWishlist] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testDeleteProductButton(): ProductCardModelMatcher {

    val productCardModel = ProductCardModel(
            productName = "Delete product Button with Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 4,
            reviewCount = 60,
            hasDeleteProductButton = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.productCardImage] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.linearLayoutImageRating] = isDisplayed()
        it[R.id.imageViewRating1] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating2] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating3] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating4] = withDrawable(R.drawable.product_card_ic_rating_active)
        it[R.id.imageViewRating5] = withDrawable(R.drawable.product_card_ic_rating_default)
        it[R.id.textViewReviewCount] = isDisplayedWithText("(${productCardModel.reviewCount})")
        it[R.id.buttonDeleteProduct] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testSimilarProductButton(useViewStub: Boolean): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Similar Product Button",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp8.999.000",
        shopLocation = "DKI Jakarta",
        hasSimilarProductButton = false
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.productCardImage] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        if (useViewStub) it[R.id.buttonSeeSimilarProductStub] = isEnabled()
        else it[R.id.buttonSeeSimilarProduct] = isNotDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testBestSellerLayout(): ProductCardModelMatcher {
    val labelRibbon = LabelGroup(position = LABEL_RIBBON, type = GOLD, title = "#1")
    val labelIntegrity =
        LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")
    val labelETA = LabelGroup(position = LABEL_ETA, title = "Tiba 28 Feb - 1 Mar", type = TEXT_DARK_GREY)

    val productCardModel = ProductCardModel(
        productImageUrl = productImageUrl,
        productName = "Best Seller Layout can only have 1 line in product name.............",
        formattedPrice = "Rp8.999.000",
        discountPercentage = "50%",
        slashedPrice = "Rp8.000.000",
        countSoldRating = "4.5",
        labelGroupList = listOf(labelRibbon, labelIntegrity, labelETA),
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        productListType = ProductCardModel.ProductListType.BEST_SELLER,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.salesRatingFloatLine to isDisplayed(),
        R.id.textViewSales to isDisplayedWithText(labelIntegrity.title),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageRibbonArch to isDisplayed(),
        R.id.imageRibbonContent to isDisplayed(),
        R.id.textRibbon to isDisplayedWithText(labelRibbon.title),
        R.id.textViewETA to isDisplayedWithText(labelETA.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testListViewExperimentLayout(): ProductCardModelMatcher {
    val labelOverlay = LabelGroup(
        position = LABEL_OVERLAY,
        title = "Terlaris",
        type = LIGHT_ORANGE,
    )

    val labelETA = LabelGroup(position = LABEL_ETA, title = "Tiba 28 Feb - 1 Mar", type = TEXT_DARK_GREY)

    val productCardModel = ProductCardModel(
        productImageUrl = productImageUrl,
        productName = "List View Layout can have 2 line in product name.............",
        formattedPrice = "Rp8.999.000",
        discountPercentage = "50%",
        slashedPrice = "Rp8.000.000",
        countSoldRating = "4.5",
        labelGroupList = listOf(labelOverlay, labelETA),
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        productListType = ProductCardModel.ProductListType.LIST_VIEW,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayed(),
        R.id.labelOverlayStatus to isDisplayedWithText(labelOverlay.title),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.textViewETA to isDisplayedWithText(labelETA.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testListViewOverlayCampaignLayout(): ProductCardModelMatcher {
    val labelOverlay = LabelGroup(
        position = LABEL_OVERLAY,
        title = "Semarak Ramadan",
        imageUrl = campaignLabelUrl,
    )

    val labelCampaign = LabelGroup(
        position = LABEL_CAMPAIGN,
        title = "Semarak Ramadan",
        imageUrl = campaignLabelUrl,
    )

    val productCardModel = ProductCardModel(
        productImageUrl = productImageUrl,
        productName = "List View Layout with label campaign and label overlay",
        formattedPrice = "Rp8.999.000",
        discountPercentage = "50%",
        slashedPrice = "Rp8.000.000",
        countSoldRating = "4.5",
        labelGroupList = listOf(labelOverlay, labelCampaign),
        freeOngkir = ProductCardModel.FreeOngkir(
            isActive = true,
            imageUrl = freeOngkirImageUrl
        ),
        productListType = ProductCardModel.ProductListType.LIST_VIEW,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayed(),
        R.id.labelOverlay to isDisplayedWithText(labelCampaign.title),
        R.id.labelOverlayBackground to isDisplayed(),
        R.id.imageFreeOngkirPromo to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testListViewOverlayBestSellerLayout(): ProductCardModelMatcher {
    val labelOverlay = LabelGroup(
        position = LABEL_OVERLAY,
        title = "Terlaris",
        type = "#E1AA1D",
    )

    val labelBestSeller = LabelGroup(
        position = LABEL_BEST_SELLER,
        title = "Terlaris",
        type = "#E1AA1D"
    )

    val productCardModel = ProductCardModel(
        productImageUrl = productImageUrl,
        productName = "List View Layout with label overlay and label best seller",
        formattedPrice = "Rp8.999.000",
        discountPercentage = "50%",
        slashedPrice = "Rp8.000.000",
        countSoldRating = "4.5",
        labelGroupList = listOf(labelOverlay, labelBestSeller),
        freeOngkir = ProductCardModel.FreeOngkir(
            isActive = true,
            imageUrl = freeOngkirImageUrl
        ),
        productListType = ProductCardModel.ProductListType.LIST_VIEW,
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayed(),
        R.id.labelOverlayStatus to isDisplayedWithText(labelOverlay.title),
        R.id.imageFreeOngkirPromo to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}
