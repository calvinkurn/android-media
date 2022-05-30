package com.tokopedia.createpost.model

import com.tokopedia.createpost.producttag.model.PagedGlobalSearchProductResponse
import com.tokopedia.createpost.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.TickerUiModel

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class GlobalSearchModelBuilder {

    fun buildResponseModel(
        size: Int = 5,
        hasNextPage: Boolean = true,
        nextCursor: String = "",
        suggestion: String = "",
        ticker: TickerUiModel = TickerUiModel(),
    ): PagedGlobalSearchProductResponse {
        return PagedGlobalSearchProductResponse(
            pagedData = PagedDataUiModel(
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
                nextCursor = nextCursor,
                hasNextPage = hasNextPage
            ),
            suggestion = suggestion,
            ticker = ticker,
        )
    }
}