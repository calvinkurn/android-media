package com.tokopedia.productcard.test

import com.tokopedia.productcard.v2.ProductCardModel

val productCardModelList = mutableListOf<ProductCardModel>().also {
    it.add(ProductCardModel(
            productName = "Novel - IM YOURS - PUTRI LAGILAGI",
            productImageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/12/29/234900908/234900908_33fe7619-52b3-4d5d-9bc9-672549dea45b_1728_1728.jpg",
            formattedPrice = "Rp 1.000.000"
    ))
}