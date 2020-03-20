package com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics

class OrderSummaryPageEnhanceECommerce {

    var data: HashMap<String, Any> = HashMap()

    fun setName(name: String) {
        data[KEY_NAME] = name
    }

    fun setId(id: String) {
        data[KEY_ID] = id
    }

    fun setPrice(price: Int) {
        data[KEY_PRICE] = price
    }

    fun setBrand(brand: String?) {
        data[KEY_BRAND] = brand ?: "none/other"
    }

    fun setCategory(category: String) {
        data[KEY_CATEGORY] = category
    }

    fun setVariant(variant: String) {
        data[KEY_VARIANT] = variant
    }

    fun setQuantity(quantity: Int) {
        data[KEY_QUANTITY] = quantity
    }

    fun setListName(listName: String) {
        data[KEY_LIST_NAME] = listName
    }

    fun setAttribution(attribution: String) {
        data[KEY_ATTRIBUTION] = attribution
    }

    fun setKeyValue(key: String, value: Any) {
        data[key] = value
    }

    fun build(step: Int, option: String): Map<String, Any> {
        return mapOf(
                KEY_CHECKOUT to mapOf(
                        KEY_ACTION_FIELD to mapOf(
                                KEY_STEP to step,
                                KEY_OPTION to option
                        ),
                        KEY_CURRENCY_CODE to "IDR",
                        KEY_PRODUCTS to listOf(data)
                )
        )
    }

    companion object {

        private const val KEY_CHECKOUT = "checkout"
        private const val KEY_ACTION_FIELD = "actionField"
        private const val KEY_STEP = "step"
        private const val KEY_OPTION = "option"
        private const val KEY_CURRENCY_CODE = "currencyCode"
        private const val KEY_PRODUCTS = "products"
        private const val KEY_NAME = "name"
        private const val KEY_ID = "id"
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = "brand"
        private const val KEY_CATEGORY = "category"
        private const val KEY_VARIANT = "variant"
        private const val KEY_QUANTITY = "quantity"
        private const val KEY_LIST_NAME = "dimension40"
        private const val KEY_ATTRIBUTION = "dimension38"
        private const val KEY_DISCOUNTED_PRICE = "dimension53"
        private const val KEY_WAREHOUSE_ID = "dimension56"
        private const val KEY_PRODUCT_WEIGHT = "dimension48"
        private const val KEY_PROMO_CODE = "dimension49"
        private const val KEY_PROMO_DETAILS = "dimension59"
        private const val KEY_CART_ID = "dimension45"
        private const val KEY_BUYER_ADDRESS_ID = "dimension11"
        private const val KEY_SP_ID = "dimension14"
        private const val KEY_COD_FLAG = "dimension10"
        private const val KEY_TOKOPEDIA_CORNER_FLAG = "dimension57"
        private const val KEY_IS_FULFILLMENT = "dimension58"
        private const val KEY_SHOP_ID = "dimension79"
        private const val KEY_SHOP_NAME = "dimension80"
        private const val KEY_SHOP_TYPE = "dimension81" //marketplace or official_store or gold_merchant
        private const val KEY_CATEGORY_ID = "dimension82"
        private const val KEY_PRODUCT_TYPE = "dimension83"
    }
}