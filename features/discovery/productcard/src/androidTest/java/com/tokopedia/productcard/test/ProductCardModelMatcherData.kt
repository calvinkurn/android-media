package com.tokopedia.productcard.test

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.FreeOngkir
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.ProductCardModel.LabelGroupVariant
import com.tokopedia.productcard.ProductCardModel.NonVariant
import com.tokopedia.productcard.ProductCardModel.ShopBadge
import com.tokopedia.productcard.ProductCardModel.Variant
import com.tokopedia.productcard.test.utils.campaignLabelUrl
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.fulfillmentBadgeImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithChildCount
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.isNotDisplayed
import com.tokopedia.productcard.test.utils.isQuantityEditorDisplayedWithValue
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.withDrawable
import com.tokopedia.productcard.utils.DARK_GREY
import com.tokopedia.productcard.utils.LABEL_BEST_SELLER
import com.tokopedia.productcard.utils.LABEL_CAMPAIGN
import com.tokopedia.productcard.utils.LABEL_CATEGORY
import com.tokopedia.productcard.utils.LABEL_CATEGORY_BOTTOM
import com.tokopedia.productcard.utils.LABEL_CATEGORY_SIDE
import com.tokopedia.productcard.utils.LABEL_COST_PER_UNIT
import com.tokopedia.productcard.utils.LABEL_ETA
import com.tokopedia.productcard.utils.LABEL_FULFILLMENT
import com.tokopedia.productcard.utils.LABEL_GIMMICK
import com.tokopedia.productcard.utils.LABEL_INTEGRITY
import com.tokopedia.productcard.utils.LABEL_PRICE
import com.tokopedia.productcard.utils.LABEL_PRODUCT_STATUS
import com.tokopedia.productcard.utils.LABEL_SHIPPING
import com.tokopedia.productcard.utils.LIGHT_GREEN
import com.tokopedia.productcard.utils.LIGHT_GREY
import com.tokopedia.productcard.utils.TEXT_DARK_GREY
import com.tokopedia.productcard.utils.TEXT_GREEN
import com.tokopedia.productcard.utils.TRANSPARENT_BLACK
import com.tokopedia.productcard.utils.TYPE_VARIANT_COLOR
import com.tokopedia.productcard.utils.TYPE_VARIANT_CUSTOM
import com.tokopedia.productcard.utils.TYPE_VARIANT_SIZE

private const val PLUS_VARIAN_LAIN_TEXT = "+ Keranjang"

