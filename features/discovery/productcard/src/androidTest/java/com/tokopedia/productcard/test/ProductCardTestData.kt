package com.tokopedia.productcard.test

import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardModel.*

private const val productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg"
private const val officialStoreBadgeImageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"
private const val freeOngkirImageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
private const val veryLongProductName = "2 Lines Product Name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla"

internal val productCardModelGridTestData = mutableListOf<ProductCardModel>().also {
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
                labelGroups.add(LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Preorder", type = DARK_GREY))
                labelGroups.add(LabelGroup(position = LABEL_PRICE, title = "Grosir", type = LIGHT_GREEN))
            },
            textGroupList = mutableListOf<TextGroup>().also { textGroups ->
                textGroups.add(TextGroup(position = TEXT_GIMMICK, title = "Best Seller", type = SMALL, weight = BOLD, color = "#FF8B00"))
            }
    ))

    it.add(4, ProductCardModel(
            productName = veryLongProductName,
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
                labelGroups.add(LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN))
            },
            textGroupList = mutableListOf<TextGroup>().also { textGroups ->
                textGroups.add(TextGroup(position = TEXT_GIMMICK, title = "Sisa 5", type = SMALL, weight = BOLD, color = "#ef144a"))
            }
    ))

    it.add(5, ProductCardModel(
            productName = veryLongProductName,
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
                labelGroups.add(LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Stok habis", type = DARK_GREY))
                labelGroups.add(LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN))
            }
    ))

    it.add(6, ProductCardModel(
            productName = veryLongProductName,
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            hasOptions = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN))
            },
            textGroupList = mutableListOf<TextGroup>().also { textGroups ->
                textGroups.add(TextGroup(position = TEXT_GIMMICK, title = "Terbaru", type = SMALL, weight = BOLD, color = "#ff8b00"))
            }
    ))

    it.add(7, ProductCardModel(
            productName = veryLongProductName,
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            shopLocation = "DKI Jakarta",
            hasOptions = true,
            freeOngkir = FreeOngkir(isActive = true, imageUrl = freeOngkirImageUrl),
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(LabelGroup(position = LABEL_PRICE, title = "Cashback", type = LIGHT_GREEN))
            },
            textGroupList = mutableListOf<TextGroup>().also { textGroups ->
                textGroups.add(TextGroup(position = TEXT_CREDIBILITY, title = "Terjual 122", type = BODY_3, weight = BOLD, color = "#ae31353b"))
            }
    ))
}