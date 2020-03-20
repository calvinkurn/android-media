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

    fun setDiscountedPrice(discountedPrice: Int) {
        data[KEY_DISCOUNTED_PRICE] = discountedPrice
    }

    fun setWarehouseId(warehouseId: Int) {
        data[KEY_WAREHOUSE_ID] = warehouseId
    }

    fun setProductWeight(productWeight: Int) {
        data[KEY_PRODUCT_WEIGHT] = productWeight
    }

    fun setPromoCode(promoCode: String) {
        data[KEY_PROMO_CODE] = promoCode
    }

    fun setPromoDetails(promoDetails: String) {
        data[KEY_PROMO_DETAILS] = promoDetails
    }

    fun setCartId(cartId: String) {
        data[KEY_CART_ID] = cartId
    }

    fun setBuyerAddressId(buyerAddressId: String) {
        data[KEY_BUYER_ADDRESS_ID] = buyerAddressId
    }

    fun setSpid(spid: String) {
        data[KEY_SP_ID] = spid
    }

    fun setCodFlag(codFlag: Boolean) {
        data[KEY_COD_FLAG] = codFlag
    }

    fun setCornerFlag(cornerFlag: Boolean) {
        data[KEY_TOKOPEDIA_CORNER_FLAG] = cornerFlag
    }

    fun setIsFullfilment(isFullfilment: Boolean) {
        data[KEY_IS_FULFILLMENT] = isFullfilment
    }

    fun setShopId(shopId: Int) {
        data[KEY_SHOP_ID] = shopId
    }

    fun setShopName(shopName: String) {
        data[KEY_SHOP_NAME] = shopName
    }

    fun setShopType(shopType: String) {
        data[KEY_SHOP_TYPE] = shopType
    }

    fun setCategoryId(categoryId: Int) {
        data[KEY_CATEGORY_ID] = categoryId
    }

    fun setProductType(productType: String) {
        data[KEY_PRODUCT_TYPE] = productType
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