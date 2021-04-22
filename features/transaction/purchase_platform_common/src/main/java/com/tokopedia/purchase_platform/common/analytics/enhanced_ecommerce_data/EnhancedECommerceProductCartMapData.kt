package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

import java.util.*

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class EnhancedECommerceProductCartMapData {
    private val Product: MutableMap<String, Any> = HashMap()
    fun setProductName(name: String?) {
        Product[KEY_NAME] = if (!name.isNullOrBlank()) name else DEFAULT_VALUE_NONE_OTHER
    }

    fun setProductID(id: String?) {
        Product[KEY_ID] = if (!id.isNullOrBlank()) id else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPrice(price: String?) {
        Product[KEY_PRICE] = if (!price.isNullOrBlank()) price else DEFAULT_VALUE_NONE_OTHER
    }

    fun setQty(qty: Int) {
        val qtyString = qty.toString()
        Product[KEY_QTY] = if (qtyString.isNotBlank()) qtyString else DEFAULT_VALUE_NONE_OTHER
    }

    fun setCategory(category: String?) {
        Product[KEY_CAT] = if (!category.isNullOrBlank()) category else DEFAULT_VALUE_NONE_OTHER
    }

    fun setAttribution(data: String?) {
        Product[KEY_ATTRIBUTION] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension38(data: String?) {
        Product[KEY_DIMENSION_38] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension83(data: String?) {
        Product[KEY_DIMENSION_83] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension54(isFulfill: Boolean) {
        val data = if (isFulfill) "tokopedia" else "regular"
        Product[KEY_DIMENSION_54] = data
    }

    fun setDimension80(data: String?) {
        Product[KEY_DIMENSION_80] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setListName(data: String?) {
        Product[KEY_LIST] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension40(data: String?) {
        Product[KEY_DIMENSION_40] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPosition(position: String?) {
        Product[KEY_POS] = if (!position.isNullOrBlank()) position else DEFAULT_VALUE_NONE_OTHER
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

    fun setShopId(shopId: String?) {
        Product[KEY_SHOP_ID] = if (!shopId.isNullOrBlank()) shopId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setShopType(shopType: String?) {
        Product[KEY_SHOP_TYPE] = if (!shopType.isNullOrBlank()) shopType else DEFAULT_VALUE_NONE_OTHER
    }

    fun setShopName(shopName: String?) {
        Product[KEY_SHOP_NAME] = if (!shopName.isNullOrBlank()) shopName else DEFAULT_VALUE_NONE_OTHER
    }

    fun setCategoryId(categoryId: String?) {
        Product[KEY_CATEGORY_ID] = categoryId ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun setCartId(cartId: String?) {
        Product[KEY_CART_ID] = if (!cartId.isNullOrBlank()) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension45(cartId: String?) {
        Product[KEY_DIMENSION_45] = if (!cartId.isNullOrBlank()) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension12(shippingCourierPrice: String?) {
        Product[KEY_DIMENSION_12] = if (!shippingCourierPrice.isNullOrBlank()) shippingCourierPrice else DEFAULT_VALUE_NONE_OTHER
    }

    fun setWarehouseId(warehouseId: String?) {
        Product[KEY_WAREHOUSE_ID] = if (!warehouseId.isNullOrBlank()) warehouseId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setProductWeight(productWeight: String?) {
        Product[KEY_PRODUCT_WEIGHT] = if (!productWeight.isNullOrBlank()) productWeight else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPromoCode(promoCodes: String?) {
        Product[KEY_PROMO_CODE] = if (!promoCodes.isNullOrBlank()) promoCodes else ""
    }

    fun setPromoDetails(promoDetails: String?) {
        Product[KEY_PROMO_DETAILS] = if (!promoDetails.isNullOrBlank()) promoDetails else ""
    }

    fun setBuyerAddressId(buyerAddressId: String?) {
        Product[KEY_BUYER_ADDRESS_ID] = if (!buyerAddressId.isNullOrBlank()) buyerAddressId else ""
    }

    fun setShippingDuration(shippingDuration: String?) {
        Product[KEY_SHIPPING_DURATION] = if (!shippingDuration.isNullOrBlank()) shippingDuration else ""
    }

    fun setCourier(courier: String?) {
        Product[KEY_COURIER] = if (!courier.isNullOrBlank()) courier else ""
    }

    fun setShippingPrice(shippingPrice: String?) {
        Product[KEY_SHIPPING_PRICE] = if (!shippingPrice.isNullOrBlank()) shippingPrice else ""
    }

    fun setCodFlag(codFlag: String?) {
        Product[KEY_COD_FLAG] = if (!codFlag.isNullOrBlank()) codFlag else ""
    }

    fun setTokopediaCornerFlag(tokopediaCornerFlag: String?) {
        Product[KEY_TOKOPEDIA_CORNER_FLAG] = if (!tokopediaCornerFlag.isNullOrBlank()) tokopediaCornerFlag else ""
    }

    fun setIsFulfillment(isFulfillment: String?) {
        Product[KEY_IS_FULFILLMENT] = if (!isFulfillment.isNullOrBlank()) isFulfillment else ""
    }

    fun setPicture(picture: String?) {
        Product[KEY_PICTURE] = if (!picture.isNullOrBlank()) picture else ""
    }

    fun setUrl(url: String?) {
        Product[KEY_URL] = if (!url.isNullOrBlank()) url else ""
    }

    fun setDimension52(shopId: String?) {
        Product[KEY_DIMENSION_52] = if (!shopId.isNullOrBlank()) shopId else ""
    }

    fun setDimension53(isDiscountPrice: Boolean) {
        Product[KEY_DIMENSION_53] = isDiscountPrice.toString()
    }

    fun setDimension57(shopName: String?) {
        Product[KEY_DIMENSION_57] = if (!shopName.isNullOrBlank()) shopName else ""
    }

    fun setDimension59(shopType: String?) {
        Product[KEY_DIMENSION_59] = if (!shopType.isNullOrBlank()) shopType else ""
    }

    fun setDimension77(cartId: String?) {
        Product[KEY_DIMENSION_77] = if (!cartId.isNullOrBlank()) cartId else ""
    }

    fun setCampaignId(campaignId: String) {
        Product[KEY_CAMPAIGN_ID] = campaignId
    }

    companion object {
        const val SHOP_TYPE_REGULER = "reguler"
        const val SHOP_TYPE_OFFICIAL_STORE = "official_store"
        const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
        private const val KEY_NAME = "name"
        private const val KEY_ID = "id"
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = "brand"
        private const val KEY_CAT = "category"
        private const val KEY_VARIANT = "variant"
        private const val KEY_QTY = "quantity"
        private const val KEY_SHOP_ID = "dimension79"
        private const val KEY_SHOP_TYPE = "dimension81"
        private const val KEY_SHOP_NAME = "dimension80"
        private const val KEY_CATEGORY_ID = "dimension82"
        private const val KEY_CART_ID = "dimension45"
        private const val KEY_POS = "position"
        const val KEY_LIST = "list"
        private const val KEY_DIMENSION_38 = "dimension38"
        private const val KEY_DIMENSION_40 = "dimension40"
        private const val KEY_DIMENSION_45 = "dimension45"
        private const val KEY_DIMENSION_54 = "dimension54"
        private const val KEY_DIMENSION_52 = "dimension52"
        private const val KEY_DIMENSION_53 = "dimension53"
        private const val KEY_DIMENSION_57 = "dimension57"
        private const val KEY_DIMENSION_59 = "dimension59"
        private const val KEY_DIMENSION_77 = "dimension77"
        private const val KEY_DIMENSION_80 = "dimension80"
        private const val KEY_DIMENSION_83 = "dimension83"
        private const val KEY_DIMENSION_12 = "dimension12"
        private const val KEY_ATTRIBUTION = "attribution"
        private const val KEY_WAREHOUSE_ID = "dimension56"
        private const val KEY_PRODUCT_WEIGHT = "dimension48"
        private const val KEY_PROMO_CODE = "dimension49"
        private const val KEY_PROMO_DETAILS = "dimension59"
        private const val KEY_BUYER_ADDRESS_ID = "dimension11"
        private const val KEY_SHIPPING_DURATION = "dimension16"
        private const val KEY_COURIER = "dimension14"
        private const val KEY_SHIPPING_PRICE = "dimension12"
        private const val KEY_COD_FLAG = "dimension10"
        private const val KEY_TOKOPEDIA_CORNER_FLAG = "dimension57"
        private const val KEY_IS_FULFILLMENT = "dimension58"
        private const val KEY_PICTURE = "picture"
        private const val KEY_URL = "url"
        private const val KEY_CAMPAIGN_ID = "dimension104"
        const val DEFAULT_VALUE_NONE_OTHER = "none/other"
        const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        const val VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir extra"
        const val RECOMMENDATION_ATTRIBUTION = "recommendation"
    }
}