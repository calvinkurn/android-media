package com.tokopedia.carouselproductcard.test

import com.tokopedia.productcard.ProductCardModel

internal var productCardNameSuffix = 0

internal val carouselProductCardTestData = mutableListOf<List<ProductCardModel>>().also {
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGridWithLabelBestSeller())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
}

private fun createCarouselProductCardGrid(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(createProductCardTwoLinesProductName())
        it.add(createProductCardWithSlashPrice())
        it.add(createProductCardOneLineProductName())
        it.add(createProductCardMaxInfoAndLabel())
        it.add(createProductCardMaxInfoAndLabel())
        it.add(createProductCardMaxInfoAndLabel())
        it.add(createProductCardMaxInfoAndLabel())
    }
}

private fun createProductCardMaxInfoAndLabel(): ProductCardModel {
    productCardNameSuffix += 1

    val labelCampaign = ProductCardModel.LabelGroup(position = "campaign", title = "WIB", imageUrl = "https://ecs7.tokopedia.net/img/jbZAUJ/2020/10/13/c8eb8cd6-3c12-4659-a290-bb0555d64e3f.png")
    val labelProductStatus = ProductCardModel.LabelGroup(position = "status", title = "Preorder", type = "darkGrey")
    val labelPrice = ProductCardModel.LabelGroup(position = "price", title = "Grosir", type = "lightGreen")
    val labelGimmick = ProductCardModel.LabelGroup(position = "gimmick", title = "Best Seller", type = "#FF8B00")

    return ProductCardModel(
            productName = "$productCardNameSuffix Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
            isTopAds = true,
            hasThreeDots = true,
            labelGroupList = mutableListOf<ProductCardModel.LabelGroup>().also { labelGroups ->
                labelGroups.add(labelCampaign)
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
            }
    )
}

private fun createProductCardTwoLinesProductName(): ProductCardModel {
    productCardNameSuffix += 1

    return ProductCardModel(
            productName = "$productCardNameSuffix Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
            hasThreeDots = true
    )
}

private fun createProductCardWithSlashPrice(): ProductCardModel {
    productCardNameSuffix += 1

    return ProductCardModel(
            productName = "$productCardNameSuffix Slash price",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            hasThreeDots = true
    )
}

private fun createProductCardOneLineProductName(): ProductCardModel {
    productCardNameSuffix += 1

    return ProductCardModel(
            productName = "$productCardNameSuffix Product name",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            hasThreeDots = true
    )
}

private fun createCarouselProductCardGridWithButtonATC(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(createProductCardTwoLinesProductNameATC())
        it.add(createProductCardWithSlashPriceATC())
        it.add(createProductCardOneLineProductNameATC())
        it.add(createProductCardMaxInfoAndLabelATC())
        it.add(createProductCardMaxInfoAndLabelATC())
        it.add(createProductCardMaxInfoAndLabelATC())
        it.add(createProductCardMaxInfoAndLabelATC())
    }
}

private fun createCarouselProductCardGridWithLabelBestSeller(): List<ProductCardModel> {
    return listOf(
            createProductCardBestSellerLabel(),
            createProductCardBestSellerLabel(),
            createProductCardBestSellerLabel(),
            createProductCardBestSellerLabel(),
            createProductCardBestSellerLabel(),
            createProductCardBestSellerLabel()
    )
}

private fun createProductCardMaxInfoAndLabelATC(): ProductCardModel {
    productCardNameSuffix += 1

    val labelProductStatus = ProductCardModel.LabelGroup(position = "status", title = "Preorder", type = "darkGrey")
    val labelPrice = ProductCardModel.LabelGroup(position = "price", title = "Grosir", type = "lightGreen")
    val labelGimmick = ProductCardModel.LabelGroup(position = "gimmick", title = "Best Seller", type = "#FF8B00")

    return ProductCardModel(
            productName = "$productCardNameSuffix Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
            isTopAds = true,
            labelGroupList = mutableListOf<ProductCardModel.LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
            },
            hasAddToCartButton = true
    )
}

private fun createProductCardTwoLinesProductNameATC(): ProductCardModel {
    productCardNameSuffix += 1

    return ProductCardModel(
            productName = "$productCardNameSuffix Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
            hasAddToCartButton = true
    )
}

private fun createProductCardWithSlashPriceATC(): ProductCardModel {
    productCardNameSuffix += 1

    return ProductCardModel(
            productName = "$productCardNameSuffix Slash price",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingCount = 4,
            reviewCount = 60,
            hasAddToCartButton = true
    )
}

private fun createProductCardOneLineProductNameATC(): ProductCardModel {
    productCardNameSuffix += 1

    return ProductCardModel(
            productName = "$productCardNameSuffix Product name",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            hasAddToCartButton = true
    )
}

private fun createProductCardBestSellerLabel(): ProductCardModel {
    productCardNameSuffix += 1

    val labelGroupIntegrity = ProductCardModel.LabelGroup(position = "integrity", title = "Terjual 12 rb", type = "textDarkGrey")
    val labelBestSeller = ProductCardModel.LabelGroup(position = "best_seller", title = "Terlaris", type = "#E1AA1D")

    return ProductCardModel(
            productName = "$productCardNameSuffix Best seller label test very long long long name sfa;l fjslkdjfl;as jfdlkajsdf ",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.8",
            labelGroupList = listOf(labelBestSeller, labelGroupIntegrity)
    )
}