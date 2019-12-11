package com.tokopedia.similarsearch.utils

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.similarsearch.tracking.ECommerce.Companion.NONE_OTHER
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.getsimilarproducts.model.Shop

internal fun safeCastRupiahToInt(price: String?): Int {
    return try {
        CurrencyFormatHelper.convertRupiahToInt(price)
    }
    catch(throwable: Throwable) {
        0
    }
}

internal fun Product.asObjectDataLayerImpressionAndClick(): Any {
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

internal fun Shop.getType(): String {
    return when {
        isOfficial -> "official_store"
        isGoldShop -> "gold_merchant"
        else -> "reguler"
    }
}

internal fun Product.asObjectDataLayerAddToCart(): Any {
    return DataLayer.mapOf(
            "name", name,
            "id", id,
            "price", safeCastRupiahToInt(price).toString(),
            "brand", NONE_OTHER,
            "category", categoryName,
            "variant", NONE_OTHER,
            "quantity", 0, // TODO:: Quantity
            "shop_id", shop.id,
            "shop_type", shop.getType(),
            "shop_name", shop.name,
            "category_id", categoryId,
            "dimension82", "" // TODO:: Cart Id
    )
}