package com.tokopedia.similarsearch.utils

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.getsimilarproducts.model.Shop
import com.tokopedia.similarsearch.tracking.ECommerce.Companion.NONE_OTHER

internal fun safeCastRupiahToInt(price: String?): Int {
    return try {
        convertRupiahToInt(price.orEmpty())
    }
    catch(throwable: Throwable) {
        0
    }
}

fun convertRupiahToInt(input: String): Int {
    var rupiah = input
    rupiah = rupiah.replace("Rp", "")
    rupiah = rupiah.replace(".", "")
    rupiah = rupiah.replace(" ", "")
    return rupiah.toIntOrZero()
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

internal fun Product.asObjectDataLayerAddToCart(cartId: String): Any {
    return DataLayer.mapOf(
            "name", name,
            "id", id,
            "price", safeCastRupiahToInt(price).toString(),
            "brand", NONE_OTHER,
            "category", categoryName,
            "variant", NONE_OTHER,
            "quantity", minOrder,
            "shop_id", shop.id,
            "shop_type", shop.getType(),
            "shop_name", shop.name,
            "category_id", categoryId,
            "dimension82", cartId
    )
}
