package com.tokopedia.cart.view.analytics

/**
 * Created by Irfan Khoirul on 2019-09-03.
 */

import java.util.*

class EnhancedECommerceProductData {
    private val product = HashMap<String, Any>()

    fun setProductName(name: String) {
        this.product.put(KEY_NAME, if (name.isNotBlank()) name else DEFAULT_VALUE_NONE_OTHER)
    }

    fun setProductID(id: String) {
        this.product.put(KEY_ID, if (id.isNotBlank()) id else DEFAULT_VALUE_NONE_OTHER)
    }

    fun setPrice(price: String) {
        this.product.put(KEY_PRICE, if (price.isNotBlank()) price else DEFAULT_VALUE_NONE_OTHER)
    }

    fun setBrand(brand: String) {
        this.product.put(KEY_BRAND, if (brand.isNotBlank()) brand else DEFAULT_VALUE_NONE_OTHER)
    }

    fun setVariant(variant: String) {
        this.product.put(KEY_VARIANT, if (variant.isNotBlank()) variant else DEFAULT_VALUE_NONE_OTHER)
    }

    fun setCategory(category: String) {
        this.product.put(KEY_CATEGORY, if (category.isNotBlank()) category else DEFAULT_VALUE_NONE_OTHER)
    }

    fun setPosition(position: String) {
        this.product.put(KEY_POSITION, if (position.isNotBlank()) position else DEFAULT_VALUE_NONE_OTHER)
    }

    fun setList(list: String) {
        this.product.put(KEY_LIST, if (list.isNotBlank()) list else DEFAULT_VALUE_NONE_OTHER)
    }

    fun getProduct(): Map<String, Any> {
        return this.product
    }

    companion object {
        private val KEY_NAME = "name"
        private val KEY_ID = "id"
        private val KEY_PRICE = "price"
        private val KEY_BRAND = "brand"
        private val KEY_CATEGORY = "category"
        private val KEY_VARIANT = "variant"
        private val KEY_LIST = "list"
        private val KEY_POSITION = "position"
        val DEFAULT_VALUE_NONE_OTHER = "none / other"
    }
}
