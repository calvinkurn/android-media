package com.tokopedia.content.common.producttag.builder

import com.tokopedia.content.common.producttag.view.uimodel.LastPurchasedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */
class LastPurchasedModelBuilder {

    fun buildModel(
        size: Int = 5,
        hasNextPage: Boolean = true,
        nextCursor: String = "1",
        coachMark: String = "This is coachmark",
        isCoachMarkShown: Boolean = true,
    ) = LastPurchasedProductUiModel(
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
        state = PagedState.Success(hasNextPage = hasNextPage),
        coachmark = coachMark,
        isCoachmarkShown = isCoachMarkShown,
    )
}