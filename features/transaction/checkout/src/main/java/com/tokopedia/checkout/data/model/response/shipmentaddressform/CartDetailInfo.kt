package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class CartDetailInfo(
    @SerializedName("cart_detail_type")
    val cartDetailType: String = "",
    @SerializedName("bmgm")
    val bmgm: BMGM = BMGM()
)

data class BMGM(
    @SerializedName("offer_id")
    val offerId: String = "",
    @SerializedName("offer_name")
    val offerName: String = "",
    @SerializedName("offer_icon")
    val offerIcon: String = "",
    @SerializedName("offer_message")
    val offer_message: String = "",
    @SerializedName("offer_landing_page_link")
    val offerLandingPageLink: String = "",
    @SerializedName("total_discount")
    val totalDiscount: Double = 0.0,
    @SerializedName("offer_json_data")
    val offerJsonData: String = "",
    @SerializedName("tiers_applied")
    val tiersApplied: List<TiersApplied> = emptyList()
)

data class TiersApplied(
    @SerializedName("tier_id")
    val tierId: String = "",
    @SerializedName("tier_message")
    val tierMessage: String = "",
    @SerializedName("tier_discount_text")
    val tierDiscountText: String = "",
    @SerializedName("tier_discount_amount")
    val tierDiscountAmount: Int = 0,
    @SerializedName("list_product")
    val listProduct: List<BMGMProduct> = emptyList(),
    @SerializedName("price_before_benefit")
    val priceBeforeBenefit: Double = 0.0,
    @SerializedName("price_after_benefit")
    val priceAfterBenefit: Double = 0.0,
)

data class BMGMProduct(
    @SerializedName("cart_id")
    val cartId: String = "",
    @SerializedName("final_price")
    val finalPrice: Double = 0.0,
    @SerializedName("price_after_bmgm")
    val priceAfterBMGM: Double = 0.0,
    @SerializedName("product_id")
    val productId: Long = 0,
    @SerializedName("qty")
    val qty: Int = 0,
    @SerializedName("warehouse_id")
    val warehouseId: Long = 0
)
