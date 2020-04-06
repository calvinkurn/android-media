package com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout

data class CheckoutOccRequest(
        val profile: Profile = Profile(),
        val carts: ParamCart = ParamCart()
) {
    companion object {
        const val PARAM_PROFILE = "profile"
        const val PARAM_CARTS = "carts"
    }
}

data class Profile(
        val profileId: Int = 0
) {
    companion object {
        const val PARAM_PROFILE_ID = "profile_id"
    }
}

data class PromoRequest(
        val type: String = "",
        val code: String = ""
) {
    companion object {
        const val PARAM_TYPE = "type"
        const val PARAM_CODE = "code"
    }
}

data class ParamCart(
        val promos: List<PromoRequest> = emptyList(),
        val data: List<ParamData> = emptyList()
) {
    companion object {
        const val PARAM_PROMOS = "promos"
        const val PARAM_DATA = "data"
    }
}

data class ParamData(
        val addressId: Int = 0,
        val shopProducts: List<ShopProduct> = emptyList()
) {
    companion object {
        const val PARAM_ADDRESS_ID = "address_id"
        const val PARAM_SHOP_PRODUCTS = "shop_products"
    }
}

data class ShopProduct(
        val promos: List<PromoRequest> = emptyList(),
        val shopId: Int = 0,
        val productData: List<ProductData> = emptyList(),
        val isPreorder: Int = 0,
        val warehouseId: Int = 0,
        val finsurance: Int = 0,
        val shippingInfo: ShippingInfo = ShippingInfo()
) {
    companion object {
        const val PARAM_PROMOS = "promos"
        const val PARAM_SHOP_ID = "shop_id"
        const val PARAM_PRODUCT_DATA = "product_data"
        const val PARAM_WAREHOUSE_ID = "warehouse_id"
        const val PARAM_IS_PREORDER = "is_preorder"
        const val PARAM_FINSURANCE = "finsurance"
        const val PARAM_SHIPPING_INFO = "shipping_info"
    }
}

data class ProductData(
        val productId: Int = 0,
        val productQuantity: Int = 0,
        val productNotes: String = ""
) {
    companion object {
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_PRODUCT_QUANTITY = "product_quantity"
        const val PARAM_PRODUCT_NOTES = "product_notes"
    }
}

data class ShippingInfo(
        val shippingId: Int = 0,
        val spId: Int = 0,
        val ratesId: String = "",
        val ut: String = "",
        val checksum: String = ""
) {
    companion object {
        const val PARAM_SHIPPING_ID = "shipping_id"
        const val PARAM_SP_ID = "sp_id"
        const val PARAM_RATES_ID = "rates_id"
        const val PARAM_UT = "ut"
        const val PARAM_CHECKSUM = "checksum"
    }
}