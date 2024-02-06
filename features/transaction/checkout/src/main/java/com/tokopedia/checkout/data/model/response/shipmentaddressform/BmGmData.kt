package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmData(
    @SerializedName("offer_id")
    val offerId: Long = 0L,
    @SerializedName("offer_type_id")
    val offerTypeId: Long = 0L,
    @SerializedName("offer_name")
    val offerName: String = "",
    @SerializedName("offer_icon")
    val offerIcon: String = "",
    @SerializedName("offer_message")
    val offerMessage: List<String> = listOf(),
    @SerializedName("offer_status")
    val offerStatus: Int = 1,
    @SerializedName("offer_landing_page_link")
    val offerLandingPageLink: String = "",
    @SerializedName("total_discount")
    val totalDiscount: Double = 0.0,
    @SerializedName("offer_json_data")
    val offerJsonData: String = "",
    @SerializedName("is_tier_achieved")
    val isTierAchieved: Boolean = false,
    @SerializedName("tier_product")
    val tierProductList: List<BmGmTierProduct> = emptyList()
) : Parcelable
