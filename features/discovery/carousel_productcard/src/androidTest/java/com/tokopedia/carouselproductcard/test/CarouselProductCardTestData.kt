package com.tokopedia.carouselproductcard.test

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup

internal var productCardNameSuffix = 0

internal val carouselProductCardTestData = mutableListOf<List<ProductCardModel>>().also {
    it.add(createCarouselProductCardGrid())
    it.add(createCarouselProductCardGridWithButtonATC())
    it.add(createCarouselProductCardGridWithLabelBestSeller())
    it.add(createCarouselProductCardGridLabelETA())
    it.add(createCarouselProductCardGridBO())
    it.add(createCarouselProductCardGridCountSoldRating())
    it.add(createCarouselProductCardGridLabelIntegrity())
    it.add(createCarouselProductCardGridCountSoldRatingAndIntegrity())
    it.add(createCarouselProductCardGridRatingAndIntegrity())
    it.add(createCarouselProductCardGridShopLocation())
    it.add(createCarouselProductCardGridLabelCategory())
    it.add(createCarouselProductCardGridLabelCostPerUnit())
    it.add(createCarouselProductCardGridLabelCategoryAndCostPerUnit())
}

private fun createSmallProductCard(): ProductCardModel {
    return ProductCardModel(
            productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
    )
}

private fun createCarouselProductCardGridBO(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(ProductCardModel(
                productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                formattedPrice = "Rp7.999.000",
                countSoldRating = "5.0",
                labelGroupList = listOf(
                        LabelGroup(position = "fulfillment", title = "TokoCabang", type = "darkGrey", imageUrl = "https://images.tokopedia.net/img/jbZAUJ/2021/2/18/6d2cc121-91b9-49bc-99c3-d57437ad64b7.png")
                ),
                hasThreeDots = true,
        ))
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
    }
}

private fun createCarouselProductCardGridCountSoldRating(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(ProductCardModel(
                productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                formattedPrice = "Rp7.999.000",
                countSoldRating = "5.0",
                hasThreeDots = true,
        ))
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
    }
}

private fun createCarouselProductCardGridLabelIntegrity(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(ProductCardModel(
                productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                formattedPrice = "Rp7.999.000",
                labelGroupList = listOf(
                        LabelGroup(position = "integrity", title = "Terjual 122", type = "#ae31353b"),
                ),
                hasThreeDots = true,
        ))
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
    }
}

private fun createCarouselProductCardGridCountSoldRatingAndIntegrity(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(ProductCardModel(
                productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                formattedPrice = "Rp7.999.000",
                labelGroupList = listOf(
                        LabelGroup(position = "integrity", title = "Terjual 122", type = "#ae31353b"),
                ),
                countSoldRating = "5.0",
                hasThreeDots = true,
        ))
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
    }
}

private fun createCarouselProductCardGridRatingAndIntegrity(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(ProductCardModel(
                productName = "Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
                productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                formattedPrice = "Rp7.999.000",
                labelGroupList = listOf(
                        LabelGroup(position = "integrity", title = "Terjual 122", type = "#ae31353b"),
                ),
                ratingCount = 4,
                reviewCount = 100,
                hasThreeDots = true,
        ))
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
    }
}

private fun createCarouselProductCardGridShopLocation(): List<ProductCardModel> {
    return mutableListOf<ProductCardModel>().also {
        it.add(ProductCardModel(
                productName = "Shop Location test very long long long name sfa;l fjslkdjfl;as jfdlkajsdf ",
                productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                formattedPrice = "Rp7.999.000",
                shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                    badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
                },
                shopLocation = "DKI Jakarta",
                countSoldRating = "4.8",
                labelGroupList = listOf(
                        LabelGroup(position = "integrity", title = "Terjual 122", type = "#ae31353b"),
                ),
        ))
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
        it.add(createSmallProductCard())
    }
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

    val labelCampaign = LabelGroup(position = "campaign", title = "WIB", imageUrl = "https://ecs7.tokopedia.net/img/jbZAUJ/2020/10/13/c8eb8cd6-3c12-4659-a290-bb0555d64e3f.png")
    val labelProductStatus = LabelGroup(position = "status", title = "Preorder", type = "darkGrey")
    val labelPrice = LabelGroup(position = "price", title = "Grosir", type = "lightGreen")
    val labelGimmick = LabelGroup(position = "gimmick", title = "Best Seller", type = "#FF8B00")

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
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
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

