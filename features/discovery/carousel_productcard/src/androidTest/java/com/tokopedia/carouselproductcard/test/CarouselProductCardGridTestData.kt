package com.tokopedia.carouselproductcard.test

import com.tokopedia.productcard.v2.ProductCardModel

internal val carouselProductCardGridTestData = mutableListOf<List<ProductCardModel>>().also {
    it.add(createCarouselProductCardGrid())
}

private fun createCarouselProductCardGrid(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(createProductCardTwoLinesProductName())
        it.add(createProductCardWithSlashPrice())
        it.add(createProductCardOneLineProductName())
        it.add(createProductCardMaxInfoAndLabel("1"))
        it.add(createProductCardMaxInfoAndLabel("2"))
        it.add(createProductCardMaxInfoAndLabel("3"))
        it.add(createProductCardMaxInfoAndLabel("4"))
    }
}

private fun createProductCardMaxInfoAndLabel(additionalName: String): ProductCardModel {
    val labelProductStatus = ProductCardModel.LabelGroup(position = "status", title = "Preorder", type = "darkGrey")
    val labelPrice = ProductCardModel.LabelGroup(position = "price", title = "Grosir", type = "lightGreen")
    val labelGimmick = ProductCardModel.LabelGroup(position = "gimmick", title = "Best Seller", type = "#FF8B00")

    return ProductCardModel(
            productName = "$additionalName Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
            isTopAds = true,
            hasOptions = true,
            labelGroupList = mutableListOf<ProductCardModel.LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
                labelGroups.add(labelGimmick)
                labelGroups.add(labelPrice)
            }
    )
}

private fun createProductCardTwoLinesProductName(): ProductCardModel {
    return ProductCardModel(
            productName = "Two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
            hasOptions = true
    )
}

private fun createProductCardWithSlashPrice(): ProductCardModel {
    return ProductCardModel(
            productName = "Slash price",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            ratingString = "4.5",
            reviewCount = 60,
            hasOptions = true
    )
}

private fun createProductCardOneLineProductName(): ProductCardModel {
    return ProductCardModel(
            productName = "Product name",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            hasOptions = true
    )
}