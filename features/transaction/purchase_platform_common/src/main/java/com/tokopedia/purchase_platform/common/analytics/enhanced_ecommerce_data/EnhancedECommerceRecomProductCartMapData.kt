package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import android.text.TextUtils
import java.util.*

class EnhancedECommerceRecomProductCartMapData {

    private val Product = HashMap<String, Any>()

    val SHOP_TYPE_REGULER = "reguler"
    val SHOP_TYPE_OFFICIAL_STORE = "official_store"
    val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"

    private val KEY_NAME = "name"
    private val KEY_ID = "id"
    private val KEY_PRICE = "price"
    private val KEY_BRAND = "brand"
    private val KEY_CAT = "category"
    private val KEY_CAT_ID = "category_id"
    private val KEY_VARIANT = "variant"
    private val KEY_QTY = "quantity"
    private val KEY_SHOP_ID = "shop_id"
    private val KEY_SHOP_TYPE = "shop_type"
    private val KEY_SHOP_NAME = "shop_name"
    private val KEY_CATEGORY_ID = "category_id"
    private val KEY_CART_ID = "dimension45"
    private val KEY_POS = "position"
    private val KEY_LIST = "list"
    private val KEY_DIMENSION_38 = "dimension38"
    private val KEY_DIMENSION_40 = "dimension40"
    private val KEY_DIMENSION_45 = "dimension45"
    private val KEY_DIMENSION_54 = "dimension54"
    private val KEY_DIMENSION_52 = "dimension52"
    private val KEY_DIMENSION_53 = "dimension53"
    private val KEY_DIMENSION_57 = "dimension57"
    private val KEY_DIMENSION_59 = "dimension59"
    private val KEY_DIMENSION_77 = "dimension77"
    private val KEY_DIMENSION_80 = "dimension80"
    private val KEY_DIMENSION_83 = "dimension83"
    private val KEY_DIMENSION_12 = "dimension12"
    private val KEY_ATTRIBUTION = "attribution"
    private val KEY_WAREHOUSE_ID = "dimension56"
    private val KEY_PRODUCT_WEIGHT = "dimension48"
    private val KEY_PROMO_CODE = "dimension49"
    private val KEY_PROMO_DETAILS = "dimension59"
    private val KEY_BUYER_ADDRESS_ID = "dimension11"
    private val KEY_SHIPPING_DURATION = "dimension16"
    private val KEY_COURIER = "dimension14"
    private val KEY_SHIPPING_PRICE = "dimension12"
    private val KEY_COD_FLAG = "dimension10"
    private val KEY_TOKOPEDIA_CORNER_FLAG = "dimension57"
    private val KEY_IS_FULFILLMENT = "dimension58"
    private val KEY_PICTURE = "picture"
    private val KEY_URL = "url"

    val DEFAULT_VALUE_NONE_OTHER = "none/other"
    val VALUE_BEBAS_ONGKIR = "bebas ongkir"

    val RECOMMENDATION_ATTRIBUTION = "recommendation"

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
//        val qtyString = qty.toString()
        Product[KEY_QTY] = qty
    }

    fun setCategory(category: String) {
        Product[KEY_CAT] = if (!TextUtils.isEmpty(category)) category else DEFAULT_VALUE_NONE_OTHER
    }

    fun setAttribution(data: String) {
        Product[KEY_ATTRIBUTION] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension38(data: String) {
        Product[KEY_DIMENSION_38] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension83(data: String) {
        Product[KEY_DIMENSION_83] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension54(isFulfill: Boolean) {
        val data = if (isFulfill) "tokopedia" else "regular"
        Product[KEY_DIMENSION_54] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension80(data: String) {
        Product[KEY_DIMENSION_80] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setListName(data: String) {
        Product[KEY_LIST] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension40(data: String) {
        Product[KEY_DIMENSION_40] = if (!TextUtils.isEmpty(data)) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPosition(position: String) {
        Product[KEY_POS] = if (!TextUtils.isEmpty(position)) position else DEFAULT_VALUE_NONE_OTHER
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

    fun setCartId(cartId: String) {
        Product[KEY_CART_ID] = if (!TextUtils.isEmpty(cartId)) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension45(cartId: String) {
        Product[KEY_DIMENSION_45] = if (!TextUtils.isEmpty(cartId)) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension12(shippingCourierPrice: String) {
        Product[KEY_DIMENSION_12] = if (!TextUtils.isEmpty(shippingCourierPrice)) shippingCourierPrice else DEFAULT_VALUE_NONE_OTHER
    }

    fun setWarehouseId(warehouseId: String) {
        Product[KEY_WAREHOUSE_ID] = if (!TextUtils.isEmpty(warehouseId)) warehouseId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setProductWeight(productWeight: String) {
        Product[KEY_PRODUCT_WEIGHT] = if (!TextUtils.isEmpty(productWeight)) productWeight else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPromoCode(promoCodes: String) {
        Product[KEY_PROMO_CODE] = if (!TextUtils.isEmpty(promoCodes)) promoCodes else ""
    }

    fun setPromoDetails(promoDetails: String) {
        Product[KEY_PROMO_DETAILS] = if (!TextUtils.isEmpty(promoDetails)) promoDetails else ""
    }

    fun setBuyerAddressId(buyerAddressId: String) {
        Product[KEY_BUYER_ADDRESS_ID] = if (!TextUtils.isEmpty(buyerAddressId)) buyerAddressId else ""
    }

    fun setShippingDuration(shippingDuration: String) {
        Product[KEY_SHIPPING_DURATION] = if (!TextUtils.isEmpty(shippingDuration)) shippingDuration else ""
    }

    fun setCourier(courier: String) {
        Product[KEY_COURIER] = if (!TextUtils.isEmpty(courier)) courier else ""
    }

    fun setShippingPrice(shippingPrice: String) {
        Product[KEY_SHIPPING_PRICE] = if (!TextUtils.isEmpty(shippingPrice)) shippingPrice else ""
    }

    fun setCodFlag(codFlag: String) {
        Product[KEY_COD_FLAG] = if (!TextUtils.isEmpty(codFlag)) codFlag else ""
    }

    fun setTokopediaCornerFlag(tokopediaCornerFlag: String) {
        Product[KEY_TOKOPEDIA_CORNER_FLAG] = if (!TextUtils.isEmpty(tokopediaCornerFlag)) tokopediaCornerFlag else ""
    }

    fun setIsFulfillment(isFulfillment: String) {
        Product[KEY_IS_FULFILLMENT] = if (!TextUtils.isEmpty(isFulfillment)) isFulfillment else ""
    }

    fun setPicture(picture: String) {
        Product[KEY_PICTURE] = if (!TextUtils.isEmpty(picture)) picture else ""
    }

    fun setUrl(url: String) {
        Product[KEY_URL] = if (!TextUtils.isEmpty(url)) url else ""
    }

    fun setDimension52(shopId: String) {
        Product[KEY_DIMENSION_52] = if (!TextUtils.isEmpty(shopId)) shopId else ""
    }

    fun setDimension53(isDiscountPrice: Boolean) {
        Product[KEY_DIMENSION_53] = isDiscountPrice.toString()
    }

    fun setDimension57(shopName: String) {
        Product[KEY_DIMENSION_57] = if (!TextUtils.isEmpty(shopName)) shopName else ""
    }

    fun setDimension59(shopType: String) {
        Product[KEY_DIMENSION_59] = if (!TextUtils.isEmpty(shopType)) shopType else ""
    }

    fun setDimension77(cartId: String) {
        Product[KEY_DIMENSION_77] = if (!TextUtils.isEmpty(cartId)) cartId else ""
    }

}