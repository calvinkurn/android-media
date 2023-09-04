package com.tokopedia.purchase_platform.common.feature.bmgm.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmData(
    @Expose
    @SerializedName("offer_id")
    val offerId: Long = 0L,
    @Expose
    @SerializedName("offer_name")
    val offerName: String = "",
    @Expose
    @SerializedName("offer_icon")
    val offerIcon: String = "",
    @Expose
    @SerializedName("offer_message")
    val offerMessage: List<String> = listOf(),
    @Expose
    @SerializedName("offer_status")
    val offerStatus: Int = 1,
    @Expose
    @SerializedName("offer_landing_page_link")
    val offerLandingPageLink: String = "",
    @Expose
    @SerializedName("total_discount")
    val totalDiscount: Double = 0.0,
    @Expose
    @SerializedName("offer_json_data")
    val offerJsonData: String = "",
    @Expose
    @SerializedName("tier_product")
    val tierProductList: List<BmGmTierProduct> = emptyList()
): Parcelable
