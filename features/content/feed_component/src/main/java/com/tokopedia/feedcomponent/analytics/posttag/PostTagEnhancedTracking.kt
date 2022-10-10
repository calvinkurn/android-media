package com.tokopedia.feedcomponent.analytics.posttag

import com.tokopedia.analyticconstant.DataLayer
import java.util.ArrayList

/**
 * @author by yfsx on 04/04/19.
 */
class PostTagEnhancedTracking {

    public val ECOMMERCE = "ecommerce"

    object Action {
        public const val IMPRESSIONS = "impressions"
        public const val CLICK = "click"
    }

    object Ecommerce {
        public val CURRENCY_CODE = "currencyCode"
        public val CURRENCY_CODE_IDR = "IDR"
        private const val ACTION_FIELD = "actionField"
        private const val PRODUCTS = "products"

        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = "brand"
        private const val KEY_CATEGORY = "category"
        private const val KEY_VARIANT = "variant"
        private const val KEY_LIST = "list"
        private const val KEY_POSITION = "position"

        fun getEcommerceView(listProduct: List<Product>): Map<String, Any> {
            return DataLayer.mapOf(CURRENCY_CODE, CURRENCY_CODE_IDR,
                    Action.IMPRESSIONS, getProducts(listProduct))
        }

        fun getEcommerceClick(listProduct: List<Product>, listSource: String): Map<String, Any> {
            return DataLayer.mapOf(Action.CLICK, getEcommerceClickValue(listProduct, listSource))
        }

        fun getEcommerceClickValue(listProduct: List<Product>, listSource: String): Map<String, Any> {
            return DataLayer.mapOf(
                    ACTION_FIELD, getEcommerceActionFieldValue(listSource),
                    PRODUCTS, getProducts(listProduct))
        }

        fun getEcommerceActionFieldValue(listSource: String): Map<String, Any> {
            return DataLayer.mapOf(KEY_LIST, listSource)
        }

        fun getProducts(listProduct: List<Product>): List<Any> {
            val list = ArrayList<Map<String, Any>>()
            for (promo in listProduct) {
                val map = createProductMap(promo)
                list.add(map)
            }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        fun createProductMap(product: Product) : Map<String, Any> {
            val map = java.util.HashMap<String, Any>()
            map[KEY_ID] = product.id
            map[KEY_NAME] = product.name
            map[KEY_PRICE] = product.price
            map[KEY_BRAND] = product.brand
            map[KEY_CATEGORY] = product.category
            map[KEY_VARIANT] = product.variant
            map[KEY_LIST] = product.list
            map[KEY_POSITION] = product.position
            return map
        }
    }

    class Product(id: String, name: String, price: String, brand: String, category: String,
                    variant: String, list: String, position: Int) {
        var id: String = ""
            internal set
        var name: String
            internal set
        var price: String = ""
            internal set
        var brand: String
            internal set
        var category: String
            internal set
        var variant: String
            internal set
        var list: String
            internal set
        var position: Int
            internal set

        init {
            this.id = id
            this.name = name
            this.price = price
            this.brand = brand
            this.category = category
            this.variant = variant
            this.list = list
            this.position = position
        }
    }

}
