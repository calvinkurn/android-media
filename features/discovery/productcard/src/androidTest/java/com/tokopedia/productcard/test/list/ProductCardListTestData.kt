package com.tokopedia.productcard.test.list

import com.tokopedia.productcard.test.freeOngkirImageUrl
import com.tokopedia.productcard.test.officialStoreBadgeImageUrl
import com.tokopedia.productcard.test.productCardGeneralTestData
import com.tokopedia.productcard.test.productImageUrl
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.v2.ProductCardModel

internal val productCardListTestData = productCardGeneralTestData + mutableListOf<ProductCardModel>().also {
    it.add(ProductCardModel(
            productName = "With Add to Cart Button",
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
            hasOptions = true,
            labelGroupList = mutableListOf<ProductCardModel.LabelGroup>().also { labelGroups ->
                labelGroups.add(ProductCardModel.LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = DARK_GREY))
                labelGroups.add(ProductCardModel.LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN))
                labelGroups.add(ProductCardModel.LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00"))
            },
            hasAddToCartButton = true,
            hasRemoveFromWishlistButton = true
    ))
}