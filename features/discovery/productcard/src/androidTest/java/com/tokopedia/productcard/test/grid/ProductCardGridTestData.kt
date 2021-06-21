package com.tokopedia.productcard.test.grid

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.test.ProductCardModelMatcher
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.productCardModelMatcherData
import com.tokopedia.productcard.test.utils.freeOngkirImageUrl
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.isQuantityEditorDisplayedWithValue
import com.tokopedia.productcard.test.utils.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.test.utils.withDrawable
import com.tokopedia.productcard.utils.*
import org.hamcrest.Matcher

internal val productCardGridTestData = productCardModelMatcherData + mutableListOf<ProductCardModelMatcher>().also {
    it.add(testOutOfStock())
    it.add(testAddToCartButtonNonVariant())
    it.add(testAddToCartButtonNonVariantWithQuantity())
    it.add(testAddToCartVariantWithNoQuantity())
    it.add(testAddToCartVariantWithQuantity())
    it.add(testAddToCartVariantWithQuantityAbove99())
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

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.outOfStockOverlay] = isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
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
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            nonVariant = ProductCardModel.NonVariant(
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
    val nonVariant = ProductCardModel.NonVariant(
            quantity = 30,
            minQuantity = 1,
            maxQuantity = 100
    )
    val productCardModel = ProductCardModel(
            productName = "Add to Cart Button from Non Variant with Quantity",
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
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            nonVariant = nonVariant
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
        it[R.id.quantityEditorNonVariant] = isQuantityEditorDisplayedWithValue(nonVariant.quantity)
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
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            variant = ProductCardModel.Variant(
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
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            variant = ProductCardModel.Variant(
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
        it[R.id.textVariantQuantity] = isDisplayedWithText("${productCardModel.variant?.quantity} pcs")
    }

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
                labelGroups.add(labelPrice)
                labelGroups.add(labelGimmick)
            },
            hasAddToCartButton = false,
            variant = ProductCardModel.Variant(
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
        it[R.id.textVariantQuantity] = isDisplayedWithText("99+ pcs")
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}