private fun createCarouselProductCardGridLabelETA(): List<ProductCardModel> {
    return listOf(
            createProductCardLabelETA(),
            createProductCardOneLineProductName(),
            createProductCardOneLineProductName(),
            createProductCardOneLineProductName(),
            createProductCardOneLineProductName(),
            createProductCardOneLineProductName(),
    )
}

private fun createProductCardMaxInfoAndLabelATC(): ProductCardModel {
    productCardNameSuffix += 1

    val labelProductStatus = LabelGroup(position = "status", title = "Preorder", type = "darkGrey")
    val labelPrice = LabelGroup(position = "price", title = "Grosir", type = "lightGreen")
    val labelGimmick = LabelGroup(position = "gimmick", title = "Best Seller", type = "#FF8B00")

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
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
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

    val labelGroupIntegrity = LabelGroup(position = "integrity", title = "Terjual 12 rb", type = "textDarkGrey")
    val labelBestSeller = LabelGroup(position = "best_seller", title = "Terlaris", type = "#E1AA1D")

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

private fun createProductCardLabelETA(): ProductCardModel {
    productCardNameSuffix += 1

    return ProductCardModel(
            productName = "$productCardNameSuffix Label ETA Maximum Info and Label with two lines product name on any view of any screensize no matter what...... blablabla blablabla blablabla blablabla blablabla",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp7.999.000",
            shopBadgeList = mutableListOf<ProductCardModel.ShopBadge>().also { badges ->
                badges.add(ProductCardModel.ShopBadge(isShown = true, imageUrl = "https://ecs7.tokopedia.net/img/official_store_badge.png"))
            },
            shopLocation = "DKI Jakarta",
            countSoldRating = "4.5",
            freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://ecs7.tokopedia.net/img/ic_bebas_ongkir.png"),
            labelGroupList = listOf(
                    LabelGroup(position = "eta", title = "Tiba 28 Feb - 1 Mar", type = "textDarkGrey")
            ),
    )
}

private fun createCarouselProductCardGridLabelCategory(): List<ProductCardModel> {
    return listOf(
        ProductCardModel(
                productName = "Label Category test very long long long name sfa;l fjslkdjfl;as jfdlkajsdf ",
                productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                formattedPrice = "Rp7.999.000",
                labelGroupList = listOf(
                        LabelGroup(position = "category", title = "Halal", type = "textGreen"),
                ),
        ),
        createSmallProductCard(),
        createSmallProductCard(),
        createSmallProductCard(),
        createSmallProductCard(),
        createSmallProductCard(),
    )
}

private fun createCarouselProductCardGridLabelCostPerUnit(): List<ProductCardModel> {
    return listOf(
            ProductCardModel(
                    productName = "Label Cost per Unit test very long long long name sfa;l fjslkdjfl;as jfdlkajsdf ",
                    productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                    formattedPrice = "Rp7.999.000",
                    labelGroupList = listOf(
                            LabelGroup(position = "costperunit", title = "Rp6.500/100 g", type = "textDarkGrey"),
                    ),
            ),
            createSmallProductCard(),
            createSmallProductCard(),
            createSmallProductCard(),
            createSmallProductCard(),
            createSmallProductCard(),
    )
}

private fun createCarouselProductCardGridLabelCategoryAndCostPerUnit(): List<ProductCardModel> {
    return listOf(
            ProductCardModel(
                    productName = "Label Category & Cost per Unit test very long long long name sfa;l fjslkdjfl;as jfdlkajsdf ",
                    productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
                    formattedPrice = "Rp7.999.000",
                    labelGroupList = listOf(
                            LabelGroup(position = "category", title = "Halal", type = "textGreen"),
                            LabelGroup(position = "costperunit", title = "Rp6.500/100 g", type = "textDarkGrey"),
                    ),
            ),
            createSmallProductCard(),
            createSmallProductCard(),
            createSmallProductCard(),
            createSmallProductCard(),
            createSmallProductCard(),
    )
}