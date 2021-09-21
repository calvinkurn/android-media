package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import android.text.TextUtils
import java.util.*

class EnhancedECommerceRecomProductCartMapData {

    companion object {
        const val KEY_NAME = "name"
        const val KEY_ID = "id"
        const val KEY_PRICE = "price"
        const val KEY_BRAND = "brand"
        const val KEY_CAT = "category"
        const val KEY_VARIANT = "variant"
        const val KEY_QTY = "quantity"
        const val KEY_SHOP_ID = "shop_id"
        const val KEY_SHOP_TYPE = "shop_type"
        const val KEY_SHOP_NAME = "shop_name"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_DIMENSION_40 = "dimension40"
        const val KEY_DIMENSION_45 = "dimension45"
        const val KEY_DIMENSION_53 = "dimension53"
        const val KEY_DIMENSION_83 = "dimension83"

        val DEFAULT_VALUE_NONE_OTHER = "none/other"
        val VALUE_BEBAS_ONGKIR = "bebas ongkir"
    }

    private val Product = HashMap<String, Any>()

    fun setProductName(name: String) {
        Product[KEY_NAME] = if (name.isNotBlank()) name else DEFAULT_VALUE_NONE_OTHER
    }

    fun setProductID(id: String) {
        Product[KEY_ID] = if (id.isNotBlank()) id else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPrice(price: String) {
        Product[KEY_PRICE] = if (price.isNotBlank()) price else DEFAULT_VALUE_NONE_OTHER
    }

    fun setQty(qty: Int) {
        Product[KEY_QTY] = qty.coerceAtLeast(1)
    }

    fun setCategory(category: String) {
        Product[KEY_CAT] = if (category.isNotBlank()) category else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension83(data: String) {
        Product[KEY_DIMENSION_83] = if (data.isNotBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension40(data: String) {
        Product[KEY_DIMENSION_40] = if (data.isNotBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setBrand(brand: String?) {
        Product[KEY_BRAND] = brand ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun setVariant(variant: String?) {
        Product[KEY_VARIANT] = variant ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun getProduct(): Map<String, Any> {
        return Product
    }

    fun setShopId(shopId: String) {
        Product[KEY_SHOP_ID] = if (shopId.isNotBlank()) shopId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setShopType(shopType: String) {
        Product[KEY_SHOP_TYPE] = if (shopType.isNotBlank()) shopType else DEFAULT_VALUE_NONE_OTHER
    }

    fun setShopName(shopName: String) {
        Product[KEY_SHOP_NAME] = if (shopName.isNotBlank()) shopName else DEFAULT_VALUE_NONE_OTHER
    }

    fun setCategoryId(categoryId: String?) {
        Product[KEY_CATEGORY_ID] = categoryId ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension45(cartId: String) {
        Product[KEY_DIMENSION_45] = if (cartId.isNotBlank()) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension53(isDiscountPrice: Boolean) {
        Product[KEY_DIMENSION_53] = isDiscountPrice.toString()
    }

}