internal fun getProductCardModelMatcherData(useViewStub: Boolean): List<ProductCardModelMatcher> {
    return listOf(
        testOneLineProductName(),
        testLabelDiscountAndSlashPrice(),
        testSlashPriceWithoutLabelDiscount(),
        testLabelDiscountWithoutSlashPrice(),
        testLabelPriceAndSlashPrice(),
        testTwoLinesProductName(),
        testMaximumInfoAndLabel(),
        testLabelGimmickNumberOfStock(),
        testLabelSoldOut(),
        testLabelNewProduct(),
        testLabelIntegrity(),
        testLabelFulfillment(),
        testLabelFulfillmentWithNoShopBadge(),
        testLabelShippingInfo(),
        testNoShopBadge(),
        testNoRatingButHasReviewCount(),
        testHasRatingButNoReviewCount(),
        testProductRatingStar1(),
        testProductRatingStar2(),
        testProductRatingStar3(),
        testProductRatingStar4(),
        testProductRatingStar5(),
        testPriceRange(),
        testAddToCartButton(),
        testAddToCartButtonAndShortContent(),
        testProductCardWithNameAndPdpView(),
        testProductCardWithNameAndStockBar(),
        testProductCardWithNameAndStockBarTwoLine(),
        testProductCardWithNameAndStockBarTwoLineEmptyStock(),
        testProductCardWithNameAndStockBarPdpView(),
        testProductCardWithNameAndStockBarPdpViewBebasOngkir(),
        testProductCardWithSpoilerPrice(),
        testProductCardWithSpoilerPriceAndViewCount(),
        testProductCardWithSpoilerPriceAndStockBar(),
        testProductCardWithNameAndStockBarAndStockBarLabelColor(),
        testLongerProductCardWithStockBar(),
        testHasBadgeNoLocation(),
        testHasRatingReviewAndLabelIntegrity(),
        testHasFreeOngkirAndLabelShipping(),
        testShopRatingBlue(),
        testShopRatingBlue2(),
        testShopRatingBlue3(),
        testShopRatingBlue4(),
        testShopRatingBlue5(),
        testShopRatingYellow(),
        testHasRatingSales(),
        testNoLabelIntegrityAndHasRatingFloat(),
        testPriorityRatingAverage(),
        testLabelCampaign(),
        testNotifyMeButton(),
        testLabelVariantColor(),
        testLabelVariantSize(),
        testLabelBestSeller(),
        testLabelBestSellerAndCategorySide(),
        testLabelBestSellerAndCategoryBottom(),
        testLabelCategorySideAndBottomWithoutBestSeller(),
        testLabelETA(),
        testLabelCategory(),
        testLabelCostPerUnit(),
        testLabelCategoryAndCostPerUnit(),
        testLabelVariantWithCategoryAndCostPerUnit(),
        testAddToCartButtonNonVariant(),
        testAddToCartButtonNonVariantWithQuantity(),
        testAddToCartButtonNonVariantIgnoreHasAddToCartFlag(),
        testAddToCartVariantWithNoQuantity(),
        testAddToCartVariantWithQuantity(),
        testAddToCartVariantWithQuantityAbove99(),
        testAddToCartButtonWishlist(useViewStub),
        testSeeSimilarProductButtonWishlist(useViewStub),
        testOutOfStock(),
    )
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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndPdpView(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With Pdp View Gaes",
            productImageUrl = productImageUrl,
            pdpViewCount = "17.9k view gaes"
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPdpView to isDisplayedWithText(productCardModel.pdpViewCount),
        R.id.imageViewPdpView to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBar(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With Stock",
            productImageUrl = productImageUrl,
            stockBarLabel = "Banyak Sisa",
            stockBarPercentage = 20
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBarTwoLine(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With Stock 2 Line Yang Pasti",
            productImageUrl = productImageUrl,
            stockBarLabel = "Banyak Sisa",
            stockBarPercentage = 20
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithNameAndStockBarTwoLineEmptyStock(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Product Card With 2 Line Empty Stock",
            productImageUrl = productImageUrl,
            stockBarLabel = "Tersedia",
            stockBarPercentage = 0
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
        R.id.textViewPdpView to isDisplayedWithText(productCardModel.pdpViewCount),
        R.id.imageViewPdpView to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
        R.id.textViewPdpView to isDisplayedWithText(productCardModel.pdpViewCount),
        R.id.imageViewPdpView to isDisplayed(),
        R.id.imageFreeOngkirPromo to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithSpoilerPrice(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productImageUrl = productImageUrl,
            slashedPrice = "Rp 1.000.000",
            formattedPrice = "Rp ???.??0"
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testProductCardWithSpoilerPriceAndViewCount(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productImageUrl = productImageUrl,
            pdpViewCount = "171k View Gaes",
            slashedPrice = "Rp 1.000.000",
            formattedPrice = "Rp ???.??0"
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.imageViewPdpView to isDisplayed(),
        R.id.textViewPdpView to isDisplayedWithText(productCardModel.pdpViewCount),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.productCardImage to isDisplayed(),
        R.id.imageViewPdpView to isDisplayed(),
        R.id.textViewPdpView to isDisplayedWithText(productCardModel.pdpViewCount),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLongerProductCardWithStockBar(): ProductCardModelMatcher {
    val labelIntegrity = LabelGroup(position = LABEL_INTEGRITY, title = "Terjual 122", type = "#ae31353b")

    val productCardModel = ProductCardModel(
        productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
            badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
        },
        shopLocation = "DKI Jakarta",
        pdpViewCount = "17.7k View",
        freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        stockBarLabel = "Segera Habis",
        stockBarPercentage = 80,
        stockBarLabelColor = "#ef144a",
        countSoldRating = "4.5",
        labelGroupList = listOf(labelIntegrity)
    )

    val productCardMatcher = mapOf(
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.productCardImage to isDisplayed(),
        R.id.imageViewPdpView to isDisplayed(),
        R.id.textViewPdpView to isDisplayedWithText(productCardModel.pdpViewCount),
        R.id.textViewPrice to isDisplayed(),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayed(),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.textViewStockLabel to isDisplayedWithText(productCardModel.stockBarLabel),
        R.id.progressBarStock to isDisplayed(),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayed(),
        R.id.salesRatingFloatLine to isDisplayed(),
        R.id.textViewSales to isDisplayedWithText(labelIntegrity.title),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelDiscount to isDisplayedWithText(productCardModel.discountPercentage),
        R.id.textViewSlashedPrice to isDisplayedWithText(productCardModel.slashedPrice),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.textViewIntegrity to isDisplayedWithText(labelIntegrity.title),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewFulfillment to isDisplayedWithText(labelFulfillment.title),
        R.id.imageFulfillment to isDisplayed(),
        R.id.dividerFulfillment to isDisplayed(),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewFulfillment to isDisplayedWithText(labelFulfillment.title),
        R.id.imageFulfillment to isDisplayed(),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.textViewShipping to isDisplayedWithText(labelShipping.title),
        R.id.imageThreeDots to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testNoShopBadge(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "No Shop Badge",
            productImageUrl = productImageUrl,
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = false, imageUrl = "https://images.tokopedia.net/img/blank.gif"))
            },
            formattedPrice = "Rp7.999.000",
            shopLocation = "DKI Jakarta"
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_active),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testPriceRange(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Test Price range",
            productImageUrl = productImageUrl,
            priceRange = "Rp25.999.000 - Rp28.499.000",
            formattedPrice = "this string does not matter, should take price range"
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.priceRange),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.buttonAddToCart to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButtonAndShortContent(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button and short content",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            hasAddToCartButton = true
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.buttonAddToCart to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.salesRatingFloatLine to isDisplayed(),
        R.id.textViewSales to isDisplayedWithText(labelIntegrity.title),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.salesRatingFloatLine to isDisplayed(),
        R.id.textViewSales to isDisplayedWithText(labelIntegrity.title),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageShopRating to withDrawable(R.drawable.product_card_ic_shop_rating),
        R.id.textViewShopRating to isDisplayedWithText(MethodChecker.fromHtml(productCardModel.shopRating).toString()),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.textViewIntegrity to isDisplayedWithText(labelIntegrity.title),
        R.id.imageShopRating to withDrawable(R.drawable.product_card_ic_shop_rating),
        R.id.textViewShopRating to isDisplayedWithText("Nilai Toko 14.5 "),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.textViewIntegrity to isDisplayedWithText(labelIntegrity.title),
        R.id.imageShopRating to withDrawable(R.drawable.product_card_ic_shop_rating),
        R.id.textViewShopRating to isDisplayedWithText("Nilai Toko 14.5"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.textViewIntegrity to isDisplayedWithText(labelIntegrity.title),
        R.id.imageShopRating to withDrawable(R.drawable.product_card_ic_shop_rating),
        R.id.textViewShopRating to isDisplayedWithText("Nilai 14.5 Toko"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.textViewIntegrity to isDisplayedWithText(labelIntegrity.title),
        R.id.imageShopRating to withDrawable(R.drawable.product_card_ic_shop_rating),
        R.id.textViewShopRating to isDisplayedWithText("14.5 Nilai bold Toko bold"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageShopRating to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.textViewShopRating to isDisplayedWithText(MethodChecker.fromHtml(productCardModel.shopRating).toString()),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.labelCampaignBackground to isDisplayed(),
        R.id.textViewLabelCampaign to isDisplayedWithText(labelCampaign.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelVariantContainer to isDisplayedWithChildCount(6),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelVariantContainer to isDisplayedWithChildCount(4),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayedWithChildCount(5),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.linearLayoutImageRating to isDisplayed(),
        R.id.imageViewRating1 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating2 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating3 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating4 to withDrawable(R.drawable.product_card_ic_rating_active),
        R.id.imageViewRating5 to withDrawable(R.drawable.product_card_ic_rating_default),
        R.id.textViewReviewCount to isDisplayedWithText("(${productCardModel.reviewCount})"),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.buttonNotify to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.labelBestSeller to isDisplayedWithText(labelBestSeller.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelBestSellerAndCategorySide(): ProductCardModelMatcher {
    val labelBestSeller = LabelGroup(position = LABEL_BEST_SELLER, title = "Terlaris #1", type = "#E1AA1D")
    val labelCategorySide = LabelGroup(position = LABEL_CATEGORY_SIDE, title = "di iOS", type = "textLightGrey")

    val productCardModel = ProductCardModel(
        productName = "Test Label Best Seller Category Side",
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
        labelGroupList = listOf(labelBestSeller, labelCategorySide)
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.labelBestSeller to isDisplayedWithText(labelBestSeller.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
        R.id.textCategorySide to isDisplayedWithText(labelCategorySide.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelBestSellerAndCategoryBottom(): ProductCardModelMatcher {
    val labelBestSeller = LabelGroup(position = LABEL_BEST_SELLER, title = "Terlaris #1", type = "#E1AA1D")
    val labelCategoryBottom = LabelGroup(position = LABEL_CATEGORY_BOTTOM, title = "di iOS", type = "textLightGrey")

    val productCardModel = ProductCardModel(
        productName = "Test Label Best Seller Category Bottom",
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
        labelGroupList = listOf(labelBestSeller, labelCategoryBottom)
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.labelBestSeller to isDisplayedWithText(labelBestSeller.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
        R.id.textCategoryBottom to isDisplayedWithText(labelCategoryBottom.title),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelCategorySideAndBottomWithoutBestSeller(): ProductCardModelMatcher {
    val labelCategorySide = LabelGroup(position = LABEL_CATEGORY_SIDE, title = "di iOS", type = "textLightGrey")
    val labelCategoryBottom = LabelGroup(position = LABEL_CATEGORY_BOTTOM, title = "di iOS", type = "textLightGrey")

    val productCardModel = ProductCardModel(
        productName = "Test Label Category Side and Bottom without Best Seller",
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
        labelGroupList = listOf(labelCategorySide, labelCategoryBottom)
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
        R.id.textViewETA to isDisplayedWithText(labelETA.title),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewCategory to isDisplayedWithText(labelCategory.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewCostPerUnit to isDisplayedWithText(labelCostPerUnit.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewCategory to isDisplayedWithText(labelCategory.title),
        R.id.dividerCategory to isDisplayed(),
        R.id.textViewCostPerUnit to isDisplayedWithText(labelCostPerUnit.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testLabelVariantWithCategoryAndCostPerUnit(): ProductCardModelMatcher {
    val labelSize1 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "S", type = LIGHT_GREY)
    val labelSize2 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "M", type = LIGHT_GREY)
    val labelSize3 = LabelGroupVariant(typeVariant = TYPE_VARIANT_SIZE, title = "XXXL", type = LIGHT_GREY)
    val labelCustom = LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val labelCategory = LabelGroup(position = LABEL_CATEGORY, title = "Halal", type = TEXT_GREEN)
    val labelCostPerUnit = LabelGroup(position = LABEL_COST_PER_UNIT, title = "Rp6.500/100 g", type = TEXT_DARK_GREY)

    val productCardModel = ProductCardModel(
            productName = "Label Variant will be shown with Category and Cost per Unit",
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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelVariantContainer to isDisplayedWithChildCount(4),
        R.id.textViewCategory to isDisplayedWithText(labelCategory.title),
        R.id.dividerCategory to isDisplayed(),
        R.id.textViewCostPerUnit to isDisplayedWithText(labelCostPerUnit.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.imageThreeDots to isDisplayed(),
    )

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
            countSoldRating = "4.5",
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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.buttonAddToCart to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButtonNonVariantWithQuantity(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val nonVariant = NonVariant(
            quantity = 30,
            minQuantity = 1,
            maxQuantity = 100
    )
    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Non Variant with Quantity",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.3",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            nonVariant = nonVariant
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.quantityEditorNonVariant to isQuantityEditorDisplayedWithValue(nonVariant.quantity),
        R.id.buttonDeleteCart to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButtonNonVariantIgnoreHasAddToCartFlag(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val nonVariant = NonVariant(
        quantity = 30,
        minQuantity = 1,
        maxQuantity = 100
    )
    val productCardModel = ProductCardModel(
        productName = "Add to Cart Button Non Variant Ignore hasAddToCart flag",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
            badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
        },
        shopLocation = "DKI Jakarta",
        countSoldRating = "4.3",
        freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        isTopAds = true,
        labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
            labelGroups.add(labelProductStatus)
            labelGroups.add(labelPrice)
            labelGroups.add(labelGimmick)
        },
        hasAddToCartButton = true,
        nonVariant = nonVariant
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.quantityEditorNonVariant to isQuantityEditorDisplayedWithValue(nonVariant.quantity),
        R.id.buttonDeleteCart to isDisplayed(),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartVariantWithNoQuantity(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)
    val labelGimmick = LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00")
    val labelColor1 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#05056b")
    val labelColor2 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#800000")
    val labelColor3 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#f400a1")
    val labelColor4 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#faf0be")
    val labelColor5 = LabelGroupVariant(typeVariant = TYPE_VARIANT_COLOR, hexColor = "#ebcca3")
    val labelCustom = LabelGroupVariant(typeVariant = TYPE_VARIANT_CUSTOM, title = "2")

    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Variant with No Quantity",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            isTopAds = true,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            variant = Variant(quantity = 0),
            labelGroupVariantList = listOf(
                    labelColor1,
                    labelColor2,
                    labelColor3,
                    labelColor4,
                    labelColor5,
                    labelCustom,
            )
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelVariantContainer to isDisplayedWithChildCount(6),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.buttonAddVariant to isDisplayedWithText(PLUS_VARIAN_LAIN_TEXT),
    )

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
            countSoldRating = "4.5",
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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.buttonAddVariant to isDisplayedWithText(PLUS_VARIAN_LAIN_TEXT),
        R.id.textVariantQuantity to isDisplayedWithText("${productCardModel.variant?.quantity} pcs"),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartVariantWithQuantityAbove99(): ProductCardModelMatcher {
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
            countSoldRating = "4.5",
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

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textTopAds to isDisplayed(),
        R.id.textViewGimmick to isDisplayedWithText(labelGimmick.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.buttonAddVariant to isDisplayedWithText(PLUS_VARIAN_LAIN_TEXT),
        R.id.textVariantQuantity to isDisplayedWithText("99+ pcs"),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testAddToCartButtonWishlist(useViewStub: Boolean): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
        productName = "Add to Cart Button Wishlist",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
            badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
        },
        shopLocation = "DKI Jakarta",
        countSoldRating = "4.5",
        freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
            labelGroups.add(labelProductStatus)
            labelGroups.add(labelPrice)
        },
        hasAddToCartButton = false,
        hasAddToCartWishlist = true,
        hasSimilarProductWishlist = false,
        hasButtonThreeDotsWishlist = true
    )

    val productCardMatcher = mutableMapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.rlPrimaryButtonWishlist to isDisplayed(),
        R.id.buttonAddToCartWishlist to isDisplayed(),
        R.id.buttonThreeDotsWishlist to isDisplayed(),
        R.id.imageButtonThreeDotsWishlist to isDisplayed(),
    )

    if (!useViewStub) productCardMatcher[R.id.buttonAddToCart] = isNotDisplayed()

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testSeeSimilarProductButtonWishlist(useViewStub: Boolean): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = TRANSPARENT_BLACK)
    val labelPrice = LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN)

    val productCardModel = ProductCardModel(
        productName = "See Similar Product Button Wishlist",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp7.999.000",
        shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
            badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
        },
        shopLocation = "DKI Jakarta",
        countSoldRating = "4.5",
        freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
        labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
            labelGroups.add(labelProductStatus)
            labelGroups.add(labelPrice)
        },
        hasAddToCartButton = false,
        hasAddToCartWishlist = false,
        hasSimilarProductWishlist = true,
        hasButtonThreeDotsWishlist = true
    )

    val productCardMatcher = mutableMapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.labelPrice to isDisplayedWithText(labelPrice.title),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
        R.id.imageShopBadge to isDisplayed(),
        R.id.textViewShopLocation to isDisplayedWithText(productCardModel.shopLocation),
        R.id.productCardImageSalesRatingFloat to isDisplayed(),
        R.id.salesRatingFloat to isDisplayedWithText(productCardModel.countSoldRating),
        R.id.imageFreeOngkirPromo to isDisplayed(),
        R.id.rlPrimaryButtonWishlist to isDisplayed(),
        R.id.buttonSeeSimilarProductWishlist to isDisplayed(),
        R.id.buttonThreeDotsWishlist to isDisplayed(),
        R.id.imageButtonThreeDotsWishlist to isDisplayed(),
    )

    if (!useViewStub) {
        productCardMatcher[R.id.buttonAddToCart] = isNotDisplayed()
        productCardMatcher[R.id.buttonAddToCartWishlist] = isNotDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}

private fun testOutOfStock(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Stok habis", type = TRANSPARENT_BLACK)
    val productCardModel = ProductCardModel(
        isOutOfStock = true,
        productImageUrl = productImageUrl,
        labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
            labelGroups.add(labelProductStatus)
        },
        productName = "Out of stock",
        formattedPrice = "Rp7.999.000"
    )

    val productCardMatcher = mapOf(
        R.id.productCardImage to isDisplayed(),
        R.id.outOfStockOverlay to isDisplayed(),
        R.id.labelProductStatus to isDisplayedWithText(labelProductStatus.title),
        R.id.textViewProductName to isDisplayedWithText(productCardModel.productName),
        R.id.textViewPrice to isDisplayedWithText(productCardModel.formattedPrice),
    )

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}
