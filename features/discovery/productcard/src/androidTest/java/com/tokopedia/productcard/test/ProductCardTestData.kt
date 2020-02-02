package com.tokopedia.productcard.test

import com.tokopedia.productcard.v2.ProductCardModel

private const val productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg"
private const val officialStoreBadgeImageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"
private const val freeOngkirImageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"

internal val productCardModelSmallGridList = mutableListOf<ProductCardModel>().also {
    it.add(ProductCardModel(
            productName = "Product Name",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasOptions = true
    ))

    it.add(ProductCardModel(
            productName = "Product Name",
            productImageUrl = productImageUrl,
            discountPercentage = "20",
            slashedPrice = "Rp8.499.000",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasOptions = true
    ))

    it.add(ProductCardModel(
            productName = "2 Lines Product Name on Grid View",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            hasOptions = true
    ))
}