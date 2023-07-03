package com.tokopedia.checkout.data.model.request.checkout

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellRequest
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics

const val FEATURE_TYPE_REGULAR_PRODUCT = 3
const val FEATURE_TYPE_TOKONOW_PRODUCT = 12

data class CheckoutRequest(
    @SerializedName("carts")
    val carts: Carts,
    @SerializedName("is_one_click_shipment")
    val isOneClickShipment: String,
    @SerializedName("dynamic_data")
    val dynamicData: String,
    @SerializedName("is_trade_in")
    val isTradeIn: Boolean,
    @SerializedName("is_trade_in_drop_off")
    val isTradeInDropOff: Boolean,
    @SerializedName("dev_id")
    val devId: String,
    @SerializedName("optional")
    val optional: Int,
    @SerializedName("is_thankyou_native_new")
    val isThankyouNativeNew: Boolean,
    @SerializedName("is_thankyou_native")
    val isThankyouNative: Boolean,
    @SerializedName("is_express")
    val isExpress: Boolean,
    @SerializedName("fingerprint_support")
    val fingerprintSupport: String,
    @SerializedName("fingerprint_publickey")
    val fingerprintPublickey: String
)

data class Carts(
    @SerializedName("has_promo_stacking")
    var hasPromoStacking: Boolean = false,
    @SerializedName("is_donation")
    var isDonation: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("leasing_id")
    var leasingId: Long = 0,
    @SerializedName("promos")
    var promos: List<Promo> = emptyList(),
    @SerializedName("egold_data")
    var egold: Egold = Egold(),
    @SerializedName("tokopedia_corner_data")
    var tokopediaCorner: TokopediaCorner? = null,
    @SerializedName("feature_type")
    var featureType: Int = 0,
    @SerializedName("cross_sell")
    var crossSell: CrossSellRequest? = null,
    @SerializedName("data")
    var data: List<Data> = emptyList()
) {
    val protectionAnalyticsData: ArrayList<String>
        get() {
            val pppLabelList = arrayListOf<String>()
            data.forEach { data ->
                data.groupOrders.forEach { groupOrder ->
                    groupOrder.shopOrders.forEach { shopOrders ->
                        shopOrders.bundle.forEach { bundle ->
                            bundle.productData.forEach { product ->
                                if (product.isProtectionAvailable) {
                                    val eventLabel = if (product.isPpp) {
                                        ConstantTransactionAnalytics.EventLabel.SUCCESS_TICKED_PPP
                                    } else {
                                        ConstantTransactionAnalytics.EventLabel.SUCCESS_UNTICKED_PPP
                                    }
                                    pppLabelList.add(
                                        "${product.protectionTitle} - $eventLabel - ${product.productCategoryId} - ${product.protectionPricePerProduct} - ${product.cartId}"
                                    )
                                }
                            }
                        }
                    }
                }
            }
            return pppLabelList
        }
}

data class Data(
    @SuppressLint("Invalid Data Type")
    @SerializedName("address_id")
    var addressId: Long = 0,
    @SerializedName("group_orders")
    var groupOrders: List<GroupOrder> = emptyList()
)

data class GroupOrder(
    @SerializedName("group_type")
    var groupType: Int = 0,
    @SerializedName("cart_string_group")
    var cartStringGroup: String = "",
    @SerializedName("shop_orders")
    var shopOrders: List<ShopOrder> = emptyList(),
    @SerializedName("shipping_info")
    var shippingInfo: ShippingInfo = ShippingInfo(),
    @SerializedName("dropship_data")
    var dropship: Dropship = Dropship(),
    @SerializedName("group_order_metadata")
    var orderMetadata: List<OrderMetadata> = emptyList(),
    @SerializedName("addon_items")
    var checkoutGiftingOrderLevel: List<CheckoutGiftingAddOn> = emptyList()
)

