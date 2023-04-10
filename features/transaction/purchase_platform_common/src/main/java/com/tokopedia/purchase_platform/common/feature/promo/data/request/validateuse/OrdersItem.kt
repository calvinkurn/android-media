package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrdersItem(
    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    var shippingId: Int = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("shop_id")
    var shopId: Long = 0,
    @SerializedName("codes")
    var codes: MutableList<String> = mutableListOf(),
    @SerializedName("unique_id")
    var uniqueId: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("sp_id")
    var spId: Int = 0,
    @SerializedName("product_details")
    var productDetails: List<ProductDetailsItem> = listOf(),
    @SerializedName("free_shipping_metadata")
    var freeShippingMetadata: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("bo_campaign_id")
    var boCampaignId: Long = 0,
    @SerializedName("shipping_subsidy")
    var shippingSubsidy: Long = 0,
    @SerializedName("benefit_class")
    var benefitClass: String = "",
    @SerializedName("shipping_price")
    var shippingPrice: Double = 0.0,
    @SerializedName("eta_txt")
    var etaText: String = "",
    @SerializedName("validation_metadata")
    var validationMetadata: String = "",
    @Transient
    var boType: Int = 0,
    @Transient
    var isPo: Boolean = false,
    @Transient
    var poDuration: Int = 0,
    @Transient
    var warehouseId: Long = 0
) : Parcelable
