package com.tokopedia.productcard.test

import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardModel.*

private const val productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg"
private const val officialStoreBadgeImageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"
private const val freeOngkirImageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
private const val veryLongProductName = "2 Lines Product Name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla"

internal val productCardModelSmallGridList = mutableListOf<ProductCardModel>().also {
    it.add(0, ProductCardModel(
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
    ))

    it.add(1, ProductCardModel(
            productName = "Product Name",
            productImageUrl = productImageUrl,
            discountPercentage = "20",
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
    ))

    it.add(2, ProductCardModel(
            productName = veryLongProductName,
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
    ))

    it.add(3, ProductCardModel(
            productName = veryLongProductName,
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
                labelGroups.add(LabelGroup(position = "status", title = "Preorder", type = "darkGrey"))
                labelGroups.add(LabelGroup(position = "price", title = "Grosir", type = "lightGreen"))
            },
            textGroupList = mutableListOf<TextGroup>().also { textGroups ->
                textGroups.add(TextGroup(position = "gimmick", title = "Best Seller", type = "small", weight = "bold", color = "#FF8B00"))
            }
    ))
}