data class ShopOrder(
    @SerializedName("bundle")
    var bundle: List<Bundle> = emptyList(),
    @SerializedName("cart_string_order")
    var cartStringOrder: String = "",
    @SerializedName("is_preorder")
    var isPreorder: Int = 0,
    @SerializedName("order_feature")
    var orderFeature: OrderFeature = OrderFeature(),
    @SerializedName("promos")
    var promos: List<Promo> = emptyList(),
    @SuppressLint("Invalid Data Type")
    @SerializedName("shop_id")
    var shopId: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("warehouse_id")
    var warehouseId: Long = 0,
    @Transient
    var isTokoNow: Boolean = false
)

data class Bundle(
    @SerializedName("bundle_info")
    var bundleInfo: BundleInfo = BundleInfo(),
    @SerializedName("product_data")
    var productData: List<Product> = emptyList()
)

data class Product(
    @SerializedName("is_ppp")
    var isPpp: Boolean = false,
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("items")
    var checkoutGiftingProductLevel: List<CheckoutGiftingAddOn> = emptyList(),
    @Transient
    var cartId: String = "",
    @Transient
    var productCategoryId: String = "",
    @Transient
    var protectionPricePerProduct: Int = 0,
    @Transient
    var protectionTitle: String = "",
    @Transient
    var isProtectionAvailable: Boolean = false
)

data class CheckoutGiftingAddOn(
    @SerializedName("item_type")
    var itemType: String = "",
    @SerializedName("item_id")
    var itemId: String = "",
    @SerializedName("item_qty")
    var itemQty: Int = 0,
    @SerializedName("item_metadata")
    var itemMetadata: String = "",
    @SerializedName("item_unique_id")
    var itemUniqueId: String = ""
)

data class BundleInfo(
    @SerializedName("bundle_group_id")
    var bundleGroupId: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("bundle_id")
    var bundleId: Long = 0
)

data class Dropship(
    @SerializedName("is_dropship")
    var isDropship: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("telp_no")
    var telpNo: String = ""
)

data class OrderFeature(
    @SerializedName("is_order_priority")
    var isOrderPriority: Int = 0
)

data class ShippingInfo(
    @SerializedName("checksum")
    var checksum: String = "",
    @SerializedName("finsurance")
    var finsurance: Int = 0,
    @SerializedName("rates_feature")
    var ratesFeature: RatesFeature = RatesFeature(),
    @SerializedName("rates_id")
    var ratesId: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    var shippingId: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("sp_id")
    var spId: Long = 0,
    @SerializedName("ut")
    var ut: String = ""
)

data class RatesFeature(
    @SerializedName("ontime_delivery_guarantee")
    var ontimeDeliveryGuarantee: OntimeDeliveryGuarantee = OntimeDeliveryGuarantee()
)

data class OntimeDeliveryGuarantee(
    @SerializedName("available")
    var available: Boolean = false,
    @SerializedName("duration")
    var duration: Int = 0
)

data class Egold(
    @SerializedName("gold_amount")
    var goldAmount: Long = 0,
    @SerializedName("is_egold")
    var isEgold: Boolean = false
)

data class Promo(
    @SerializedName("code")
    var code: String = "",
    @SerializedName("type")
    var type: String = ""
) {
    companion object {
        const val TYPE_GLOBAL = "global"
        const val TYPE_MERCHANT = "merchant"
        const val TYPE_LOGISTIC = "logistic"
    }
}

data class TokopediaCorner(
    @SerializedName("is_tokopedia_corner")
    var isTokopediaCorner: Boolean = false,
    @SerializedName("user_corner_id")
    var userCornerId: String = "",
    @SerializedName("corner_id")
    @SuppressLint("Invalid Data Type")
    var cornerId: Long = 0
)

data class OrderMetadata(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
) {
    companion object {
        const val FREE_SHIPPING_METADATA = "free_shipping_metadata"
        const val UPLOAD_PRESCRIPTION_META_DATA_KEY = "prescription_ids"
        const val MINI_CONSULTATION_META_DATA_KEY = "epharm_consultation"
        const val SCHEDULE_DELIVERY_META_DATA_KEY = "shipping_validation_metadata"
    }
}
