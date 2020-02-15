package com.tokopedia.productcard.test

import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardModel.*

private const val productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg"
private const val officialStoreBadgeImageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"
private const val freeOngkirImageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"
private const val veryLongProductName = "2 Lines Product Name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla"

internal val productCardModelTestData = mutableListOf<ProductCardModel>().also {
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
                labelGroups.add(LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00"))
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
                labelGroups.add(LabelGroup(position = LABEL_GIMMICK, title = "Sisa 5", type = "#ef144a"))
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
                labelGroups.add(LabelGroup(position = LABEL_GIMMICK, title = "Terbaru", type = "#ff8b00"))
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
                labelGroups.add(LabelGroup(position = LABEL_CREDIBILITY, title = "Terjual 122", type = "#ae31353b"))
            }
    ))

    it.add(8, ProductCardModel(
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
                labelGroups.add(LabelGroup(position = LABEL_SHIPPING, title = "Ongkir Rp11 rb", type = "#7031353b"))
            }
    ))

    it.add(9, ProductCardModel(
            productName = veryLongProductName,
            productImageUrl = productImageUrl,
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = false, imageUrl = "https://ecs7.tokopedia.net/img/blank.gif"))
            },
            formattedPrice = "Rp7.999.000",
            shopLocation = "DKI Jakarta"
    ))

    it.add(10, ProductCardModel(
            productName = veryLongProductName,
            productImageUrl = productImageUrl,
            shopBadgeList = mutableListOf<ShopBadge>().also { badges ->
                badges.add(ShopBadge(isShown = true, imageUrl = officialStoreBadgeImageUrl))
            },
            formattedPrice = "Rp7.999.000",
            shopLocation = "DKI Jakarta",
            reviewCount = 60
    ))

    it.add(11, ProductCardModel(
            productName = "Product Rating Star 1",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 1,
            reviewCount = 60
    ))

    it.add(12, ProductCardModel(
            productName = "Product Rating Star 2",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 2,
            reviewCount = 60
    ))

    it.add(13, ProductCardModel(
            productName = "Product Rating Star 3",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 3,
            reviewCount = 60
    ))

    it.add(14, ProductCardModel(
            productName = "Product Rating Star 4",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 4,
            reviewCount = 60
    ))

    it.add(15, ProductCardModel(
            productName = "Product Rating Star 5",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            ratingCount = 5,
            reviewCount = 60
    ))

    it.add(16, ProductCardModel(
            productName = "With Add to Cart Button",
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
                labelGroups.add(LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00"))
            },
            hasAddToCartButton = true
    ))

    it.add(17, ProductCardModel(
            productName = "With Add to Cart Button and small content",
            productImageUrl = productImageUrl,
            formattedPrice = "Rp7.999.000",
            hasOptions = true,
            hasAddToCartButton = true
    ))

    it.add(18, ProductCardModel(
            productName = "With Add to Cart Button",
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
                labelGroups.add(LabelGroup(position = LABEL_GIMMICK, title = "Best Seller", type = "#FF8B00"))
            },
            hasAddToCartButton = true,
            hasRemoveFromWishlistButton = true
    ))
}