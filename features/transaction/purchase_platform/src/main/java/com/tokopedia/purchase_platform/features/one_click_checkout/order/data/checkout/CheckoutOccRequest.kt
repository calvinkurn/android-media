package com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout

data class CheckoutOccRequest(
        val profile: Profile = Profile(),
        val carts: ParamCart = ParamCart()
)

data class Profile(
        val profileId: Int = 0
)

data class PromoRequest(
        val type: String = "",
        val code: String = ""
)

data class ParamCart(
        val promos: List<PromoRequest> = emptyList(),
        val data: List<ParamData> = emptyList()
)

data class ParamData(
        val addressId: Int = 0,
        val shopProducts: List<ShopProduct> = emptyList()
)

data class ShopProduct(
        val promos: List<PromoRequest> = emptyList(),
        val shopId: Int = 0,
        val productData: List<ProductData> = emptyList(),
        val isPreorder: Int = 0,
        val warehouseId: Int = 0,
        val finsurance: Int = 0,
        val shippingInfo: ShippingInfo = ShippingInfo()
)

data class ProductData(
        val productId: Int = 0,
        val productQuantity: Int = 0,
        val productNotes: String = ""
)

data class ShippingInfo(
        val shippingId: Int = 0,
        val spId: Int = 0,
        val ratesId: String = "",
        val ut: String = "",
        val checksum: String = ""
)