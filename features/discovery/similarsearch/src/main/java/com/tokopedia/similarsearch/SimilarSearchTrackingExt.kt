package com.tokopedia.similarsearch

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.design.utils.CurrencyFormatHelper

internal fun Product.asObjectDataLayer(position: Int): Any {
    return DataLayer.mapOf(
            "name", name,
            "id", id,
            "price", getPriceInRupiah(price),
            "brand", "none / other",
            "category", categoryName,
            "variant", "none / other",
            "list", "/similarproduct",
            "position", position
    )
}

private fun getPriceInRupiah(price: String): String {
    return try {
        if (price.trim { it <= ' ' }.isNotEmpty()) {
            CurrencyFormatHelper.convertRupiahToInt(price).toString()
        } else {
            ""
        }
    }
    catch (throwable: Throwable) {
        ""
    }
}