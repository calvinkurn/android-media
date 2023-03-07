package com.tokopedia.content.common.model

import com.tokopedia.content.common.producttag.view.uimodel.LastPurchasedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class LastPurchasedModelBuilder {

    fun buildUiModelList(
        size: Int = 5,
        hasNextPage: Boolean = true,
        nextCursor: String = "",
        coachmark: String = "",
        isCoachmarkShown: Boolean = false,
    ): LastPurchasedProductUiModel {
        return LastPurchasedProductUiModel(
            products = List(size) {
                ProductUiModel(
                    id = it.toString(),
                    shopID = it.toString(),
                    shopName = "Shop $it",
                    shopBadge = listOf(),
                    name = "Product $it",
                    coverURL = "",
                    webLink = "",
                    appLink = "",
                    star = (it % 5).toString(),
                    price = (it * 10000).toDouble(),
                    priceFmt = "Rp ${(it * 10000)}",
                    isDiscount = true,
                    discount = 5.0,
                    discountFmt = "5%",
                    priceOriginal = (it * 10000).toDouble(),
                    priceOriginalFmt = "Rp ${(it * 10000).toDouble()}",
                    priceDiscount = (it * 10000).toDouble(),
                    priceDiscountFmt = "Rp ${(it * 10000).toDouble()}",
                    totalSold = it,
                    totalSoldFmt = it.toString(),
                    isBebasOngkir = false,
                    bebasOngkirStatus = "",
                    bebasOngkirURL = "",
                )
            },
            nextCursor = nextCursor,
            state = PagedState.Success(hasNextPage = size != 0),
            coachmark = coachmark,
            isCoachmarkShown = isCoachmarkShown,
        )
    }
}