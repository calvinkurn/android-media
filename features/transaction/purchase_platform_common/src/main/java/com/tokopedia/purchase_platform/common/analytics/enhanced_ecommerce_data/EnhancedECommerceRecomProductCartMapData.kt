package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import android.text.TextUtils
import java.util.*

class EnhancedECommerceRecomProductCartMapData {

    private val Product = HashMap<String, Any>()

    private val KEY_NAME = "name"
    private val KEY_ID = "id"
    private val KEY_PRICE = "price"
    private val KEY_BRAND = "brand"
    private val KEY_CAT = "category"
    private val KEY_VARIANT = "variant"
    private val KEY_QTY = "quantity"
    private val KEY_SHOP_ID = "shop_id"
    private val KEY_SHOP_TYPE = "shop_type"
    private val KEY_SHOP_NAME = "shop_name"
    private val KEY_CATEGORY_ID = "category_id"
    private val KEY_DIMENSION_40 = "dimension40"
    private val KEY_DIMENSION_45 = "dimension45"
    private val KEY_DIMENSION_53 = "dimension53"
    private val KEY_DIMENSION_83 = "dimension83"

    val DEFAULT_VALUE_NONE_OTHER = "none/other"
    val VALUE_BEBAS_ONGKIR = "bebas ongkir"

    fun setProductName(name: String) {
        Product[KEY_NAME] = if (!TextUtils.isEmpty(name)) name else DEFAULT_VALUE_NONE_OTHER
    }

    fun setProductID(id: String) {
        Product[KEY_ID] = if (!TextUtils.isEmpty(id)) id else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPrice(price: String) {
        Product[KEY_PRICE] = if (!TextUtils.isEmpty(price)) price else DEFAULT_VALUE_NONE_OTHER
    }

    fun setQty(qty: Int) {
        Product[KEY_QTY] = qty.coerceAtLeast(1)
    }

    fun setCategory(category: String) {
        Product[KEY_CAT] = if (!TextUtils.isEmpty(category)) category else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension83(data: String) {
        Product[KEY_DIMENSION_83] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension40(data: String) {
        Product[KEY_DIMENSION_40] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
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
        Product[KEY_SHOP_ID] = if (!TextUtils.isEmpty(shopId)) shopId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setShopType(shopType: String) {
        Product[KEY_SHOP_TYPE] = if (!TextUtils.isEmpty(shopType)) shopType else DEFAULT_VALUE_NONE_OTHER
    }

    fun setShopName(shopName: String) {
        Product[KEY_SHOP_NAME] = if (!TextUtils.isEmpty(shopName)) shopName else DEFAULT_VALUE_NONE_OTHER
    }

    fun setCategoryId(categoryId: String?) {
        Product[KEY_CATEGORY_ID] = categoryId ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension45(cartId: String) {
        Product[KEY_DIMENSION_45] = if (!TextUtils.isEmpty(cartId)) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension53(isDiscountPrice: Boolean) {
        Product[KEY_DIMENSION_53] = isDiscountPrice.toString()
    }

}