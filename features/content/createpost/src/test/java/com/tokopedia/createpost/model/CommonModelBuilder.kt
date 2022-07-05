package com.tokopedia.createpost.model

import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel
import com.tokopedia.createpost.producttag.view.uimodel.ShopUiModel
import com.tokopedia.createpost.producttag.view.uimodel.SortUiModel

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class CommonModelBuilder {

    fun buildException(message: String = "Something went wrong"): Exception {
        return Exception(message)
    }

    fun buildProduct(
        id: String = "1",
    ): ProductUiModel {
        return ProductUiModel(
            id = id,
            shopID = id,
            shopName = "Shop $id",
            shopBadge = listOf(),
            name = "Product $id",
            coverURL = "",
            webLink = "",
            appLink = "",
            star = "5",
            price = 10000.0,
            priceFmt = "Rp 10.000",
            isDiscount = true,
            discount = 5.0,
            discountFmt = "5%",
            priceOriginal = 10000.0,
            priceOriginalFmt = "Rp 10.000}",
            priceDiscount = 10000.0,
            priceDiscountFmt = "Rp 10.000}",
            totalSold = 5,
            totalSoldFmt = "5",
            isBebasOngkir = false,
            bebasOngkirStatus = "",
            bebasOngkirURL = "",
        )
    }

    fun buildSortModel(
        text: String = "Sort",
        key: String = "key",
        value: String = "value",
        isSelected: Boolean = true,
    ) = SortUiModel(
        text = text,
        key = key,
        value = value,
        isSelected = true,
    )

    fun buildShopModel(
        shopStatus: Int = 0,
    ) = ShopUiModel(
        shopStatus = shopStatus,
    )
}