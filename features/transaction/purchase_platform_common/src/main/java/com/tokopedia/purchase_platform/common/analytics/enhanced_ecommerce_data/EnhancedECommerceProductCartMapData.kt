package com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data

/**
 * @author anggaprasetiyo on 05/06/18.
 */
class EnhancedECommerceProductCartMapData {

    private val product: MutableMap<String, Any> = HashMap()

    fun setProductName(name: String?) {
        product[KEY_NAME] = if (!name.isNullOrBlank()) name else DEFAULT_VALUE_NONE_OTHER
    }

    fun setProductID(id: String?) {
        product[KEY_ID] = if (!id.isNullOrBlank()) id else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPrice(price: String?) {
        product[KEY_PRICE] = if (!price.isNullOrBlank()) price else DEFAULT_VALUE_NONE_OTHER
    }

    fun setQty(qty: Int) {
        val qtyString = qty.toString()
        product[KEY_QTY] = if (qtyString.isNotBlank()) qtyString else DEFAULT_VALUE_NONE_OTHER
    }

    fun setCategory(category: String?) {
        product[KEY_CAT] = if (!category.isNullOrBlank()) category else DEFAULT_VALUE_NONE_OTHER
    }

    fun setAttribution(data: String?) {
        product[KEY_ATTRIBUTION] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension38(data: String?) {
        product[KEY_DIMENSION_38] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension83(data: String?) {
        product[KEY_DIMENSION_83] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension54(isFulfill: Boolean) {
        val data = if (isFulfill) "tokopedia" else "regular"
        product[KEY_DIMENSION_54] = data
    }

    fun setDimension80(data: String?) {
        product[KEY_DIMENSION_80] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setListName(data: String?) {
        product[KEY_LIST] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension40(data: String?) {
        product[KEY_DIMENSION_40] = if (!data.isNullOrBlank()) data else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPosition(position: String?) {
        product[KEY_POS] = if (!position.isNullOrBlank()) position else DEFAULT_VALUE_NONE_OTHER
    }

    fun setBrand(brand: String?) {
        product[KEY_BRAND] = brand ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun setVariant(variant: String?) {
        product[KEY_VARIANT] = variant ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun getProduct(): Map<String, Any> {
        return product
    }

    fun setShopId(shopId: String?) {
        product[KEY_SHOP_ID] = if (!shopId.isNullOrBlank()) shopId else DEFAULT_VALUE_NONE_OTHER
        product[KEY_SHOP_ID_DIMENSION] = if (!shopId.isNullOrBlank()) shopId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setShopType(shopType: String?) {
        product[KEY_SHOP_TYPE] = if (!shopType.isNullOrBlank()) shopType else VALUE_REGULER
        product[KEY_SHOP_TYPE_DIMENSION] = if (!shopType.isNullOrBlank()) shopType else VALUE_REGULER
    }

    fun setShopName(shopName: String?) {
        product[KEY_SHOP_NAME] = if (!shopName.isNullOrBlank()) shopName else DEFAULT_VALUE_NONE_OTHER
        product[KEY_SHOP_NAME_DIMENSION] = if (!shopName.isNullOrBlank()) shopName else DEFAULT_VALUE_NONE_OTHER
    }

    fun setCategoryId(categoryId: String?) {
        product[KEY_CATEGORY_ID] = categoryId ?: DEFAULT_VALUE_NONE_OTHER
    }

    fun setCartId(cartId: String?) {
        product[KEY_CART_ID] = if (!cartId.isNullOrBlank()) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension45(cartId: String?) {
        product[KEY_DIMENSION_45] = if (!cartId.isNullOrBlank()) cartId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setDimension12(shippingCourierPrice: String?) {
        product[KEY_DIMENSION_12] = if (!shippingCourierPrice.isNullOrBlank()) shippingCourierPrice else DEFAULT_VALUE_NONE_OTHER
    }

    fun setWarehouseId(warehouseId: String?) {
        product[KEY_WAREHOUSE_ID] = if (!warehouseId.isNullOrBlank()) warehouseId else DEFAULT_VALUE_NONE_OTHER
    }

    fun setProductWeight(productWeight: String?) {
        product[KEY_PRODUCT_WEIGHT] = if (!productWeight.isNullOrBlank()) productWeight else DEFAULT_VALUE_NONE_OTHER
    }

    fun setPromoCode(promoCodes: String?) {
        product[KEY_PROMO_CODE] = if (!promoCodes.isNullOrBlank()) promoCodes else ""
    }

    fun setPromoDetails(promoDetails: String?) {
        product[KEY_PROMO_DETAILS] = if (!promoDetails.isNullOrBlank()) promoDetails else ""
    }

    fun setBuyerAddressId(buyerAddressId: String?) {
        product[KEY_BUYER_ADDRESS_ID] = if (!buyerAddressId.isNullOrBlank()) buyerAddressId else ""
    }

    fun setShippingDuration(shippingDuration: String?) {
        product[KEY_SHIPPING_DURATION] = if (!shippingDuration.isNullOrBlank()) shippingDuration else ""
    }

    fun setCourier(courier: String?) {
        product[KEY_COURIER] = if (!courier.isNullOrBlank()) courier else ""
    }

    fun setShippingPrice(shippingPrice: String?) {
        product[KEY_SHIPPING_PRICE] = if (!shippingPrice.isNullOrBlank()) shippingPrice else ""
    }

    fun setCodFlag(codFlag: String?) {
        product[KEY_COD_FLAG] = if (!codFlag.isNullOrBlank()) codFlag else ""
    }

    fun setTokopediaCornerFlag(tokopediaCornerFlag: String?) {
        product[KEY_TOKOPEDIA_CORNER_FLAG] = if (!tokopediaCornerFlag.isNullOrBlank()) tokopediaCornerFlag else ""
    }

    fun setIsFulfillment(isFulfillment: String?) {
        product[KEY_IS_FULFILLMENT] = if (!isFulfillment.isNullOrBlank()) isFulfillment else ""
    }

    fun setPicture(picture: String?) {
        product[KEY_PICTURE] = if (!picture.isNullOrBlank()) picture else ""
    }

    fun setUrl(url: String?) {
        product[KEY_URL] = if (!url.isNullOrBlank()) url else ""
    }

    fun setDimension52(shopId: String?) {
        product[KEY_DIMENSION_52] = if (!shopId.isNullOrBlank()) shopId else ""
    }

    fun setDimension53(isDiscountPrice: Boolean) {
        product[KEY_DIMENSION_53] = isDiscountPrice.toString()
    }

    fun setDimension56(warehouseId: String?) {
        product[KEY_DIMENSION_56] = if (!warehouseId.isNullOrBlank()) warehouseId else ""
    }

    fun setDimension57(shopName: String?) {
        product[KEY_DIMENSION_57] = if (!shopName.isNullOrBlank()) shopName else ""
    }

    fun setDimension58(isFulfillment: Boolean) {
        product[KEY_DIMENSION_58] = isFulfillment.toString()
    }

    fun setDimension59(shopType: String?) {
        product[KEY_DIMENSION_59] = if (!shopType.isNullOrBlank()) shopType else ""
    }

    fun setDimension82(categoryId: String?) {
        product[KEY_DIMENSION_82] = if (!categoryId.isNullOrBlank()) categoryId else ""
    }

    fun setDimension73(productTagInfo: String?) {
        product[KEY_DIMENSION_73] = if (!productTagInfo.isNullOrBlank()) productTagInfo else ""
    }

    fun setDimension77(cartId: String?) {
        product[KEY_DIMENSION_77] = if (!cartId.isNullOrBlank()) cartId else ""
    }

    fun setCampaignId(campaignId: String) {
        product[KEY_CAMPAIGN_ID] = campaignId
    }

    fun setPageSource(pageSource: String) {
        product[KEY_PAGE_SOURCE] = pageSource
    }

    fun setDimension117(bundleType: String) {
        product[KEY_DIMENSION117] = bundleType
    }

    fun setDimension118(bundleId: String) {
        if (bundleId == "0") {
            product[KEY_DIMENSION118] = ""
        } else {
            product[KEY_DIMENSION118] = bundleId
        }
    }

    fun setBoAffordability(boAffordabilityValue: String) {
        product[KEY_BO_AFFORDABILITY] = boAffordabilityValue
    }

    fun setDimension136(cartStringGroup: String) {
        product[KEY_DIMENSION136] = cartStringGroup
    }

    fun setDimension137(offerId: String) {
        product[KEY_DIMENSION137] = offerId
    }

    fun setDimension98(productLabelType: String) {
        product[KEY_DIMENSION98] = productLabelType
    }

    fun setImpressionAlgorithm(value: String?) {
        product[KEY_IMPRESSION_ALGORITHM] = value.orEmpty()
    }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_ID = "id"
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = "brand"
        const val KEY_CAT = "category"
        private const val KEY_VARIANT = "variant"
        private const val KEY_QTY = "quantity"
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SHOP_ID_DIMENSION = "dimension79"
        private const val KEY_SHOP_TYPE = "shop_type"
        private const val KEY_SHOP_TYPE_DIMENSION = "dimension81"
        private const val KEY_SHOP_NAME = "shop_name"
        private const val KEY_SHOP_NAME_DIMENSION = "dimension80"
        private const val KEY_CATEGORY_ID = "dimension82"
        private const val KEY_CART_ID = "dimension45"
        private const val KEY_POS = "position"
        const val KEY_LIST = "list"
        const val KEY_DIMENSION_38 = "dimension38"
        const val KEY_DIMENSION_40 = "dimension40"
        private const val KEY_DIMENSION_45 = "dimension45"
        private const val KEY_DIMENSION_54 = "dimension54"
        private const val KEY_DIMENSION_52 = "dimension52"
        private const val KEY_DIMENSION_53 = "dimension53"
        private const val KEY_DIMENSION_56 = "dimension56"
        private const val KEY_DIMENSION_57 = "dimension57"
        private const val KEY_DIMENSION_58 = "dimension58"
        private const val KEY_DIMENSION_59 = "dimension59"
        private const val KEY_DIMENSION_73 = "dimension73"
        private const val KEY_DIMENSION_77 = "dimension77"
        private const val KEY_DIMENSION_80 = "dimension80"
        private const val KEY_DIMENSION_82 = "dimension82"
        private const val KEY_DIMENSION_83 = "dimension83"
        private const val KEY_DIMENSION_12 = "dimension12"
        const val KEY_ATTRIBUTION = "attribution"
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
        private const val KEY_PAGE_SOURCE = "dimension90"
        private const val KEY_DIMENSION117 = "dimension117"
        private const val KEY_DIMENSION118 = "dimension118"
        private const val KEY_BO_AFFORDABILITY = "dimension119"
        private const val KEY_DIMENSION136 = "dimension136"
        private const val KEY_DIMENSION137 = "dimension137"
        private const val KEY_DIMENSION98 = "dimension98"
        private const val KEY_IMPRESSION_ALGORITHM = "dimension96"
        const val DEFAULT_VALUE_NONE_OTHER = "none/other"
        const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        const val VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir extra"
        const val RECOMMENDATION_ATTRIBUTION = "recommendation"
        const val VALUE_REGULER = "reguler"
    }
}
