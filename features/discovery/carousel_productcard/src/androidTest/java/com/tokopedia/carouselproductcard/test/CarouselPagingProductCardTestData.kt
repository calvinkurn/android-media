package com.tokopedia.carouselproductcard.test

import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupProductModel
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.CardUnify2

internal fun productCardList(groupIndex: Int, size: Int) = List(size) { productIndex ->
    ProductCardModel(
        productImageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
        productName = "$groupIndex $productIndex 1 Line Product here lorem ips...",
        formattedPrice = "Rp10.000",
        slashedPrice = "Rp100.0000",
        discountPercentage = "50%",
        labelGroupList = listOf(
            ProductCardModel.LabelGroup(
                position = "ribbon",
                type = "gold",
                title = "#${productIndex + 1}"
            ),
            ProductCardModel.LabelGroup(
                position = "integrity",
                title = "Terjual 122",
                type = "#ae31353b"
            ),
        ),
        countSoldRating = "4.5",
        freeOngkir = ProductCardModel.FreeOngkir(isActive = true, imageUrl = "https://images.tokopedia.net/img/ic_bebas_ongkir.png"),
        cardType = CardUnify2.TYPE_CLEAR,
        productListType = ProductCardModel.ProductListType.BEST_SELLER,
    )
}

internal val data = listOf(
    CarouselPagingModel(
        productCardGroupList = List(2) {
            CarouselPagingGroupProductModel(
                group = CarouselPagingGroupModel("Test Group ${it + 1}"),
                productItemList = productCardList(it, 10),
            )
        },
    ),
    CarouselPagingModel(
        productCardGroupList = listOf(
            CarouselPagingGroupProductModel(
                group = CarouselPagingGroupModel("Test Group 1"),
                productItemList = productCardList(0, 4),
            ),
            CarouselPagingGroupProductModel(
                group = CarouselPagingGroupModel("Test Group 2 no product"),
            ),
            CarouselPagingGroupProductModel(
                group = CarouselPagingGroupModel("Test Group 3"),
                productItemList = productCardList(0, 5),
            ),
        ),
        currentGroupPosition = CarouselPagingModel.FIRST_GROUP,
    ),
)
