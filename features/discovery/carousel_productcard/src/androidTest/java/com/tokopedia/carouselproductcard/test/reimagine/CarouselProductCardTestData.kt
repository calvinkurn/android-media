package com.tokopedia.carouselproductcard.test.reimagine

import com.tokopedia.carouselproductcard.reimagine.viewallcard.CarouselProductCardViewAllCardModel
import com.tokopedia.productcard.reimagine.ProductCardModel

internal const val WORDING_SEGERA_HABIS = "Segera Habis"
internal const val WORDING_LAGI_DIMINATI = "Lagi Diminati"
internal const val WORDING_TERSEDIA = "Tersedia"

internal val carouselProductCardTestData = listOf(
    productCardListFullInfo(),
    productCardListSmallInfo(),
    productCardListMixedHeight(),
    productCardListWithViewAllCard(),
    productCardListInlineSlashedPriceAndStockInfoWithViewAllCard(),
)

private fun productCardListFullInfo() = CarouselProductCardTestCase(
    productCardModelList = listOf(
        productCard(),
        productCard(),
        productCard(),
        productCard(),
    )
)

private fun productCardListSmallInfo() = CarouselProductCardTestCase(
    productCardModelList = listOf(
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
    )
)

private fun productCardListMixedHeight() = CarouselProductCardTestCase(
    productCardModelList = listOf(
        productCard(),
        productCard(),
        productCard(),
        productCard(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
    )
)

private fun productCardListWithViewAllCard() = CarouselProductCardTestCase(
    productCardModelList = listOf(
        productCard(),
        productCard(),
        productCard(),
        productCard(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
    ),
    viewAllCard = viewAllCard(),
)

private fun productCardListInlineSlashedPriceAndStockInfoWithViewAllCard() = CarouselProductCardTestCase(
    productCardModelList = listOf(
        productCard(),
        productCard(),
        productCard(),
        productCard(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardSmallInfo(),
        productCardRibbonDiscountSlashedPriceInline(),
        productCardRibbonDiscountSlashedPriceInline(),
        productCardRibbonDiscountSlashedPriceInlineStockInfo(40, WORDING_TERSEDIA),
        productCardRibbonDiscountSlashedPriceInline(),
        productCardRibbonDiscountSlashedPriceInlineStockInfo(90, WORDING_SEGERA_HABIS),
        productCardRibbonDiscountSlashedPriceInline(),
        productCardRibbonDiscountSlashedPriceInlineStockInfo(60, WORDING_LAGI_DIMINATI),
    ),
    viewAllCard = viewAllCard(),
)

private fun productCardSmallInfo(): ProductCardModel {
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = "ri_product_credibility",
        title = "10 rb+ terjual",
        type = "textDarkGrey",
    )

    return ProductCardModel(
        imageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(reimagineCredibilityLabel),
    )
}

private fun productCard(): ProductCardModel {
    val reimagineBenefitLabel = ProductCardModel.LabelGroup(
        position = "ri_product_benefit",
        title = "Cashback Rp10 rb",
        type = "lightGreen",
    )
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = "ri_product_credibility",
        title = "10 rb+ terjual",
        type = "textDarkGrey",
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = "https://images.tokopedia.net/img/official_store_badge.png",
        title = "Shop Name paling panjang",
    )

    return ProductCardModel(
        imageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 10,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel
        ),
        rating = "4.5",
        shopBadge = shopBadge,
    )
}

private fun viewAllCard() = CarouselProductCardViewAllCardModel(
    title = "Title here",
    description = "Description here (mandatory)",
    ctaText = "Lihat Semua",
)

private fun productCardRibbonDiscountSlashedPriceInline(): ProductCardModel {
    val reimagineBenefitLabel = ProductCardModel.LabelGroup(
        position = "ri_product_benefit",
        title = "Cashback Rp10 rb",
        type = "lightGreen",
    )
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = "ri_product_credibility",
        title = "10 rb+ terjual",
        type = "textDarkGrey",
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = "https://images.tokopedia.net/img/official_store_badge.png",
        title = "Shop Name paling panjang",
    )
    val reimagineRibbon = ProductCardModel.LabelGroup(
        position = "ri_ribbon",
        title = "10%",
        type = "red",
    )

    return ProductCardModel(
        imageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 0,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
            reimagineRibbon
        ),
        rating = "4.5",
        shopBadge = shopBadge,
    )
}

private fun productCardRibbonDiscountSlashedPriceInlineStockInfo(percentageStock: Int, labelStock: String): ProductCardModel {
    val reimagineBenefitLabel = ProductCardModel.LabelGroup(
        position = "ri_product_benefit",
        title = "Cashback Rp10 rb",
        type = "lightGreen",
    )
    val reimagineCredibilityLabel = ProductCardModel.LabelGroup(
        position = "ri_product_credibility",
        title = "10 rb+ terjual",
        type = "textDarkGrey",
    )
    val shopBadge = ProductCardModel.ShopBadge(
        imageUrl = "https://images.tokopedia.net/img/official_store_badge.png",
        title = "Shop Name paling panjang",
    )
    val reimagineRibbon = ProductCardModel.LabelGroup(
        position = "ri_ribbon",
        title = "10%",
    )
    val stockInfo = ProductCardModel.StockInfo(
        percentage = percentageStock,
        label = labelStock,
    )

    return ProductCardModel(
        imageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
        name = "1 Line Product Name",
        price = "Rp79.000",
        slashedPrice = "Rp100.000",
        discountPercentage = 0,
        labelGroupList = listOf(
            reimagineBenefitLabel,
            reimagineCredibilityLabel,
            reimagineRibbon
        ),
        rating = "4.5",
        shopBadge = shopBadge,
        stockInfo = stockInfo
    )
}
