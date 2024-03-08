package com.tokopedia.cartcommon.data.response.bmgm

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
) : Parcelable {

    fun isGiftWithPurchase(): Boolean {
        return offerTypeId == GWP_OFFER_TYPE_ID
    }

    fun isValidGiftPurchase(): Boolean {
        return isGiftWithPurchase() && (tierProductList.firstOrNull()?.productsBenefit?.isNotEmpty() == true)
    }

    companion object {
        private const val GWP_OFFER_TYPE_ID = 2L
    }
}
