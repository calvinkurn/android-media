package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.data.model.response.WholesalePrice
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.CartDataResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProductDataResponse

data class OrderCart(
        var product: OrderProduct = OrderProduct(),
        var shop: OrderShop = OrderShop(),
        var kero: Kero = Kero()
)

data class Kero(
        var keroToken: String = "",
        var keroDiscomToken: String = "",
        var keroUT: String = ""
)

data class OrderShop(
        var shopId: Int = 0,
        var userId: Int = 0,
        var shopName: String = "",
        var shopImage: String = "",
        var shopUrl: String = "",
        var shopStatus: Int = 0,
        var isGold: Int = 0,
        var isGoldBadge: Boolean = false,
        var isOfficial: Int = 0,
        var isFreeReturns: Int = 0,
        var addressId: Int = 0,
        var postalCode: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var districtId: Int = 0,
        var districtName: String = "",
        var origin: Int = 0,
        var addressStreet: String = "",
        var provinceId: Int = 0,
        var cityId: Int = 0,
        var cityName: String = "",
        var shopShipment: List<ShopShipment> = emptyList(),
        var cartResponse: CartDataResponse = CartDataResponse()
)

data class OrderProduct(
        var parentId: Int = 0,
        var productId: Int = 0,
        var productName: String = "",
        var productPrice: Int = 0,
        var wholesalePrice: List<WholesalePrice> = arrayListOf(),
        var productImageUrl: String = "",
        var productChildrenList: ArrayList<OrderProductChild> = arrayListOf(),
        var selectedVariantOptionsIdMap: LinkedHashMap<Int, Int> = LinkedHashMap(),
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var originalPrice: Int = productPrice,
        var discountedPercentage: Float = 0f,
        var isFreeOngkir: Boolean = false,
        var freeOngkirImg: String = "",
        var weight: Int = 0,
        var quantity: QuantityUiModel? = null,
        var notes: String = "",
        var typeVariantList: ArrayList<VariantUiModel>? = null,
        var productResponse: ProductDataResponse = ProductDataResponse())

data class OrderProductChild(
        var productId: Int = 0,
        var productName: String = "",
        var productPrice: Int = 0,
        var productImageUrl: String = "",
        var isAvailable: Boolean = false,
        var isSelected: Boolean = false,
        var stockWording: String = "",
        var stock: Int = 0,
        var minOrder: Int = 0,
        var maxOrder: Int = 0,
        var optionsId: ArrayList<Int> = ArrayList()
)

data class QuantityUiModel(
        var stockWording: String = "",
        var maxOrderQuantity: Int = 0,
        var minOrderQuantity: Int = 0,
        var orderQuantity: Int = 0,
        var errorFieldBetween: String = "",
        var errorFieldMaxChar: String = "",
        var errorFieldRequired: String = "",
        var errorProductAvailableStock: String = "",
        var errorProductAvailableStockDetail: String = "",
        var errorProductMaxQuantity: String = "",
        var errorProductMinQuantity: String = "",
        var isStateError: Boolean = false,
        var stockFromWarehouse: Int = 0,
        var stockWordingFromWarehouse: String = ""
)