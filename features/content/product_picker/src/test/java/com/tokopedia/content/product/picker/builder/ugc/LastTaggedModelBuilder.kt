package com.tokopedia.content.product.picker.builder.ugc

import com.tokopedia.content.product.picker.ugc.view.uimodel.PagedDataUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class LastTaggedModelBuilder {

    fun buildPagedDataModel(
        size: Int = 5,
        hasNextPage: Boolean = true,
        nextCursor: String = "",
    ): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = List(size) {
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
            hasNextPage = hasNextPage,
            nextCursor = nextCursor,
        )
    }
}
