package com.tokopedia.oneclickcheckout.order.data.payment

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class PaymentRequest(
    @SerializedName("payment")
    val payment: PaymentData,
    @SerializedName("cart_details")
    val cartDetail: CartDetail,
    @SerializedName("promo_details")
    val promoDetail: PromoDetail
)

data class PaymentData(
    @SerializedName("profile_code")
    val profileCode: String = "",
    @SerializedName("gateway_code")
    val gatewayCode: String = "",
    @SerializedName("payment_amount")
    val paymentAmount: Double = 0.0
)

data class CartDetail(
    @SerializedName("carts")
    val cart: CartData,
    @SerializedName("trade_in_data")
    val tradeIn: TradeInData = TradeInData()
)

data class CartData(
    @SerializedName("cross_sell")
    val crossSell: CrossSellData = CrossSellData(),
    @SerializedName("data")
    val data: List<CartDetailData>,
    @SerializedName("egold_data")
    val egold: EgoldData = EgoldData(),
    @SerializedName("donation_data")
    val donation: DonationData = DonationData()
)

data class CrossSellData(
    @SerializedName("items")
    val items: List<CrossSellItem> = emptyList()
)

data class CrossSellItem(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("price")
    val price: Double = 0.0
)

data class CartDetailData(
    @SerializedName("address")
    val address: CartAddressData,
    @SerializedName("group_orders")
    val groupOrders: List<CartGroupData>
)

data class CartAddressData(
    @SerializedName("id")
    val id: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("postal_code")
    val postalCode: String
)

data class CartGroupData(
    @SerializedName("cart_string_group")
    val cartStringGroup: String,
    @SerializedName("shipping_info")
    val shippingInfo: CartShippingInfoData,
    @SerializedName("shop_orders")
    val shopOrders: List<CartShopOrderData>
)

data class CartShippingInfoData(
    @SerializedName("shipping_product_id")
    val spId: String,
    @SerializedName("shipping_price")
    val originalShippingPrice: Double,
    @SerializedName("shipping_service_name")
    val serviceName: String,
    @SerializedName("shipper_name")
    val shipperName: String,
    @SerializedName("shipper_eta")
    val eta: String,
    @SerializedName("insurance_price")
    val insurancePrice: Double
)

data class CartShopOrderData(
    @SerializedName("shop_id")
    val shopId: String,
    @SerializedName("warehouse_id")
    val warehouseId: Long,
    @SerializedName("shop_tier")
    val shopTier: Long,
    @SerializedName("products")
    val products: List<CartProductData>,
    @SerializedName("bundle_data")
    val bundle: List<CartBundleData>,
    @SerializedName("addon_items")
    val addonItems: List<CartAddOnData>,
    @SerializedName("cart_string_order")
    val cartStringOrder: String
)

data class CartProductData(
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("name")
    val name: String,
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long,
    @SerializedName("quantity")
    val quantity: Long,
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    val totalPrice: Long,
    @SerializedName("bundle_group_id")
    val bundleGroupId: String,
    @SerializedName("addon_items")
    val addonItems: List<CartAddOnData>,
    @SerializedName("category")
    val category: CartProductCategoryData
)

data class CartAddOnData(
    @SerializedName("name")
    val name: String,
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long,
    @SerializedName("quantity")
    val quantity: Long,
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_price")
    val totalPrice: Long
)

data class CartProductCategoryData(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("identifier")
    val identifier: String
)

data class CartBundleData(
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("bundle_id")
    val bundleId: String,
    @SerializedName("title")
    val title: String
)

data class EgoldData(
    @SerializedName("gold_amount")
    val goldAmount: Long = 0,
    @SerializedName("is_egold")
    val isEgold: Boolean = false
)

data class DonationData(
    @SerializedName("donation_amount")
    val donationAmount: Long = 0,
    @SerializedName("is_donation")
    val isDonation: Boolean = false
)

data class TradeInData(
    @SerializedName("trade_in_amount")
    val tradeInAmount: Long = 0,
    @SerializedName("is_trade_in")
    val isTradeIn: Boolean = false
)

data class PromoDetail(
    @SerializedName("benefit_summary_info")
    val benefitSummaryInfo: BenefitSummaryInfoData,
    @SerializedName("voucher_orders")
    val voucherOrders: List<VoucherOrderItemData>,
    @SerializedName("additional_info")
    val additionalInfo: AdditionalInfoData
)

data class VoucherOrderItemData(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("unique_id")
    val uniqueId: String = "",
    @SerializedName("shipping_id")
    val shippingId: Long = 0,
    @SerializedName("sp_id")
    val spId: Long = 0,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("cart_string_group")
    val cartStringGroup: String = ""
)

data class BenefitSummaryInfoData(
    @SerializedName("summaries")
    val summaries: List<SummariesItemData> = emptyList()
)

data class SummariesItemData(
    @SerializedName("details")
    val details: List<DetailsItemData> = emptyList(),
    @SerializedName("type")
    val type: String = ""
)

data class DetailsItemData(
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("type")
    val type: String = ""
)

data class AdditionalInfoData(
    @SerializedName("usage_summaries")
    val usageSummaries: List<UsageSummariesData> = emptyList()
)

data class UsageSummariesData(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("amount_str")
    val amountString: String = "",
    @SerializedName("amount")
    val amount: Double = 0.0
)
