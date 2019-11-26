package com.tokopedia.similarsearch.utils

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.similarsearch.tracking.ECommerce.Companion.NONE_OTHER
import com.tokopedia.similarsearch.getsimilarproducts.model.Product

internal fun safeCastRupiahToInt(price: String?): Int {
    return try {
        CurrencyFormatHelper.convertRupiahToInt(price)
    }
    catch(throwable: Throwable) {
        0
    }
}

internal fun Product.asObjectDataLayer(): Any {
    return DataLayer.mapOf(
            "name", name,
            "id", id,
            "price", safeCastRupiahToInt(price).toString(),
            "brand", NONE_OTHER,
            "category", categoryName,
            "variant", NONE_OTHER,
            "list", "/similarproduct",
            "position", position
    )
}